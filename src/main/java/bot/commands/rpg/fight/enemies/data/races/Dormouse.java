package bot.commands.rpg.fight.enemies.data.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.FighterData;
import bot.data.fight.FighterStatus;

public class Dormouse extends EnemiesOfRace {
	public static final String DORMOUSE_1 = "DORMOUSE_1";
	public static final String DORMOUSE_2 = "DORMOUSE_2";
	public static final String DORMOUSE_3 = "DORMOUSE_3";

	public Dormouse() {
		super(MonsterGirlRace.DORMOUSE);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		makeBuilder(DORMOUSE_1)//
				.strength(2).agility(3).intelligence(6)//
				.baseHp(5)//
				.diff(1)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(3, RPGFightAction.ATTACK_S_2), //
						pair(1, RPGFightAction.DORMOUSE_SLEEP))//
						.dormouseSleep()//
						.gentle())//
				.build(rpgEnemies);
		makeBuilder(DORMOUSE_2)//
				.strength(3).agility(4).intelligence(6)//
				.baseHp(5)//
				.diff(2)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(2, RPGFightAction.ATTACK_S_2), //
						pair(1, RPGFightAction.DORMOUSE_SLEEP))//
						.dormouseSleep()//
						.gentle())//
				.build(rpgEnemies);

		final ActionSelector actionSelector3 = ((ActionSelector) data -> {
			if (data.fight.turn >= 3 && !data.activeFighter.attacked) {
				return RPGFightAction.ESCAPE;
			}

			for (final FighterData enemy : data.fight.fighters.values()) {
				if (!enemy.team.equals(data.activeFighter.team)) {
					if (enemy.statuses.isStatus(FighterStatus.CHARMED)) {
						data.targetId = enemy.id;
						return RPGFightAction.ATTACK_S_2;
					}
				}
			}

			return RPGFightAction.DORMOUSE_SLEEP;
		}).dormouseSleep();

		makeBuilder(DORMOUSE_3)//
				.strength(3).agility(4).intelligence(6)//
				.baseHp(5)//
				.diff(3)//
				.actionSelector(actionSelector3)//
				.build(rpgEnemies);
	}
}