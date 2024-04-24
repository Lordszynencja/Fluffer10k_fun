package bot.commands.rpg.fight.enemies.races;

import static bot.commands.rpg.fight.RPGFightAction.ATTACK_A_1;
import static bot.commands.rpg.fight.RPGFightAction.CHARM;
import static bot.commands.rpg.fight.RPGFightAction.GRAB;
import static bot.commands.rpg.fight.RPGFightAction.MANTICORE_VENOM;
import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.MonsterGirls.MonsterGirlRace;

public class Manticore extends EnemiesOfRace {
	public Manticore() {
		super(MonsterGirlRace.MANTICORE);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector actionSelector = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
				pair(4, ATTACK_A_1), //
				pair(1, GRAB), //
				pair(1, MANTICORE_VENOM), //
				pair(1, CHARM));

		for (int i = 0; i <= 5; i++) {
			makeStandardBuilder(i, 11, 2, 15, 4, 5, 2, 10, 6)//
					.actionSelector(actionSelector)//
					.build(rpgEnemies);
		}
	}
}