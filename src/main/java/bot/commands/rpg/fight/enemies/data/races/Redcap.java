package bot.commands.rpg.fight.enemies.data.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;

public class Redcap extends EnemiesOfRace {
	public static final String REDCAP_1 = "REDCAP_1";
	public static final String REDCAP_2 = "REDCAP_2";

	public Redcap() {
		super(MonsterGirlRace.REDCAP);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		rpgEnemies.add(makeBuilder(REDCAP_1)//
				.strength(6).agility(6).intelligence(4)//
				.baseHp(5)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(1, RPGFightAction.ATTACK_S_6))));
		rpgEnemies.add(makeBuilder(REDCAP_2)//
				.strength(8).agility(8).intelligence(4)//
				.baseHp(10)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(1, RPGFightAction.ATTACK_S_6))));
	}
}
