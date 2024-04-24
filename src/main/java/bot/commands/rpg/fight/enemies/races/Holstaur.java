package bot.commands.rpg.fight.enemies.races;

import static bot.commands.rpg.fight.RPGFightAction.ATTACK_S_1;
import static bot.commands.rpg.fight.RPGFightAction.GRAB;
import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;

public class Holstaur extends EnemiesOfRace {
	public Holstaur() {
		super(MonsterGirlRace.HOLSTAUR);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		for (int i = 0; i <= 5; i++) {
			makeStandardBuilder(i, 5, 4, 3, 1, 2, 1, 20, 5)//
					.armor(1)//
					.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
							pair(7 - i, ATTACK_S_1), pair(1, GRAB)))//
					.build(rpgEnemies);
		}
	}
}