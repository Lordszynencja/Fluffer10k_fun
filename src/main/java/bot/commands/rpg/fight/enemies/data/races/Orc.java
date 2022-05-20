package bot.commands.rpg.fight.enemies.data.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;

public class Orc extends EnemiesOfRace {
	public static final String ORC_1 = "ORC_1";
	public static final String ORC_2 = "ORC_2";
	public static final String ORC_3 = "ORC_3";

	public Orc() {
		super(MonsterGirlRace.ORC);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		makeBuilder(ORC_1)//
				.strength(8).agility(4).intelligence(3)//
				.baseHp(10)//
				.diff(1)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(4, RPGFightAction.ATTACK_S_8), //
						pair(1, RPGFightAction.GRAB)))//
				.build(rpgEnemies);
		makeBuilder(ORC_2)//
				.strength(12).agility(6).intelligence(3)//
				.baseHp(15)//
				.diff(1)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(3, RPGFightAction.ATTACK_S_8), //
						pair(1, RPGFightAction.GRAB)))//
				.build(rpgEnemies);
		makeBuilder(ORC_3)//
				.strength(15).agility(7).intelligence(4)//
				.baseHp(20)//
				.diff(1)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(4, RPGFightAction.ATTACK_S_8), //
						pair(1, RPGFightAction.SPECIAL_ATTACK_BASH), //
						pair(1, RPGFightAction.GRAB)))//
				.build(rpgEnemies);
	}
}