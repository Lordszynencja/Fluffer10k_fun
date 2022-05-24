package bot.commands.rpg.blacksmith.tasks.data;

import static bot.commands.rpg.blacksmith.tasks.utils.BlacksmithTaskTarget.raceTarget;
import static bot.data.items.data.WeaponItems.BOLAS;
import static bot.data.items.data.WeaponItems.DWARVEN_AXE;
import static bot.data.items.data.WeaponItems.DWARVEN_BOW;
import static bot.data.items.data.WeaponItems.ELVEN_BOW;
import static bot.data.items.data.WeaponItems.GAUNTLET;
import static bot.data.items.data.WeaponItems.HEAVY_CROSSBOW;
import static bot.data.items.data.WeaponItems.HOOK_SWORD;
import static bot.data.items.data.WeaponItems.KATANA;
import static bot.data.items.data.WeaponItems.MAGIC_SWORD;
import static bot.data.items.data.WeaponItems.RITUAL_DAGGER;
import static bot.data.items.data.WeaponItems.RUNIC_SCIMITAR;
import static bot.data.items.data.WeaponItems.SHURIKENS;
import static bot.data.items.data.WeaponItems.TRIDENT;
import static bot.data.items.data.WeaponItems.getArchmageSpellbookId;
import static bot.data.items.data.WeaponItems.getArchmageStaffId;
import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

import bot.commands.rpg.blacksmith.tasks.BlacksmithTier;
import bot.commands.rpg.blacksmith.tasks.utils.BlacksmithTaskBuilder;
import bot.commands.rpg.blacksmith.tasks.utils.BlacksmithTaskTarget;
import bot.commands.rpg.blacksmith.tasks.utils.BlacksmithTaskTargetItemGroup;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.items.data.GemItems.GemType;

public class Tier3BlacksmithTasks {
	private static void add(final String id, final String description, final BlacksmithTaskTarget target) {
		new BlacksmithTaskBuilder("TIER_3_" + id)//
				.tier(BlacksmithTier.TIER_3)//
				.description(description)//
				.target(target)//
				.add();
	}

	public static void addTasks() {
		final List<String> weapons = new ArrayList<>(asList(//
				BOLAS, //
				DWARVEN_AXE, //
				DWARVEN_BOW, //
				ELVEN_BOW, //
				GAUNTLET, //
				HEAVY_CROSSBOW, //
				HOOK_SWORD, //
				KATANA, //
				MAGIC_SWORD, //
				RITUAL_DAGGER, //
				RUNIC_SCIMITAR, //
				SHURIKENS, //
				TRIDENT));
		for (final GemType gemType : GemType.values()) {
			weapons.add(getArchmageSpellbookId(gemType));
			weapons.add(getArchmageStaffId(gemType));
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

		add("BAPHOMET", //
				"Baphomet seized bunch of my weapons! Take them back and I'll give you a blueprint.", //
				raceTarget(MonsterGirlRace.BAPHOMET, 1));
		add("CHIMAERA", //
				"Chimaera is terrorizing the nearby mine! Defeat her and I'll give you a blueprint.", //
				raceTarget(MonsterGirlRace.CHIMAERA, 1));
	}
}
