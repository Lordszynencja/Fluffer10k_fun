package bot.commands.rpg.fight;

import static bot.data.fight.FighterClass.SLIME_REGEN;
import static bot.data.fight.FighterClass.STARTS_IN_SPIRIT_FORM;
import static java.lang.Math.max;

import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

import bot.Fluffer10kFun;
import bot.commands.rpg.RPGStatUtils.RPGStatsData;
import bot.commands.rpg.fight.enemies.RPGEnemyData;
import bot.data.fight.EnemyFighterData;
import bot.data.fight.FightData;
import bot.data.fight.FighterData;
import bot.data.fight.FighterData.FighterType;
import bot.data.fight.FighterStatus;
import bot.data.fight.FighterStatusData;
import bot.data.fight.PlayerFighterData;
import bot.userData.ServerUserData;

public class FighterCreator {
	private final Fluffer10kFun fluffer10kFun;

	public FighterCreator(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;
	}

	private void giveStartingBuffs(final Server server, final FighterData fighter) {
		final RPGStatsData stats = fluffer10kFun.rpgStatUtils.getTotalStatsInFight(fighter, server.getId());
		if (fighter.type == FighterType.PLAYER) {
			final int charmResistance = stats.intelligence / 4;
			if (charmResistance > 0) {
				fighter.statuses.addStatus(
						new FighterStatusData(FighterStatus.CHARM_RESISTANCE).endless().stacks(charmResistance));
			}

			final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(server.getId(),
					fighter.player().userId);

			if (userData.rpg.undyingAvailable) {
				fighter.statuses.addStatus(new FighterStatusData(FighterStatus.UNDYING).endless());
			}
		}

		if (stats.classes.contains(STARTS_IN_SPIRIT_FORM)) {
			fighter.statuses.addStatus(new FighterStatusData(FighterStatus.SPIRIT_FORM).endless());
		}

		if (stats.classes.contains(SLIME_REGEN)) {
			fighter.statuses.addStatus(new FighterStatusData(FighterStatus.SLIME_REGEN).endless());
		}
	}

	public FighterData getPlayerFighter(final Server server, final User user, final ServerUserData userData,
			final String fighterId, final String team) {
		final String name = userData.rpg.getName(fluffer10kFun.apiUtils, user, server);
		final RPGStatsData stats = fluffer10kFun.rpgStatUtils.getTotalStats(userData);
		final FighterData fighter = PlayerFighterData.fromStats(fighterId, user.getId(), team, name, stats);
		giveStartingBuffs(server, fighter);

		return fighter;
	}

	public FighterData getAIFighter(final Server server, final String id, final String fighterId, final String team) {
		final RPGEnemyData enemyData = fluffer10kFun.rpgEnemies.get(id);
		return getAIFighter(server, enemyData, fighterId, team);
	}

	public FighterData getAIFighter(final Server server, final RPGEnemyData enemyData, final String fighterId,
			final String team) {
		final EnemyFighterData fighter = EnemyFighterData.fromStats(fighterId, enemyData, team);
		giveStartingBuffs(server, fighter);
		return fighter;
	}

	public void addFighter(final Server server, final FightData fight, final String id, final String team) {
		int newId = 0;
		for (final String fighterId : fight.fightersOrder) {
			if (fighterId.startsWith(team)) {
				try {
					newId = max(newId, Integer.valueOf(fighterId.substring(team.length() + 1)) + 1);
				} catch (final NumberFormatException e) {
				}
			}
		}

		final FighterData newFighter = getAIFighter(server, id, team + "_" + newId, team);
		newFighter.fight = fight;

		fight.fighters.put(newFighter.id, newFighter);
		fight.fightersOrder.add(newFighter.id);
	}
}
