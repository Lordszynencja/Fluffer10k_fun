package bot.commands.rpg.fight.enemies.data.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.FighterClass;

public class Ratatoskr extends EnemiesOfRace {
	public static final String RATATOSKR_0 = "RATATOSKR_0";
	public static final String RATATOSKR_1 = "RATATOSKR_1";
	public static final String RATATOSKR_2 = "RATATOSKR_2";
	public static final String RATATOSKR_3 = "RATATOSKR_3";
	public static final String RATATOSKR_4 = "RATATOSKR_4";
	public static final String RATATOSKR_5 = "RATATOSKR_5";

	public Ratatoskr() {
		super(MonsterGirlRace.RATATOSKR);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector actionSelector = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
				pair(3, RPGFightAction.ATTACK_A_1), //
				pair(1, RPGFightAction.CHARM_RATATOSKR))//
				.gentle();

		for (int i = 0; i <= 5; i++) {
			makeBuilder2(i)//
					.strength(3 + i).agility(5 + 3 * i).intelligence(5 + 2 * i)//
					.baseHp(5 + 2 * i)//
					.classes(FighterClass.USES_MAGIC)//
					.actionSelector(actionSelector)//
					.build(rpgEnemies);
		}
	}
}
