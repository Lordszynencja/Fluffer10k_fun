package bot.commands.rpg.fight.enemies.races;

import static bot.commands.rpg.fight.RPGFightAction.ATTACK_S_7;
import static bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.action;
import static bot.data.fight.FighterClass.UNDEAD;
import static bot.data.fight.FighterClass.WEAK_TO_FIRE;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.MonsterGirls.MonsterGirlRace;

public class Zombie extends EnemiesOfRace {
	public Zombie() {
		super(MonsterGirlRace.ZOMBIE);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector actionSelector = action(ATTACK_S_7);
		for (int i = 0; i <= 5; i++) {
			makeStandardBuilder(i, 4, 1, 1, 0, 1, 0, 5, 2)//
					.classes(UNDEAD, WEAK_TO_FIRE)//
					.actionSelector(actionSelector)//
					.build(rpgEnemies);
		}

	}
}
