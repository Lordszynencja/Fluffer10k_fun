package bot.commands.rpg.blacksmith.tasks.utils;

import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.items.Items;
import bot.userData.ServerUserData;

public class BlacksmithTaskTargetMonsterGirlRace implements BlacksmithTaskTarget {

	private final MonsterGirlRace race;
	private final int amount;

	public BlacksmithTaskTargetMonsterGirlRace(final MonsterGirlRace race, final int amount) {
		this.race = race;
		this.amount = amount;
	}

	@Override
	public boolean isMet(final ServerUserData userData) {
		return userData.blacksmith.currentTask.monsterGirlsDefeated.getOrDefault(race, 0) >= amount;
	}

	@Override
	public String progressDescription(final ServerUserData userData, final Items items) {
		final int defeated = userData == null || userData.blacksmith.currentTask == null ? 0
				: userData.blacksmith.currentTask.monsterGirlsDefeated.getOrDefault(race, 0);
		return race.race + " defeated: " + defeated + "/" + amount;
	}

	@Override
	public void apply(final ServerUserData userData) {
	}
}
