package bot.commands.rpg.fight.enemies.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;

public class Slime extends EnemiesOfRace {
	public Slime() {
		super(MonsterGirlRace.SLIME);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		for (int i = 0; i <= 5; i++) {
			makeStandardBuilder(i, 2, 1, 1, 2, 1, 1, 1, 2)//
					.slime()//
					.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom( //
							pair(8 - i, RPGFightAction.ATTACK_S_3), //
							pair(1, RPGFightAction.GRAB_SLIME)))//
					.build(rpgEnemies);
		}
	}
}