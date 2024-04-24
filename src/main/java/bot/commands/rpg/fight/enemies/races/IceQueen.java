package bot.commands.rpg.fight.enemies.races;

import static bot.commands.rpg.fight.RPGFightAction.SPELL_BLIZZARD;
import static bot.commands.rpg.fight.RPGFightAction.SPELL_FROST_ARMOR;
import static bot.commands.rpg.fight.RPGFightAction.SPELL_ICE_BOLT;
import static bot.commands.rpg.fight.RPGFightAction.SPELL_MAGIC_SHIELD;
import static bot.data.fight.FighterClass.CANT_BE_FROZEN;
import static bot.data.fight.FighterClass.FREEZING_AURA;
import static bot.data.fight.FighterClass.ICY;
import static bot.data.fight.FighterClass.USES_MAGIC;
import static bot.data.fight.FighterClass.WEAK_TO_FIRE;
import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.MonsterGirls.MonsterGirlRace;

public class IceQueen extends EnemiesOfRace {
	public IceQueen() {
		super(MonsterGirlRace.ICE_QUEEN);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector actionSelector = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
				pair(4, SPELL_ICE_BOLT), //
				pair(2, SPELL_BLIZZARD), //
				pair(1, SPELL_FROST_ARMOR), //
				pair(1, SPELL_MAGIC_SHIELD));

		for (int i = 0; i <= 5; i++) {
			makeStandardBuilder(i, 10, 1, 10, 2, 16, 4, 15, 3)//
					.armor(1 + i)//
					.classes(CANT_BE_FROZEN, FREEZING_AURA, ICY, USES_MAGIC, WEAK_TO_FIRE)//
					.actionSelector(actionSelector)//
					.build(rpgEnemies);
		}
	}
}
