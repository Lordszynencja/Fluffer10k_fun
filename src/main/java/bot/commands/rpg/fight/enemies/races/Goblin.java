package bot.commands.rpg.fight.enemies.races;

import static bot.commands.rpg.fight.RPGFightAction.ATTACK_S_2;
import static bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.action;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.MonsterGirls.MonsterGirlRace;

public class Goblin extends EnemiesOfRace {
	public Goblin() {
		super(MonsterGirlRace.GOBLIN);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector actionSelector = action(ATTACK_S_2);

		for (int i = 0; i < 6; i++) {
			makeStandardBuilder(i, 2, 1, 3, 1, 1 + i / 2, 0, 5, 2)//
					.actionSelector(actionSelector)//
					.build(rpgEnemies);
		}
	}
}