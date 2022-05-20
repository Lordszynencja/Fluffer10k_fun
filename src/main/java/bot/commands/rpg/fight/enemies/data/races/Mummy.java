package bot.commands.rpg.fight.enemies.data.races;

import static bot.commands.rpg.fight.RPGFightAction.ATTACK_S_1;
import static bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.action;
import static bot.data.fight.FighterClass.UNDEAD;
import static bot.data.fight.FighterClass.WEAK_TO_FIRE;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.MonsterGirls.MonsterGirlRace;

public class Mummy extends EnemiesOfRace {
	public static final String MUMMY_0 = "MUMMY_0";
	public static final String MUMMY_1 = "MUMMY_1";
	public static final String MUMMY_2 = "MUMMY_2";
	public static final String MUMMY_3 = "MUMMY_3";
	public static final String MUMMY_4 = "MUMMY_4";
	public static final String MUMMY_5 = "MUMMY_5";

	public Mummy() {
		super(MonsterGirlRace.MUMMY);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector actionSelector = action(ATTACK_S_1);

		for (int i = 0; i <= 5; i++) {
			makeBuilder2(i)//
					.strength(10 + 2 * i).agility(1).intelligence(1 + i)//
					.baseHp(10 + 4 * i)//
					.classes(UNDEAD, WEAK_TO_FIRE)//
					.actionSelector(actionSelector)//
					.build(rpgEnemies);
		}
	}
}