package bot.commands.rpg.fight.enemies.special;

import static bot.commands.rpg.fight.RPGFightAction.ATTACK_A_1;
import static bot.commands.rpg.fight.RPGFightAction.ATTACK_S_1;
import static bot.commands.rpg.fight.RPGFightAction.CHARM;
import static bot.commands.rpg.fight.RPGFightAction.FRENZY;
import static bot.commands.rpg.fight.RPGFightAction.GRAB;
import static bot.commands.rpg.fight.RPGFightAction.LEVEL_DRAIN_0;
import static bot.commands.rpg.fight.RPGFightAction.LEVEL_DRAIN_1;
import static bot.commands.rpg.fight.RPGFightAction.MANTICORE_VENOM;
import static bot.commands.rpg.fight.RPGFightAction.MONSTER_LORD_RESTRICTION_0;
import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.commands.rpg.fight.enemies.special.OtherEnemyData.OtherEnemyDataBuilder;
import bot.data.fight.FighterStatus;

public class MonsterArmyOfficer {
	public static final String MONSTER_ARMY_OFFICER_0 = "MONSTER_ARMY_OFFICER_0";
	public static final String MONSTER_ARMY_OFFICER_1 = "MONSTER_ARMY_OFFICER_1";
	public static final String MONSTER_ARMY_OFFICER_2 = "MONSTER_ARMY_OFFICER_2";
	public static final String MONSTER_ARMY_OFFICER_3 = "MONSTER_ARMY_OFFICER_3";
	public static final String MONSTER_ARMY_OFFICER_4 = "MONSTER_ARMY_OFFICER_4";
	public static final String MONSTER_ARMY_OFFICER_5 = "MONSTER_ARMY_OFFICER_5";
	public static final String MONSTER_ARMY_OFFICER_6 = "MONSTER_ARMY_OFFICER_6";

	private static void addLilian(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector actionSelector0 = data -> (!data.fight.fighters.get("PLAYER").statuses
				.isStatus(FighterStatus.RESTRICTED)) ? MONSTER_LORD_RESTRICTION_0 : LEVEL_DRAIN_0;
		final ActionSelector actionSelector1 = data -> (data.fight.turn > 2
				&& !data.fight.fighters.get("PLAYER").statuses.isStatus(FighterStatus.RESTRICTED))
						? MONSTER_LORD_RESTRICTION_0
						: LEVEL_DRAIN_0;
		final ActionSelector actionSelector2 = data -> (data.fight.turn > 4
				&& !data.fight.fighters.get("PLAYER").statuses.isStatus(FighterStatus.RESTRICTED))
						? MONSTER_LORD_RESTRICTION_0
						: LEVEL_DRAIN_0;

		new OtherEnemyDataBuilder(MONSTER_ARMY_OFFICER_0, "Powerful manticore")//
				.strength(25).agility(40).intelligence(30)//
				.baseHp(100)//
				.armor(10)//
				.level(999)//
				.actionSelector(actionSelector0)//
				.build(rpgEnemies);
		new OtherEnemyDataBuilder(MONSTER_ARMY_OFFICER_1, "Powerful manticore Lilian")//
				.strength(25).agility(40).intelligence(30)//
				.baseHp(100)//
				.armor(10)//
				.level(45)//
				.actionSelector(actionSelector1)//
				.build(rpgEnemies);
		new OtherEnemyDataBuilder(MONSTER_ARMY_OFFICER_2, "Powerful manticore Lilian")//
				.strength(25).agility(40).intelligence(30)//
				.baseHp(100)//
				.armor(10)//
				.level(45)//
				.actionSelector(actionSelector2)//
				.build(rpgEnemies);

		final ActionSelector subactionSelector3 = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
				pair(4, ATTACK_A_1), //
				pair(1, GRAB), //
				pair(1, MANTICORE_VENOM), //
				pair(1, CHARM));

		final ActionSelector actionSelector3 = data -> {
			if (data.fight.turn < 3) {
				return LEVEL_DRAIN_0;
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

	private static void addHelga(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector subactionSelectorMinotaur = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
				pair(3, ATTACK_S_1), //
				pair(1, GRAB), //
				pair(1, FRENZY));

		final ActionSelector actionSelectorA = data -> (data.fight.turn < 3 || data.fight.turn % 3 == 0) ? LEVEL_DRAIN_1
				: subactionSelectorMinotaur.select(data);

		new OtherEnemyDataBuilder(MONSTER_ARMY_OFFICER_4, "Minotaur officer Helga")//
				.strength(50).agility(30).intelligence(25)//
				.baseHp(125)//
				.armor(12)//
				.level(55)//
				.actionSelector(actionSelectorA)//
				.build(rpgEnemies);
		new OtherEnemyDataBuilder(MONSTER_ARMY_OFFICER_5, "Minotaur officer Helga")//
				.strength(50).agility(30).intelligence(25)//
				.baseHp(125)//
				.armor(12)//
				.level(55)//
				.actionSelector(actionSelectorA)//
				.build(rpgEnemies);

		final ActionSelector actionSelectorB = data -> (data.fight.turn < 3) ? LEVEL_DRAIN_1
				: subactionSelectorMinotaur.select(data);

		new OtherEnemyDataBuilder(MONSTER_ARMY_OFFICER_6, "Minotaur officer Helga")//
				.strength(50).agility(30).intelligence(25)//
				.baseHp(125)//
				.armor(12)//
				.level(55)//
				.actionSelector(actionSelectorB)//
				.build(rpgEnemies);
	}

	public static void add(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		addLilian(fluffer10kFun, rpgEnemies);
		addHelga(fluffer10kFun, rpgEnemies);
	}
}
