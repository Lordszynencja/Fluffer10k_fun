package bot.commands.rpg.fight.enemies.races;

import static bot.commands.rpg.fight.RPGFightAction.ATTACK_S_1;
import static bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.action;
import static bot.data.fight.FighterClass.FLYING;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.MonsterGirls.MonsterGirlRace;

public class Angel extends EnemiesOfRace {
	public Angel() {
		super(MonsterGirlRace.ANGEL);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector actionSelector = action(ATTACK_S_1);

		for (int i = 0; i <= 5; i++) {
			makeStandardBuilder(i, 3, 2, 4, 2, 2, 1, 5, 3)//
					.classes(FLYING)//
					.actionSelector(actionSelector)//
					.build(rpgEnemies);
		}
	}
}