package bot.commands.rpg.fight.enemies.races;

import static bot.data.fight.FighterClass.USES_MAGIC;
import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.MonsterGirls.MonsterGirlRace;

public class Alice extends EnemiesOfRace {
	public Alice() {
		super(MonsterGirlRace.ALICE);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector actionSelector = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom( //
				pair(2, RPGFightAction.ATTACK_S_1), //
				pair(1, RPGFightAction.CHARM_CUTE))//
				.gentle();

		for (int i = 0; i <= 5; i++) {
			makeStandardBuilder(i, 2, 1, 3, 2, 7, 2, 5, 2)//
					.classes(USES_MAGIC)//
					.actionSelector(actionSelector)//
					.build(rpgEnemies);
		}
	}
}
