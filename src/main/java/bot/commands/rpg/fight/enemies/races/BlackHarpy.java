package bot.commands.rpg.fight.enemies.races;

import static bot.commands.rpg.fight.RPGFightAction.ATTACK_A_1;
import static bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.basicSpells;
import static bot.data.fight.FighterClass.FLYING;
import static bot.data.fight.FighterClass.USES_MAGIC;
import static bot.data.items.loot.LootTable.single;
import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.MonsterGirls.MonsterGirlRace;

public class BlackHarpy extends EnemiesOfRace {
	public static String getId(final int tier) {
		return MonsterGirlRace.BLACK_HARPY.name() + "_" + tier;
	}

	public BlackHarpy() {
		super(MonsterGirlRace.BLACK_HARPY);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector youngActionSelector = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom( //
				pair(1, RPGFightAction.CALL_BLACK_HARPIES))//
				.gentle();

		makeBuilder("YOUNG_BLACK_HARPY").name("Young black harpy")//
				.standard()//
				.strength(2)//
				.agility(5)//
				.intelligence(5)//
				.baseHp(5)//
				.actionSelector(youngActionSelector)//
				.build(rpgEnemies);

		final ActionSelector actionSelectorA = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom( //
				pair(1, ATTACK_A_1));
		final ActionSelector actionSelectorB = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFromTables( //
				pair(2, single(ATTACK_A_1)), //
				pair(1, basicSpells));
		final ActionSelector actionSelectorC = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFromTables( //
				pair(1, single(ATTACK_A_1)), //
				pair(1, basicSpells));

		for (int i = 0; i <= 5; i++) {
			makeStandardBuilder(i, 6, 1, 9, 2, 11, 3, 10, 3)//
					.classes(FLYING, USES_MAGIC)//
					.actionSelector(i < 3 ? actionSelectorA//
							: i < 5 ? actionSelectorB//
									: actionSelectorC)//
					.build(rpgEnemies);
		}
	}
}
