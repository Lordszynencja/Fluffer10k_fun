package bot.commands.rpg.fight.enemies.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;

public class Weresheep extends EnemiesOfRace {
	public static final String WERESHEEP_1 = "WERESHEEP_1";

	public Weresheep() {
		super(MonsterGirlRace.WERESHEEP);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		rpgEnemies.add(makeBuilderOld(WERESHEEP_1)//
				.strength(3).agility(2).intelligence(2)//
				.baseHp(3)//
				.diff(1)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(3, RPGFightAction.ATTACK_A_1), //
						pair(1, RPGFightAction.GRAB_CLING), //
						pair(1, RPGFightAction.WERESHEEP_WOOL))));
	}
}
