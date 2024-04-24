package bot.commands.rpg.fight.enemies.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.FighterClass;

public class Mothman extends EnemiesOfRace {
	public static final String MOTHMAN_1 = "MOTHMAN_1";

	public Mothman() {
		super(MonsterGirlRace.MOTHMAN);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		makeBuilderOld(MOTHMAN_1)//
				.strength(2).agility(3).intelligence(7)//
				.baseHp(10)//
				.diff(2)//
				.classes(FighterClass.FLYING, FighterClass.USES_MAGIC)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(4, RPGFightAction.ATTACK_A_3), //
						pair(1, RPGFightAction.CHARM_WINGS), //
						pair(1, RPGFightAction.MOTHMAN_POWDER)))//
				.build(rpgEnemies);
	}
}