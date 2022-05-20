package bot.commands.rpg.fight.enemies.data.races;

import static bot.data.fight.FighterClass.USES_MAGIC;
import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.MonsterGirls.MonsterGirlRace;

public class Alice extends EnemiesOfRace {
	public static final String ALICE_0 = "ALICE_0";
	public static final String ALICE_1 = "ALICE_1";
	public static final String ALICE_2 = "ALICE_2";
	public static final String ALICE_3 = "ALICE_3";
	public static final String ALICE_4 = "ALICE_4";
	public static final String ALICE_5 = "ALICE_5";

	public Alice() {
		super(MonsterGirlRace.ALICE);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector actionSelector = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom( //
				pair(2, RPGFightAction.ATTACK_S_1), //
				pair(1, RPGFightAction.CHARM_CUTE))//
				.gentle();

		for (int i = 0; i <= 5; i++) {
			makeBuilder2(i)//
					.strength(2 + i).agility(3 + i * 2).intelligence(7 + i * 2)//
					.baseHp(5 + i * 2)//
					.classes(USES_MAGIC)//
					.actionSelector(actionSelector)//
					.build(rpgEnemies);
		}
	}
}
