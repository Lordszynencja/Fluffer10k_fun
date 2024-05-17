package bot.commands.rpg.fight.fightRewards;

import static bot.data.items.ItemUtils.getFormattedMonies;
import static bot.data.items.loot.MonsterGirlRaceLoot.getMGLoot;
import static bot.data.items.loot.RPGLootByLevel.getLoot;
import static bot.util.CollectionUtils.addToIntOnMap;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.RandomUtils.getRandom;
import static bot.util.RandomUtils.getRandomBoolean;
import static bot.util.RandomUtils.getRandomDouble;
import static bot.util.apis.MessageUtils.getServer;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.FightEnd.RewardCreator;
import bot.commands.rpg.fight.FightTempData;
import bot.commands.rpg.fight.enemies.RPGEnemyData.EnemyType;
import bot.commands.rpg.quests.QuestANuttySituation;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.EnemyFighterData;
import bot.data.fight.FightData;
import bot.data.fight.FighterClass;
import bot.data.fight.FighterData;
import bot.data.fight.FighterData.FighterType;
import bot.data.fight.PlayerFighterData;
import bot.data.items.data.QuestItems;
import bot.data.items.loot.LootList;
import bot.data.quests.QuestType;
import bot.userData.ServerUserData;
import bot.userData.UserBlessingData.Blessing;
import bot.userData.rpg.questData.QuestStep;
import bot.userData.rpg.questData.UserANuttySituationQuestSearchingForAcornsStepData;

public class DefaultReward implements RewardCreator {

	private final Fluffer10kFun fluffer10kFun;

	public DefaultReward(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;
	}

	private static String winningTeam(final FightData fight) {
		for (final FighterData fighter : fight.fighters.values()) {
			if (!fighter.isOut()) {
				return fighter.team;
			}
		}

		return null;
	}

	private int getAverageLevel(final FightData fight) {
		int averageLevel = 0;
		for (final FighterData fighter : fight.fighters.values()) {
			averageLevel += fluffer10kFun.rpgStatUtils.getLevel(fighter);
		}
		return averageLevel / fight.fighters.size();
	}

	private List<PlayerFighterData> players(final FightData fight) {
		return fight.fighters.values().stream().filter(fighter -> fighter.type == FighterType.PLAYER)//
				.map(fighter -> fighter.player())//
				.collect(toList());
	}

	private void handlePlayerWon(final TextChannel channel, final ServerUserData userData,
			final PlayerFighterData player, final List<EnemyFighterData> leftMonsterGirlFighters,
			final int averageLevel) {
		if (leftMonsterGirlFighters.isEmpty()) {
			return;
		}

		if (userData.blacksmith.available && userData.blacksmith.currentTask != null) {
			for (final EnemyFighterData enemy : leftMonsterGirlFighters) {
				addToIntOnMap(userData.blacksmith.currentTask.monsterGirlsDefeated,
						enemy.enemyData(fluffer10kFun).mg().race, 1);
			}
		}

		final LootList loot = new LootList();
		for (final EnemyFighterData mgFighter : leftMonsterGirlFighters) {
			loot.add(getLoot(mgFighter.enemyData(fluffer10kFun).level));
			final MonsterGirlRace race = mgFighter.enemyData(fluffer10kFun).mg().race;
			loot.add(getMGLoot(race));
		}

		EmbedBuilder rewardEmbed;
		if (!loot.isEmpty()) {
			if (userData.blessings.blessingsObtained.contains(Blessing.LUCKY_DUCKY)) {
				loot.goldAmount = (long) (loot.goldAmount * 1.5);
			}
			loot.addToUser(userData);

			final String lootDescription = loot.getDescription(fluffer10kFun.items);
			rewardEmbed = makeEmbed("Reward for " + player.name, lootDescription);
		} else {
			rewardEmbed = makeEmbed(player.name + " found nothing worth taking");
		}
		channel.sendMessage(rewardEmbed);
	}

	private void handlePlayerLost(final TextChannel channel, final ServerUserData userData,
			final PlayerFighterData player, final List<EnemyFighterData> leftMonsterGirlFighters,
			final int averageLevel) {
		long moneyLost = min((long) (averageLevel * (0.5 + getRandomDouble()) * 10), userData.monies);
		if (userData.blessings.blessingsObtained.contains(Blessing.MARTYRDOM)) {
			final boolean allEnemiesBelow25Percent = leftMonsterGirlFighters.stream()//
					.allMatch(mg -> mg.hp < mg.maxHp / 4);
			if (allEnemiesBelow25Percent) {
				moneyLost = 0;
			}
		}

		userData.monies -= moneyLost;

		final String title = player.name + " lost the battle"
				+ (moneyLost > 0 ? " and " + getFormattedMonies(moneyLost) : "") + "!";
		channel.sendMessage(makeEmbed(title));

		final Server server = getServer(channel);
		final User user = fluffer10kFun.apiUtils.getUser(player.userId);
		for (final EnemyFighterData mgFighter : leftMonsterGirlFighters) {
			if (!fluffer10kFun.rpgStatUtils.getClasses(mgFighter).contains(FighterClass.SLOW)) {
				final int cums = max(1, (int) (averageLevel * 0.2 * getRandomDouble()));
				fluffer10kFun.commandMgLove.addCums(server, user, cums);
				channel.sendMessage(fluffer10kFun.commandMgLove.makeLovedByEmbed(server, user, cums, mgFighter.name));
			}
		}
	}

	private void checkANuttySituationQuest(final TextChannel channel, final ServerUserData userData,
			final List<EnemyFighterData> leftMonsterGirlFighters, final boolean fightWon) {
		if (!userData.rpg.questIsOnStep(QuestType.A_NUTTY_SITUATION_QUEST, QuestStep.SEARCHING_FOR_ACORNS)) {
			return;
		}
		if (!fightWon) {
			return;
		}
		if (!leftMonsterGirlFighters.stream()//
				.anyMatch(f -> f.enemyData(fluffer10kFun).mg().race == MonsterGirlRace.RATATOSKR)) {
			return;
		}

		final UserANuttySituationQuestSearchingForAcornsStepData quest = userData.rpg.quests
				.get(QuestType.A_NUTTY_SITUATION_QUEST).asSpecific();
		if (!quest.acornsLeft.isEmpty()) {
			final String acornFound = getRandom(quest.acornsLeft);
			quest.acornsLeft.remove(acornFound);
			quest.description = QuestANuttySituation.getDescription(quest.acornsLeft);
			channel.sendMessage(makeEmbed("Special item found!", "You found " + acornFound + "!"));
		}
	}

	private void checkBusinessAsUsual1Quest(final TextChannel channel, final ServerUserData userData,
			final List<EnemyFighterData> leftMonsterGirlFighters, final boolean fightWon) {
		if (!userData.rpg.questIsOnStep(QuestType.BUSINESS_AS_USUAL_1_QUEST, QuestStep.GETTING_TOOLS)) {
			return;
		}
		if (!fightWon) {
			return;
		}

		if (leftMonsterGirlFighters.stream()//
				.anyMatch(f -> f.enemyData(fluffer10kFun).mg().race == MonsterGirlRace.HOBGOBLIN)
				&& !userData.hasItem(QuestItems.BLACKSMITH_TOOLS_ADVANCED)) {
			userData.addItem(QuestItems.BLACKSMITH_TOOLS_ADVANCED);
			channel.sendMessage(makeEmbed("Special item found!", "You found advanced blacksmith tools!"));
		} else if (leftMonsterGirlFighters.stream()//
				.anyMatch(f -> f.enemyData(fluffer10kFun).mg().race == MonsterGirlRace.GOBLIN)
				&& !userData.hasItem(QuestItems.BLACKSMITH_TOOLS_SIMPLE, 5)) {
			userData.addItem(QuestItems.BLACKSMITH_TOOLS_SIMPLE);
			channel.sendMessage(makeEmbed("Special item found!", "You found simple blacksmith tools!"));
		}
	}

	private void checkChromeBookQuest(final TextChannel channel, final ServerUserData userData,
			final List<EnemyFighterData> leftMonsterGirlFighters, final boolean fightWon) {
		if (!userData.rpg.questIsOnStep(QuestType.CHROME_BOOK_QUEST, QuestStep.SEARCHING_FOR_THE_BOOK)) {
			return;
		}
		if (!getRandomBoolean(0.25)) {
			return;
		}
		if (!fightWon) {
			return;
		}
		if (userData.hasItem(QuestItems.NECROFILICON)) {
			return;
		}
		if (!leftMonsterGirlFighters.stream()
				.anyMatch(f -> fluffer10kFun.rpgStatUtils.getClasses(f).contains(FighterClass.UNDEAD))) {
			return;
		}

		userData.addItem(QuestItems.NECROFILICON);
		channel.sendMessage(makeEmbed("Special item found!", "You found Necrofilicon!"));
	}

	private void checkHeroAcademyBerserker4Quest(final TextChannel channel, final ServerUserData userData) {
		if (!userData.rpg.questIsOnStep(QuestType.HERO_ACADEMY_QUEST, QuestStep.BERSERKER_4)) {
			return;
		}

		fluffer10kFun.questUtils.questHeroAcademy().continueBerserker4Step(channel, userData);
	}

	private void checkHeroAcademyMage4Quest(final TextChannel channel, final ServerUserData userData) {
		if (!userData.rpg.questIsOnStep(QuestType.HERO_ACADEMY_QUEST, QuestStep.MAGE_4)) {
			return;
		}

		fluffer10kFun.questUtils.questHeroAcademy().continueMage4Step(channel, userData);
	}

	private void checkHeroAcademyPaladin3Quest(final TextChannel channel, final ServerUserData userData,
			final List<EnemyFighterData> leftMonsterGirlFighters, final boolean fightWon) {
		if (!userData.rpg.questIsOnStep(QuestType.HERO_ACADEMY_QUEST, QuestStep.PALADIN_3)) {
			return;
		}
		if (!fightWon) {
			return;
		}
		if (!leftMonsterGirlFighters.stream()
				.anyMatch(f -> fluffer10kFun.rpgStatUtils.getClasses(f).contains(FighterClass.UNDEAD))) {
			return;
		}

		fluffer10kFun.questUtils.questHeroAcademy().continuePaladin3Step(channel, userData);
	}

	private void checkHeroAcademyPaladin1Quest(final TextChannel channel, final ServerUserData userData,
			final List<EnemyFighterData> leftMonsterGirlFighters) {
		if (!userData.rpg.questIsOnStep(QuestType.HERO_ACADEMY_QUEST, QuestStep.PALADIN_1)) {
			return;
		}
		if (!leftMonsterGirlFighters.stream()
				.anyMatch(f -> fluffer10kFun.rpgStatUtils.getClasses(f).contains(FighterClass.UNDEAD))) {
			return;
		}

		fluffer10kFun.questUtils.questHeroAcademy().continuePaladin1Step(channel, userData);
	}

	private void checkHeroAcademyRanger3Quest(final TextChannel channel, final PlayerFighterData player,
			final ServerUserData userData, final boolean fightWon) {
		if (!userData.rpg.questIsOnStep(QuestType.HERO_ACADEMY_QUEST, QuestStep.RANGER_3)) {
			return;
		}
		if (!fightWon) {
			return;
		}

		fluffer10kFun.questUtils.questHeroAcademy().continueRanger3Step(channel, userData);
	}

	private void checkHeroAcademyRanger2Quest(final FightTempData data, final ServerUserData userData) {
		if (!userData.rpg.questIsOnStep(QuestType.HERO_ACADEMY_QUEST, QuestStep.RANGER_2)) {
			return;
		}
		if (!data.fight.fighters.values().stream()
				.anyMatch(f -> !fluffer10kFun.rpgStatUtils.getClasses(f).contains(FighterClass.FLYING))) {
			return;
		}

		fluffer10kFun.questUtils.questHeroAcademy().continueRanger2Step(data.channel, userData);
	}

	private void checkHeroAcademyRanger1Quest(final FightTempData data, final ServerUserData userData) {
		if (!userData.rpg.questIsOnStep(QuestType.HERO_ACADEMY_QUEST, QuestStep.RANGER_1)) {
			return;
		}
		if (!data.fight.fighters.values().stream()
				.anyMatch(f -> fluffer10kFun.rpgStatUtils.getClasses(f).contains(FighterClass.FLYING))) {
			return;
		}

		fluffer10kFun.questUtils.questHeroAcademy().continueRanger1Step(data.channel, userData);
	}

	private void checkHeroAcademyRogue3Quest(final TextChannel channel, final ServerUserData userData,
			final boolean fightWon) {
		if (!userData.rpg.questIsOnStep(QuestType.HERO_ACADEMY_QUEST, QuestStep.ROGUE_3)) {
			return;
		}
		if (!fightWon) {
			return;
		}

		fluffer10kFun.questUtils.questHeroAcademy().continueRogue3Step(channel, userData);
	}

	private void checkHeroAcademyWarlock4Quest(final TextChannel channel, final ServerUserData userData) {
		if (!userData.rpg.questIsOnStep(QuestType.HERO_ACADEMY_QUEST, QuestStep.WARLOCK_4)) {
			return;
		}

		fluffer10kFun.questUtils.questHeroAcademy().continueWarlock4Step(channel, userData);
	}

	private void checkHeroAcademyWarlock1Quest(final TextChannel channel, final PlayerFighterData player,
			final ServerUserData userData) {
		if (!userData.rpg.questIsOnStep(QuestType.HERO_ACADEMY_QUEST, QuestStep.WARLOCK_1)) {
			return;
		}
		if (!player.fight.fighters.values().stream().anyMatch(
				f -> fluffer10kFun.rpgStatUtils.getTotalStatsInFight(f).classes.contains(FighterClass.USES_MAGIC))) {
			return;
		}

		fluffer10kFun.questUtils.questHeroAcademy().continueWarlock1Step(channel, userData);
	}

	private void checkHeroAcademyQuest(final FightTempData data, final PlayerFighterData player,
			final ServerUserData userData, final List<EnemyFighterData> leftMonsterGirlFighters,
			final boolean fightWon) {
		checkHeroAcademyBerserker4Quest(data.channel, userData);
		checkHeroAcademyMage4Quest(data.channel, userData);
		checkHeroAcademyPaladin3Quest(data.channel, userData, leftMonsterGirlFighters, fightWon);
		checkHeroAcademyPaladin1Quest(data.channel, userData, leftMonsterGirlFighters);
		checkHeroAcademyRanger3Quest(data.channel, player, userData, fightWon);
		checkHeroAcademyRanger2Quest(data, userData);
		checkHeroAcademyRanger1Quest(data, userData);
		checkHeroAcademyRogue3Quest(data.channel, userData, fightWon);
		checkHeroAcademyWarlock4Quest(data.channel, userData);
		checkHeroAcademyWarlock1Quest(data.channel, player, userData);
	}

	private void checkQuestProgressionOnFightEnd(final FightTempData data, final PlayerFighterData player,
			final ServerUserData userData, final List<EnemyFighterData> leftMonsterGirlFighters,
			final boolean fightWon) {
		checkANuttySituationQuest(data.channel, userData, leftMonsterGirlFighters, fightWon);
		checkBusinessAsUsual1Quest(data.channel, userData, leftMonsterGirlFighters, fightWon);
		checkChromeBookQuest(data.channel, userData, leftMonsterGirlFighters, fightWon);
		checkHeroAcademyQuest(data, player, userData, leftMonsterGirlFighters, fightWon);
	}

	private void addNewHaremMembers(final FightTempData data, final List<EnemyFighterData> leftMonsterGirlFighters,
			final List<PlayerFighterData> winningPlayers) {
		for (final EnemyFighterData monsterGirl : leftMonsterGirlFighters) {
			final PlayerFighterData luckyPlayer = getRandom(winningPlayers);
			final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(luckyPlayer);
			double chance = 0.1;
			if (userData.blessings.blessingsObtained.contains(Blessing.FAMILY_MAN)) {
				chance += 0.1;
			}
			if (!getRandomBoolean(chance)) {
				continue;
			}

			if (userData.getHaremSize() < userData.getHouseSize()) {
				final MonsterGirlRace race = monsterGirl.enemyData(fluffer10kFun).mg().race;
				userData.addWife(race);
				data.channel.sendMessage(makeEmbed(
						monsterGirl.name + " decided to join " + luckyPlayer.name + "'s harem!", null, race.imageLink));
			}
		}
	}

	@Override
	public void giveRewards(final FightTempData data) {
		final List<EnemyFighterData> leftMonsterGirlFighters = data.fight.fighters.values().stream()//
				.map(fighter -> fighter.enemy())//
				.filter(fighter -> fighter != null//
						&& !fighter.escaped//
						&& fighter.enemyData(fluffer10kFun).type == EnemyType.MONSTER_GIRL)//
				.collect(toList());

		if (leftMonsterGirlFighters.isEmpty()) {
			return;
		}

		final String winningTeam = winningTeam(data.fight);
		final int averageLevel = getAverageLevel(data.fight);
		final List<PlayerFighterData> winningPlayers = new ArrayList<>();
		for (final PlayerFighterData player : players(data.fight)) {
			final boolean winner = player.team.equals(winningTeam);
			final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(player);
			if (winner) {
				winningPlayers.add(player);
				handlePlayerWon(data.channel, userData, player, leftMonsterGirlFighters, averageLevel);
			} else if (!player.escaped) {
				handlePlayerLost(data.channel, userData, player, leftMonsterGirlFighters, averageLevel);
			}
			checkQuestProgressionOnFightEnd(data, player, userData, leftMonsterGirlFighters, winner);
		}

		if (!winningPlayers.isEmpty()) {
			addNewHaremMembers(data, leftMonsterGirlFighters, winningPlayers);
		}
	}

}
