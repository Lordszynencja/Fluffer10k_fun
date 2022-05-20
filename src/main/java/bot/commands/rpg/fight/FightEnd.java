package bot.commands.rpg.fight;

import static bot.commands.rpg.fight.fightRewards.FightEndReward.BLESSING_0;
import static bot.commands.rpg.fight.fightRewards.FightEndReward.BLESSING_1;
import static bot.commands.rpg.fight.fightRewards.FightEndReward.BLESSING_2;
import static bot.commands.rpg.fight.fightRewards.FightEndReward.BLESSING_3;
import static bot.commands.rpg.fight.fightRewards.FightEndReward.BUSINESS_AS_USUAL_QUEST_FIGHT_REWARD;
import static bot.commands.rpg.fight.fightRewards.FightEndReward.DEFAULT;
import static bot.commands.rpg.fight.fightRewards.FightEndReward.MINERS_HOME_QUEST_REWARD;
import static bot.util.CollectionUtils.toMap;
import static bot.util.Utils.Pair.pair;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.pow;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Map;

import org.javacord.api.entity.message.MessageUpdater;
import org.javacord.api.entity.user.User;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.fightRewards.Blessing0Reward;
import bot.commands.rpg.fight.fightRewards.Blessing1Reward;
import bot.commands.rpg.fight.fightRewards.Blessing2Reward;
import bot.commands.rpg.fight.fightRewards.Blessing3Reward;
import bot.commands.rpg.fight.fightRewards.BusinessAsUsualQuestFightReward;
import bot.commands.rpg.fight.fightRewards.DefaultReward;
import bot.commands.rpg.fight.fightRewards.FightEndReward;
import bot.commands.rpg.fight.fightRewards.MinersHomeQuestFightReward;
import bot.data.fight.EnemyFighterData;
import bot.data.fight.FightData;
import bot.data.fight.FightData.FightType;
import bot.data.fight.FighterData;
import bot.data.fight.FighterData.FighterType;
import bot.data.fight.FighterStatus;
import bot.data.fight.PlayerFighterData;
import bot.userData.ServerUserData;
import bot.userData.UserBlessingData.Blessing;

public class FightEnd {
	public static interface RewardCreator {
		void giveRewards(FightTempData data);
	}

	private final Fluffer10kFun fluffer10kFun;

	private final Map<FightEndReward, RewardCreator> rewardCreators;

	public FightEnd(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;

		rewardCreators = toMap(//
				pair(BLESSING_0, new Blessing0Reward(fluffer10kFun)), //
				pair(BLESSING_1, new Blessing1Reward(fluffer10kFun)), //
				pair(BLESSING_2, new Blessing2Reward(fluffer10kFun)), //
				pair(BLESSING_3, new Blessing3Reward(fluffer10kFun)), //
				pair(BUSINESS_AS_USUAL_QUEST_FIGHT_REWARD, new BusinessAsUsualQuestFightReward(fluffer10kFun)), //
				pair(DEFAULT, new DefaultReward(fluffer10kFun)), //
				pair(MINERS_HOME_QUEST_REWARD, new MinersHomeQuestFightReward(fluffer10kFun)));
	}

	private void updateFightMessage(final FightTempData data) {
		final MessageUpdater updater = new MessageUpdater(data.message);
		fluffer10kFun.fightSender.addFight(updater, data.fight);
		updater.replaceMessage();
	}

	private int getAverageLevel(final FightData fight) {
		int averageLevel = 0;
		for (final FighterData fighter : fight.fighters.values()) {
			averageLevel += fluffer10kFun.rpgStatUtils.getLevel(fighter);
		}
		return averageLevel / fight.fighters.size();
	}

	private static final Map<Integer, Double> expMultipliers = toMap(//
			pair(-8, 0.2), //
			pair(-7, 0.3), //
			pair(-6, 0.5), //
			pair(-5, 0.6), //
			pair(-4, 0.7), //
			pair(-3, 0.8), //
			pair(-2, 0.9), //
			pair(-1, 0.95), //
			pair(0, 1.0), //
			pair(1, 1.05), //
			pair(2, 1.1), //
			pair(3, 1.15), //
			pair(4, 1.25), //
			pair(5, 1.35), //
			pair(6, 1.5), //
			pair(7, 1.75), //
			pair(8, 2.0));

	private long getExpForEnemy(final int fighterLevel, int enemyLevel) {
		final int levelDifferenceIndex = max(-8, min(8, enemyLevel - fighterLevel));
		double levelMultiplier = 1;
		if (enemyLevel > 30) {
			levelMultiplier *= pow(1.05, enemyLevel - 30);
			enemyLevel = 30;
		}
		if (enemyLevel > 20) {
			levelMultiplier *= pow(1.1, enemyLevel - 20);
			enemyLevel = 20;
		}
		if (enemyLevel > 10) {
			levelMultiplier *= pow(1.2, enemyLevel - 10);
			enemyLevel = 10;
		}
		levelMultiplier *= pow(1.25, enemyLevel);

		return (long) (15 * levelMultiplier * expMultipliers.get(levelDifferenceIndex));
	}

	private long getExp(final long exp, final int averageLevel, final int fighterLevel,
			final List<FighterData> enemies) {
		final int levelDifferenceIndex = max(-8, min(8, averageLevel - fighterLevel));
		final long activityExp = (long) (exp * expMultipliers.get(levelDifferenceIndex));

		long enemiesDefeatedExp = 0;
		for (final FighterData enemy : enemies) {
			final long expForEnemy = getExpForEnemy(fighterLevel, fluffer10kFun.rpgStatUtils.getLevel(enemy));
			if (enemy.isOut()) {
				enemiesDefeatedExp += expForEnemy;
			} else {
				enemiesDefeatedExp += expForEnemy * (enemy.maxHp - enemy.hp) / enemy.maxHp;
			}
		}

		return activityExp + enemiesDefeatedExp;
	}

	private void addExp(final FightTempData data) {
		final int averageLevel = getAverageLevel(data.fight);

		for (final FighterData fighter : data.fight.fighters.values()) {
			if (fighter.type != FighterType.PLAYER) {
				continue;
			}

			final List<FighterData> enemies = data.fight.fighters.values().stream()//
					.filter(enemy -> !enemy.team.equals(fighter.team))//
					.collect(toList());
			final PlayerFighterData playerFighter = fighter.player();
			final long exp = getExp(playerFighter.exp, averageLevel, fluffer10kFun.rpgStatUtils.getLevel(fighter),
					enemies);

			final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(playerFighter);
			final User user = fluffer10kFun.apiUtils.getUser(playerFighter.userId);
			data.channel.sendMessage(userData.addExpAndMakeEmbed(exp, user, data.channel.getServer()));
		}
	}

	private void setAfterFightFlags(final FightData fight) {
		fight.fighters.values().stream()//
				.map(fighter -> fighter.player())//
				.filter(player -> player != null)//
				.map(player -> pair(player, fluffer10kFun.serverUserDataUtils.getUserData(player)))//
				.forEach(playerData -> playerData.b.rpg.undyingAvailable &= playerData.a.statuses
						.isStatus(FighterStatus.UNDYING));
	}

	private void giveBackStaminaForQuickRecovery(final FightData fight) {
		fight.fighters.values().stream()//
				.map(fighter -> fighter.player())//
				.filter(player -> player != null)//
				.map(player -> pair(player, fluffer10kFun.serverUserDataUtils.getUserData(player)))//
				.forEach(playerData -> {
					if (playerData.b.blessings.blessingsObtained.contains(Blessing.QUICK_RECOVERY)
							&& playerData.a.hp >= playerData.a.maxHp / 4) {
						final double percent = (1.0 * playerData.a.hp / playerData.a.maxHp - 0.25) / 0.75;
						final int staminaRecovered = (int) (25 * percent);
						playerData.b.addStamina(staminaRecovered);
					}
				});
	}

	private void clearFight(final FightData fight) {
		fight.fighters.values().stream()//
				.map(fighter -> fighter.player())//
				.filter(player -> player != null)//
				.map(player -> fluffer10kFun.serverUserDataUtils.getUserData(player))//
				.forEach(userData -> userData.rpg.fightId = null);

		fluffer10kFun.botDataUtils.fights.remove(fight.id);
	}

	private void addWins(final FightData fight) {
		if (fight.type == FightType.PVE) {
			final PlayerFighterData player = fight.fighters.get("PLAYER").player();
			for (final FighterData fighter : fight.fighters.values()) {
				if (fighter.enemy() != null) {
					final EnemyFighterData enemy = fighter.enemy();

					if (player.level >= 0) {
						fluffer10kFun.botDataUtils.addFightResult(enemy.enemyId, player.level, player.isOut());
					}
				}
			}
		}
	}

	public void endFight(final FightTempData data) {
		updateFightMessage(data);

		if (data.fight.type == FightType.NO_NORMAL_REWARDS) {
			rewardCreators.get(data.fight.fightEndReward).giveRewards(data);
		} else {
			addExp(data);
			rewardCreators.get(data.fight.fightEndReward).giveRewards(data);
			setAfterFightFlags(data.fight);
			giveBackStaminaForQuickRecovery(data.fight);
		}

		clearFight(data.fight);
		addWins(data.fight);
	}

}
