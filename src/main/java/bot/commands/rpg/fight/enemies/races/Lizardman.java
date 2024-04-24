package bot.commands.rpg.fight.enemies.races;

import static bot.commands.rpg.fight.RPGFightAction.ATTACK_S_15;
import static bot.commands.rpg.fight.RPGFightAction.GRAB;
import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.MonsterGirls.MonsterGirlRace;

public class Lizardman extends EnemiesOfRace {
	public Lizardman() {
		super(MonsterGirlRace.LIZARDMAN);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector actionSelector = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
				pair(2, ATTACK_S_15), //
				pair(1, GRAB));

		for (int i = 0; i <= 5; i++) {
			makeStandardBuilder(i, 11, 3, 7, 3, 5, 2, 15, 3)//
					.actionSelector(actionSelector)//
					.build(rpgEnemies);
		}
	}
}