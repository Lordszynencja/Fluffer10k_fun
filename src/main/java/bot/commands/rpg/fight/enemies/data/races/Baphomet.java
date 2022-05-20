package bot.commands.rpg.fight.enemies.data.races;

import static bot.commands.rpg.fight.RPGFightAction.ATTACK_S_1;
import static bot.commands.rpg.fight.RPGFightAction.CHARM_CUTE;
import static bot.commands.rpg.fight.RPGFightAction.SPELL_FIREBALL;
import static bot.commands.rpg.fight.RPGFightAction.SPELL_HEAL;
import static bot.commands.rpg.fight.RPGFightAction.SPELL_ICE_BOLT;
import static bot.commands.rpg.fight.RPGFightAction.SPELL_LIGHTNING;
import static bot.commands.rpg.fight.RPGFightAction.SPELL_MAGIC_SHIELD;
import static bot.data.fight.FighterClass.USES_MAGIC;
import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.MonsterGirls.MonsterGirlRace;

public class Baphomet extends EnemiesOfRace {
	public static final String BAPHOMET_0 = "BAPHOMET_0";
	public static final String BAPHOMET_1 = "BAPHOMET_1";
	public static final String BAPHOMET_2 = "BAPHOMET_2";
	public static final String BAPHOMET_3 = "BAPHOMET_3";
	public static final String BAPHOMET_4 = "BAPHOMET_4";
	public static final String BAPHOMET_5 = "BAPHOMET_5";

	public Baphomet() {
		super(MonsterGirlRace.BAPHOMET);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector actionSelector = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
				pair(6, ATTACK_S_1), //
				pair(1, SPELL_FIREBALL), //
				pair(1, SPELL_ICE_BOLT), //
				pair(1, SPELL_LIGHTNING), //
				pair(1, SPELL_HEAL), //
				pair(1, CHARM_CUTE), //
				pair(1, SPELL_MAGIC_SHIELD));

		for (int i = 0; i < 6; i++) {
			makeBuilder2(i)//
					.strength(30 + 4 * i).agility(15 + 2 * i).intelligence(15 + 3 * i)//
					.baseHp(30 + 4 * i)//
					.classes(USES_MAGIC)//
					.actionSelector(actionSelector)//
					.build(rpgEnemies);
		}
	}
}
