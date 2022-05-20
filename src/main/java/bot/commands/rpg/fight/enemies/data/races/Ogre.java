package bot.commands.rpg.fight.enemies.data.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;

public class Ogre extends EnemiesOfRace {
	public static final String OGRE_1 = "OGRE_1";
	public static final String OGRE_2 = "OGRE_2";

	public Ogre() {
		super(MonsterGirlRace.OGRE);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		makeBuilder(OGRE_1)//
				.strength(10).agility(4).intelligence(3)//
				.baseHp(15)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(4, RPGFightAction.ATTACK_S_1), //
						pair(1, RPGFightAction.GRAB), //
						pair(1, RPGFightAction.FRENZY)))//
				.build(rpgEnemies);
		makeBuilder(OGRE_2)//
				.strength(14).agility(5).intelligence(4)//
				.baseHp(20)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(3, RPGFightAction.ATTACK_S_1), //
						pair(1, RPGFightAction.GRAB), //
						pair(1, RPGFightAction.FRENZY)))//
				.build(rpgEnemies);
	}
}