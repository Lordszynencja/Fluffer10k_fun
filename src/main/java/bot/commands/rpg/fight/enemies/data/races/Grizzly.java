package bot.commands.rpg.fight.enemies.data.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;

public class Grizzly extends EnemiesOfRace {
	public static final String GRIZZLY_1 = "GRIZZLY_1";
	public static final String GRIZZLY_2 = "GRIZZLY_2";
	public static final String GRIZZLY_3 = "GRIZZLY_3";
	public static final String GRIZZLY_4 = "GRIZZLY_4";

	public Grizzly() {
		super(MonsterGirlRace.GRIZZLY);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		rpgEnemies.add(makeBuilder(GRIZZLY_1)//
				.strength(17).agility(7).intelligence(3)//
				.baseHp(25)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(7, RPGFightAction.ATTACK_S_2), //
						pair(1, RPGFightAction.GRAB), //
						pair(1, RPGFightAction.FRENZY_GRIZZLY))));
		rpgEnemies.add(makeBuilder(GRIZZLY_2)//
				.strength(19).agility(8).intelligence(3)//
				.baseHp(30)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(6, RPGFightAction.ATTACK_S_2), //
						pair(1, RPGFightAction.GRAB), //
						pair(1, RPGFightAction.FRENZY_GRIZZLY))));
		rpgEnemies.add(makeBuilder(GRIZZLY_3)//
				.strength(21).agility(9).intelligence(4)//
				.baseHp(35)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(5, RPGFightAction.ATTACK_S_2), //
						pair(1, RPGFightAction.GRAB), //
						pair(1, RPGFightAction.FRENZY_GRIZZLY))));
		rpgEnemies.add(makeBuilder(GRIZZLY_4)//
				.strength(23).agility(10).intelligence(4)//
				.baseHp(40)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(4, RPGFightAction.ATTACK_S_2), //
						pair(1, RPGFightAction.GRAB), //
						pair(1, RPGFightAction.FRENZY_GRIZZLY))));
	}
}
