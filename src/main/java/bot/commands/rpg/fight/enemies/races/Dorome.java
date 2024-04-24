package bot.commands.rpg.fight.enemies.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.FighterClass;

public class Dorome extends EnemiesOfRace {
	public Dorome() {
		super(MonsterGirlRace.DOROME);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector actionSelector = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom( //
				pair(1, RPGFightAction.ATTACK_S_3), //
				pair(1, RPGFightAction.GRAB_SLIME));

		for (int i = 0; i <= 5; i++) {
			makeStandardBuilder(i, 5, 3, 1, 1, 1, 1, 10, 5)//
					.classes(FighterClass.SLIME_REGEN)//
					.actionSelector(actionSelector)//
					.build(rpgEnemies);
		}
	}
}