package bot.commands.rpg.fight.enemies.data.races;

import static bot.commands.rpg.fight.RPGFightAction.ATLACH_NACHA_VENOM;
import static bot.commands.rpg.fight.RPGFightAction.ATTACK_S_11;
import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.MonsterGirls.MonsterGirlRace;

public class AtlachNacha extends EnemiesOfRace {
	public static final String ATLACH_NACHA_0 = "ATLACH_NACHA_0";
	public static final String ATLACH_NACHA_1 = "ATLACH_NACHA_1";
	public static final String ATLACH_NACHA_2 = "ATLACH_NACHA_2";
	public static final String ATLACH_NACHA_3 = "ATLACH_NACHA_3";
	public static final String ATLACH_NACHA_4 = "ATLACH_NACHA_4";
	public static final String ATLACH_NACHA_5 = "ATLACH_NACHA_5";

	public AtlachNacha() {
		super(MonsterGirlRace.ATLACH_NACHA);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector actionSelector = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
				pair(2, ATTACK_S_11), //
				pair(1, ATLACH_NACHA_VENOM));

		for (int i = 0; i <= 5; i++) {
			makeBuilder2(i)//
					.strength(8 + i * 2).agility(10 + i * 3).intelligence(6 + i * 2)//
					.baseHp(20 + i * 6)//
					.armor(2 + i)//
					.actionSelector(actionSelector)//
					.build(rpgEnemies);
		}
	}
}