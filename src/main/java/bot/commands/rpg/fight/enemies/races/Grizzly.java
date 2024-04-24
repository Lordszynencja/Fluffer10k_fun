package bot.commands.rpg.fight.enemies.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;

public class Grizzly extends EnemiesOfRace {
	public Grizzly() {
		super(MonsterGirlRace.GRIZZLY);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		for (int i = 0; i <= 5; i++) {
			makeStandardBuilder(i, 14, 6, 5, 1, 5, 0, 20, 6)//
					.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
							pair(9 - i, RPGFightAction.ATTACK_S_2), //
							pair(1, RPGFightAction.GRAB), //
							pair(1, RPGFightAction.FRENZY_GRIZZLY)))//
					.build(rpgEnemies);
		}
	}
}
