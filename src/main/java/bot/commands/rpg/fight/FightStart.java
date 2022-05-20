package bot.commands.rpg.fight;

import static bot.util.CollectionUtils.toMap;
import static bot.util.Utils.Pair.pair;
import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

import bot.Fluffer10kFun;
import bot.commands.rpg.RPGStatUtils.RPGStatsData;
import bot.commands.rpg.fight.enemies.RPGEnemyData;
import bot.commands.rpg.fight.fightRewards.FightEndReward;
import bot.data.fight.EnemyFighterData;
import bot.data.fight.FightData;
import bot.data.fight.FightData.FightType;
import bot.data.fight.FighterClass;
import bot.data.fight.FighterData;
import bot.data.fight.FighterData.FighterType;
import bot.data.fight.FighterStatus;
import bot.data.fight.FighterStatusData;
import bot.data.fight.PlayerFighterData;
import bot.userData.ServerUserData;

public class FightStart {

	private final Fluffer10kFun fluffer10kFun;

	public FightStart(final Fluffer10kFun fluffer10kFun) {
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
	}

	private List<String> getfightersOrder(final Server server, final Map<String, FighterData> fighters) {
		final List<String> fightersOrder = new ArrayList<>(fighters.keySet());
		fightersOrder.sort((a, b) -> {
			final RPGStatsData aStats = fluffer10kFun.rpgStatUtils.getTotalStatsInFight(fighters.get(a),
					server.getId());
			final RPGStatsData bStats = fluffer10kFun.rpgStatUtils.getTotalStatsInFight(fighters.get(b),
					server.getId());

			final boolean aQuick = aStats.classes.contains(FighterClass.QUICK);
			final boolean bQuick = bStats.classes.contains(FighterClass.QUICK);
			if (aQuick != bQuick) {
				return aQuick ? -1 : 1;
			}

			final int statsQuickness = (bStats.agility - aStats.agility) / 6;
			if (statsQuickness != 0) {
				return statsQuickness;
			}

			final boolean aPlayer = fighters.get(a).player() != null;
			final boolean bPlayer = fighters.get(b).player() != null;
			return aPlayer == bPlayer ? 0 : aPlayer ? -1 : 1;
		});

		return fightersOrder;
	}

	public void startFightPvE(final ServerTextChannel channel, final User user, final RPGEnemyData enemy) {
		startFightPvE(channel, user, enemy, FightEndReward.DEFAULT);
	}

	public void startFightPvE(final ServerTextChannel channel, final User user, final String enemyId,
			final FightEndReward reward) {
		startFightPvE(channel, user, asList(fluffer10kFun.rpgEnemies.get(enemyId)), reward);
	}

	public void startFightPvE(final ServerTextChannel channel, final User user, final RPGEnemyData enemy,
			final FightEndReward reward) {
		startFightPvE(channel, user, asList(enemy), reward);
	}

	private Map<String, FighterData> prepareFighters(final Server server, final User user,
			final ServerUserData userData, final List<RPGEnemyData> enemies) {
		final String name = userData.rpg.getName(user, server);
		final RPGStatsData playerStats = fluffer10kFun.rpgStatUtils.getTotalStats(userData);
		final FighterData playerFighter = PlayerFighterData.fromStats("PLAYER", user.getId(), "PLAYER", name,
				playerStats);

		final Map<String, FighterData> fighters = toMap(pair("PLAYER", playerFighter));
		for (int i = 0; i < enemies.size(); i++) {
			final String id = "ENEMY_" + i;
			final FighterData enemyFighter = EnemyFighterData.fromStats(id, enemies.get(i), "ENEMY");
			fighters.put(id, enemyFighter);
		}

		return fighters;
	}

	public void startFightPvE(final ServerTextChannel channel, final User user, final List<RPGEnemyData> enemies,
			final FightEndReward reward) {
		final Server server = channel.getServer();
		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(server, user);
		final Map<String, FighterData> fighters = prepareFighters(server, user, userData, enemies);

		startFight(channel, FightType.PVE, fighters, reward);
	}

	public void startFight(final ServerTextChannel channel, final FightType type,
			final Map<String, FighterData> fighters, final FightEndReward reward) {
		final Server server = channel.getServer();

		fighters.values().forEach(fighter -> giveStartingBuffs(server, fighter));
		final List<String> fightersOrder = getfightersOrder(server, fighters);

		final FightData fight = new FightData(server.getId(), type, fighters, fightersOrder, reward);
		for (final String fighterId : fightersOrder) {
			fight.addTurnDescription(fighters.get(fighterId).name + " is ready to fight!");
		}

		fluffer10kFun.botDataUtils.addFight(fight);

		for (final FighterData fighter : fighters.values()) {
			final PlayerFighterData player = fighter.player();
			if (player != null) {
				fluffer10kFun.serverUserDataUtils.getUserData(player).rpg.fightId = fight.id;
			}
		}

		fluffer10kFun.fightActionsHandler.startFight(channel, fight);
	}
}
