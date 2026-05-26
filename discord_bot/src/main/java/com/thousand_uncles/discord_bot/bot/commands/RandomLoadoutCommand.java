package com.thousand_uncles.discord_bot.bot.commands;

import com.thousand_uncles.discord_bot.bot.util.GlobalThings;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Random;

@Component
public class RandomLoadoutCommand implements SlashCommand {
    String[][][] items;
    String[] classes = {"Scout", "Soldier", "Pyro", "Demo", "Heavy", "Engineer", "Medic", "Sniper", "Spy"};



    RandomLoadoutCommand(){
        System.out.println("Generating random sequence seed...");
        GlobalThings.rand = new Random();

        items = new String[9][][];

// Scout
        items[0] = new String[3][];
        items[0][0] = new String[] {"Scattergun", "Force-a-Nature", "Shortstop", "Soda Popper", "Baby Face's Blaster", "Back Scatter"};
        items[0][1] = new String[] {"Pistol", "Bonk! Atomic Punch", "Crit-a-Cola", "Flying Guillotine", "Mad Milk", "Pretty Boy's Pocket Pistol", "Winger"};
        items[0][2] = new String[] {"Bat", "Atomizer", "Boston Basher", "Candy Cane", "Fan O'War", "Sandman", "Sun-on-a-Stick", "Wrap Assassin"};

// Soldier
        items[1] = new String[3][];
        items[1][0] = new String[] {"Rocket Launcher", "Original", "Air Strike", "Beggar's Bazooka", "Black Box", "Cow Mangler 500", "Direct Hit", "Liberty Launcher", "Rocket Jumper"};
        items[1][1] = new String[] {"Shotgun", "Battalion's Backup", "Buff Banner", "Concheror", "Gunboats", "Mantreads", "Panic Attack", "Reserve Shooter", "Righteous Bison"};
        items[1][2] = new String[] {"Shovel", "Disciplinary Action", "Equalizer", "Escape Plan", "Half-Zatoichi", "Market Gardener", "Pain Train"};

// Pyro
        items[2] = new String[3][];
        items[2][0] = new String[] {"Flame Thrower", "Backburner", "Degreaser", "Dragon's Fury", "Phlogistinator"};
        items[2][1] = new String[] {"Shotgun", "Detonator", "Flare Gun", "Gas Passer", "Manmelter", "Panic Attack", "Reserve Shooter", "Scorch Shot", "Thermal Thruster"};
        items[2][2] = new String[] {"Fire Axe", "Axtinguisher", "Back Scratcher", "Homewrecker", "Hot Hand", "Neon Annihilator", "Powerjack", "Sharpened Volcano Fragment", "Third Degree"};

// Demoman
        items[3] = new String[3][];
        items[3][0] = new String[] {"Grenade Launcher", "Ali Baba's Wee Booties", "B.A.S.E. Jumper", "Iron Bomber", "Loch-n-Load", "Loose Cannon"};
        items[3][1] = new String[] {"Stickybomb Launcher", "Chargin' Targe", "Quickiebomb Launcher", "Scottish Resistance", "Splendid Screen", "Sticky Jumper", "Tide Turner"};
        items[3][2] = new String[] {"Bottle", "Claidheamh Mòr", "Eyelander", "Half-Zatoichi", "Pain Train", "Persian Persuader", "Scotsman's Skullcutter", "Ullapool Caber"};

// Heavy
        items[4] = new String[3][];
        items[4][0] = new String[] {"Minigun", "Brass Beast", "Huo-Long Heater", "Natascha", "Tomislav"};
        items[4][1] = new String[] {"Shotgun", "Buffalo Steak Sandvich", "Dalokohs Bar", "Family Business", "Panic Attack", "Sandvich", "Second Banana"};
        items[4][2] = new String[] {"Fists", "Eviction Notice", "Fists of Steel", "Gloves of Running Urgently", "Holiday Punch", "Killing Gloves of Boxing", "Warrior's Spirit"};

// Engineer
        items[5] = new String[3][];
        items[5][0] = new String[] {"Shotgun", "Frontier Justice", "Panic Attack", "Pomson 6000", "Rescue Ranger", "Widowmaker"};
        items[5][1] = new String[] {"Pistol", "Short Circuit", "Wrangler"};
        items[5][2] = new String[] {"Wrench", "Eureka Effect", "Gunslinger", "Jag", "Southern Hospitality"};

// Medic
        items[6] = new String[3][];
        items[6][0] = new String[] {"Syringe Gun", "Blutsauger", "Crusader's Crossbow", "Overdose"};
        items[6][1] = new String[] {"Medi Gun", "Kritzkrieg", "Quick-Fix", "Vaccinator"};
        items[6][2] = new String[] {"Bonesaw", "Amputator", "Solemn Vow", "Übersaw", "Vita-Saw"};

// Sniper
        items[7] = new String[3][];
        items[7][0] = new String[] {"Sniper Rifle", "Bazaar Bargain", "The Classic", "Hitman's Heatmaker", "Huntsman", "Machina", "Sydney Sleeper"};
        items[7][1] = new String[] {"Submachine Gun", "Cleaner's Carbine", "Cozy Camper", "Darwin's Danger Shield", "Jarate", "Razorback"};
        items[7][2] = new String[] {"Kukri", "Bushwacka", "Shahanshah", "Tribalman's Shiv"};

// Spy
        items[8] = new String[3][];
        items[8][0] = new String[] {"Revolver", "Ambassador", "Enforcer", "Diamondback", "L'Etranger"};
        items[8][1] = new String[] {"Invis watch", "Cloak and Dagger", "Dead Ringer"};
        items[8][2] = new String[] {"Knife", "Big Earner", "Conniver's Kunai", "Spy-cicle", "Your Eternal Reward"};

    }

    @Override
    public String getName() {
        return "random_loadout";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {

//        Class
        int tf2_class = GlobalThings.rand.nextInt(9);
        String response = "You're playing:\n" + classes[tf2_class];

//        Primary
        int n = GlobalThings.rand.nextInt(items[tf2_class][0].length);
        response += "\n Primary: " + items[tf2_class][0][n];

//        Secondary
        n = GlobalThings.rand.nextInt(items[tf2_class][1].length);
        response += "\n Secondary: " + items[tf2_class][1][n];

//        Melee
        n = GlobalThings.rand.nextInt(items[tf2_class][2].length);
        response += "\n Melee: " + items[tf2_class][2][n];


        return event.reply()
                .withEphemeral(true)
                .withContent(response);
    }
}