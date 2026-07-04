package com.thousand_uncles.discord_bot.bot.listeners;

import com.thousand_uncles.discord_bot.bot.fun_stuff.Roulette;
import com.thousand_uncles.discord_bot.bot.util.GlobalThings;
import com.thousand_uncles.discord_bot.bot.util.Pair;
import com.thousand_uncles.discord_bot.bot.util.Triple;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ModalSubmitInteractionEvent;
import discord4j.core.object.component.*;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
@Component
public class ModalListenerProd {
    GatewayDiscordClient client;

    ModalListenerProd(GatewayDiscordClient client){
        this.client = client;

        client.on(ModalSubmitInteractionEvent.class, this::onModalSubmit).subscribe();
    }

    public Mono<Void> onModalSubmit(ModalSubmitInteractionEvent event){
        String[] customIdParts = event.getCustomId().split("-");

        if (customIdParts[0].equals("roulette")){
            return rouletteHandle(event, customIdParts);
        }

        return Mono.empty();
    }

    private Mono<Void> rouletteHandle(ModalSubmitInteractionEvent event, String[] customIdParts){
        System.out.println("got submission:");
        switch (customIdParts[1]){
            case "straightup":
                return RouletteSubmissionsHandlers.straightUpSubmission(event);
            case "splitbet":
                return RouletteSubmissionsHandlers.splitBetSubmission(event);
            case "streetbet":
                return RouletteSubmissionsHandlers.streetBetSubmission(event);
            case "cornerbet":
                return RouletteSubmissionsHandlers.cornerBetSubmission(event);
            case "sixlinebet":
                return RouletteSubmissionsHandlers.sixLineBetSubmission(event);
            case "redorblackbet":
                return RouletteSubmissionsHandlers.redOrBlackSubmission(event);
            case "oddorevenbet":
                return RouletteSubmissionsHandlers.oddOrEvenSubmission(event);
            case "loworhigh":
                return RouletteSubmissionsHandlers.lowOrHighSubmission(event);
            case "dozens":
                return RouletteSubmissionsHandlers.dozensSubmission(event);
            case "columns":
                return RouletteSubmissionsHandlers.columnsSubmission(event);

        }
            /*List<ICanBeUsedInContainerComponent> firstComponents = new ArrayList<>();
            firstComponents.add(TextDisplay.of(String.format("Comments: %s", "comments")));


            Container container = Container.of(firstComponents);

            return event.reply(InteractionApplicationCommandCallbackSpec.builder().addComponent(container).build());*/

        System.out.println("original interaction: " + event.getInteraction().getMessage().get().getContent());
        return Mono.empty();
    }

    private static class RouletteSubmissionsHandlers{
        private static Mono<Void> straightUpSubmission(ModalSubmitInteractionEvent event){
            String betType = "Straight Up";
            System.out.println(betType);
            int betNumber = -1;
            String stake = "nothing apparently";
            String user = event.getUser().getGlobalName().orElse("someone");
            List<TextInput> textInputComponents = event.getComponents(TextInput.class);
            if (textInputComponents.isEmpty()) {
                return event.reply("No components found!");
            }
            for (TextInput component : textInputComponents) {
                try{
                    if (component.getCustomId().equals("betNumber")){
                        betNumber = Integer.parseInt(component.getValue().orElse("-1"));
                    } else if (component.getCustomId().equals("betAmount")) {
                        stake = component.getValue().orElse("nothing apparently");
                    }
                } catch (NumberFormatException e){
                    return event.reply()
                            .withEphemeral(true)
                            .withContent("Error converting numbers. Try again :p");
                }
            }
            if (betNumber > 36){
                return event.reply()
                        .withEphemeral(true)
                        .withContent("There isn't such number on EU roulette");
            }
            Triple<Integer, Boolean, String> rollResult = Roulette.Rolls.rollStraightUp(betNumber, stake);
            event.deferReply();
            return betOutput(user, betType, String.valueOf(betNumber), rollResult);
        }

        private static Mono<Void> splitBetSubmission(ModalSubmitInteractionEvent event){
            String betType = "Split";
            String stake = "";
            String user = event.getUser().getGlobalName().orElse("someone");
            Pair<Integer, Integer> betNumbers = new Pair<>();
            List<TextInput> textInputComponents = event.getComponents(TextInput.class);
            if (textInputComponents.isEmpty()) {return event.reply("No components found!");}
            for (TextInput component : textInputComponents) {
                if (component.getCustomId().equals("betNumbers")){
                    String[] numbersSplit = component.getValue().orElse("0,0").strip().split(",");
                    try{
                        betNumbers.setFirst(Integer.parseInt(numbersSplit[0]));
                        if (betNumbers.getFirst() > 36 | betNumbers.getFirst() <= 0){
                            numbersError(event);
                        }
                        betNumbers.setSecond(Integer.parseInt(numbersSplit[1]));
                        if (
                                betNumbers.getSecond() > 36 |
                                        betNumbers.getSecond() == -1 |
                                        (
                                                betNumbers.getSecond() != (betNumbers.getFirst()+1) &&
                                                betNumbers.getSecond() != (betNumbers.getFirst()+3)
                                        )
                        ){
                            numbersError(event);
                        }
                    } catch (NumberFormatException e){
                        numbersError(event);
                    }
                } else if (component.getCustomId().equals("betAmount")) {
                    stake = component.getValue().orElse("-1");
                }
            }
            Triple<Integer, Boolean, String> rollResult = Roulette.Rolls.rollSplitBet(betNumbers, stake);
            event.deferReply();
            return betOutput(user, betType, String.valueOf(betNumbers), rollResult);
        }

        private static Mono<Void> streetBetSubmission(ModalSubmitInteractionEvent event){
            String betType = "Street";
            String stake = "";
            String user = event.getUser().getGlobalName().orElse("someone");
            List<Integer> numbers = new ArrayList<>();
            List<TextInput> textInputComponents = event.getComponents(TextInput.class);
            if (textInputComponents.isEmpty()) {return event.reply("No components found!");}

            for (TextInput component : textInputComponents) {
                if (component.getCustomId().equals("betNumbers")) {
                    String numberToConvert = component.getValue().orElse("0");
                    if (numberToConvert.equals("0")){
                        numbersError(event);
                    }
                    int convertedNumber = 0;
                    try {
                        convertedNumber = Integer.parseInt(numberToConvert);
                    } catch (NumberFormatException e) {
                        numbersError(event);
                    }
                    numbers.add(convertedNumber);
                    numbers.add(convertedNumber+1);
                    numbers.add(convertedNumber+2);
                    if (numbers.getFirst() > 34 | numbers.getFirst() == -1) {
                        numbersError(event);
                    }
                } else if (component.getCustomId().equals("betAmount")) {
                    stake = component.getValue().orElse("-1");
                }
            }

            Triple<Integer, Boolean, String> rollResult = Roulette.Rolls.rollStreetBet(numbers, stake);
            event.deferReply();
            return betOutput(user, betType, numbers.toString(), rollResult);
        }

        private static Mono<Void> cornerBetSubmission(ModalSubmitInteractionEvent event){
            String betType = "Corner";
            String stake = "";
            String user = event.getUser().getGlobalName().orElse("someone");
            List<Integer> numbers = new ArrayList<>();
            List<TextInput> textInputComponents = event.getComponents(TextInput.class);
            if (textInputComponents.isEmpty()) {return event.reply("No components found!");}

            for (TextInput component : textInputComponents) {
                if (component.getCustomId().equals("betNumbers")) {
                    String[] numbersSplit = component.getValue().orElse("0").strip().split(",");
                    if (numbersSplit[0].equals("0")){
                        numbersError(event);
                    }
                    try {
                        numbers.add(Integer.parseInt(numbersSplit[0]));
                        numbers.add(Integer.parseInt(numbersSplit[1]));
                        numbers.add(Integer.parseInt(numbersSplit[2]));
                        numbers.add(Integer.parseInt(numbersSplit[3]));
                    } catch (Exception e) {
                        numbersError(event);
                    }
                    if (
                            numbers.getFirst() > 32 |
                                    numbers.getFirst() == -1 |
                                    numbers.get(1) == numbers.getFirst()+1 |
                                    numbers.get(2) == numbers.getFirst()+3 |
                                    numbers.get(3) == numbers.getFirst()+4
                    ) {
                        numbersError(event);
                    }
                } else if (component.getCustomId().equals("betAmount")) {
                    stake = component.getValue().orElse("-1");
                }
            }

            Triple<Integer, Boolean, String> rollResult = Roulette.Rolls.rollCornerBet(numbers, stake);
            event.deferReply();
            return betOutput(user, betType, numbers.toString(), rollResult);
        }

        private static Mono<Void> sixLineBetSubmission(ModalSubmitInteractionEvent event){
            String betType = "Six Line";
            String stake = "";
            String user = event.getUser().getGlobalName().orElse("someone");
            List<Integer> numbers = new ArrayList<>();
            List<TextInput> textInputComponents = event.getComponents(TextInput.class);
            if (textInputComponents.isEmpty()) {return event.reply("No components found!");}

            for (TextInput component : textInputComponents) {
                if (component.getCustomId().equals("betNumbers")) {
                    String[] numbersSplit = component.getValue().orElse("0").strip().split(",");
                    if (numbersSplit[0].equals("0")){
                        numbersError(event);
                    }
                    int convertedFirstNumber = 0;
                    int convertedSecondNumber = 0;
                    try {
                        convertedFirstNumber = Integer.parseInt(numbersSplit[0]);
                        convertedSecondNumber = Integer.parseInt(numbersSplit[1]);
                    } catch (NumberFormatException e) {
                        numbersError(event);
                    }
                    if (convertedSecondNumber != convertedFirstNumber + 3) {numbersError(event);}
                    for (int i = 0; i < 6; i++) {
                        numbers.add(convertedFirstNumber+i);
                    }
                    if (numbers.getFirst() > 34 | numbers.getFirst() == -1) {
                        numbersError(event);
                    }
                } else if (component.getCustomId().equals("betAmount")) {
                    stake = component.getValue().orElse("-1");
                }
            }

            Triple<Integer, Boolean, String> rollResult = Roulette.Rolls.rollSixLineBet(numbers, stake);
            event.reply();
            return betOutput(user, betType, numbers.toString(), rollResult);
        }

        private static Mono<Void> redOrBlackSubmission(ModalSubmitInteractionEvent event){
            String betType = "Red or Black";
            String stake = "";
            String user = event.getUser().getGlobalName().orElse("someone");
            String color = "";
            List<TextInput> textInputComponents = event.getComponents(TextInput.class);
            if (textInputComponents.isEmpty()) {return event.reply("No components found!");}

            for (TextInput component : textInputComponents) {
                if (component.getCustomId().equals("betNumbers")) {
                    color = component.getValue().orElse("0");
                } else if (component.getCustomId().equals("betAmount")) {
                    stake = component.getValue().orElse("-1");
                }
            }
            if (color.equals("red") | color.equals("black")){
                Triple<Integer, Boolean, String> rollResult = Roulette.Rolls.rollRedOrBlack(color, stake);
                return betOutput(user, betType, color, rollResult);
            } else {
                return event.reply()
                        .withEphemeral(true)
                        .withContent("Not the right word :p");
            }
        }

        private static Mono<Void> oddOrEvenSubmission(ModalSubmitInteractionEvent event){
            String betType = "Odd or Even";
            String stake = "";
            String user = event.getUser().getGlobalName().orElse("someone");
            String oddOrEven = "";
            List<TextInput> textInputComponents = event.getComponents(TextInput.class);
            if (textInputComponents.isEmpty()) {return event.reply("No components found!");}

            for (TextInput component : textInputComponents) {
                if (component.getCustomId().equals("betNumbers")) {
                    oddOrEven = component.getValue().orElse("0");
                } else if (component.getCustomId().equals("betAmount")) {
                    stake = component.getValue().orElse("-1");
                }
            }
            if (oddOrEven.equals("odd") | oddOrEven.equals("even")){
                Triple<Integer, Boolean, String> rollResult = Roulette.Rolls.rollOddOrEven(oddOrEven, stake);
                event.deferReply();
                return betOutput(user, betType, oddOrEven, rollResult);
            } else {
                return event.reply()
                        .withEphemeral(true)
                        .withContent("Not the right word :p");
            }
        }

        private static Mono<Void> lowOrHighSubmission(ModalSubmitInteractionEvent event){
            String betType = "Low or High";
            String stake = "";
            String user = event.getUser().getGlobalName().orElse("someone");
            String lowOrHigh = "";
            List<TextInput> textInputComponents = event.getComponents(TextInput.class);
            if (textInputComponents.isEmpty()) {return event.reply("No components found!");}

            for (TextInput component : textInputComponents) {
                if (component.getCustomId().equals("betNumbers")) {
                    lowOrHigh = component.getValue().orElse("0");
                } else if (component.getCustomId().equals("betAmount")) {
                    stake = component.getValue().orElse("-1");
                }
            }
            if (lowOrHigh.equals("low") | lowOrHigh.equals("high")){
                Triple<Integer, Boolean, String> rollResult = Roulette.Rolls.rollLowOrHigh(lowOrHigh, stake);
                event.deferReply();
                return betOutput(user, betType, lowOrHigh, rollResult);
            } else {
                return event.reply()
                        .withEphemeral(true)
                        .withContent("Not the right word :p");
            }
        }

        private static Mono<Void> dozensSubmission(ModalSubmitInteractionEvent event){
            String betType = "Dozens";
            int dozens = -1;
            String stake = "nothing apparently";
            String user = event.getUser().getGlobalName().orElse("someone");
            List<TextInput> textInputComponents = event.getComponents(TextInput.class);
            if (textInputComponents.isEmpty()) {
                return event.reply("No components found!");
            }
            for (TextInput component : textInputComponents) {
                try{
                    if (component.getCustomId().equals("betNumbers")){
                        dozens = Integer.parseInt(component.getValue().orElse("-1"));
                    } else if (component.getCustomId().equals("betAmount")) {
                        stake = component.getValue().orElse("nothing apparently");
                    }
                } catch (NumberFormatException e){
                    return event.reply()
                            .withEphemeral(true)
                            .withContent("Error converting numbers. Try again :p");
                }
            }
            System.out.println("dozen: " + dozens);
            if (dozens > 3 | dozens < 1){
                return event.reply()
                        .withEphemeral(true)
                        .withContent("Dozen number doesn't exist :p");
            }
            Triple<Integer, Boolean, String> rollResult = Roulette.Rolls.rollDozens(dozens, stake);
            event.deferReply();
            return betOutput(user, betType, String.valueOf(dozens), rollResult);
        }

        private static Mono<Void> columnsSubmission(ModalSubmitInteractionEvent event){
            String betType = "Dozens";
            int column = -1;
            String stake = "nothing apparently";
            String user = event.getUser().getGlobalName().orElse("someone");
            List<TextInput> textInputComponents = event.getComponents(TextInput.class);
            if (textInputComponents.isEmpty()) {
                return event.reply("No components found!");
            }
            for (TextInput component : textInputComponents) {
                try{
                    if (component.getCustomId().equals("betNumbers")){
                        column = Integer.parseInt(component.getValue().orElse("-1"));
                    } else if (component.getCustomId().equals("betAmount")) {
                        stake = component.getValue().orElse("nothing apparently");
                    }
                } catch (NumberFormatException e){
                    return event.reply()
                            .withEphemeral(true)
                            .withContent("Error converting numbers. Try again :p");
                }
            }
            if (column > 36 | column < 34){
                return event.reply()
                        .withEphemeral(true)
                        .withContent("Column doesn't exist :p");
            }
            Triple<Integer, Boolean, String> rollResult = Roulette.Rolls.rollColumns(column, stake);
            event.deferReply();
            return betOutput(user, betType, String.valueOf(column), rollResult);
        }

        private static Mono<Void> numbersError(ModalSubmitInteractionEvent event){
            return event.reply()
                    .withEphemeral(true)
                    .withContent("Issue with number :p");
        }

        private static Mono<Void> betOutput(String user, String betType, String bet, Triple<Integer, Boolean, String> rollResult){
            String betRollResultContent = user + "'s " + betType + " bet on: " + bet + "\n" +
                    "Rolled: " + rollResult.getFirst() + "\n";
            if (rollResult.getSecond()){
                GlobalThings.getTheCave().createMessage()
                        .withContent(betRollResultContent + Roulette.getContentW() + rollResult.getThird()).block();
            } else {
                GlobalThings.getTheCave().createMessage()
                        .withContent(betRollResultContent + Roulette.getContentL() + rollResult.getThird()).block();
            }
            return Mono.empty();
        }
    }
}


