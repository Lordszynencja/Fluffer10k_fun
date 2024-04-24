package bot.commands.rpg.fight.enemies.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.FighterClass;

public class Tritonia extends EnemiesOfRace {
	public static final String TRITONIA_1 = "TRITONIA_1";

	public Tritonia() {
		super(MonsterGirlRace.TRITONIA);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		makeBuilderOld(TRITONIA_1)//
				.strength(3).agility(3).intelligence(6)//
				.baseHp(5)//
				.classes(FighterClass.SALTABLE, FighterClass.WET, FighterClass.WEAK_TO_FIRE)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom( //
						pair(2, RPGFightAction.ATTACK_S_3), //
						pair(1, RPGFightAction.GRAB_SLIME), //
						pair(1, RPGFightAction.CHARM_FINS))//
						.gentle())//
				.build(rpgEnemies);
	}
}