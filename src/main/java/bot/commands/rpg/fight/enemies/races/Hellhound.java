package bot.commands.rpg.fight.enemies.races;

import static bot.commands.rpg.fight.RPGFightAction.ATTACK_S_2;
import static bot.commands.rpg.fight.RPGFightAction.GRAB;
import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.MonsterGirls.MonsterGirlRace;

public class Hellhound extends EnemiesOfRace {
	public Hellhound() {
		super(MonsterGirlRace.HELLHOUND);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector actionSelector = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
				pair(2, ATTACK_S_2), //
				pair(1, GRAB));

		for (int i = 0; i <= 5; i++) {
			makeStandardBuilder(i, 13, 3, 11, 2, 5, 1, 15, 7)//
					.armor(1 + (i + 1) / 2)//
					.actionSelector(actionSelector)//
					.build(rpgEnemies);
		}
	}
}