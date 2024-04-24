package bot.commands.rpg.fight.enemies.races;

import static bot.commands.rpg.fight.RPGFightAction.ATTACK_A_1;
import static bot.commands.rpg.fight.RPGFightAction.GRAB;
import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.MonsterGirls.MonsterGirlRace;

public class Jinko extends EnemiesOfRace {
	public Jinko() {
		super(MonsterGirlRace.JINKO);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector actionSelector = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
				pair(3, ATTACK_A_1), //
				pair(1, GRAB));

		for (int i = 0; i <= 5; i++) {
			makeStandardBuilder(i, 10, 2, 14, 3, 8, 1, 15, 4)//
					.armor(i)//
					.actionSelector(actionSelector)//
					.build(rpgEnemies);
		}

	}
}
