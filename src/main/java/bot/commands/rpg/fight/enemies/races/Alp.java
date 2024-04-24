package bot.commands.rpg.fight.enemies.races;

import static bot.commands.rpg.fight.RPGFightAction.ATTACK_A_3;
import static bot.commands.rpg.fight.RPGFightAction.CHARM;
import static bot.data.fight.FighterClass.USES_MAGIC;
import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.commands.rpg.fight.enemies.RPGEnemyMonsterGirlDataBuilder;
import bot.data.MonsterGirls.MonsterGirlRace;

public class Alp extends EnemiesOfRace {
	public Alp() {
		super(MonsterGirlRace.ALP);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector actionSelectorA = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
				pair(1, ATTACK_A_3));
		final ActionSelector actionSelectorB = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
				pair(1, ATTACK_A_3), //
				pair(1, CHARM));

		for (int i = 0; i <= 5; i++) {
			final RPGEnemyMonsterGirlDataBuilder builder = makeStandardBuilder(i, 3, 1, 15, 2, 8, 2, 10, 2)//
					.armor(i * 2 / 5);
			if (i % 2 == 0) {
				builder.actionSelector(actionSelectorA);
			} else {
				builder.actionSelector(actionSelectorB)//
						.classes(USES_MAGIC);
			}
			builder.build(rpgEnemies);
		}
	}
}