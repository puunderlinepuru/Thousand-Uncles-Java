package com.thousand_uncles.discord_bot.bot.util;

import java.io.ByteArrayOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ValveServerQueries {
    private static final String SERVER_IP = "146.59.52.20";
    private static final int SERVER_PORT = 27015;
    private static final int TIMEOUT_MS = 3000;
    private static final int HEADER = -1;
    private static final int SPLIT_PACKET_HEADER = -2;
    private static final byte A2S_INFO = 0x54;
    private static final byte A2S_PLAYER = 0x55;
    private static final byte A2S_INFO_RESPONSE = 0x49;
    private static final byte A2S_PLAYER_RESPONSE = 0x44;
    private static final byte CHALLENGE_RESPONSE = 0x41;

    public static void main(String[] args) throws Exception {
        makeQuety();
    }

    public static void makeQuety() throws Exception{
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(TIMEOUT_MS);

            InetAddress address = InetAddress.getByName(SERVER_IP);
            ServerInfo info = queryInfo(socket, address);
            List<Player> players = queryPlayers(socket, address);

            System.out.println("Address: " + SERVER_IP + ":" + SERVER_PORT);
            System.out.println("Server: " + info.name);
            System.out.println("Map: " + info.map);
            System.out.println("Folder: " + info.folder);
            System.out.println("Game: " + info.game + " (app " + info.appId + ")");
            System.out.println("Players: " + info.players + "/" + info.maxPlayers + " (" + info.bots + " bots)");
            System.out.println("Server type: " + info.serverType);
            System.out.println("Platform: " + info.platform);
            System.out.println("Password protected: " + info.passwordProtected);
            System.out.println("VAC enabled: " + info.vacEnabled);
            System.out.println("Version: " + info.version);
            System.out.println();

            if (players.isEmpty()) {
                System.out.println("No player rows returned.");
                return;
            }

            for (Player player : players) {
                if (!player.name.isBlank()) {
                    System.out.printf(
                            "%-32s score=%d time=%.0fs%n",
                            player.name,
                            player.score,
                            player.durationSeconds
                    );
                }
            }
        }
    }

    private static ServerInfo queryInfo(DatagramSocket socket, InetAddress address) throws Exception {
        byte[] request = buildInfoRequest();
        ByteBuffer data = sendAndReceive(socket, address, request);
        ResponseHeader response = readHeader(data);

        if (response.isChallenge()) {
            data = sendAndReceive(socket, address, appendInt(request, data.getInt()));
            response = readHeader(data);
        }

        requireResponse(response, A2S_INFO_RESPONSE, "A2S_INFO");

        data.get(); // protocol

        ServerInfo info = new ServerInfo();
        info.name = readCString(data);
        info.map = readCString(data);
        info.folder = readCString(data);
        info.game = readCString(data);
        info.appId = Short.toUnsignedInt(data.getShort());
        info.players = Byte.toUnsignedInt(data.get());
        info.maxPlayers = Byte.toUnsignedInt(data.get());
        info.bots = Byte.toUnsignedInt(data.get());
        info.serverType = String.valueOf((char) data.get());
        info.platform = String.valueOf((char) data.get());
        info.passwordProtected = data.get() != 0;
        info.vacEnabled = data.get() != 0;
        info.version = readCString(data);

        return info;
    }

    private static List<Player> queryPlayers(DatagramSocket socket, InetAddress address) throws Exception {
        ByteBuffer challengeRequest = ByteBuffer.allocate(9).order(ByteOrder.LITTLE_ENDIAN);
        challengeRequest.putInt(HEADER);
        challengeRequest.put(A2S_PLAYER);
        challengeRequest.putInt(-1);

        ByteBuffer data = sendAndReceive(socket, address, challengeRequest.array());
        ResponseHeader response = readHeader(data);

        if (!response.isChallenge()) {
            requireResponse(response, A2S_PLAYER_RESPONSE, "A2S_PLAYER");
            return parsePlayers(data);
        }

        data = sendAndReceive(socket, address, buildPlayerRequest(data.getInt()));
        response = readHeader(data);

        if (response.isChallenge()) {
            data = sendAndReceive(socket, address, buildPlayerRequest(data.getInt()));
            response = readHeader(data);
        }

        requireResponse(response, A2S_PLAYER_RESPONSE, "A2S_PLAYER");
        return parsePlayers(data);
    }

    private static List<Player> parsePlayers(ByteBuffer data) {
        int count = Byte.toUnsignedInt(data.get());
        List<Player> players = new ArrayList<>();

        for (int i = 0; i < count && data.hasRemaining(); i++) {
            Player player = new Player();
            data.get(); // index
            player.name = readCString(data);
            player.score = data.getInt();
            player.durationSeconds = data.getFloat();
            players.add(player);
        }

        return players;
    }

    private static byte[] buildPlayerRequest(int challenge) {
        ByteBuffer request = ByteBuffer.allocate(9).order(ByteOrder.LITTLE_ENDIAN);
        request.putInt(HEADER);
        request.put(A2S_PLAYER);
        request.putInt(challenge);
        return request.array();
    }

    private static ResponseHeader readHeader(ByteBuffer data) {
        return new ResponseHeader(data.getInt(), data.get());
    }

    private static void requireResponse(
            ResponseHeader response,
            byte expectedType,
            String requestName
    ) {
        if (response.header != HEADER || response.type != expectedType) {
            throw new IllegalStateException(String.format(
                    "Unexpected %s response: header=0x%08X type=0x%02X",
                    requestName,
                    response.header,
                    Byte.toUnsignedInt(response.type)
            ));
        }
    }

    private static ByteBuffer sendAndReceive(
            DatagramSocket socket,
            InetAddress address,
            byte[] request
    ) throws Exception {
        socket.send(new DatagramPacket(request, request.length, address, SERVER_PORT));
        return receiveResponse(socket);
    }

    private static ByteBuffer receiveResponse(DatagramSocket socket) throws Exception {
        byte[] firstPacket = receiveDatagram(socket);
        ByteBuffer first = ByteBuffer.wrap(firstPacket).order(ByteOrder.LITTLE_ENDIAN);
        int header = first.getInt();

        if (header != SPLIT_PACKET_HEADER) {
            return ByteBuffer.wrap(firstPacket).order(ByteOrder.LITTLE_ENDIAN);
        }

        int responseId = first.getInt();
        if ((responseId & 0x80000000) != 0) {
            throw new IllegalStateException("Compressed split packets are not supported.");
        }

        int totalPackets = Byte.toUnsignedInt(first.get());
        int packetNumber = Byte.toUnsignedInt(first.get());
        first.getShort(); // split packet size

        byte[][] parts = new byte[totalPackets][];
        parts[packetNumber] = readRemaining(first);

        int receivedPackets = 1;
        while (receivedPackets < totalPackets) {
            byte[] packet = receiveDatagram(socket);
            ByteBuffer part = ByteBuffer.wrap(packet).order(ByteOrder.LITTLE_ENDIAN);

            if (part.getInt() != SPLIT_PACKET_HEADER || part.getInt() != responseId) {
                continue;
            }

            int partTotal = Byte.toUnsignedInt(part.get());
            int partNumber = Byte.toUnsignedInt(part.get());
            part.getShort(); // split packet size

            if (partTotal == totalPackets && partNumber < totalPackets && parts[partNumber] == null) {
                parts[partNumber] = readRemaining(part);
                receivedPackets++;
            }
        }

        byte[] assembled = join(parts);
        if (assembled.length >= 4 && ByteBuffer.wrap(assembled).order(ByteOrder.LITTLE_ENDIAN).getInt() == HEADER) {
            return ByteBuffer.wrap(assembled).order(ByteOrder.LITTLE_ENDIAN);
        }

        byte[] withHeader = new byte[assembled.length + 4];
        ByteBuffer.wrap(withHeader).order(ByteOrder.LITTLE_ENDIAN).putInt(HEADER);
        System.arraycopy(assembled, 0, withHeader, 4, assembled.length);
        return ByteBuffer.wrap(withHeader).order(ByteOrder.LITTLE_ENDIAN);
    }

    private static byte[] receiveDatagram(DatagramSocket socket) throws Exception {
        byte[] buffer = new byte[1400];
        DatagramPacket response = new DatagramPacket(buffer, buffer.length);
        socket.receive(response);
        return Arrays.copyOf(response.getData(), response.getLength());
    }

    private static byte[] buildInfoRequest() {
        byte[] payload = "Source Engine Query\0".getBytes(StandardCharsets.UTF_8);
        byte[] request = new byte[5 + payload.length];

        request[0] = (byte) 0xFF;
        request[1] = (byte) 0xFF;
        request[2] = (byte) 0xFF;
        request[3] = (byte) 0xFF;
        request[4] = A2S_INFO;

        System.arraycopy(payload, 0, request, 5, payload.length);
        return request;
    }

    private static byte[] appendInt(byte[] request, int value) {
        byte[] result = Arrays.copyOf(request, request.length + 4);
        ByteBuffer.wrap(result, request.length, 4)
                .order(ByteOrder.LITTLE_ENDIAN)
                .putInt(value);
        return result;
    }

    private static byte[] readRemaining(ByteBuffer data) {
        byte[] bytes = new byte[data.remaining()];
        data.get(bytes);
        return bytes;
    }

    private static byte[] join(byte[][] parts) {
        int length = 0;
        for (byte[] part : parts) {
            length += part.length;
        }

        byte[] result = new byte[length];
        int offset = 0;
        for (byte[] part : parts) {
            System.arraycopy(part, 0, result, offset, part.length);
            offset += part.length;
        }

        return result;
    }

    private static String readCString(ByteBuffer data) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        while (data.hasRemaining()) {
            byte value = data.get();
            if (value == 0) {
                break;
            }
            bytes.write(value);
        }

        return new String(bytes.toByteArray(), StandardCharsets.UTF_8);
    }

    private static final class ResponseHeader {
        final int header;
        final byte type;

        ResponseHeader(int header, byte type) {
            this.header = header;
            this.type = type;
        }

        boolean isChallenge() {
            return header == HEADER && type == CHALLENGE_RESPONSE;
        }
    }

    private static final class ServerInfo {
        String name;
        String map;
        String folder;
        String game;
        int appId;
        int players;
        int maxPlayers;
        int bots;
        String serverType;
        String platform;
        boolean passwordProtected;
        boolean vacEnabled;
        String version;
    }

    private static final class Player {
        String name;
        int score;
        float durationSeconds;
    }
}
