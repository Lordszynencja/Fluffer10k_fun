package bot.commands.rpg.fight.enemies.data.races;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.FightTempData;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.FighterData;
import bot.data.fight.FighterStatus;

public class Myconid extends EnemiesOfRace {
	public static final String MYCONID_1 = "MYCONID_1";
	public static final String MYCONID_2 = "MYCONID_2";

	public Myconid() {
		super(MonsterGirlRace.MYCONID);
	}

	private static RPGFightAction actionSelect(final FightTempData data) {
		if (data.fight.turn % 3 == 0) {
			for (final FighterData enemy : data.fight.fighters.values()) {
				if (data.activeFighter.id.equals(enemy.heldBy)) {
					data.targetId = enemy.id;
					return enemy.statuses.isStatus(FighterStatus.CHARMED) ? RPGFightAction.ATTACK_A_3
							: RPGFightAction.CHARM_SPORES;
				}
				if (!enemy.team.equals(data.activeFighter.team) && enemy.heldBy == null) {
					data.targetId = enemy.id;
				}
			}

			if (data.targetId != null) {
				return RPGFightAction.GRAB_JUMP;
			}
		}

		return RPGFightAction.ATTACK_A_3;
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		rpgEnemies.add(makeBuilder(MYCONID_1)//
				.strength(2).agility(6).intelligence(2)//
				.baseHp(5)//
				.diff(1)//
				.actionSelector(Myconid::actionSelect));

		rpgEnemies.add(makeBuilder(MYCONID_2)//
				.strength(2).agility(8).intelligence(3)//
				.baseHp(10)//
				.diff(1)//
				.actionSelector(Myconid::actionSelect));
	}
}