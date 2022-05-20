package bot.commands.rpg.fight.enemies.data.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.FighterClass;

public class BubbleSlime extends EnemiesOfRace {
	public static final String BUBBLE_SLIME_1 = "BUBBLE_SLIME_1";

	public BubbleSlime() {
		super(MonsterGirlRace.BUBBLE_SLIME);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		makeBuilder(BUBBLE_SLIME_1)//
				.strength(7).agility(3).intelligence(3)//
				.baseHp(15)//
				.diff(2)//
				.classes(FighterClass.USES_MAGIC, FighterClass.WET, FighterClass.WEAK_TO_FIRE)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom( //
						pair(2, RPGFightAction.ATTACK_S_3), //
						pair(1, RPGFightAction.CHARM), //
						pair(1, RPGFightAction.GRAB_SLIME)))//
				.build(rpgEnemies);
	}
}
