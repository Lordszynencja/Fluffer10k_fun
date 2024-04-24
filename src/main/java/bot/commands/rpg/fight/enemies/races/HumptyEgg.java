package bot.commands.rpg.fight.enemies.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.FighterClass;

public class HumptyEgg extends EnemiesOfRace {
	public static final String HUMPTY_EGG_1 = "HUMPTY_EGG_1";

	public HumptyEgg() {
		super(MonsterGirlRace.HUMPTY_EGG);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		makeBuilderOld(HUMPTY_EGG_1)//
				.strength(4).agility(3).intelligence(6)//
				.baseHp(20)//
				.armor(1)//
				.classes(FighterClass.SLIME_REGEN, FighterClass.WET, FighterClass.WEAK_TO_FIRE)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom( //
						pair(1, RPGFightAction.ATTACK_S_3), //
						pair(1, RPGFightAction.EGG_SHELL), //
						pair(1, RPGFightAction.GRAB_SLIME)))//
				.build(rpgEnemies);
	}
}