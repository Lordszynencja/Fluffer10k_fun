package bot.commands.rpg.fight.enemies.data.races;

import static bot.commands.rpg.fight.RPGFightAction.ATTACK_A_1;
import static bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.action;
import static bot.data.fight.FighterClass.FLYING;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.MonsterGirls.MonsterGirlRace;

public class CrowTengu extends EnemiesOfRace {
	public static final String CROW_TENGU_0 = "CROW_TENGU_0";
	public static final String CROW_TENGU_1 = "CROW_TENGU_1";
	public static final String CROW_TENGU_2 = "CROW_TENGU_2";
	public static final String CROW_TENGU_3 = "CROW_TENGU_3";
	public static final String CROW_TENGU_4 = "CROW_TENGU_4";
	public static final String CROW_TENGU_5 = "CROW_TENGU_5";

	public CrowTengu() {
		super(MonsterGirlRace.CROW_TENGU);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector actionSelector = action(ATTACK_A_1);

		for (int i = 0; i <= 5; i++) {
			makeBuilder2(i)//
					.strength(7 + i).agility(16 + i * 2).intelligence(11 + i)//
					.baseHp(15 + i * 3)//
					.classes(FLYING)//
					.actionSelector(actionSelector)//
					.build(rpgEnemies);
		}
	}
}