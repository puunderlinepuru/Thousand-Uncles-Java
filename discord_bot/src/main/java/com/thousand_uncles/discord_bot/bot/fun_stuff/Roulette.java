package com.thousand_uncles.discord_bot.bot.fun_stuff;

import com.thousand_uncles.discord_bot.bot.util.GlobalThings;
import com.thousand_uncles.discord_bot.bot.util.Pair;
import com.thousand_uncles.discord_bot.bot.util.Triple;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.event.domain.interaction.SelectMenuInteractionEvent;
import discord4j.core.object.component.*;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.InteractionPresentModalSpec;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Roulette {

    private static final List<Character> rouletteColors = Arrays.asList(
            'g', //0
            'r', //1
            'b', //2
            'r', //3
            'b', //4
            'r', //5
            'b', //6
            'r', //7
            'b', //8
            'r', //9
            'b', //10
            'b', //11
            'r', //12
            'b', //13
            'r', //14
            'b', //15
            'r', //16
            'b', //17
            'r', //18
            'r', //19
            'b', //20
            'r', //21
            'b', //22
            'r', //23
            'b', //24
            'r', //25
            'b', //26
            'r', //27
            'b', //28
            'b', //29
            'r', //30
            'b', //31
            'r', //32
            'b', //33
            'r', //34
            'b', //35
            'r'  //36
            );

    private static final String[] prettyBetTypes = {
            "Straight Up",
            "Split Bet",
            "Street Bet",
            "Corner Bet",
            "Six Line Bet",
            "Red Or Black",
            "Odd Or Even",
            "1-18 or 19-36",
            "Dozens",
            "Columns"
    };

    private static final String[] betTypes = {
            "StraightUp",
            "SplitBet",
            "StreetBet",
            "CornerBet",
            "SixLineBet",
            "RedOrBlack",
            "OddOrEven",
            "LowOrHigh",
            "Dozens",
            "Columns"
    };

    private static final HashMap<String, Integer> betsAndPayouts;
    static {
        betsAndPayouts = new HashMap<>();
        betsAndPayouts.put(betTypes[0], 35);
        betsAndPayouts.put(betTypes[1], 17);
        betsAndPayouts.put(betTypes[2], 11);
        betsAndPayouts.put(betTypes[3], 8);
        betsAndPayouts.put(betTypes[4], 5);
        betsAndPayouts.put(betTypes[5], 1);
        betsAndPayouts.put(betTypes[6], 1);
        betsAndPayouts.put(betTypes[7], 1);
        betsAndPayouts.put(betTypes[8], 2);
        betsAndPayouts.put(betTypes[9], 2);
    }

    public static String getRouletteMenuContent(){
        return """
                **Straight Up** - Bet on a single number
                Payout: 35 to 1
                **Split Bet** - Bet on two adjacent numbers
                Payout: 17 to 1
                **Street Bet** - Bet on three numbers in a row
                Payout: 11 to 1
                **Corner Bet** - Bet on four numbers forming a square
                Payout: 8 to 1
                **Six Line Bet** - Covers two rows (six numbers)
                Payout: 5 to 1
                **Red or Black**
                Payout: 1 to 1
                **Odd or Even**
                Payout: 1 to 1
                **1–18 or 19–36**
                Payout: 1 to 1
                **Dozens (1–12, 13–24, 25–36)**
                Payout: 2 to 1
                **Columns**
                Payout: 2 to 1""";
    }

    public static Mono<Void> getRouletteMenu(ChatInputInteractionEvent event){
        List<SelectMenu.Option> rouletteOptions = new ArrayList<>(List.of());

        for (int i = 0; i < 10; i++) {
            rouletteOptions.add(i, SelectMenu.Option.of(prettyBetTypes[i], betTypes[i]));
        }

        EmbedCreateSpec embed = EmbedCreateSpec.builder()
                .image("https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fwww.sevenjackpots.com%2Fwp-content%2Fuploads%2F2021%2F04%2Feuropean-roulette-table-2020.png&f=1&nofb=1&ipt=2e2c8e3f345d9984ab8071bf60a9825fbd4bb550fdbbce19a4a1eef3a4941a0a")
                .build();

        return event.reply()
                .withEphemeral(true)
                .withContent(getRouletteMenuContent())
                .withEmbeds(embed)
                .withComponents(ActionRow.of(SelectMenu.of("roulette", rouletteOptions)));
    }

    @SuppressWarnings("unused")
    public static Mono<Void> resetRouletteMenu(SelectMenuInteractionEvent event){
        List<SelectMenu.Option> rouletteOptions = new ArrayList<>(List.of());

        for (int i = 0; i < 10; i++) {
            rouletteOptions.add(i, SelectMenu.Option.of(prettyBetTypes[i], betTypes[i]));
        }
        event.edit()
                .withEphemeral(true)
                .withComponents(ActionRow.of(SelectMenu.of("roulette", rouletteOptions)))
                .block();

        return Mono.empty();
    }

    public static Mono<Void> handleSet(SelectMenuInteractionEvent event, String selectedOption){
        return switch (selectedOption) {
            case "StraightUp" -> TypeHandlers.setStraightUp(event);
            case "SplitBet" -> TypeHandlers.setSplitBet(event);
            case "StreetBet" -> TypeHandlers.setStreetBet(event);
            case "CornerBet" -> TypeHandlers.setCornerBet(event);
            case "SixLineBet" -> TypeHandlers.setSixLineBet(event);
            case "RedOrBlack" -> TypeHandlers.setRedOrBlack(event);
            case "OddOrEven" -> TypeHandlers.setOddOrEven(event);
            case "LowOrHigh" -> TypeHandlers.set18or36(event);
            case "Dozens" -> TypeHandlers.setDozens(event);
            case "Columns" -> TypeHandlers.setColumns(event);
            default -> Mono.empty();
        };
    }

    public static String getContentW() {
        return "**You win!**" + "\n" +
                "Won: ";
    }

    public static String getContentL() {
        return "**You lost(**" + "\n" +
                "Lost: ";
    }

    private static class TypeHandlers{

        public static Mono<Void> setStraightUp(SelectMenuInteractionEvent event){
            return event.presentModal(InteractionPresentModalSpec.builder()
                    .title("Straight Up")
                    .customId("roulette-straightup")
                    .addAllComponents(Arrays.asList(
                                    TextDisplay.of("Alright!"),
                                    Label.of("Number to bet on", TextInput.small("betNumber", 1,
                                            2).placeholder("...what are we betting on?").required(true)),
                                    Label.of("Stakes", TextInput.small("betAmount", 1,
                                            30).placeholder("...what's at stake?").required(true))
                            )
                    ).build());
        }

        public static Mono<Void> setSplitBet(SelectMenuInteractionEvent event){
            return event.presentModal(InteractionPresentModalSpec.builder()
                    .title("Split Bet")
                    .customId("roulette-splitbet")
                    .addAllComponents(Arrays.asList(
                                    TextDisplay.of("Alright!"),
                                    Label.of("Pick two adjacent numbers like \"1,2\" or \"1,4\"", TextInput.small("betNumbers", 3,
                                            5).placeholder("...what are we betting on?").required(true)),
                                    Label.of("Stakes", TextInput.small("betAmount", 1,
                                            30).placeholder("...what's at stake?").required(true))
                            )
                    ).build());
        }

        public static Mono<Void> setStreetBet(SelectMenuInteractionEvent event){
            return event.presentModal(InteractionPresentModalSpec.builder()
                    .title("Street Bet")
                    .customId("roulette-streetbet")
                    .addAllComponents(Arrays.asList(
                                    TextDisplay.of("Alright!"),
                                    Label.of("Pick the 1st number in a line - 1=1,2,3", TextInput.small("betNumbers", 1,
                                            2).placeholder("...what are we betting on?").required(true)),
                                    Label.of("Stakes", TextInput.small("betAmount", 1,
                                            30).placeholder("...what's at stake?").required(true))
                            )
                    ).build());
        }

        public static Mono<Void> setCornerBet(SelectMenuInteractionEvent event){
            return event.presentModal(InteractionPresentModalSpec.builder()
                    .title("Split Bet")
                    .customId("roulette-cornerbet")
                    .addAllComponents(Arrays.asList(
                                    TextDisplay.of("Alright!"),
                                    Label.of("Pick 4 numbers that make a square: \"1,2,4,5\"", TextInput.small("betNumbers", 7,
                                            11).placeholder("...what are we betting on?").required(true)),
                                    Label.of("Stakes", TextInput.small("betAmount", 1,
                                            30).placeholder("...what's at stake?").required(true))
                            )
                    ).build());
        }

        public static Mono<Void> setSixLineBet(SelectMenuInteractionEvent event){
            return event.presentModal(InteractionPresentModalSpec.builder()
                    .title("Six Line Bet")
                    .customId("roulette-sixlinebet")
                    .addAllComponents(Arrays.asList(
                                    TextDisplay.of("Alright!"),
                                    Label.of("Pick 1st numbers of 2 adjacent lines:\"1,4\"", TextInput.small("betNumbers", 3,
                                            5).placeholder("...what are we betting on?").required(true)),
                                    Label.of("Stakes", TextInput.small("betAmount", 1,
                                            30).placeholder("...what's at stake?").required(true))
                            )
                    ).build());
        }

        public static Mono<Void> setRedOrBlack(SelectMenuInteractionEvent event){
            return event.presentModal(InteractionPresentModalSpec.builder()
                    .title("Red or Black")
                    .customId("roulette-redorblackbet")
                    .addAllComponents(Arrays.asList(
                                    TextDisplay.of("Alright!"),
                                    Label.of("\"red\" or \"black\"", TextInput.small("betNumbers", 3,
                                            5).placeholder("...what are we betting on?").required(true)),
                                    Label.of("Stakes", TextInput.small("betAmount", 1,
                                            30).placeholder("...what's at stake?").required(true))
                            )
                    )
                    .build());
        }

        public static Mono<Void> setOddOrEven(SelectMenuInteractionEvent event){
            return event.presentModal(InteractionPresentModalSpec.builder()
                    .title("Odd or Even")
                    .customId("roulette-oddorevenbet")
                    .addAllComponents(Arrays.asList(
                                    TextDisplay.of("Alright!"),
                                    Label.of("\"odd\" or \"even\"", TextInput.small("betNumbers", 3,
                                            4).placeholder("...what are we betting on?").required(true)),
                                    Label.of("Stakes", TextInput.small("betAmount", 1,
                                            30).placeholder("...what's at stake?").required(true))
                            )
                    ).build());
        }

        public static Mono<Void> set18or36(SelectMenuInteractionEvent event){
            return event.presentModal(InteractionPresentModalSpec.builder()
                    .title("Low(1-18) or High(19-36)")
                    .customId("roulette-loworhigh")
                    .addAllComponents(Arrays.asList(
                                    TextDisplay.of("Alright!"),
                                    Label.of("\"low\" or \"high\"", TextInput.small("betNumbers", 3,
                                            4).placeholder("...what are we betting on?").required(true)),
                                    Label.of("Stakes", TextInput.small("betAmount", 1,
                                            30).placeholder("...what's at stake?").required(true))
                            )
                    ).build());
        }

        public static Mono<Void> setDozens(SelectMenuInteractionEvent event){
            return event.presentModal(InteractionPresentModalSpec.builder()
                    .title("Dozens")
                    .customId("roulette-dozens")
                    .addAllComponents(Arrays.asList(
                                    TextDisplay.of("Alright!"),
                                    Label.of("Pick a number for dozen - 1 for 1-12 etc.", TextInput.small("betNumbers", 1,
                                            1).placeholder("...what are we betting on?").required(true)),
                                    Label.of("Stakes", TextInput.small("betAmount", 1,
                                            30).placeholder("...what's at stake?").required(true))
                            )
                    ).build());
        }

        public static Mono<Void> setColumns(SelectMenuInteractionEvent event){
            return event.presentModal(InteractionPresentModalSpec.builder()
                    .title("Columns")
                    .customId("roulette-columns")
                    .addAllComponents(Arrays.asList(
                                    TextDisplay.of("Alright!"),
                                    Label.of("Pick the last number of column - 34,35 or 36", TextInput.small("betNumbers", 2,
                                            2).placeholder("...what are we betting on?").required(true)),
                                    Label.of("Stakes", TextInput.small("betAmount", 1,
                                            30).placeholder("...what's at stake?").required(true))
                            )
                    ).build());
        }
    }

    public static class Rolls{

        public static Triple<Integer, Boolean, String> rollStraightUp(int number, String stake){
            int rolledNumber = rollNumber();
            boolean win = (rolledNumber == number);
            String payout = win ? getPayout("StraightUp", stake) : stake;
            return new Triple<>(rolledNumber, win, payout);
        }

        public static Triple<Integer, Boolean, String> rollSplitBet(Pair<Integer, Integer> betNumbers, String stake){
            int rolledNumber = rollNumber();
            if (rolledNumber == 0){return new Triple<>(rolledNumber, false, stake);}
            boolean win = (betNumbers.getFirst() == rolledNumber) || (betNumbers.getSecond() == rolledNumber);
            String payout = win ? getPayout("SplitBet", stake) : stake;
            return new Triple<>(rolledNumber, win, payout);
        }

        public static Triple<Integer, Boolean, String> rollStreetBet(List<Integer> numbers, String stake){
            int rolledNumber = rollNumber();
            if (rolledNumber == 0){return new Triple<>(rolledNumber, false, stake);}
            boolean win = numbers.contains(rolledNumber);
            String payout = win ? getPayout("StreetBet", stake) : stake;
            return new Triple<>(rolledNumber, win, payout);
        }

        public static Triple<Integer, Boolean, String> rollCornerBet(List<Integer> numbers, String stake){
            int rolledNumber = rollNumber();
            if (rolledNumber == 0){return new Triple<>(rolledNumber, false, stake);}
            boolean win = numbers.contains(rolledNumber);
            String payout = win ? getPayout("CornerBet", stake) : stake;
            return new Triple<>(rolledNumber, win, payout);
        }

        public static Triple<Integer, Boolean, String> rollSixLineBet(List<Integer> numbers, String stake){
            int rolledNumber = rollNumber();
            if (rolledNumber == 0){return new Triple<>(rolledNumber, false, stake);}
            boolean win = numbers.contains(rolledNumber);
            String payout = win ? getPayout("SixLineBet", stake) : stake;
            System.out.println("numbers got: " + numbers + "\n" +
                    "result: " + rolledNumber + ", " + win + ", " + payout);
            return new Triple<>(rolledNumber, win, payout);
        }

        public static Triple<Integer, Boolean, String> rollRedOrBlack(String color, String stake){
            int rolledNumber = rollNumber();
            if (rolledNumber == 0){return new Triple<>(rolledNumber, false, stake);}
            char rolledColor = rouletteColors.get(rolledNumber);
            boolean win  = (rolledColor  == color.charAt(0));
            String payout = win ? getPayout("RedOrBlack", stake) : stake;
            return new Triple<>(rolledNumber, win, payout);
        }

        public static Triple<Integer, Boolean, String> rollOddOrEven(String oddOrEven, String stake){
            int rolledNumber = rollNumber();
            if (rolledNumber == 0){return new Triple<>(rolledNumber, false, stake);}
            boolean isEven = (rolledNumber % 2 == 0);
            boolean win = (oddOrEven.equals("even") && isEven) || (oddOrEven.equals("odd") && !isEven);
            String payout = win ? getPayout("OddOrEven", stake) : stake;
            return new Triple<>(rolledNumber, win, payout);
        }

        public static Triple<Integer, Boolean, String> rollLowOrHigh(String lowOrHigh, String stake){
            int rolledNumber = rollNumber();
            if (rolledNumber == 0){return new Triple<>(rolledNumber, false, stake);}
            boolean win = (lowOrHigh.equals("low") && rolledNumber <= 18) ||
                    (lowOrHigh.equals("high") && rolledNumber >= 19);
            String payout = win ? getPayout("LowOrHigh", stake) : stake;
            return new Triple<>(rolledNumber, win, payout);
        }

        public static Triple<Integer, Boolean, String> rollDozens(int dozen, String stake){
            int rolledNumber = rollNumber();
            if (rolledNumber == 0){return new Triple<>(rolledNumber, false, stake);}
            boolean win = (dozen == 1 && rolledNumber <= 12) ||
                    (dozen == 2 && rolledNumber >= 13 && rolledNumber <= 24) ||
                    (dozen == 3 && rolledNumber >= 25);
            String payout = win ? getPayout("Dozens", stake) : stake;
            return new Triple<>(rolledNumber, win, payout);
        }

        public static Triple<Integer, Boolean, String> rollColumns(int column, String stake){
            int rolledNumber = rollNumber();
            if (rolledNumber == 0){return new Triple<>(rolledNumber, false, stake);}
            boolean win = (column == 1 && (rolledNumber % 3 == 1)) ||
                    (column == 2 && (rolledNumber % 3 == 2)) ||
                    (column == 3 && (rolledNumber % 3 == 0));
            String payout = win ? getPayout("Columns", stake) : stake;
            return new Triple<>(rolledNumber, win, payout);
        }

        private static int rollNumber(){
            return GlobalThings.getRand().nextInt(37);
        }

        private static String getPayout(String betType, String stake){
            String payout;
            try{
                int amount = Integer.parseInt(stake);
                payout = String.valueOf(amount * betsAndPayouts.get(betType));
            } catch (NumberFormatException e) {
                payout = stake;
            }
            return payout;
        }
    }
}
