package bot.commands.rpg.fight.enemies.races;

import static bot.data.fight.FighterClass.CANT_BE_FROZEN;
import static bot.data.fight.FighterClass.FREEZING_AURA;
import static bot.data.fight.FighterClass.ICY;
import static bot.data.fight.FighterClass.USES_MAGIC;
import static bot.data.fight.FighterClass.WEAK_TO_FIRE;
import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.MonsterGirls.MonsterGirlRace;

public class Glacies extends EnemiesOfRace {
	public Glacies() {
		super(MonsterGirlRace.GLACIES);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector actionSelector = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
				pair(4, RPGFightAction.SPELL_ICE_BOLT), //
				pair(1, RPGFightAction.SPELL_BLIZZARD));

		for (int i = 0; i <= 5; i++) {
			makeStandardBuilder(i, 5, 2, 5, 1, 10, 3, 10, 3)//
					.armor(2 + i * 4 / 5)//
					.classes(CANT_BE_FROZEN, FREEZING_AURA, ICY, USES_MAGIC, WEAK_TO_FIRE)//
					.actionSelector(actionSelector)//
					.build(rpgEnemies);
		}
	}
}
