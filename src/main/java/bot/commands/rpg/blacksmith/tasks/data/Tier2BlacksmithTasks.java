package bot.commands.rpg.blacksmith.tasks.data;

import static bot.commands.rpg.blacksmith.tasks.utils.BlacksmithTaskTarget.itemTarget;
import static bot.commands.rpg.blacksmith.tasks.utils.BlacksmithTaskTarget.raceTarget;
import static bot.data.items.data.WeaponItems.BATTLE_AXE;
import static bot.data.items.data.WeaponItems.BLOWGUN;
import static bot.data.items.data.WeaponItems.BROADSWORD;
import static bot.data.items.data.WeaponItems.CLAYMORE;
import static bot.data.items.data.WeaponItems.DAGGER;
import static bot.data.items.data.WeaponItems.ESTOC;
import static bot.data.items.data.WeaponItems.GLAIVE;
import static bot.data.items.data.WeaponItems.HALBERD;
import static bot.data.items.data.WeaponItems.KAMA;
import static bot.data.items.data.WeaponItems.KUKRI;
import static bot.data.items.data.WeaponItems.KUSARIGAMA;
import static bot.data.items.data.WeaponItems.LIGHT_CROSSBOW;
import static bot.data.items.data.WeaponItems.LONG_BOW;
import static bot.data.items.data.WeaponItems.LONG_SWORD;
import static bot.data.items.data.WeaponItems.MACHETE;
import static bot.data.items.data.WeaponItems.NUNCHUCK;
import static bot.data.items.data.WeaponItems.RAPIER;
import static bot.data.items.data.WeaponItems.SPEAR;
import static bot.data.items.data.WeaponItems.SPIKED_KNUCKLES;
import static bot.data.items.data.WeaponItems.SPIKED_MACE;
import static bot.data.items.data.WeaponItems.THROWING_KNIVES;
import static bot.data.items.data.WeaponItems.TOMAHAWK;
import static bot.data.items.data.WeaponItems.getMageSpellbookId;
import static bot.data.items.data.WeaponItems.getMageStaffId;
import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

import bot.commands.rpg.blacksmith.tasks.BlacksmithTier;
import bot.commands.rpg.blacksmith.tasks.utils.BlacksmithTaskBuilder;
import bot.commands.rpg.blacksmith.tasks.utils.BlacksmithTaskTarget;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.items.data.GemItems.GemType;

public class Tier2BlacksmithTasks {
	private static void add(final String id, final String description, final BlacksmithTaskTarget target) {
		new BlacksmithTaskBuilder("TIER_2_" + id)//
				.tier(BlacksmithTier.TIER_2)//
				.description(description)//
				.target(target)//
				.add();
	}

	public static void addTasks() {
		final List<String> weapons = new ArrayList<>(asList( //
				BATTLE_AXE, //
				BLOWGUN, //
				BROADSWORD, //
				CLAYMORE, //
				DAGGER, //
				ESTOC, //
				GLAIVE, //
				HALBERD, //
				KAMA, //
				KUKRI, //
				KUSARIGAMA, //
				LIGHT_CROSSBOW, //
				LONG_BOW, //
				LONG_SWORD, //
				MACHETE, //
				NUNCHUCK, //
				RAPIER, //
				SPEAR, //
				SPIKED_KNUCKLES, //
				SPIKED_MACE, //
				THROWING_KNIVES, //
				TOMAHAWK));
		for (final GemType gemType : GemType.values()) {
			weapons.add(getMageSpellbookId(gemType));
			weapons.add(getMageStaffId(gemType));
		}

		for (final String weaponId : weapons) {
			add(weaponId, //
					"I couldn't make a weapon in time, bring me it so I can sell it and I'll give you a blueprint.", //
					itemTarget(weaponId));
		}

		add("CROW_TENGU", //
				"Some crow tengu is causing trouble on my supply line! I'll give you a blueprint if you defeat her.", //
				raceTarget(MonsterGirlRace.CROW_TENGU, 1));
		add("DRAGON_ZOMBIE", //
				"Dragon zombie attacked mine I get ore from! I'll give you a blueprint if you manage to secure it.", //
				raceTarget(MonsterGirlRace.DRAGON_ZOMBIE, 1));
		add("ICE_QUEEN", //
				"Ice queen stopped my ore transport! I'll give you a blueprint if you defeat her.", //
				raceTarget(MonsterGirlRace.ICE_QUEEN, 1));
		add("LILIM", //
				"Lilim took my stuff for free! I'll give you a blueprint if you teach her that it's wrong.", //
				raceTarget(MonsterGirlRace.LILIM, 1));
		add("OCELOMEH", //
				"An ocelomeh says that all my weapons are worthless! I'll give you a blueprint if you prove her wrong.", //
				raceTarget(MonsterGirlRace.OCELOMEH, 1));
		add("SUCCUBUS", //
				"A succubus is making everyone not fight and make love instead, which drives my sales down! I'll give you a blueprint if you scare her away.", //
				raceTarget(MonsterGirlRace.SUCCUBUS, 1));
		add("WURM", //
				"Some wurm is eating my ore! Scare her away and I'll give you a blueprint.", //
				raceTarget(MonsterGirlRace.WURM, 1));
	}
}
