package bot.commands.rpg.fight.enemies.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.MonsterGirls.MonsterGirlRace;

public class Bunyip extends EnemiesOfRace {
	public Bunyip() {
		super(MonsterGirlRace.BUNYIP);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector actionSelector = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
				pair(1, RPGFightAction.ATTACK_S_2), //
				pair(1, RPGFightAction.GRAB_COIL));

		for (int i = 0; i <= 5; i++) {
			makeStandardBuilder(i, 23, 2, 19, 2, 7, 1, 30, 2)//
					.actionSelector(actionSelector)//
					.build(rpgEnemies);
		}
	}
}
