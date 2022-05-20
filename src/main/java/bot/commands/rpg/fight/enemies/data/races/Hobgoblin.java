package bot.commands.rpg.fight.enemies.data.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.MonsterGirls.MonsterGirlRace;

public class Hobgoblin extends EnemiesOfRace {
	public static final String HOBGOBLIN_1 = "HOBGOBLIN_1";
	public static final String HOBGOBLIN_2 = "HOBGOBLIN_2";

	public Hobgoblin() {
		super(MonsterGirlRace.HOBGOBLIN);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector selector1 = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
				pair(2, RPGFightAction.ATTACK_S_2), //
				pair(1, RPGFightAction.GRAB));

		rpgEnemies.add(makeBuilder(HOBGOBLIN_1)//
				.strength(5).agility(4).intelligence(1)//
				.baseHp(10)//
				.actionSelector(selector1));

		final ActionSelector selector2 = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
				pair(1, RPGFightAction.ATTACK_S_2), //
				pair(1, RPGFightAction.GRAB));
		rpgEnemies.add(makeBuilder(HOBGOBLIN_2)//
				.strength(9).agility(5).intelligence(2)//
				.baseHp(15)//
				.actionSelector(selector2));
	}
}
