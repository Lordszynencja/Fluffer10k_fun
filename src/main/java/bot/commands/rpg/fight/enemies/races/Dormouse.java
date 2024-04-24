package bot.commands.rpg.fight.enemies.races;

import static bot.commands.rpg.fight.RPGFightAction.ATTACK_S_2;
import static bot.commands.rpg.fight.RPGFightAction.DORMOUSE_SLEEP;
import static bot.commands.rpg.fight.RPGFightAction.ESCAPE;
import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.FighterData;
import bot.data.fight.FighterStatus;

public class Dormouse extends EnemiesOfRace {
	public Dormouse() {
		super(MonsterGirlRace.DORMOUSE);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector actionSelectorA = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
				pair(3, ATTACK_S_2), //
				pair(1, DORMOUSE_SLEEP))//
				.dormouseSleep()//
				.gentle();

		final ActionSelector actionSelectorB = fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
				pair(2, ATTACK_S_2), //
				pair(1, DORMOUSE_SLEEP))//
				.dormouseSleep()//
				.gentle();

		final ActionSelector actionSelectorC = ((ActionSelector) data -> {
			if (data.fight.turn >= 3 && !data.activeFighter.attacked) {
				return ESCAPE;
			}

			for (final FighterData enemy : data.fight.fighters.values()) {
				if (!enemy.team.equals(data.activeFighter.team)) {
					if (enemy.statuses.isStatus(FighterStatus.CHARMED)) {
						data.targetId = enemy.id;
						return ATTACK_S_2;
					}
				}
			}

			return DORMOUSE_SLEEP;
		}).dormouseSleep();

		for (int i = 0; i <= 5; i++) {
			makeStandardBuilder(i)//
					.strength(i).agility(5 + i).intelligence(5 + i)//
					.baseHp(5 + i)//
					.diff(i < 4 ? 0 : 5)//
					.actionSelector(i < 2 ? actionSelectorA//
							: i < 4 ? actionSelectorB//
									: actionSelectorC)//
					.build(rpgEnemies);
		}
	}
}