package bot.commands.rpg.fight.enemies.races;

import static bot.commands.rpg.fight.RPGFightAction.ATTACK_S_2;
import static bot.commands.rpg.fight.RPGFightAction.GRAB_COIL;
import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.MonsterGirls.MonsterGirlRace;

public class Wurm extends EnemiesOfRace {
	public Wurm() {
		super(MonsterGirlRace.WURM);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector actionSelector = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
				pair(4, ATTACK_S_2), //
				pair(1, GRAB_COIL));

		for (int i = 0; i <= 5; i++) {
			makeStandardBuilder(i, 20, 5, 8, 2, 2, 1, 30, 6)//
					.armor(3 + i)//
					.actionSelector(actionSelector)//
					.build(rpgEnemies);
		}
	}
}
