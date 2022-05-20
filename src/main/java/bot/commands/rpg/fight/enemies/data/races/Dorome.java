package bot.commands.rpg.fight.enemies.data.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;

public class Dorome extends EnemiesOfRace {
	public static final String DOROME_1 = "DOROME_1";
	public static final String DOROME_2 = "DOROME_2";
	public static final String DOROME_3 = "DOROME_3";

	public Dorome() {
		super(MonsterGirlRace.DOROME);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		makeBuilder(DOROME_1)//
				.strength(7).agility(1).intelligence(1)//
				.baseHp(10)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom( //
						pair(1, RPGFightAction.ATTACK_S_3), //
						pair(1, RPGFightAction.GRAB_SLIME)))//
				.build(rpgEnemies);
		makeBuilder(DOROME_2)//
				.strength(10).agility(2).intelligence(2)//
				.baseHp(15)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom( //
						pair(1, RPGFightAction.ATTACK_S_3), //
						pair(1, RPGFightAction.GRAB_SLIME)))//
				.build(rpgEnemies);
		makeBuilder(DOROME_3)//
				.strength(14).agility(3).intelligence(3)//
				.baseHp(25)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom( //
						pair(1, RPGFightAction.ATTACK_S_3), //
						pair(1, RPGFightAction.GRAB_SLIME)))//
				.build(rpgEnemies);
	}
}