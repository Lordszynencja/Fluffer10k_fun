package bot.commands.rpg.fight.enemies.data.races;

import static bot.data.fight.FighterClass.USES_MAGIC;
import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.MonsterGirls.MonsterGirlRace;

public class CheshireCat extends EnemiesOfRace {
	public static final String CHESHIRE_CAT_0 = "CHESHIRE_CAT_1";
	public static final String CHESHIRE_CAT_1 = "CHESHIRE_CAT_1";
	public static final String CHESHIRE_CAT_2 = "CHESHIRE_CAT_2";
	public static final String CHESHIRE_CAT_3 = "CHESHIRE_CAT_3";
	public static final String CHESHIRE_CAT_4 = "CHESHIRE_CAT_4";
	public static final String CHESHIRE_CAT_5 = "CHESHIRE_CAT_5";

	public CheshireCat() {
		super(MonsterGirlRace.CHESHIRE_CAT);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector actionSelector = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
				pair(3, RPGFightAction.ATTACK_A_1), //
				pair(1, RPGFightAction.GRAB), //
				pair(1, RPGFightAction.CHARM_SWEET_VOICE));

		for (int i = 0; i <= 5; i++) {
			makeBuilder2(i)//
					.strength(5 + i * 2).agility(11 + i * 4).intelligence(8 + i * 2)//
					.baseHp(15 + i * 4)//
					.classes(USES_MAGIC)//
					.actionSelector(actionSelector)//
					.build(rpgEnemies);
		}
	}
}