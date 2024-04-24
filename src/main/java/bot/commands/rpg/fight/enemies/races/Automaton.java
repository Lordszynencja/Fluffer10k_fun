package bot.commands.rpg.fight.enemies.races;

import static bot.data.fight.FighterClass.MECHANICAL;
import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.MonsterGirls.MonsterGirlRace;

public class Automaton extends EnemiesOfRace {
	public Automaton() {
		super(MonsterGirlRace.AUTOMATON);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector actionSelector = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom( //
				pair(2, RPGFightAction.ATTACK_S_7), //
				pair(1, RPGFightAction.GRAB));

		for (int i = 0; i <= 5; i++) {
			makeStandardBuilder(i, 16, 4, 8, 2, 4, 1, 20, 5)//
					.armor(5 + i)//
					.classes(MECHANICAL)//
					.actionSelector(actionSelector)//
					.build(rpgEnemies);
		}
	}
}
