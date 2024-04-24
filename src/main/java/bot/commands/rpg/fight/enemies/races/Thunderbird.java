package bot.commands.rpg.fight.enemies.races;

import static bot.commands.rpg.fight.RPGFightAction.ATTACK_A_1;
import static bot.commands.rpg.fight.RPGFightAction.SPELL_PARALYZING_THUNDER;
import static bot.data.fight.FighterClass.FLYING;
import static bot.data.fight.FighterClass.USES_MAGIC;
import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.MonsterGirls.MonsterGirlRace;

public class Thunderbird extends EnemiesOfRace {

	public Thunderbird() {
		super(MonsterGirlRace.THUNDERBIRD);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector actionSelector = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom( //
				pair(2, ATTACK_A_1), //
				pair(1, SPELL_PARALYZING_THUNDER));

		for (int i = 0; i <= 5; i++) {
			makeStandardBuilder(i, 3, 1, 5, 1, 5, 2, 5, 3)//
					.classes(FLYING, USES_MAGIC)//
					.actionSelector(actionSelector)//
					.build(rpgEnemies);
		}
	}
}
