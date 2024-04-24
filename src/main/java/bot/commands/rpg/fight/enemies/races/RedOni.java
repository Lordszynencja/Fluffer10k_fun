package bot.commands.rpg.fight.enemies.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;

public class RedOni extends EnemiesOfRace {
	public static final String RED_ONI_1 = "RED_ONI_1";
	public static final String RED_ONI_2 = "RED_ONI_2";
	public static final String RED_ONI_3 = "RED_ONI_3";

	public RedOni() {
		super(MonsterGirlRace.RED_ONI);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		makeBuilderOld(RED_ONI_1)//
				.strength(14).agility(6).intelligence(6)//
				.baseHp(20)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(6, RPGFightAction.ATTACK_S_13), //
						pair(1, RPGFightAction.SAKE)))//
				.build(rpgEnemies);
		makeBuilderOld(RED_ONI_2)//
				.strength(19).agility(8).intelligence(7)//
				.baseHp(25)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(4, RPGFightAction.ATTACK_S_13), //
						pair(1, RPGFightAction.SAKE)))//
				.build(rpgEnemies);
		makeBuilderOld(RED_ONI_3)//
				.strength(22).agility(9).intelligence(8)//
				.baseHp(30)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(3, RPGFightAction.ATTACK_S_13), //
						pair(1, RPGFightAction.SAKE)))//
				.build(rpgEnemies);
	}
}
