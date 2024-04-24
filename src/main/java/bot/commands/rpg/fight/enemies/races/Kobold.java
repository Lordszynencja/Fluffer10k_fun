package bot.commands.rpg.fight.enemies.races;

import static bot.commands.rpg.fight.RPGFightAction.ATTACK_S_1;
import static bot.commands.rpg.fight.RPGFightAction.GRAB_CLING;
import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.MonsterGirls.MonsterGirlRace;

public class Kobold extends EnemiesOfRace {
	public Kobold() {
		super(MonsterGirlRace.KOBOLD);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector actionSelector = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
				pair(3, ATTACK_S_1), //
				pair(1, GRAB_CLING))//
				.gentle();

		for (int i = 0; i <= 5; i++) {
			makeStandardBuilder(i, 4, 2, 3, 1, 2, 1, 5, 2)//
					.actionSelector(actionSelector)//
					.build(rpgEnemies);
		}
	}
}