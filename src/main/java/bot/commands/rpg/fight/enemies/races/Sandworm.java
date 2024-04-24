package bot.commands.rpg.fight.enemies.races;

import static bot.commands.rpg.fight.RPGFightAction.ATTACK_S_7;
import static bot.commands.rpg.fight.RPGFightAction.GRAB_COIL;
import static bot.commands.rpg.fight.RPGFightAction.SANDWORM_HIDE;
import static bot.commands.rpg.fight.RPGFightAction.SANDWORM_OUT;
import static bot.commands.rpg.fight.RPGFightAction.WAIT;
import static bot.util.RandomUtils.getRandomBoolean;
import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.FighterStatus;

public class Sandworm extends EnemiesOfRace {
	public Sandworm() {
		super(MonsterGirlRace.SANDWORM);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector normalActions = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
				pair(3, ATTACK_S_7), //
				pair(1, GRAB_COIL), //
				pair(1, SANDWORM_HIDE));

		final ActionSelector actionSelector = data -> {
			if (data.activeFighter.statuses.isStatus(FighterStatus.SANDWORM_SHELL)) {
				if (data.activeFighter.hp == data.activeFighter.maxHp || getRandomBoolean()) {
					return SANDWORM_OUT;
				}

				return WAIT;
			}

			return normalActions.select(data);
		};

		for (int i = 0; i <= 5; i++) {
			makeStandardBuilder(i, 15, 3, 7, 2, 2, 1, 30, 5)//
					.armor(3 + i)//
					.actionSelector(actionSelector)//
					.build(rpgEnemies);
		}
	}
}
