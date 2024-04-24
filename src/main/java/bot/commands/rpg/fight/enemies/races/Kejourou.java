package bot.commands.rpg.fight.enemies.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.FighterClass;

public class Kejourou extends EnemiesOfRace {
	public static final String KEJOUROU_1 = "KEJOUROU_1";
	public static final String KEJOUROU_2 = "KEJOUROU_2";

	public Kejourou() {
		super(MonsterGirlRace.KEJOUROU);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		makeBuilderOld(KEJOUROU_1)//
				.strength(6).agility(4).intelligence(8)//
				.baseHp(10)//
				.diff(1)//
				.classes(FighterClass.USES_MAGIC)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(3, RPGFightAction.ATTACK_S_9), //
						pair(1, RPGFightAction.CHARM), //
						pair(1, RPGFightAction.KEJOUROU_HAIR)))//
				.build(rpgEnemies);
		makeBuilderOld(KEJOUROU_2)//
				.strength(8).agility(5).intelligence(11)//
				.baseHp(15)//
				.diff(2)//
				.classes(FighterClass.USES_MAGIC)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(2, RPGFightAction.ATTACK_S_9), //
						pair(1, RPGFightAction.CHARM), //
						pair(1, RPGFightAction.KEJOUROU_HAIR)))//
				.build(rpgEnemies);
	}
}