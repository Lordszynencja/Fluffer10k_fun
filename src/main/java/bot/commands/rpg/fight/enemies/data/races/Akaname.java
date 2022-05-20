package bot.commands.rpg.fight.enemies.data.races;

import static bot.commands.rpg.fight.RPGFightAction.ATTACK_A_3;
import static bot.commands.rpg.fight.RPGFightAction.GRAB_CLING;
import static bot.commands.rpg.fight.RPGFightAction.LICK;
import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.MonsterGirls.MonsterGirlRace;

public class Akaname extends EnemiesOfRace {
	public static final String AKANAME_0 = "AKANAME_0";
	public static final String AKANAME_1 = "AKANAME_1";
	public static final String AKANAME_2 = "AKANAME_2";
	public static final String AKANAME_3 = "AKANAME_3";
	public static final String AKANAME_4 = "AKANAME_4";
	public static final String AKANAME_5 = "AKANAME_5";

	public Akaname() {
		super(MonsterGirlRace.AKANAME);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector actionSelector = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
				pair(3, ATTACK_A_3), //
				pair(1, LICK), //
				pair(1, GRAB_CLING))//
				.gentle();

		for (int i = 0; i <= 5; i++) {
			makeBuilder2(i)//
					.strength(2 + i * 3 / 5).agility(6 + i).intelligence(3 + i * 3 / 5)//
					.baseHp(5 + i)//
					.actionSelector(actionSelector)//
					.build(rpgEnemies);
		}
	}
}
