package bot.commands.rpg.blacksmith.tasks.data;

import static bot.commands.rpg.blacksmith.tasks.utils.BlacksmithTaskTarget.raceTarget;
import static bot.data.items.data.WeaponItems.CHAINED_KUNAI;
import static bot.data.items.data.WeaponItems.DARTS;
import static bot.data.items.data.WeaponItems.JAVELIN;
import static bot.data.items.data.WeaponItems.KAMA;
import static bot.data.items.data.WeaponItems.KNIFE;
import static bot.data.items.data.WeaponItems.KUNAI;
import static bot.data.items.data.WeaponItems.SCYTHE;
import static bot.data.items.data.WeaponItems.SHORT_BOW;
import static bot.data.items.data.WeaponItems.SHORT_SWORD;
import static bot.data.items.data.WeaponItems.SLINGSHOT;
import static bot.data.items.data.WeaponItems.WHIP;
import static bot.data.items.data.WeaponItems.getApprenticeBookId;
import static bot.data.items.data.WeaponItems.getApprenticeStaffId;
import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

import bot.commands.rpg.blacksmith.tasks.BlacksmithTier;
import bot.commands.rpg.blacksmith.tasks.utils.BlacksmithTaskBuilder;
import bot.commands.rpg.blacksmith.tasks.utils.BlacksmithTaskTarget;
import bot.commands.rpg.blacksmith.tasks.utils.BlacksmithTaskTargetItemGroup;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.items.data.GemItems.GemType;

public class Tier1BlacksmithTasks {
	private static void add(final String id, final String description, final BlacksmithTaskTarget target) {
		new BlacksmithTaskBuilder("TIER_1_" + id)//
				.tier(BlacksmithTier.TIER_1)//
				.description(description)//
				.target(target)//
				.add();
	}

	public static void addTasks() {
		final List<String> weapons = new ArrayList<>(asList(//
				CHAINED_KUNAI, //
				DARTS, //
				JAVELIN, //
				KAMA, //
				KNIFE, //
				KUNAI, //
				SCYTHE, //
				SHORT_BOW, //
				SHORT_SWORD, //
				SLINGSHOT, //
				WHIP));
		for (final GemType gemType : GemType.values()) {
			weapons.add(getApprenticeBookId(gemType));
			weapons.add(getApprenticeStaffId(gemType));
		}

		final BlacksmithTaskTargetItemGroup weaponTarget = new BlacksmithTaskTargetItemGroup(weapons, "tier 1 weapon");

		final int weaponTasks = 10;
		final String[] descriptions = { //
				"If you give me a weapon, I'll give you a blueprint.", //
				"I need a weapon to stock up, I'll share a blueprint if you give me one", //
				"Weapon for a blueprint is a deal, yes?", //
				"I need to put something on display, can you find me a nice looking weapon?", //
				"I'm low on stock, any weapon will do, you will get a blueprint for that" };

		for (int i = 0; i < weaponTasks; i++) {
			add("WEAPON_" + i, descriptions[i % descriptions.length], weaponTarget);
		}

		add("BEELZEBUB", //
				"A beelzebub stole my tools, please take them back. I'll reward you with a blueprint", //
				raceTarget(MonsterGirlRace.BEELZEBUB, 1));
		add("CURSED_SWORD", //
				"A cursed sword is tempting people to use her instead of normal weapons, please stop her. I'll reward you with a blueprint", //
				raceTarget(MonsterGirlRace.CURSED_SWORD, 1));
		add("DWARF", //
				"An other blacksmith is telling everyone my weapons suck, please stop her. I'll reward you with a blueprint", //
				raceTarget(MonsterGirlRace.DWARF, 1));
		add("GYOUBU_DANUKI", //
				"A danuki sold me fake ore! Please teach her a lesson and I'll reward you with a blueprint", //
				raceTarget(MonsterGirlRace.GYOUBU_DANUKI, 1));
		add("HAKUTAKU", //
				"A hakutaku that lives nearby is taking my clients away by teaching everyone that magic is better for defense. Show her that weapons are just as good and I'll reward you with a blueprint", //
				raceTarget(MonsterGirlRace.HAKUTAKU, 1));
		add("HOLSTAUR", //
				"A holstaur is on a rampage nearby, stopping my ore transport. Scare her away and I'll reward you with a blueprint", //
				raceTarget(MonsterGirlRace.HOLSTAUR, 1));
		add("KAKUEN", //
				"A kakuen stole a weapon from me, please get it back, I'll reward you with a blueprint.", //
				raceTarget(MonsterGirlRace.KAKUEN, 1));
		add("KAMAITACHI", //
				"A group of kamaitachi stole my stuff, please get it back, I'll reward you with a blueprint.", //
				raceTarget(MonsterGirlRace.KAMAITACHI, 1));
		add("LIZARDMAN", //
				"A lizardman girl said my weapons suck, go show her that she is wrong! I'll give you a blueprint for that.", //
				raceTarget(MonsterGirlRace.LIZARDMAN, 1));
		add("OGRE", //
				"An ogre stole my ore! I'll give you a blueprint if you get it back.", //
				raceTarget(MonsterGirlRace.OGRE, 1));
		add("ORC", //
				"An orc stole weapons from me! I'll give you a blueprint if you get them back.", //
				raceTarget(MonsterGirlRace.ORC, 1));
		add("PARASITE_SLIME_SLIME_CARRIER", //
				"A parasite slime carrier stole my stuff! I'll give you a blueprint if you get it back.", //
				raceTarget(MonsterGirlRace.PARASITE_SLIME_SLIME_CARRIER, 1));
		add("RED_ONI", //
				"A red oni wrecks havoc nearby! I'll give you a blueprint if you stop her.", //
				raceTarget(MonsterGirlRace.RED_ONI, 1));
		add("SATYROS", //
				"A drunk satyros stormed in and took bunch of my stuff! I'll give you a blueprint if you get it back.", //
				raceTarget(MonsterGirlRace.SATYROS, 1));
	}
}