package bot.commands.rpg.fight.enemies.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;

public class Werecat extends EnemiesOfRace {
	public static final String WERECAT_1 = "WERECAT_1";
	public static final String WERECAT_2 = "WERECAT_2";

	public Werecat() {
		super(MonsterGirlRace.WERECAT);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		rpgEnemies.add(makeBuilderOld(WERECAT_1)//
				.strength(3).agility(5).intelligence(3)//
				.baseHp(10)//
				.diff(1)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(7, RPGFightAction.ATTACK_A_1), //
						pair(1, RPGFightAction.CHARM_SWEET_VOICE))));
		rpgEnemies.add(makeBuilderOld(WERECAT_2)//
				.strength(3).agility(9).intelligence(4)//
				.baseHp(15)//
				.diff(2)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(3, RPGFightAction.ATTACK_A_1), //
						pair(1, RPGFightAction.CHARM_SWEET_VOICE))));
	}
}
