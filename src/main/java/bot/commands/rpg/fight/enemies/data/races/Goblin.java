package bot.commands.rpg.fight.enemies.data.races;

import static bot.commands.rpg.fight.RPGFightAction.ATTACK_S_2;
import static bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.action;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.MonsterGirls.MonsterGirlRace;

public class Goblin extends EnemiesOfRace {
	public static final String GOBLIN_0 = "GOBLIN_0";
	public static final String GOBLIN_1 = "GOBLIN_1";
	public static final String GOBLIN_2 = "GOBLIN_2";
	public static final String GOBLIN_3 = "GOBLIN_3";
	public static final String GOBLIN_4 = "GOBLIN_4";
	public static final String GOBLIN_5 = "GOBLIN_5";

	public Goblin() {
		super(MonsterGirlRace.GOBLIN);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector actionSelector = action(ATTACK_S_2);

		for (int i = 0; i < 6; i++) {
			makeBuilder2(i)//
					.strength(2 + i).agility(3 + i).intelligence(1 + i / 2)//
					.baseHp(5 + 2 * i)//
					.actionSelector(actionSelector)//
					.build(rpgEnemies);
		}
	}
}