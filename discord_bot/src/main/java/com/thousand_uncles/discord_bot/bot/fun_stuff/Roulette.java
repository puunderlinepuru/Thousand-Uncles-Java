package com.thousand_uncles.discord_bot.bot.fun_stuff;

import com.thousand_uncles.discord_bot.bot.util.GlobalThings;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.event.domain.interaction.ModalSubmitInteractionEvent;
import discord4j.core.event.domain.interaction.SelectMenuInteractionEvent;
import discord4j.core.object.component.*;
import discord4j.core.object.entity.Message;
import discord4j.core.spec.InteractionPresentModalSpec;
import discord4j.core.spec.MessageEditMono;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Roulette {
//    private int[] blackIndexes = { 0, 2, 4, 6, 8, 10, 11, 13, 15, 17, 20, 22, 24, 26, 28, 29, 31, 33, 35};

    private final List<Character> rouletteColors = Arrays.asList('B', //0
            'R', //1
            'B',
            'R',
            'B',
            'R',
            'B',
            'R',
            'B',
            'R',
            'B',
            'B',
            'R',
            'B',
            'R',
            'B',
            'R',
            'B',
            'R',
            'R',
            'B',
            'R',
            'B',
            'R',
            'B',
            'R',
            'B',
            'R',
            'B',
            'B',
            'R',
            'B',
            'R',
            'B',
            'R',
            'B',
            'R'
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
            "1_18or19_36",
            "Dozens",
            "Columns"
    };

    private static HashMap<String, Integer> betsAndPayouts;
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

    public List<Character> getRouletteColors() {
        return rouletteColors;
    }

    public static String getRouletteMenuContent(){
        return "Inside Bets (Higher Risk, Higher Reward)" +
        "\nThese bets are placed directly on numbers inside the grid."+
        "\n**Straight Up** - Bet on a single number" +
        "\nPayout: 35 to 1" +
        "\n**Split Bet** - Bet on two adjacent numbers" +
        "\nPayout: 17 to 1" +
        "\n**Street Bet** - Bet on three numbers in a row" +
        "\nPayout: 11 to 1" +
        "\n**Corner Bet** - Bet on four numbers forming a square" +
        "\nPayout: 8 to 1" +
        "\n**Six Line Bet** - Covers two rows (six numbers)" +
        "\nPayout: 5 to 1" +
        "\n**Red or Black**" +
        "\nPayout: 1 to 1" +
        "\n**Odd or Even**" +
        "\nPayout: 1 to 1" +
        "\n**1–18 or 19–36**" +
        "\nPayout: 1 to 1" +
        "\n**Dozens (1–12, 13–24, 25–36)**" +
        "\nPayout: 2 to 1" +
        "\n**Columns**" +
        "\nPayout: 2 to 1";
    }

    public static Mono<Void> getRouletteMenu(ChatInputInteractionEvent event){
        List<SelectMenu.Option> rouletteOptions = new java.util.ArrayList<>(List.of());

        for (int i = 0; i < 10; i++) {
            rouletteOptions.add(i, SelectMenu.Option.of(prettyBetTypes[i], betTypes[i]));
        }
        return event.reply()
                .withEphemeral(true)
                .withContent(getRouletteMenuContent())
                .withComponents(ActionRow.of(SelectMenu.of("roulette", rouletteOptions)));
    }

    public static Mono<Void> resetRouletteMenu(SelectMenuInteractionEvent event){
        List<SelectMenu.Option> rouletteOptions = new java.util.ArrayList<>(List.of());

        for (int i = 0; i < 10; i++) {
            rouletteOptions.add(i, SelectMenu.Option.of(prettyBetTypes[i], betTypes[i]));
        }
        event.edit()
                .withEphemeral(true)
                .withComponents(ActionRow.of(SelectMenu.of("roulette", rouletteOptions)));

        return Mono.empty();
    }

    public static Mono<Void> handleSet(SelectMenuInteractionEvent event, String selectedOption){
        return switch (selectedOption) {
            case "StraightUp" -> {
                System.out.println("a");
                yield TypeHandlers.setStraightUp(event);
            }
            case "SplitBet" -> TypeHandlers.setSplitBet(event);
            case "StreetBet" -> TypeHandlers.setStreetBet(event);
            case "CornerBet" -> TypeHandlers.setCornerBet(event);
            case "SixLineBet" -> TypeHandlers.setSixLineBet(event);
            case "RedOrBlack" -> TypeHandlers.setRedOrBlack(event);
            case "OddOrEven" -> TypeHandlers.setOddOrEven(event);
            case "1_18or19_36" -> TypeHandlers.set18or36(event);
            case "Dozens" -> TypeHandlers.setDozens(event);
            case "Columns" -> TypeHandlers.setColumns(event);
            default -> Mono.empty();
        };
    }

    private static class TypeHandlers{
        public static Mono<Void> setStraightUp(SelectMenuInteractionEvent event){

            resetRouletteMenu(event);

            return event.presentModal(InteractionPresentModalSpec.builder()
                    .title("Example modal")
                    .customId("roulette-straightup")
                    .addAllComponents(Arrays.asList(
                                    TextDisplay.of("Alright!"),
                                    Label.of("Number to bet on", TextInput.small("betNumber", 1,
                                            2).placeholder("...what are we betting on?").required(true)),
                                    Label.of("How much to bet", TextInput.small("betAmount", 1,
                                            2).placeholder("...how much?").required(true))
                            )
                    ).build());
        }

        public static Mono<Void> setSplitBet(SelectMenuInteractionEvent event){
            return Mono.empty();
        }

        public static Mono<Void> setStreetBet(SelectMenuInteractionEvent event){
            return Mono.empty();
        }

        public static Mono<Void> setCornerBet(SelectMenuInteractionEvent event){
            return Mono.empty();
        }

        public static Mono<Void> setSixLineBet(SelectMenuInteractionEvent event){
            return Mono.empty();
        }

        public static Mono<Void> setRedOrBlack(SelectMenuInteractionEvent event){
            return Mono.empty();
        }

        public static Mono<Void> setOddOrEven(SelectMenuInteractionEvent event){
            return Mono.empty();
        }

        public static Mono<Void> set18or36(SelectMenuInteractionEvent event){
            return Mono.empty();
        }

        public static Mono<Void> setDozens(SelectMenuInteractionEvent event){
            return Mono.empty();
        }

        public static Mono<Void> setColumns(SelectMenuInteractionEvent event){
            return Mono.empty();
        }
    }

    public static class Rolls{
        public static Mono<Void> rollStraightUp(ModalSubmitInteractionEvent event, int number){

            if (number > 36){
                return event.reply()
                        .withEphemeral(true)
                        .withContent("Wrong range");
            }

            int roll = GlobalThings.getRand().nextInt(37);

            if (number == roll){
                return event.reply()
                        .withEphemeral(false)
                        .withContent("Bet on: " + number + "\n" +
                                "Rolled: " + roll + "\n" +
                                "**You win!**");
            } else {
                return event.reply()
                        .withEphemeral(false)
                        .withContent("Bet on: " + number + "\n" +
                                "Rolled: " + roll + "\n" +
                                "**You lose(**");
            }
        }
    }
}
