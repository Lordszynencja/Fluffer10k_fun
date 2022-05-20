package bot.commands.rpg.fight.enemies.data.races;

import static bot.commands.rpg.fight.RPGFightAction.ATTACK_S_1;
import static bot.commands.rpg.fight.RPGFightAction.GRAB_CLING;
import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.MonsterGirls.MonsterGirlRace;

public class Kobold extends EnemiesOfRace {
	public static final String KOBOLD_0 = "KOBOLD_0";
	public static final String KOBOLD_1 = "KOBOLD_1";
	public static final String KOBOLD_2 = "KOBOLD_2";
	public static final String KOBOLD_3 = "KOBOLD_3";
	public static final String KOBOLD_4 = "KOBOLD_4";
	public static final String KOBOLD_5 = "KOBOLD_5";

	public Kobold() {
		super(MonsterGirlRace.KOBOLD);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector actionSelector = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
				pair(3, ATTACK_S_1), //
				pair(1, GRAB_CLING))//
				.gentle();

		for (int i = 0; i <= 5; i++) {
			makeBuilder2(i)//
					.strength(4 + i * 4 / 5).agility(3 + i * 3 / 5).intelligence(2 + i * 2 / 5)//
					.baseHp(5 + i * 2)//
					.actionSelector(actionSelector)//
					.build(rpgEnemies);
		}
	}
}