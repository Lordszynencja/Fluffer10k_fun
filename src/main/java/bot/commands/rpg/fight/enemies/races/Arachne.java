package bot.commands.rpg.fight.enemies.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.MonsterGirls.MonsterGirlRace;

public class Arachne extends EnemiesOfRace {
	public Arachne() {
		super(MonsterGirlRace.ARACHNE);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector actionSelector = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
				pair(3, RPGFightAction.ATTACK_S_7), //
				pair(1, RPGFightAction.WRAP_IN_WEB));
		for (int i = 0; i <= 5; i++) {
			makeStandardBuilder(i, 9, 1, 13, 3, 5, 1, 15, 3)//
					.armor(2 + i)//
					.actionSelector(actionSelector)//
					.build(rpgEnemies);
		}
	}
}