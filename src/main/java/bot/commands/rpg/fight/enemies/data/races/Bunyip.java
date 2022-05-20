package bot.commands.rpg.fight.enemies.data.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.MonsterGirls.MonsterGirlRace;

public class Bunyip extends EnemiesOfRace {
	public static final String BUNYIP_0 = "BUNYIP_0";
	public static final String BUNYIP_1 = "BUNYIP_1";
	public static final String BUNYIP_2 = "BUNYIP_2";
	public static final String BUNYIP_3 = "BUNYIP_3";
	public static final String BUNYIP_4 = "BUNYIP_4";
	public static final String BUNYIP_5 = "BUNYIP_5";

	public Bunyip() {
		super(MonsterGirlRace.BUNYIP);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector actionSelector = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
				pair(1, RPGFightAction.ATTACK_S_2), //
				pair(1, RPGFightAction.GRAB_COIL));

		for (int i = 0; i <= 5; i++) {
			makeBuilder2(i)//
					.strength(23 + 2 * i).agility(19 + 2 * i).intelligence(7 + i)//
					.baseHp(30)//
					.actionSelector(actionSelector)//
					.build(rpgEnemies);
		}
	}
}
