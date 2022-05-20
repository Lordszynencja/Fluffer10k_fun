package bot.commands.rpg.fight.enemies.data.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.FighterClass;

public class Fairy extends EnemiesOfRace {
	public static final String FAIRY_0 = "FAIRY_0";
	public static final String FAIRY_1 = "FAIRY_1";
	public static final String FAIRY_2 = "FAIRY_2";
	public static final String FAIRY_3 = "FAIRY_3";
	public static final String FAIRY_4 = "FAIRY_4";
	public static final String FAIRY_5 = "FAIRY_5";

	public Fairy() {
		super(MonsterGirlRace.FAIRY);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		makeBuilder(FAIRY_0)//
				.strength(1).agility(1).intelligence(1)//
				.baseHp(1)//
				.classes(FighterClass.USES_MAGIC)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(4, RPGFightAction.ATTACK_S_5), //
						pair(1, RPGFightAction.CHARM_CUTE)))//
				.build(rpgEnemies);
		makeBuilder(FAIRY_1)//
				.strength(1).agility(1).intelligence(1)//
				.baseHp(3)//
				.classes(FighterClass.USES_MAGIC)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(4, RPGFightAction.ATTACK_S_5), //
						pair(1, RPGFightAction.CHARM_CUTE)))//
				.build(rpgEnemies);
		makeBuilder(FAIRY_2)//
				.strength(2).agility(1).intelligence(1)//
				.baseHp(3)//
				.classes(FighterClass.USES_MAGIC)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(4, RPGFightAction.ATTACK_S_5), //
						pair(1, RPGFightAction.CHARM_CUTE)))//
				.build(rpgEnemies);
		makeBuilder(FAIRY_3)//
				.strength(1).agility(2).intelligence(1)//
				.baseHp(3)//
				.classes(FighterClass.USES_MAGIC)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(4, RPGFightAction.ATTACK_S_5), //
						pair(1, RPGFightAction.CHARM_CUTE)))//
				.build(rpgEnemies);
		makeBuilder(FAIRY_4)//
				.strength(1).agility(1).intelligence(2)//
				.baseHp(3)//
				.classes(FighterClass.USES_MAGIC)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(2, RPGFightAction.ATTACK_S_5), //
						pair(1, RPGFightAction.CHARM_CUTE)))//
				.build(rpgEnemies);
		makeBuilder(FAIRY_5)//
				.strength(2).agility(2).intelligence(2)//
				.baseHp(4)//
				.classes(FighterClass.USES_MAGIC)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(2, RPGFightAction.ATTACK_S_5), //
						pair(1, RPGFightAction.CHARM_CUTE)))//
				.build(rpgEnemies);
	}
}
