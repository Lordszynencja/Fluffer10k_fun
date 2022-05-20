package bot.commands.rpg.fight.enemies.data.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;

public class Kakuen extends EnemiesOfRace {
	public static final String KAKUEN_1 = "KAKUEN_1";
	public static final String KAKUEN_2 = "KAKUEN_2";
	public static final String KAKUEN_3 = "KAKUEN_3";

	public Kakuen() {
		super(MonsterGirlRace.KAKUEN);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		makeBuilder(KAKUEN_1)//
				.strength(4).agility(9).intelligence(5)//
				.baseHp(15)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(5, RPGFightAction.ATTACK_A_2), //
						pair(1, RPGFightAction.GRAB_CLING)))//
				.build(rpgEnemies);
		makeBuilder(KAKUEN_2)//
				.strength(6).agility(12).intelligence(6)//
				.baseHp(20)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(4, RPGFightAction.ATTACK_A_2), //
						pair(1, RPGFightAction.GRAB_CLING)))//
				.build(rpgEnemies);
		makeBuilder(KAKUEN_3)//
				.strength(7).agility(15).intelligence(7)//
				.baseHp(25)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(3, RPGFightAction.ATTACK_A_2), //
						pair(1, RPGFightAction.GRAB_CLING)))//
				.build(rpgEnemies);
	}
}