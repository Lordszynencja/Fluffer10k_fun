package bot.commands.rpg.blacksmith.tasks.data;

import static bot.commands.rpg.blacksmith.tasks.utils.BlacksmithTaskTarget.raceTarget;

import bot.commands.rpg.blacksmith.tasks.BlacksmithTier;
import bot.commands.rpg.blacksmith.tasks.utils.BlacksmithTaskBuilder;
import bot.commands.rpg.blacksmith.tasks.utils.BlacksmithTaskTarget;
import bot.data.MonsterGirls.MonsterGirlRace;

public class TierEnchantBlacksmithTasks {
	private static void add(final String id, final String description, final BlacksmithTaskTarget target) {
		new BlacksmithTaskBuilder("TIER_ENCHANT_" + id)//
				.tier(BlacksmithTier.TIER_ENCHANTMENT)//
				.description(description)//
				.target(target)//
				.add();
	}

	public static void addTasks() {
		add("WURM_ATTACK_1", //
				"Some wurm ate my ores! Explain to her that it's wrong and I'll give you a blueprint.", //
				BlacksmithTaskTarget.multipleTargets(raceTarget(MonsterGirlRace.WURM, 1)));
	}
}
