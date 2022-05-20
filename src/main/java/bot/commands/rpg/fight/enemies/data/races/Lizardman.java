package bot.commands.rpg.fight.enemies.data.races;

import static bot.commands.rpg.fight.RPGFightAction.ATTACK_S_15;
import static bot.commands.rpg.fight.RPGFightAction.GRAB;
import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.MonsterGirls.MonsterGirlRace;

public class Lizardman extends EnemiesOfRace {
	public static final String LIZARDMAN_0 = "LIZARDMAN_0";
	public static final String LIZARDMAN_1 = "LIZARDMAN_1";
	public static final String LIZARDMAN_2 = "LIZARDMAN_2";
	public static final String LIZARDMAN_3 = "LIZARDMAN_3";
	public static final String LIZARDMAN_4 = "LIZARDMAN_4";
	public static final String LIZARDMAN_5 = "LIZARDMAN_5";

	public Lizardman() {
		super(MonsterGirlRace.LIZARDMAN);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector actionSelector = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
				pair(2, ATTACK_S_15), //
				pair(1, GRAB));

		for (int i = 0; i <= 5; i++) {
			makeBuilder2(i)//
					.strength(10 + i * 3).agility(7 + i * 3).intelligence(5 + i * 2)//
					.baseHp(15 + i * 2)//
					.actionSelector(actionSelector)//
					.build(rpgEnemies);
		}
	}
}