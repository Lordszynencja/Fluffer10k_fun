package bot.commands.rpg.quests;

import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.Utils.bold;
import static bot.util.apis.MessageUtils.getServer;
import static bot.util.apis.MessageUtils.getServerTextChannel;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.MessageComponentInteraction;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.enemies.races.Werebat;
import bot.commands.rpg.fight.fightRewards.FightEndReward;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.items.data.WeaponItems;
import bot.data.quests.QuestType;
import bot.userData.ServerUserData;
import bot.userData.rpg.questData.QuestStep;
import bot.userData.rpg.questData.UserQuestData;

public class QuestMinersHome extends Quest {
	private static final String werebatsLeftParam = "werebatsLeft";

	private final Fluffer10kFun fluffer10kFun;

	public QuestMinersHome(final Fluffer10kFun fluffer10kFun) {
		super(QuestType.MINERS_HOME, 1);

		this.fluffer10kFun = fluffer10kFun;

		continueQuestHandlers.put(QuestStep.CLEARING_MINE, this::continueClearingMineStep);
		continueQuestHandlers.put(QuestStep.CHOPPING_WOOD, this::continueChoppingWoodStep);
	}

	private static final String stepClearingMineText = String.join("\n", //
			description(
					"While at the blacksmith to repair and sharpen your weapons, you ask where the blacksmith gets resources."), //
			dialogue(
					"Well, I have to import them since the mine nearby was abandoned. Hey, maybe you want to take it over? Needs just a bit of tidying up and then you can mine there! I'll show you where it is."), //
			description("You agree and are led to the place where an old, abandoned mine is located."), //
			dialogue(
					"Here it is, just scare away the werebats living there and tidy it up and its yours, I hope you will bring me some ores soon."));
	private static final String stepClearingMineDescription = String.join("\n", //
			"You have to clear the mine of werebats.", //
			"Werebats left: %1$d.", //
			"Use " + bold("/quest continue") + " to go into the mine to find them.");

	private static String getDescription(final int werebatsLeft) {
		return String.format(stepClearingMineDescription, werebatsLeft);
	}

	@Override
	public void start(final SlashCommandInteraction interaction, final ServerUserData userData) {
		final UserQuestData questData = new UserQuestData(type, QuestStep.CLEARING_MINE, null, true)//
				.set(werebatsLeftParam, 5);
		questData.description(getDescription(questData.getI(werebatsLeftParam)));
		userData.rpg.setQuest(questData);

		interaction.createImmediateResponder()
				.addEmbed(newQuestMessage(stepClearingMineText).setImage(MonsterGirlRace.DWARF.imageLink)).respond();
	}

	private void continueClearingMineStep(final MessageComponentInteraction interaction,
			final ServerUserData userData) {
		if (!userData.canStartOtherFight()) {
			String msg;
			if (userData.rpg.fightId != null) {
				msg = "You can't start another fight now, finish current one first.";
			} else {
				msg = "You are too tired to fight.";
			}
			interaction.createOriginalMessageUpdater().addEmbed(makeEmbed(msg)).update();
			return;
		}
		userData.startOtherFight();

		interaction.createImmediateResponder()
				.addEmbed(newQuestMessage("You enter the mine and soon you are attacked by a werebat!")).respond()
				.join();

		fluffer10kFun.fightStart.startFightPvE(getServerTextChannel(interaction), interaction.getUser(),
				Werebat.WEREBAT_1, FightEndReward.MINERS_HOME_QUEST_REWARD);
	}

	private static final String stepChoppingWoodText = String.join("\n", //
			description(
					"After clearing the mine, next step is to put up some supporting beams so the ceiling wont fall on your head. You will need axe for that."));
	private static final String stepChoppingWoodDescription = String.join("\n", //
			"You need an axe to chop down some trees to make beams for the support.", //
			"Use " + bold("/quest continue") + " when you have it.");

	public void afterFightWon(final TextChannel channel, final ServerUserData userData, final User user,
			final int enemiesDefeated) {
		final UserQuestData quest = userData.rpg.quests.get(type);
		final int werebatsLeft = quest.getI(werebatsLeftParam) - enemiesDefeated;
		if (werebatsLeft > 0) {
			quest.set(werebatsLeftParam, werebatsLeft)//
					.description(getDescription(werebatsLeft));

			channel.sendMessage(makeEmbed(type.name, "You managed to clear the mine a bit, but some werebats remain."));
			return;
		}

		userData.rpg.setQuest(new UserQuestData(type, QuestStep.CHOPPING_WOOD, stepChoppingWoodDescription, true));

		channel.sendMessage(makeEmbed(type.name, stepChoppingWoodText), //
				userData.addExpAndMakeEmbed(250, user, getServer(channel)));
	}

	private static final String stepFinishedText = description(
			"You finish the work and the mine is ready to use, Now you try to remember where you put your pick...");
	private static final String stepFinishedDescription = "Mine is ready to use.";

	private void continueChoppingWoodStep(final MessageComponentInteraction interaction,
			final ServerUserData userData) {
		if (!userData.hasItem(WeaponItems.LUMBERJACK_AXE) && //
				!userData.hasItem(WeaponItems.BATTLE_AXE) && //
				!userData.hasItem(WeaponItems.DWARVEN_AXE)) {
			interaction.createOriginalMessageUpdater().addEmbed(makeEmbed(type.name, "You don't have an axe."))
					.update();
			return;
		}

		userData.rpg.mineUnlocked = true;
		userData.rpg.setQuest(new UserQuestData(type, QuestStep.FINISHED, stepFinishedDescription));

		interaction.createOriginalMessageUpdater().addEmbeds(makeEmbed(type.name, stepFinishedText), //
				userData.addExpAndMakeEmbed(250, interaction.getUser(), interaction.getServer().get()), //
				makeEmbed("Unlocked reward",
						"You have access to the mine!\nUse " + bold("/blacksmith mine") + " to mine in it"))//
				.update();
	}
}
