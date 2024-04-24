package bot.commands.rpg.fight.enemies.races;

import static bot.commands.rpg.fight.RPGFightAction.ATTACK_A_3;
import static bot.commands.rpg.fight.RPGFightAction.GRAB_CLING;
import static bot.commands.rpg.fight.RPGFightAction.LICK;
import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.MonsterGirls.MonsterGirlRace;

public class Akaname extends EnemiesOfRace {
	public Akaname() {
		super(MonsterGirlRace.AKANAME);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector actionSelector = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
				pair(3, ATTACK_A_3), //
				pair(1, LICK), //
				pair(1, GRAB_CLING))//
				.gentle();

		for (int i = 0; i <= 5; i++) {
			makeStandardBuilder(i, 2, 1, 6, 1, 3, 2, 5, 1)//
					.actionSelector(actionSelector)//
					.build(rpgEnemies);
		}
	}
}
