package bot.commands.rpg.fight.enemies.data.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.FighterClass;

public class CaitSith extends EnemiesOfRace {
	public static final String CAIT_SITH_1 = "CAIT_SITH_1";
	public static final String CAIT_SITH_2 = "CAIT_SITH_2";

	public CaitSith() {
		super(MonsterGirlRace.CAIT_SITH);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		makeBuilder(CAIT_SITH_1)//
				.strength(4).agility(5).intelligence(10)//
				.baseHp(10)//
				.diff(1)//
				.classes(FighterClass.USES_MAGIC)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(2, RPGFightAction.SPELL_FIREBALL), //
						pair(2, RPGFightAction.SPELL_ICE_BOLT), //
						pair(2, RPGFightAction.SPELL_LIGHTNING), //
						pair(1, RPGFightAction.SPELL_HEAL), //
						pair(1, RPGFightAction.CHARM_CUTE), //
						pair(10, RPGFightAction.SPELL_MAGIC_SHIELD)))//
				.build(rpgEnemies);
		makeBuilder(CAIT_SITH_2)//
				.strength(5).agility(8).intelligence(14)//
				.baseHp(15)//
				.diff(1)//
				.classes(FighterClass.USES_MAGIC)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(2, RPGFightAction.SPELL_FIREBALL), //
						pair(2, RPGFightAction.SPELL_ICE_BOLT), //
						pair(2, RPGFightAction.SPELL_LIGHTNING), //
						pair(1, RPGFightAction.SPELL_HEAL), //
						pair(1, RPGFightAction.CHARM_CUTE), //
						pair(10, RPGFightAction.SPELL_MAGIC_SHIELD)))//
				.build(rpgEnemies);
	}
}
