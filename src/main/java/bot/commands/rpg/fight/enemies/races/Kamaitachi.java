package bot.commands.rpg.fight.enemies.races;

import static bot.commands.rpg.fight.RPGFightAction.ATTACK_A_1;
import static bot.commands.rpg.fight.RPGFightAction.ATTACK_S_1;
import static bot.commands.rpg.fight.RPGFightAction.GRAB;
import static bot.commands.rpg.fight.RPGFightAction.SPELL_HEAL_SISTERS;
import static bot.data.fight.FighterClass.TRIPLE_ATTACK;
import static bot.data.fight.FighterClass.USES_MAGIC;
import static bot.util.RandomUtils.getRandomBoolean;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.MonsterGirls.MonsterGirlRace;

public class Kamaitachi extends EnemiesOfRace {
	public Kamaitachi() {
		super(MonsterGirlRace.KAMAITACHI);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector actionSelector = data -> {
			if (data.fight.actionsLeft == 3) {
				return getRandomBoolean(0.25) ? GRAB : ATTACK_S_1;
			}

			if (data.fight.actionsLeft == 2) {
				return ATTACK_A_1;
			}
			if (data.fight.actionsLeft == 1) {
				return getRandomBoolean(0.33) ? SPELL_HEAL_SISTERS : ATTACK_A_1;
			}

			return ATTACK_S_1;
		};

		for (int i = 0; i <= 5; i++) {
			makeStandardBuilder(i, 5, 2, 5, 2, 5, 1, 15, 3)//
					.classes(TRIPLE_ATTACK, USES_MAGIC)//
					.actionSelector(actionSelector)//
					.build(rpgEnemies);
		}
	}
}