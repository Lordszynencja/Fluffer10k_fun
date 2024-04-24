package bot.commands.rpg.fight.enemies.races;

import static bot.commands.rpg.fight.RPGFightAction.ATTACK_S_14;
import static bot.commands.rpg.fight.RPGFightAction.FRENZY;
import static bot.data.fight.FighterClass.OCELOMEH_SWORD;
import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.MonsterGirls.MonsterGirlRace;

public class Ocelomeh extends EnemiesOfRace {
	public Ocelomeh() {
		super(MonsterGirlRace.OCELOMEH);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector actionSelector = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
				pair(4, ATTACK_S_14), //
				pair(1, FRENZY));

		for (int i = 0; i <= 5; i++) {
			makeStandardBuilder(i, 13, 3, 11, 3, 5, 1, 20, 5)//
					.classes(OCELOMEH_SWORD)//
					.actionSelector(actionSelector)//
					.build(rpgEnemies);
		}
	}
}