package bot.commands.rpg.fight.enemies.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.FighterClass;

public class Skeleton extends EnemiesOfRace {
	public static final String SKELETON_1 = "SKELETON_1";
	public static final String SKELETON_2 = "SKELETON_2";

	public Skeleton() {
		super(MonsterGirlRace.SKELETON);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		rpgEnemies.add(makeBuilderOld(SKELETON_1)//
				.strength(2).agility(2).intelligence(1)//
				.baseHp(5)//
				.armor(1)//
				.classes(FighterClass.UNDEAD, FighterClass.WEAK_TO_FIRE)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(1, RPGFightAction.ATTACK_S_1))));
		rpgEnemies.add(makeBuilderOld(SKELETON_2)//
				.strength(4).agility(3).intelligence(2)//
				.baseHp(5)//
				.armor(1)//
				.classes(FighterClass.UNDEAD, FighterClass.WEAK_TO_FIRE)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(1, RPGFightAction.ATTACK_S_1))));
	}
}