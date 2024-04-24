package bot.commands.rpg.fight.enemies.races;

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
	public DragonZombie() {
		super(MonsterGirlRace.DRAGON_ZOMBIE);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector actionSelector = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
				pair(4, ATTACK_S_4), //
				pair(1, CHARM_ROTTEN_BREATH));

		for (int i = 0; i <= 5; i++) {
			makeStandardBuilder(i, 13, 3, 8, 2, 5, 1, 20, 6)//
					.armor(2 + i * 4 / 5)//
					.classes(UNDEAD, USES_MAGIC)//
					.actionSelector(actionSelector)//
					.build(rpgEnemies);
		}
	}
}