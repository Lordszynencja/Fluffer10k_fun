package bot.commands.rpg.fight.enemies.data.races;

import static bot.commands.rpg.fight.RPGFightAction.ATTACK_A_1;
import static bot.commands.rpg.fight.RPGFightAction.SPELL_PARALYZING_THUNDER;
import static bot.data.fight.FighterClass.FLYING;
import static bot.data.fight.FighterClass.USES_MAGIC;
import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.MonsterGirls.MonsterGirlRace;

public class Thunderbird extends EnemiesOfRace {
	public static final String THUNDERBIRD_0 = "THUNDERBIRD_0";
	public static final String THUNDERBIRD_1 = "THUNDERBIRD_1";
	public static final String THUNDERBIRD_2 = "THUNDERBIRD_2";
	public static final String THUNDERBIRD_3 = "THUNDERBIRD_3";
	public static final String THUNDERBIRD_4 = "THUNDERBIRD_4";
	public static final String THUNDERBIRD_5 = "THUNDERBIRD_5";

	public Thunderbird() {
		super(MonsterGirlRace.THUNDERBIRD);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector actionSelector = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom( //
				pair(2, ATTACK_A_1), //
				pair(1, SPELL_PARALYZING_THUNDER));

		for (int i = 0; i <= 5; i++) {
			makeBuilder2(i)//
					.strength(3 + i).agility(5 + i).intelligence(5 + i * 2)//
					.baseHp(5 + 3 * i)//
					.classes(FLYING, USES_MAGIC)//
					.actionSelector(actionSelector)//
					.build(rpgEnemies);
		}
	}
}
