package bot.commands.rpg.fight.enemies;

import static bot.util.CollectionUtils.addToSet;
import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.FighterClass;

public class RPGEnemyMonsterGirlData extends RPGEnemyData {
	public static final int calculateLevel(final int strength, final int agility, final int intelligence,
			final int armor, final int baseHp, final int diff, final Set<FighterClass> classes) {
		final List<Integer> stats = new ArrayList<>(asList(strength, agility, intelligence));
		stats.sort(null);
		final double statsValue = stats.get(0) * 0.45 + stats.get(1) * 0.7 + stats.get(2) * 1.1 + armor + baseHp / 8.0;
		double valueWithModifiers = statsValue - 4;
		if (valueWithModifiers < 0) {
			valueWithModifiers = 0;
		}

		int totalValue = 0;
		if (valueWithModifiers > 30) {
			totalValue += (valueWithModifiers - 30) * 0.6;
			valueWithModifiers = 30;
		}
		if (valueWithModifiers > 20) {
			totalValue += (valueWithModifiers - 20) * 0.7;
			valueWithModifiers = 20;
		}
		if (valueWithModifiers > 10) {
			totalValue += (valueWithModifiers - 10) * 0.75;
			valueWithModifiers = 10;
		}
		totalValue += valueWithModifiers * 0.8;

		if (classes.contains(FighterClass.CANT_BE_FROZEN)) {
			totalValue += 1;
		}
		if (classes.contains(FighterClass.SALTABLE)) {
			totalValue -= 1;
		}

		if (classes.contains(FighterClass.CLEVER)) {
			totalValue += totalValue * 0.3;
		}
		if (classes.contains(FighterClass.DOUBLE_ATTACK)) {
			totalValue += totalValue * 0.1;
		}
		if (classes.contains(FighterClass.FLYING)) {
			totalValue += totalValue * 0.1;
		}
		if (classes.contains(FighterClass.FREEZING_AURA)) {
			totalValue += totalValue * 0.05;
		}
		if (classes.contains(FighterClass.MECHANICAL)) {
			totalValue += totalValue * 0.05;
		}
		if (classes.contains(FighterClass.POISONOUS_ATTACK)) {
			totalValue += totalValue * 0.05;
		}
		if (classes.contains(FighterClass.SLIME_REGEN)) {
			totalValue += totalValue * 0.1;
		}
		if (classes.contains(FighterClass.STARTS_IN_SPIRIT_FORM)) {
			totalValue += totalValue * 0.1;
		}
		if (classes.contains(FighterClass.TRIPLE_ATTACK)) {
			totalValue += totalValue * 0.25;
		}

		return totalValue + diff;
	}

	public static RPGEnemyMonsterGirlData from(final String id, final MonsterGirlRace race, final String name,
			final int strength, final int agility, final int intelligence, final int armor, final int baseHp,
			final int diff, final Set<FighterClass> classes, final ActionSelector actionSelector) {
		final int level = calculateLevel(strength, agility, intelligence, armor, baseHp, diff, classes);

		return new RPGEnemyMonsterGirlData(id, race, name, strength, agility, intelligence, armor, baseHp, level,
				classes, actionSelector);
	}

	public final MonsterGirlRace race;

	public RPGEnemyMonsterGirlData(final String id, final MonsterGirlRace race, final String name, final int strength,
			final int agility, final int intelligence, final int armor, final int baseHp, final int level,
			final Set<FighterClass> classes, final ActionSelector actionSelector) {
		super(id, EnemyType.MONSTER_GIRL, name, strength, agility, intelligence, armor, baseHp, level,
				addToSet(classes, FighterClass.MONSTER_GIRL), actionSelector);

		this.race = race;
	}

	@Override
	public String imgUrl() {
		return race.imageLink;
	}

}
