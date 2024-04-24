package bot.commands.rpg.fight.enemies.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.FighterClass;

public class Ratatoskr extends EnemiesOfRace {

	public Ratatoskr() {
		super(MonsterGirlRace.RATATOSKR);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector actionSelector = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
				pair(3, RPGFightAction.ATTACK_A_1), //
				pair(1, RPGFightAction.CHARM_RATATOSKR))//
				.gentle();

		for (int i = 0; i <= 5; i++) {
			makeStandardBuilder(i)//
					.strength(3 + i).agility(5 + 3 * i).intelligence(5 + 2 * i)//
					.baseHp(5 + 2 * i)//
					.classes(FighterClass.USES_MAGIC)//
					.actionSelector(actionSelector)//
					.build(rpgEnemies);
		}
	}
}
