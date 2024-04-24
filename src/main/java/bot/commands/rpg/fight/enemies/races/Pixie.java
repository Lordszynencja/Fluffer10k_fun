package bot.commands.rpg.fight.enemies.races;

import static bot.commands.rpg.fight.RPGFightAction.ATTACK_A_1;
import static bot.commands.rpg.fight.RPGFightAction.CHARM;
import static bot.commands.rpg.fight.RPGFightAction.GROW_UP;
import static bot.commands.rpg.fight.RPGFightAction.SHRINK_DOWN;
import static bot.data.MonsterGirls.MonsterGirlRace.PIXIE;
import static bot.data.fight.FighterClass.USES_MAGIC;
import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;

public class Pixie extends EnemiesOfRace {
	public Pixie() {
		super(PIXIE);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector actionSelector = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
				pair(1, ATTACK_A_1), //
				pair(1, CHARM), //
				pair(1, GROW_UP), //
				pair(1, SHRINK_DOWN));

		for (int i = 0; i <= 5; i++) {
			makeStandardBuilder(i)//
					.strength(2 + i / 2).agility(3 + (i + 1) / 2).intelligence(5 + i)//
					.baseHp(5 + i)//
					.classes(USES_MAGIC)//
					.actionSelector(actionSelector)//
					.build(rpgEnemies);
		}
	}
}
