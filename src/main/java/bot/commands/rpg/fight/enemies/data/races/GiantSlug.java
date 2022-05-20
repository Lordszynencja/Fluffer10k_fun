package bot.commands.rpg.fight.enemies.data.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.FighterClass;

public class GiantSlug extends EnemiesOfRace {
	public static final String GIANT_SLUG_1 = "GIANT_SLUG_1";

	public GiantSlug() {
		super(MonsterGirlRace.GIANT_SLUG);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		makeBuilder(GIANT_SLUG_1)//
				.strength(5).agility(2).intelligence(3)//
				.baseHp(5)//
				.classes(FighterClass.SLOW, FighterClass.SALTABLE)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom( //
						pair(4, RPGFightAction.ATTACK_S_3), //
						pair(1, RPGFightAction.GRAB_TOP)))//
				.build(rpgEnemies);
	}
}
