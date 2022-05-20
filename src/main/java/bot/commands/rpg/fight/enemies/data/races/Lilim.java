package bot.commands.rpg.fight.enemies.data.races;

import static bot.commands.rpg.fight.RPGFightAction.ATTACK_A_3;
import static bot.commands.rpg.fight.RPGFightAction.CHARM;
import static bot.commands.rpg.fight.RPGFightAction.SPELL_FIREBALL;
import static bot.commands.rpg.fight.RPGFightAction.SPELL_ICE_BOLT;
import static bot.commands.rpg.fight.RPGFightAction.SPELL_LIFE_DRAIN;
import static bot.commands.rpg.fight.RPGFightAction.SPELL_LIGHTNING;
import static bot.data.fight.FighterClass.USES_MAGIC;
import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.MonsterGirls.MonsterGirlRace;

public class Lilim extends EnemiesOfRace {
	public static final String LILIM_0 = "LILIM_0";
	public static final String LILIM_1 = "LILIM_1";
	public static final String LILIM_2 = "LILIM_2";
	public static final String LILIM_3 = "LILIM_3";
	public static final String LILIM_4 = "LILIM_4";
	public static final String LILIM_5 = "LILIM_5";

	public Lilim() {
		super(MonsterGirlRace.LILIM);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector actionSelector = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
				pair(1, ATTACK_A_3), //
				pair(1, SPELL_FIREBALL), //
				pair(1, SPELL_ICE_BOLT), //
				pair(1, SPELL_LIGHTNING), //
				pair(1, SPELL_LIFE_DRAIN), //
				pair(1, CHARM));

		for (int i = 0; i <= 5; i++) {
			makeBuilder2(i)//
					.strength(10 + i * 4).agility(15 + i * 5).intelligence(20 + i * 6)//
					.baseHp(30 + i * 5)//
					.classes(USES_MAGIC)//
					.actionSelector(actionSelector)//
					.build(rpgEnemies);
		}
	}
}
