package bot.commands.rpg.fight.enemies.races;

import static bot.commands.rpg.fight.RPGFightAction.ATTACK_DOUBLE_PUNCH;
import static bot.commands.rpg.fight.RPGFightAction.GRAB;
import static bot.commands.rpg.fight.RPGFightAction.PARALYZE_CHIMAERA;
import static bot.commands.rpg.fight.RPGFightAction.SPELL_FIREBALL;
import static bot.commands.rpg.fight.RPGFightAction.SPELL_HEAL;
import static bot.commands.rpg.fight.RPGFightAction.SPELL_ICE_BOLT;
import static bot.commands.rpg.fight.RPGFightAction.SPELL_LIGHTNING;
import static bot.commands.rpg.fight.RPGFightAction.SPELL_MAGIC_SHIELD;
import static bot.commands.rpg.fight.RPGFightAction.SPELL_QUADRUPLE_FLAME;
import static bot.data.MonsterGirls.MonsterGirlRace.CHIMAERA;
import static bot.data.fight.FighterClass.USES_MAGIC;
import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;

public class Chimaera extends EnemiesOfRace {
	public Chimaera() {
		super(CHIMAERA);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector actionSelector = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
				pair(4, ATTACK_DOUBLE_PUNCH), //
				pair(2, SPELL_FIREBALL), //
				pair(2, SPELL_ICE_BOLT), //
				pair(2, SPELL_LIGHTNING), //
				pair(1, SPELL_QUADRUPLE_FLAME), //
				pair(1, SPELL_HEAL), //
				pair(1, SPELL_MAGIC_SHIELD), //
				pair(2, PARALYZE_CHIMAERA), //
				pair(2, GRAB));

		for (int i = 0; i < 6; i++) {
			makeStandardBuilder(i, 25, 3, 20, 3, 25, 3, 30, 6)//
					.armor(2 + i)//
					.classes(USES_MAGIC)//
					.actionSelector(actionSelector)//
					.build(rpgEnemies);
		}
	}
}
