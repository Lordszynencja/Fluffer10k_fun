package bot.commands.rpg.fight.enemies.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.MonsterGirls.MonsterGirlRace;

public class AntArachne extends EnemiesOfRace {
	public AntArachne() {
		super(MonsterGirlRace.ANT_ARACHNE);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector actionSelectorA = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
				pair(3, RPGFightAction.ATTACK_A_1), //
				pair(1, RPGFightAction.WRAP_IN_WEB));
		final ActionSelector actionSelectorB = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
				pair(3, RPGFightAction.ATTACK_A_1), //
				pair(2, RPGFightAction.WRAP_IN_WEB));

		for (int i = 0; i <= 5; i++) {
			makeStandardBuilder(i)//
					.strength(3 + 2 * i).agility(5 + 3 * i).intelligence(2 + i)//
					.baseHp(5 + 2 * i)//
					.armor(2 + i * 3 / 5)//
					.actionSelector(i < 3 ? actionSelectorA : actionSelectorB)//
					.build(rpgEnemies);
		}
	}
}
