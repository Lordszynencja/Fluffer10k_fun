package bot.commands.rpg.fight.enemies.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;

public class Sphinx extends EnemiesOfRace {
	public static final String SPHINX_1 = "SPHINX_1";
	public static final String SPHINX_2 = "SPHINX_2";

	public Sphinx() {
		super(MonsterGirlRace.SPHINX);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		rpgEnemies.add(makeBuilderOld(SPHINX_1)//
				.strength(3).agility(4).intelligence(7)//
				.baseHp(10)//
				.diff(2)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(6, RPGFightAction.ATTACK_A_1), //
						pair(1, RPGFightAction.CURSE), //
						pair(1, RPGFightAction.CHARM))));
		rpgEnemies.add(makeBuilderOld(SPHINX_2)//
				.strength(5).agility(6).intelligence(9)//
				.baseHp(15)//
				.diff(3)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(4, RPGFightAction.ATTACK_A_1), //
						pair(1, RPGFightAction.CURSE), //
						pair(1, RPGFightAction.CHARM))));
	}
}
