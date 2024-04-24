package bot.commands.rpg.fight.enemies.races;

import static bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.action;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.MonsterGirls.MonsterGirlRace;

public class LargeMouse extends EnemiesOfRace {
	public LargeMouse() {
		super(MonsterGirlRace.LARGE_MOUSE);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector actionSelector = action(RPGFightAction.ATTACK_A_3);

		for (int i = 0; i <= 5; i++) {
			makeStandardBuilder(i, 4, 1, 3, 1, 1, 1, 5, 2)//
					.actionSelector(actionSelector)//
					.build(rpgEnemies);
		}
	}
}