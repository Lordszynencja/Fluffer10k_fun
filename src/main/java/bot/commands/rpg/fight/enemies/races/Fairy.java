package bot.commands.rpg.fight.enemies.races;

import static bot.commands.rpg.fight.RPGFightAction.ATTACK_S_5;
import static bot.commands.rpg.fight.RPGFightAction.CHARM_CUTE;
import static bot.data.fight.FighterClass.USES_MAGIC;
import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.MonsterGirls.MonsterGirlRace;

public class Fairy extends EnemiesOfRace {
	public Fairy() {
		super(MonsterGirlRace.FAIRY);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector actionSelectorA = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
				pair(4, ATTACK_S_5), //
				pair(1, CHARM_CUTE));
		final ActionSelector actionSelectorB = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
				pair(4, ATTACK_S_5), //
				pair(1, CHARM_CUTE));

		for (int i = 0; i <= 5; i++) {
			makeStandardBuilder(i)//
					.strength(i == 2 || i == 5 ? 2 : 1)//
					.agility(i == 3 || i == 5 ? 2 : 1)//
					.intelligence(i == 4 || i == 5 ? 2 : 1)//
					.baseHp(i > 0 ? 3 : 1)//
					.classes(USES_MAGIC)//
					.actionSelector(i < 4 ? actionSelectorA : actionSelectorB)//
					.build(rpgEnemies);
		}
	}
}
