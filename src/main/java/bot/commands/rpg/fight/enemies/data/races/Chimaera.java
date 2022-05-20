package bot.commands.rpg.fight.enemies.data.races;

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
	public static final String CHIMAERA_0 = "CHIMAERA_0";
	public static final String CHIMAERA_1 = "CHIMAERA_1";
	public static final String CHIMAERA_2 = "CHIMAERA_2";
	public static final String CHIMAERA_3 = "CHIMAERA_3";
	public static final String CHIMAERA_4 = "CHIMAERA_4";
	public static final String CHIMAERA_5 = "CHIMAERA_5";

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
			makeBuilder2(i)//
					.strength(25 + 3 * i).agility(20 + 3 * i).intelligence(25 + 3 * i)//
					.baseHp(30 + 5 * i)//
					.armor(2 + i)//
					.diff(5)//
					.classes(USES_MAGIC)//
					.actionSelector(actionSelector)//
					.build(rpgEnemies);
		}
	}
}
