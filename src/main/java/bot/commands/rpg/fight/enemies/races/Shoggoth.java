package bot.commands.rpg.fight.enemies.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.FighterClass;

public class Shoggoth extends EnemiesOfRace {
	public Shoggoth() {
		super(MonsterGirlRace.SHOGGOTH);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector actionSelector = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom( //
				pair(4, RPGFightAction.ATTACK_S_3), //
				pair(1, RPGFightAction.GRAB_SLIME), //
				pair(1, RPGFightAction.CHARM)) //
				.gentle();

		for (int i = 0; i <= 5; i++) {
			makeStandardBuilder(i, 4, 1, 9, 2, 11, 2, 30, 6)//
					.classes(FighterClass.SLIME_REGEN, FighterClass.WET, FighterClass.WEAK_TO_FIRE)//
					.actionSelector(actionSelector)//
					.build(rpgEnemies);
		}
	}
}