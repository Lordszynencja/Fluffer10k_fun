package bot.commands.rpg.fight.enemies.data.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.FighterClass;

public class Kappa extends EnemiesOfRace {
	public static final String KAPPA_1 = "KAPPA_1";

	public Kappa() {
		super(MonsterGirlRace.KAPPA);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		makeBuilder(KAPPA_1)//
				.strength(2).agility(4).intelligence(5)//
				.baseHp(5)//
				.diff(-1)//
				.classes(FighterClass.WET)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(1, RPGFightAction.ATTACK_S_1)))//
				.build(rpgEnemies);
	}
}