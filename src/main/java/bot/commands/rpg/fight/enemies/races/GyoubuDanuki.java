package bot.commands.rpg.fight.enemies.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.FighterClass;

public class GyoubuDanuki extends EnemiesOfRace {
	public static final String GYOUBU_DANUKI_1 = "GYOUBU_DANUKI_1";
	public static final String GYOUBU_DANUKI_2 = "GYOUBU_DANUKI_2";

	public GyoubuDanuki() {
		super(MonsterGirlRace.GYOUBU_DANUKI);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		makeBuilderOld(GYOUBU_DANUKI_1)//
				.strength(2).agility(4).intelligence(12)//
				.baseHp(5)//
				.classes(FighterClass.USES_MAGIC)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(4, RPGFightAction.ATTACK_A_1), //
						pair(1, RPGFightAction.CHARM_RATATOSKR), //
						pair(1, RPGFightAction.SUCCUBUS_NOSTRUM)))//
				.build(rpgEnemies);
		makeBuilderOld(GYOUBU_DANUKI_2)//
				.strength(2).agility(4).intelligence(12)//
				.baseHp(10)//
				.diff(1)//
				.classes(FighterClass.USES_MAGIC)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(3, RPGFightAction.ATTACK_A_1), //
						pair(1, RPGFightAction.CHARM_RATATOSKR), //
						pair(1, RPGFightAction.DORMOUSE_SLEEP), //
						pair(1, RPGFightAction.BUNSHIN_NO_JUTSU)))//
				.build(rpgEnemies);
	}
}