package bot.commands.rpg.fight.enemies.data.races;

import static bot.commands.rpg.fight.RPGFightAction.ATTACK_S_4;
import static bot.commands.rpg.fight.RPGFightAction.CHARM_ROTTEN_BREATH;
import static bot.data.fight.FighterClass.UNDEAD;
import static bot.data.fight.FighterClass.USES_MAGIC;
import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.MonsterGirls.MonsterGirlRace;

public class DragonZombie extends EnemiesOfRace {
	public static final String DRAGON_ZOMBIE_0 = "DRAGON_ZOMBIE_0";
	public static final String DRAGON_ZOMBIE_1 = "DRAGON_ZOMBIE_1";
	public static final String DRAGON_ZOMBIE_2 = "DRAGON_ZOMBIE_2";
	public static final String DRAGON_ZOMBIE_3 = "DRAGON_ZOMBIE_3";
	public static final String DRAGON_ZOMBIE_4 = "DRAGON_ZOMBIE_4";
	public static final String DRAGON_ZOMBIE_5 = "DRAGON_ZOMBIE_5";

	public DragonZombie() {
		super(MonsterGirlRace.DRAGON_ZOMBIE);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector actionSelector = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
				pair(4, ATTACK_S_4), //
				pair(1, CHARM_ROTTEN_BREATH));

		for (int i = 0; i <= 5; i++) {
			makeBuilder2(i)//
					.strength(17 + i * 3).agility(9 + i * 2).intelligence(5 + i)//
					.baseHp(20 + i * 6)//
					.armor(2 + i * 4 / 5)//
					.classes(UNDEAD, USES_MAGIC)//
					.actionSelector(actionSelector)//
					.build(rpgEnemies);
		}
	}
}