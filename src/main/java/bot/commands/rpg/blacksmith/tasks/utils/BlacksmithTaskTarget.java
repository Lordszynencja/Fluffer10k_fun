package bot.commands.rpg.blacksmith.tasks.utils;

import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.items.Items;
import bot.userData.ServerUserData;

public interface BlacksmithTaskTarget {
	public static BlacksmithTaskTargetItem itemTarget(final String itemId) {
		return new BlacksmithTaskTargetItem(itemId);
	}

	public static BlacksmithTaskTargetItem itemTarget(final String itemId, final long amount) {
		return new BlacksmithTaskTargetItem(itemId, amount);
	}

	public static BlacksmithTaskTargetMultiple multipleTargets(final BlacksmithTaskTarget... targets) {
		return new BlacksmithTaskTargetMultiple(targets);
	}

	public static BlacksmithTaskTargetMonsterGirlRace raceTarget(final MonsterGirlRace race, final int amount) {
		return new BlacksmithTaskTargetMonsterGirlRace(race, amount);
	}

	public boolean isMet(ServerUserData userData);

	public String progressDescription(ServerUserData userData, Items items);

	public void apply(ServerUserData userData);
}
