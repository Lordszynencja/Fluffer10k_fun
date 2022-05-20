package bot.commands.rpg.fight.enemies.data.races;

import static bot.commands.rpg.fight.RPGFightAction.ATTACK_S_1;
import static bot.commands.rpg.fight.RPGFightAction.CHARM;
import static bot.commands.rpg.fight.RPGFightAction.SPELL_FIREBALL;
import static bot.commands.rpg.fight.RPGFightAction.SPELL_ICE_BOLT;
import static bot.commands.rpg.fight.RPGFightAction.SPELL_LIGHTNING;
import static bot.commands.rpg.fight.RPGFightAction.SPELL_MAGIC_SHIELD;
import static bot.data.fight.FighterClass.USES_MAGIC;
import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.MonsterGirls.MonsterGirlRace;

public class Succubus extends EnemiesOfRace {
	public static final String SUCCUBUS_0 = "SUCCUBUS_0";
	public static final String SUCCUBUS_1 = "SUCCUBUS_1";
	public static final String SUCCUBUS_2 = "SUCCUBUS_2";
	public static final String SUCCUBUS_3 = "SUCCUBUS_3";
	public static final String SUCCUBUS_4 = "SUCCUBUS_4";
	public static final String SUCCUBUS_5 = "SUCCUBUS_5";

	public Succubus() {
		super(MonsterGirlRace.SUCCUBUS);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector actionSelector = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
				pair(3, ATTACK_S_1), //
				pair(1, SPELL_FIREBALL), //
				pair(1, SPELL_ICE_BOLT), //
				pair(1, SPELL_LIGHTNING), //
				pair(3, CHARM), //
				pair(1, SPELL_MAGIC_SHIELD));

		for (int i = 0; i <= 5; i++) {
			makeBuilder2(i)//
					.strength(5 + 2 * i).agility(10 + 3 * i).intelligence(13 + i * 4)//
					.baseHp(20 + i * 3)//
					.armor(2 + i * 2 / 5)//
					.classes(USES_MAGIC)//
					.actionSelector(actionSelector)//
					.build(rpgEnemies);
		}
	}
}