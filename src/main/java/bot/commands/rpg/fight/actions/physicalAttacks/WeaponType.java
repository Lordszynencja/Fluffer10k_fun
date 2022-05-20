package bot.commands.rpg.fight.actions.physicalAttacks;

import java.util.function.Function;

import bot.commands.rpg.RPGStatUtils.RPGStatsData;

public enum WeaponType {
	STRENGTH(stats -> stats.strength / 4.0), //
	AGILITY(stats -> stats.agility / 6.0);

	private final Function<RPGStatsData, Double> bonusCalculator;

	public double getStatDmgBonus(final RPGStatsData stats) {
		return bonusCalculator.apply(stats);
	}

	private WeaponType(final Function<RPGStatsData, Double> bonusCalculator) {
		this.bonusCalculator = bonusCalculator;
	}
}