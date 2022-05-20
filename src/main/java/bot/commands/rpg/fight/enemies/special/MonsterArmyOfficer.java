package bot.commands.rpg.fight.enemies.special;

import static bot.commands.rpg.fight.RPGFightAction.ATTACK_A_1;
import static bot.commands.rpg.fight.RPGFightAction.CHARM;
import static bot.commands.rpg.fight.RPGFightAction.GRAB;
import static bot.commands.rpg.fight.RPGFightAction.MANTICORE_VENOM;
import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.FightTempData;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.commands.rpg.fight.enemies.special.OtherEnemyData.OtherEnemyDataBuilder;
import bot.data.fight.FighterStatus;

public class MonsterArmyOfficer {
	public static final String MONSTER_ARMY_OFFICER_0 = "MONSTER_ARMY_OFFICER_0";
	public static final String MONSTER_ARMY_OFFICER_1 = "MONSTER_ARMY_OFFICER_1";
	public static final String MONSTER_ARMY_OFFICER_2 = "MONSTER_ARMY_OFFICER_2";
	public static final String MONSTER_ARMY_OFFICER_3 = "MONSTER_ARMY_OFFICER_3";

	private static RPGFightAction actionSelector0(final FightTempData data) {
		if (!data.fight.fighters.get("PLAYER").statuses.isStatus(FighterStatus.RESTRICTED)) {
			return RPGFightAction.MONSTER_LORD_RESTRICTION_0;
		}

		return RPGFightAction.LEVEL_DRAIN_0;
	}

	private static RPGFightAction actionSelector1(final FightTempData data) {
		if (data.fight.turn > 2 && !data.fight.fighters.get("PLAYER").statuses.isStatus(FighterStatus.RESTRICTED)) {
			return RPGFightAction.MONSTER_LORD_RESTRICTION_0;
		}

		return RPGFightAction.LEVEL_DRAIN_0;
	}

	private static RPGFightAction actionSelector2(final FightTempData data) {
		if (data.fight.turn > 4 && !data.fight.fighters.get("PLAYER").statuses.isStatus(FighterStatus.RESTRICTED)) {
			return RPGFightAction.MONSTER_LORD_RESTRICTION_0;
		}

		return RPGFightAction.LEVEL_DRAIN_0;
	}

	public static void add(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		new OtherEnemyDataBuilder(MONSTER_ARMY_OFFICER_0, "Powerful manticore")//
				.strength(25).agility(40).intelligence(30)//
				.baseHp(100)//
				.armor(10)//
				.level(999)//
				.actionSelector(MonsterArmyOfficer::actionSelector0)//
				.build(rpgEnemies);
		new OtherEnemyDataBuilder(MONSTER_ARMY_OFFICER_1, "Powerful manticore Lilian")//
				.strength(25).agility(40).intelligence(30)//
				.baseHp(100)//
				.armor(10)//
				.level(45)//
				.actionSelector(MonsterArmyOfficer::actionSelector1)//
				.build(rpgEnemies);
		new OtherEnemyDataBuilder(MONSTER_ARMY_OFFICER_2, "Powerful manticore Lilian")//
				.strength(25).agility(40).intelligence(30)//
				.baseHp(100)//
				.armor(10)//
				.level(45)//
				.actionSelector(MonsterArmyOfficer::actionSelector2)//
				.build(rpgEnemies);

		final ActionSelector subactionSelector3 = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
				pair(4, ATTACK_A_1), //
				pair(1, GRAB), //
				pair(1, MANTICORE_VENOM), //
				pair(1, CHARM));

		final ActionSelector actionSelector3 = data -> {
			if (data.fight.turn < 3) {
				return RPGFightAction.LEVEL_DRAIN_0;
			}

			return subactionSelector3.select(data);
		};

		new OtherEnemyDataBuilder(MONSTER_ARMY_OFFICER_3, "Powerful manticore Lilian")//
				.strength(25).agility(40).intelligence(30)//
				.baseHp(100)//
				.armor(10)//
				.level(45)//
				.actionSelector(actionSelector3)//
				.build(rpgEnemies);
	}
}
