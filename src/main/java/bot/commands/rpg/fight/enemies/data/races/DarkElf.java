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

public class DarkElf extends EnemiesOfRace {
	public static final String DARK_ELF_1 = "DARK_ELF_1";
	public static final String DARK_ELF_2 = "DARK_ELF_2";
	public static final String DARK_ELF_3 = "DARK_ELF_3";

	public DarkElf() {
		super(MonsterGirlRace.DARK_ELF);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		makeBuilder(DARK_ELF_1)//
				.strength(6).agility(8).intelligence(6)//
				.baseHp(10)//
				.diff(2)//
				.classes(FighterClass.USES_MAGIC)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(3, RPGFightAction.ATTACK_S_10), //
						pair(1, RPGFightAction.SPELL_FIREBALL), //
						pair(1, RPGFightAction.SPELL_ICE_BOLT), //
						pair(1, RPGFightAction.SPELL_LIGHTNING), //
						pair(3, RPGFightAction.CHARM_CUTE)))//
				.build(rpgEnemies);
		makeBuilder(DARK_ELF_2)//
				.strength(7).agility(8).intelligence(7)//
				.baseHp(15)//
				.diff(2)//
				.classes(FighterClass.USES_MAGIC)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(1, RPGFightAction.ATTACK_S_10), //
						pair(1, RPGFightAction.SPELL_FIREBALL), //
						pair(1, RPGFightAction.SPELL_ICE_BOLT), //
						pair(1, RPGFightAction.SPELL_LIGHTNING), //
						pair(1, RPGFightAction.SPELL_HEAL), //
						pair(3, RPGFightAction.CHARM_CUTE)))//
				.build(rpgEnemies);

		final Targetting withoutCharmTargetting = new Targetting(
				TargetCheck.ENEMY.without(fluffer10kFun, FighterClass.MECHANICAL)//
						.without(FighterStatus.CHARMED)//
						.withStacksLessThan(FighterStatus.CHARM_RESISTANCE, 3));

		final RPGFightAction[] attacks = { //
				RPGFightAction.ATTACK_S_10, //
				RPGFightAction.SPELL_FIREBALL, //
				RPGFightAction.SPELL_ICE_BOLT, //
				RPGFightAction.SPELL_LIGHTNING };

		final ActionSelector actionSelector3 = data -> {
			if (data.activeFighter.hp < 15) {
				data.targetId = data.activeFighter.id;
				return RPGFightAction.SPELL_HEAL;
			}

			final FighterData target = withoutCharmTargetting.getFirst(data.fight, data.activeFighter);
			if (target != null) {
				data.targetId = target.id;
				return RPGFightAction.CHARM_CUTE;
			}

			return getRandom(attacks);
		};

		makeBuilder(DARK_ELF_3)//
				.strength(7).agility(8).intelligence(9)//
				.baseHp(15)//
				.diff(15)//
				.classes(FighterClass.CLEVER, FighterClass.USES_MAGIC)//
				.actionSelector(actionSelector3)//
				.build(rpgEnemies);
	}
}