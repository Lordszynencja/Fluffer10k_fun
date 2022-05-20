package bot.commands.rpg.fight.enemies.data.races;

import static bot.commands.rpg.fight.RPGFightAction.ATTACK_S_2;
import static bot.commands.rpg.fight.RPGFightAction.GRAB;
import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.MonsterGirls.MonsterGirlRace;

public class Hellhound extends EnemiesOfRace {
	public static final String HELLHOUND_0 = "HELLHOUND_0";
	public static final String HELLHOUND_1 = "HELLHOUND_1";
	public static final String HELLHOUND_2 = "HELLHOUND_2";
	public static final String HELLHOUND_3 = "HELLHOUND_3";
	public static final String HELLHOUND_4 = "HELLHOUND_4";
	public static final String HELLHOUND_5 = "HELLHOUND_5";

	public Hellhound() {
		super(MonsterGirlRace.HELLHOUND);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector actionSelector = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
				pair(2, ATTACK_S_2), //
				pair(1, GRAB));

		for (int i = 0; i <= 5; i++) {
			makeBuilder2(i)//
					.strength(15 + 3 * i).agility(10 + 2 * i).intelligence(5 + i)//
					.baseHp(15 + 4 * i)//
					.armor(1 + (i + 1) / 2)//
					.actionSelector(actionSelector)//
					.build(rpgEnemies);
		}
	}
}