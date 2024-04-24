package bot.commands.rpg.fight.enemies.races;

import static bot.commands.rpg.fight.RPGFightAction.ATTACK_A_1;
import static bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.action;
import static bot.data.fight.FighterClass.FLYING;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.MonsterGirls.MonsterGirlRace;

public class CrowTengu extends EnemiesOfRace {
	public CrowTengu() {
		super(MonsterGirlRace.CROW_TENGU);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector actionSelector = action(ATTACK_A_1);

		for (int i = 0; i <= 5; i++) {
			makeStandardBuilder(i, 8, 1, 15, 4, 9, 2, 15, 3)//
					.classes(FLYING)//
					.actionSelector(actionSelector)//
					.build(rpgEnemies);
		}
	}
}