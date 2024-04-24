package bot.commands.rpg.fight.enemies.races;

import static bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.action;
import static bot.data.fight.FighterClass.FLYING;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.MonsterGirls.MonsterGirlRace;

public class Griffon extends EnemiesOfRace {
	public Griffon() {
		super(MonsterGirlRace.GRIFFON);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector actionSelector = action(RPGFightAction.ATTACK_A_1);

		for (int i = 0; i <= 5; i++) {
			makeStandardBuilder(i, 12, 3, 15, 2, 10, 1, 20, 4)//
					.classes(FLYING)//
					.actionSelector(actionSelector)//
					.build(rpgEnemies);
		}
	}
}
