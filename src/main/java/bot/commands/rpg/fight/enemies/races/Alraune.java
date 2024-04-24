package bot.commands.rpg.fight.enemies.races;

import static bot.data.fight.FighterClass.USES_MAGIC;
import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.MonsterGirls.MonsterGirlRace;

public class Alraune extends EnemiesOfRace {
	public Alraune() {
		super(MonsterGirlRace.ALRAUNE);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector actionSelectorA = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
				pair(3, RPGFightAction.ATTACK_S_9), //
				pair(1, RPGFightAction.CHARM), //
				pair(1, RPGFightAction.ALRAUNE_VINES));
		final ActionSelector actionSelectorB = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
				pair(2, RPGFightAction.ATTACK_S_9), //
				pair(1, RPGFightAction.ALRAUNE_NECTAR), //
				pair(1, RPGFightAction.CHARM), //
				pair(1, RPGFightAction.ALRAUNE_VINES));

		for (int i = 0; i <= 5; i++) {
			makeStandardBuilder(i, 8, 1, 5, 3, 7, 2, 10, 2)//
					.classes(USES_MAGIC)//
					.actionSelector(i < 4 ? actionSelectorA : actionSelectorB)//
					.build(rpgEnemies);
		}
	}
}