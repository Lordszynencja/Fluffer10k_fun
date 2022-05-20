package bot.commands.rpg.quests;

import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.Utils.bold;

import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.MessageComponentInteraction;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.commands.rpg.blacksmith.tasks.BlacksmithTier;
import bot.commands.rpg.fight.enemies.data.races.Dwarf;
import bot.commands.rpg.fight.fightRewards.FightEndReward;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.items.data.GemItems;
import bot.data.items.data.GemItems.GemRefinement;
import bot.data.items.data.GemItems.GemSize;
import bot.data.items.data.GemItems.GemType;
import bot.data.items.data.OreItems;
import bot.data.items.data.QuestItems;
import bot.data.quests.QuestType;
import bot.userData.ServerUserData;
import bot.userData.rpg.questData.QuestStep;
import bot.userData.rpg.questData.UserQuestData;

public class QuestBusinessAsUsual1 extends Quest {
	private final Fluffer10kFun fluffer10kFun;

	public QuestBusinessAsUsual1(final Fluffer10kFun fluffer10kFun) {
		super(QuestType.BUSINESS_AS_USUAL_1_QUEST, 2, QuestType.MINERS_HOME);

		this.fluffer10kFun = fluffer10kFun;

		continueQuestHandlers.put(QuestStep.GETTING_TOOLS, this::continueGettingToolsStep);
		continueQuestHandlers.put(QuestStep.TALKING_TO_RIVAL, this::continueTalkingToRivalStep);
	}

	private static final String stepGettingToolsText = String.join("\n", //
			description("Strolling through a small village, you hear someone shouting."), //
			dialogue("I'll get you back for that, you will see! You and your boss!"), //
			description("Intrigued, you go to ask what happened."), //
			dialogue("These damn goblins happened! Stole my tools! Again! I can't work like that at all!!"), //
			description("You offer your help."), //
			dialogue(
					"Hmmmm... well, if you wanna help then get my tools back. And if you find some coal, I will need some too, someone stole that too..."), //
			dialogue(
					"There should be 6 tools in total, as there were 5 goblins and a hobgoblin, each took one tool I think."));
	private static final String stepGettingToolsDescription = String.join("\n", //
			"The dwarf asked you to get back her tools from 5 goblins and a hobgoblin. Also find her some coal, 10 nuggets should be enough.", //
			"Use " + bold("/quest continue") + " to give her the items once you found them.");

	@Override
	public void start(final SlashCommandInteraction interaction, final ServerUserData userData) {
		final UserQuestData questData = new UserQuestData(type, QuestStep.GETTING_TOOLS, stepGettingToolsDescription,
				true);
		userData.rpg.setQuest(questData);
		interaction.createImmediateResponder()
				.addEmbed(newQuestMessage(stepGettingToolsText).setImage(MonsterGirlRace.DWARF.imageLink)).respond();
	}

	private static final String stepTalkingToRivalText = String.join("\n", //
			description("You come back to the blacksmith with her tools and some coal."), //
			dialogue(
					"Ah, my tools! Thank you! I can now work, but I still have one more favor to ask. Would you take care of the rival blacksmith from next village? She is the one that paid those goblin gang to loot me..."));
	private static final String stepTalkingToRivalDescription = String.join("\n", //
			"The dwarf blacksmith wants you to take care of her rival.", //
			"Use " + bold("/quest continue") + " when you are ready for that.");

	private void continueGettingToolsStep(final MessageComponentInteraction interaction,
			final ServerUserData userData) {
		if (!userData.hasItem(QuestItems.BLACKSMITH_TOOLS_SIMPLE, 5)//
				|| !userData.hasItem(QuestItems.BLACKSMITH_TOOLS_ADVANCED)//
				|| !userData.hasItem(OreItems.ORE_COAL, 10)) {
			interaction.createImmediateResponder().addEmbed(makeEmbed("You don't have necessary items yet.")).respond();
			return;
		}

		userData.addItem(QuestItems.BLACKSMITH_TOOLS_SIMPLE, -5);
		userData.addItem(QuestItems.BLACKSMITH_TOOLS_ADVANCED, -1);
		userData.addItem(OreItems.ORE_COAL, -10);
		userData.blacksmith.available = true;
		userData.blacksmith.tiersUnlocked.add(BlacksmithTier.TIER_1);

		final UserQuestData questData = new UserQuestData(type, QuestStep.TALKING_TO_RIVAL,
				stepTalkingToRivalDescription, true);
		userData.rpg.setQuest(questData);
		interaction.createImmediateResponder()
				.addEmbeds(newQuestMessage(stepTalkingToRivalText).setImage(MonsterGirlRace.DWARF.imageLink), //
						userData.addExpAndMakeEmbed(500, interaction.getUser(), interaction.getServer().get()), //
						makeEmbed("Blacksmith unlocked!"))
				.respond();
	}

	private void continueTalkingToRivalStep(final MessageComponentInteraction interaction,
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
				.addEmbed(newQuestMessage("You visit the rival blacksmith and soon the fight begins.")).respond()
				.join();

		fluffer10kFun.fightStart.startFightPvE(interaction.getChannel().get().asServerTextChannel().get(),
				interaction.getUser(), Dwarf.DWARF_BLACKSMITH_BOSS,
				FightEndReward.BUSINESS_AS_USUAL_QUEST_FIGHT_REWARD);
	}

	private static final String stepFinishedText = String.join("\n", //
			description("You come to the blacksmith and tell her that other girl won't disturb her anymore."), //
			dialogue(
					"Thank you~ That will surely help me a lot. As a reward, I can give you this gem, should be worth a bit."));
	private static final String stepFinishedDescription = "You helped the dwarf blacksmith to have stable future.";

	public void afterFightWon(final ServerTextChannel channel, final ServerUserData userData, final User user) {
		userData.rpg.setQuest(new UserQuestData(type, QuestStep.FINISHED, stepFinishedDescription));
		userData.addItem(GemItems.getId(GemSize.LARGE, GemRefinement.REFINED, GemType.TOPAZ));

		channel.sendMessage(makeEmbed(type.name, stepFinishedText, MonsterGirlRace.DWARF.imageLink), //
				userData.addExpAndMakeEmbed(2000, user, channel.getServer()), //
				makeEmbed("Obtained reward", "You got a topaz!"));
	}
}
