package bot.commands.rpg.fight.enemies.races;

import static bot.commands.rpg.fight.RPGFightAction.ATLACH_NACHA_VENOM;
import static bot.commands.rpg.fight.RPGFightAction.ATTACK_S_11;
import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.MonsterGirls.MonsterGirlRace;

public class AtlachNacha extends EnemiesOfRace {
	public AtlachNacha() {
		super(MonsterGirlRace.ATLACH_NACHA);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector actionSelector = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
				pair(2, ATTACK_S_11), //
				pair(1, ATLACH_NACHA_VENOM));

		for (int i = 0; i <= 5; i++) {
			makeStandardBuilder(i, 8, 2, 10, 3, 6, 2, 20, 4)//
					.armor(2 + i)//
					.actionSelector(actionSelector)//
					.build(rpgEnemies);
		}
	}
}