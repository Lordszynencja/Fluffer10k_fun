package bot.commands.rpg.fight.enemies.data.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.FighterClass;

public class Merrow extends EnemiesOfRace {
	public static final String MERROW_1 = "MERROW_1";

	public Merrow() {
		super(MonsterGirlRace.MERROW);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		makeBuilder(MERROW_1)//
				.strength(5).agility(4).intelligence(4)//
				.baseHp(10)//
				.diff(1)//
				.classes(FighterClass.WET)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom( //
						pair(2, RPGFightAction.ATTACK_S_7), //
						pair(1, RPGFightAction.CHARM)) //
						.gentle())//
				.build(rpgEnemies);
	}
}