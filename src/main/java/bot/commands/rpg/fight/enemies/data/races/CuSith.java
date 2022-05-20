package bot.commands.rpg.fight.enemies.data.races;

import static bot.util.RandomUtils.getRandom;
import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.Targetting;
import bot.commands.rpg.fight.Targetting.TargetCheck;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.FighterClass;
import bot.data.fight.FighterData;
import bot.data.fight.FighterStatus;

public class CuSith extends EnemiesOfRace {
	public static final String CU_SITH_1 = "CU_SITH_1";
	public static final String CU_SITH_2 = "CU_SITH_2";
	public static final String CU_SITH_3 = "CU_SITH_3";

	public CuSith() {
		super(MonsterGirlRace.CU_SITH);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		makeBuilder(CU_SITH_1)//
				.strength(3).agility(3).intelligence(7)//
				.baseHp(10)//
				.diff(1)//
				.classes(FighterClass.USES_MAGIC)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(3, RPGFightAction.ATTACK_S_2), //
						pair(1, RPGFightAction.SPELL_FIREBALL), //
						pair(1, RPGFightAction.SPELL_ICE_BOLT), //
						pair(1, RPGFightAction.SPELL_LIGHTNING), //
						pair(3, RPGFightAction.CHARM)))//
				.build(rpgEnemies);
		makeBuilder(CU_SITH_2)//
				.strength(4).agility(5).intelligence(9)//
				.baseHp(10)//
				.diff(3)//
				.classes(FighterClass.USES_MAGIC)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(1, RPGFightAction.SPELL_FIREBALL), //
						pair(1, RPGFightAction.SPELL_ICE_BOLT), //
						pair(1, RPGFightAction.SPELL_LIGHTNING), //
						pair(1, RPGFightAction.SPELL_HEAL), //
						pair(1, RPGFightAction.SPELL_MAGIC_SHIELD), //
						pair(3, RPGFightAction.CHARM)))//
				.build(rpgEnemies);

		final Targetting withoutCharmTargetting = new Targetting(
				TargetCheck.ENEMY.without(fluffer10kFun, FighterClass.MECHANICAL)//
						.without(FighterStatus.CHARMED)//
						.withStacksLessThan(FighterStatus.CHARM_RESISTANCE, 3));

		final RPGFightAction[] spells = { RPGFightAction.SPELL_FIREBALL, //
				RPGFightAction.SPELL_ICE_BOLT, //
				RPGFightAction.SPELL_LIGHTNING };

		final ActionSelector actionSelector3 = data -> {
			if (data.activeFighter.hp < 10) {
				data.targetId = data.activeFighter.id;
				return RPGFightAction.SPELL_HEAL;
			}

			final FighterData target = withoutCharmTargetting.getFirst(data.fight, data.activeFighter);
			if (target != null) {
				data.targetId = target.id;
				return RPGFightAction.CHARM;
			}

			if (!data.activeFighter.statuses.isStatus(FighterStatus.MAGIC_SHIELD)) {
				return RPGFightAction.SPELL_MAGIC_SHIELD;
			}

			return getRandom(spells);
		};

		makeBuilder(CU_SITH_3)//
				.strength(4).agility(6).intelligence(12)//
				.baseHp(10)//
				.diff(10)//
				.classes(FighterClass.CLEVER, FighterClass.USES_MAGIC)//
				.actionSelector(actionSelector3)//
				.build(rpgEnemies);
	}
}