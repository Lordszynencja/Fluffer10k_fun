package bot.commands.rpg.fight.enemies.data.races;

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

public class Witch extends EnemiesOfRace {
	public static final String WITCH_0 = "WITCH_0";
	public static final String WITCH_1 = "WITCH_1";
	public static final String WITCH_2 = "WITCH_2";
	public static final String WITCH_3 = "WITCH_3";
	public static final String WITCH_4 = "WITCH_4";
	public static final String WITCH_5 = "WITCH_5";

	public Witch() {
		super(MonsterGirlRace.WITCH);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector actionSelector = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
				pair(1, SPELL_FIREBALL), //
				pair(1, SPELL_ICE_BOLT), //
				pair(1, SPELL_LIGHTNING), //
				pair(1, SPELL_HEAL), //
				pair(1, CHARM_CUTE), //
				pair(1, SPELL_MAGIC_SHIELD));

		for (int i = 0; i < 6; i++) {
			makeBuilder2(i)//
					.strength(5 + i).agility(10 + 2 * i).intelligence(18 + 4 * i)//
					.baseHp(10 + 3 * i)//
					.classes(USES_MAGIC)//
					.actionSelector(actionSelector)//
					.build(rpgEnemies);
		}
	}
}