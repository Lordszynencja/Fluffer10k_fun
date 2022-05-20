package bot.commands.rpg.fight.enemies.data.races;

import static bot.commands.rpg.fight.RPGFightAction.ATTACK_A_1;
import static bot.commands.rpg.fight.RPGFightAction.GRAB;
import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.MonsterGirls.MonsterGirlRace;

public class Jinko extends EnemiesOfRace {
	public static final String JINKO_0 = "JINKO_0";
	public static final String JINKO_1 = "JINKO_1";
	public static final String JINKO_2 = "JINKO_2";
	public static final String JINKO_3 = "JINKO_3";
	public static final String JINKO_4 = "JINKO_4";
	public static final String JINKO_5 = "JINKO_5";

	public Jinko() {
		super(MonsterGirlRace.JINKO);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector actionSelector = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
				pair(3, ATTACK_A_1), //
				pair(1, GRAB));
		for (int i = 0; i <= 5; i++) {
			makeBuilder2(i)//
					.strength(10 + i * 2).agility(15 + i * 3).intelligence(10 + i)//
					.baseHp(15 + i * 4)//
					.armor(i)//
					.actionSelector(actionSelector)//
					.build(rpgEnemies);
		}

	}
}
