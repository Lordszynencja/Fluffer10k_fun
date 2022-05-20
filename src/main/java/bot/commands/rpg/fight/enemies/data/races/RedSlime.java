package bot.commands.rpg.fight.enemies.data.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.FighterClass;

public class RedSlime extends EnemiesOfRace {
	public static final String RED_SLIME_1 = "RED_SLIME_1";

	public RedSlime() {
		super(MonsterGirlRace.RED_SLIME);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		makeBuilder(RED_SLIME_1)//
				.strength(4).agility(3).intelligence(3)//
				.baseHp(10)//
				.diff(1)//
				.classes(FighterClass.WET, FighterClass.WEAK_TO_FIRE)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom( //
						pair(4, RPGFightAction.ATTACK_S_3), //
						pair(1, RPGFightAction.GRAB_SLIME)))//
				.build(rpgEnemies);
	}
}