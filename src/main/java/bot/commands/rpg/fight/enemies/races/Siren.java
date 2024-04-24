package bot.commands.rpg.fight.enemies.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.Targetting;
import bot.commands.rpg.fight.Targetting.TargetCheck;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.FighterClass;
import bot.data.fight.FighterStatus;

public class Siren extends EnemiesOfRace {
	public static final String SIREN_1 = "SIREN_1";
	public static final String SIREN_2 = "SIREN_2";
	public static final String SIREN_3 = "SIREN_3";
	public static final String SIREN_4 = "SIREN_4";

	public Siren() {
		super(MonsterGirlRace.SIREN);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		makeBuilderOld(SIREN_1)//
				.strength(3).agility(7).intelligence(5)//
				.baseHp(5)//
				.diff(2)//
				.classes(FighterClass.FLYING, FighterClass.USES_MAGIC)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom( //
						pair(4, RPGFightAction.ATTACK_A_1), //
						pair(1, RPGFightAction.SIREN_SONG)))//
				.build(rpgEnemies);
		makeBuilderOld(SIREN_2)//
				.strength(4).agility(9).intelligence(6)//
				.baseHp(10)//
				.diff(2)//
				.classes(FighterClass.FLYING, FighterClass.USES_MAGIC)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom( //
						pair(3, RPGFightAction.ATTACK_A_1), //
						pair(1, RPGFightAction.SIREN_SONG)))//
				.build(rpgEnemies);
		makeBuilderOld(SIREN_3)//
				.strength(5).agility(10).intelligence(8)//
				.baseHp(10)//
				.diff(3)//
				.classes(FighterClass.FLYING, FighterClass.USES_MAGIC)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom( //
						pair(2, RPGFightAction.ATTACK_A_1), //
						pair(1, RPGFightAction.SIREN_SONG)))//
				.build(rpgEnemies);

		final Targetting charmedTargetting = new Targetting(
				TargetCheck.ENEMY.with(FighterStatus.CHARMED).withStacksLessThan(FighterStatus.CHARM_RESISTANCE, 4));
		final Targetting notMechanicalTargetting = new Targetting(
				TargetCheck.ENEMY.without(fluffer10kFun, FighterClass.MECHANICAL));

		makeBuilderOld(SIREN_4)//
				.strength(5).agility(11).intelligence(11)//
				.baseHp(10)//
				.diff(10)//
				.classes(FighterClass.CLEVER, FighterClass.FLYING, FighterClass.USES_MAGIC)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFromTargetted( //
						data -> RPGFightAction.ATTACK_A_1, //
						pair(charmedTargetting, data -> RPGFightAction.ATTACK_A_1), //
						pair(notMechanicalTargetting, data -> RPGFightAction.SIREN_SONG)))//
				.build(rpgEnemies);
	}
}
