package bot.commands.rpg.fight.enemies.data.races;

import static bot.commands.rpg.fight.RPGFightAction.ATTACK_S_2;
import static bot.commands.rpg.fight.RPGFightAction.GRAB_COIL;
import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.MonsterGirls.MonsterGirlRace;

public class Wurm extends EnemiesOfRace {
	public static final String WURM_0 = "WURM_0";
	public static final String WURM_1 = "WURM_1";
	public static final String WURM_2 = "WURM_2";
	public static final String WURM_3 = "WURM_3";
	public static final String WURM_4 = "WURM_4";
	public static final String WURM_5 = "WURM_5";

	public Wurm() {
		super(MonsterGirlRace.WURM);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector actionSelector = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
				pair(4, ATTACK_S_2), //
				pair(1, GRAB_COIL));

		for (int i = 0; i <= 5; i++) {
			makeBuilder2(Integer.toString(i))//
					.strength(20 + 5 * i).agility(10 + 2 * i).intelligence(2 + i)//
					.baseHp(30 + 10 * i)//
					.armor(3 + i)//
					.actionSelector(actionSelector)//
					.build(rpgEnemies);
		}
	}
}
