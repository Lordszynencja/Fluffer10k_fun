package bot.commands.rpg.fight.enemies.data.races;

import static bot.commands.rpg.fight.RPGFightAction.ATTACK_S_7;
import static bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.action;
import static bot.data.fight.FighterClass.UNDEAD;
import static bot.data.fight.FighterClass.WEAK_TO_FIRE;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.MonsterGirls.MonsterGirlRace;

public class Zombie extends EnemiesOfRace {
	public static final String ZOMBIE_0 = "ZOMBIE_0";
	public static final String ZOMBIE_1 = "ZOMBIE_1";
	public static final String ZOMBIE_2 = "ZOMBIE_2";
	public static final String ZOMBIE_3 = "ZOMBIE_3";
	public static final String ZOMBIE_4 = "ZOMBIE_4";
	public static final String ZOMBIE_5 = "ZOMBIE_5";

	public Zombie() {
		super(MonsterGirlRace.ZOMBIE);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector actionSelector = action(ATTACK_S_7);
		for (int i = 0; i <= 5; i++) {
			makeBuilder2(i)//
					.strength(4 + i).agility(1).intelligence(1)//
					.baseHp(5 + i * 2)//
					.classes(UNDEAD, WEAK_TO_FIRE)//
					.actionSelector(actionSelector)//
					.build(rpgEnemies);
		}

	}
}
