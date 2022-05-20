package bot.commands.rpg.fight.enemies.data.races;

import static bot.commands.rpg.fight.RPGFightAction.ATTACK_A_1;
import static bot.commands.rpg.fight.RPGFightAction.CHARM;
import static bot.commands.rpg.fight.RPGFightAction.GRAB;
import static bot.commands.rpg.fight.RPGFightAction.MANTICORE_VENOM;
import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.MonsterGirls.MonsterGirlRace;

public class Manticore extends EnemiesOfRace {
	public static final String MANTICORE_0 = "MANTICORE_0";
	public static final String MANTICORE_1 = "MANTICORE_1";
	public static final String MANTICORE_2 = "MANTICORE_2";
	public static final String MANTICORE_3 = "MANTICORE_3";
	public static final String MANTICORE_4 = "MANTICORE_4";
	public static final String MANTICORE_5 = "MANTICORE_5";

	public Manticore() {
		super(MonsterGirlRace.MANTICORE);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector actionSelector = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
				pair(4, ATTACK_A_1), //
				pair(1, GRAB), //
				pair(1, MANTICORE_VENOM), //
				pair(1, CHARM));

		for (int i = 0; i <= 5; i++) {
			makeBuilder2(i)//
					.strength(10 + 2 * i).agility(15 + 4 * i).intelligence(5 + 2 * i)//
					.baseHp(10 + 8 * i)//
					.actionSelector(actionSelector)//
					.build(rpgEnemies);
		}
	}
}