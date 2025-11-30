package bot.commands.rpg.quests;

import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.Utils.bold;
import static bot.util.Utils.joinNames;
import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.SelectMenu;
import org.javacord.api.entity.message.component.SelectMenuOption;
import org.javacord.api.interaction.MessageComponentInteraction;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.items.data.SpecialItems;
import bot.data.quests.QuestType;
import bot.userData.ServerUserData;
import bot.userData.rpg.questData.QuestStep;
import bot.userData.rpg.questData.UserANuttySituationQuestSearchingForAcornsStepData;
import bot.userData.rpg.questData.UserQuestData;
import bot.util.apis.APIUtils;

public class QuestANuttySituation extends Quest {
	private final Fluffer10kFun fluffer10kFun;

	public QuestANuttySituation(final Fluffer10kFun fluffer10kFun) {
		super(QuestType.A_NUTTY_SITUATION_QUEST, 5);

		this.fluffer10kFun = fluffer10kFun;

		continueQuestHandlers.put(QuestStep.SEARCHING_FOR_ACORNS, this::continueSearchingForAcornsStep);
		fluffer10kFun.apiUtils.commandHandlers.addMessageComponentHandler("quest_a_nutty_situation_reward",
				this::onPickColor);
	}

	private static final List<String> acorns = asList("red acorn", "orange acorn", "yellow acorn", "green acorn",
			"blue acorn", "indigo acorn", "violet acorn");

	private static final String stepSearchingForAcornsText = String.join("\n", //
			dialogue("A-Are you a hero?? You must be! You sure look like one!"), //
			description("She seemed distressed still as if something was bothering her."), //
			description("As a result, you ask her what's wrong."), //
			dialogue(
					"My acorns..!! My special acorns have been taken away! The other Ratatoskr took them away! Please, help me get them back, Hero!"), //
			description("You ask how many acorns were missing."), //
			dialogue("There's seven of them!!"), //
			dialogue("One red."), //
			dialogue("One orange."), //
			dialogue("One yellow."), //
			dialogue("One green."), //
			dialogue("One blue."), //
			dialogue("One indigo and lastly one violet!"), //
			description(
					"You realised that she described to you the colors of the rainbow and that ironically made it easier to keep count of which ones you needed to find."), //
			dialogue("I'll wait here in the forest! Just give a whistle when you've found them all!"), //
			description("The small Ratatoskr scampers up and hides within a tree in the meantime."), //
			description("Seems like the best place to look would be within the forest..."));
	private static final String stepSearchingForAcornsDescription = String.join("\n", //
			"A small Ratatoskr has asked you to retrieve her 7 colored acorns.", //
			"Acorns left: %1$s.", //
			"Use the " + bold("/quest continue") + " to give her the items once you found them.");

	public static String getDescription(final List<String> acornsLeft) {
		return String.format(stepSearchingForAcornsDescription, joinNames(acornsLeft));
	}

	@Override
	public void start(final SlashCommandInteraction interaction, final ServerUserData userData) {
		final UserANuttySituationQuestSearchingForAcornsStepData questData = new UserANuttySituationQuestSearchingForAcornsStepData(
				getDescription(acorns), new ArrayList<>(acorns));
		userData.rpg.setQuest(questData);
		interaction.createImmediateResponder()
				.addEmbed(newQuestMessage(stepSearchingForAcornsText).setImage(MonsterGirlRace.RATATOSKR.imageLink))
				.respond();
	}

	private static final String chooseRewardText = String.join("\n", //
			description(
					"You head back to the forest where you met the small Ratatoskr, looking up to where she might've been."), //
			description(
					"You hear a rustle in the leaves when you see her jump down from her hiding place, just in front of you."), //
			dialogue("Hero! You're back!"), //
			description("She looks to be jumpy and excited."), //
			dialogue("You have my acorns, right?"), //
			description("You take them out from your backpack and show all 7 of them to her."), //
			description("As you do, she gets wide eyed and jumps with joy before taking them from you."), //
			dialogue(
					"Thank you so much, Hero! Oh, which of these colors do you like the most? I will give you ring with it!"), //
			description("She waits for your decision."));

	private static final List<SelectMenuOption> rewards = asList(//
			SelectMenuOption.create("Red (strength)", SpecialItems.RED_ACORN_RING), //
			SelectMenuOption.create("Orange (armor)", SpecialItems.ORANGE_ACORN_RING), //
			SelectMenuOption.create("Yellow (agility)", SpecialItems.YELLOW_ACORN_RING), //
			SelectMenuOption.create("Green (HP)", SpecialItems.GREEN_ACORN_RING), //
			SelectMenuOption.create("Blue (intelligence)", SpecialItems.BLUE_ACORN_RING), //
			SelectMenuOption.create("Indigo (mana)", SpecialItems.INDIGO_ACORN_RING), //
			SelectMenuOption.create("Violet (mana regeneration)", SpecialItems.VIOLET_ACORN_RING));

	private void continueSearchingForAcornsStep(final APIUtils apiUtils, final MessageComponentInteraction interaction,
			final ServerUserData userData) {
		final UserANuttySituationQuestSearchingForAcornsStepData questData = (UserANuttySituationQuestSearchingForAcornsStepData) userData.rpg.quests
				.get(type);
		if (!questData.acornsLeft.isEmpty()) {
			interaction.createOriginalMessageUpdater().addEmbed(makeEmbed(type.name, "You didn't find all acorns yet."))
					.update();
			return;
		}

		interaction.createImmediateResponder()
				.addEmbed(makeEmbed(type.name, chooseRewardText, MonsterGirlRace.RATATOSKR.imageLink))//
				.addComponents(ActionRow.of(SelectMenu
						.createStringMenu("quest_a_nutty_situation_reward " + interaction.getUser().getId(), rewards)))//
				.respond();
	}

	private static final String stepFinishedText = String.join("\n", //
			description("Ratatoskr climbs up the tree and then back down with a small box."), //
			dialogue("For you! Thanks a lot for help, oh great hero!"), //
			description("Then she hides back on the tree."));

	private static final String stepFinishedDescription = "You returned all of the colored acorns back to the small Ratatoskr. She seemed very pleased with your work.";

	private void onPickColor(final MessageComponentInteraction interaction) {
		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(interaction.getServer().get(),
				interaction.getUser());
		if (!userData.rpg.questIsOnStep(type, QuestStep.SEARCHING_FOR_ACORNS)) {
			interaction.createOriginalMessageUpdater().addEmbed(makeEmbed(type.name, "You already chose the reward!"))
					.update();
			return;
		}

		final String itemId = interaction.asSelectMenuInteraction().get().getChosenOptions().get(0).getValue();

		userData.rpg.setQuest(new UserQuestData(type, QuestStep.FINISHED, stepFinishedDescription));
		userData.addItem(itemId);

		interaction.createOriginalMessageUpdater()
				.addEmbeds(makeEmbed(type.name, stepFinishedText, MonsterGirlRace.RATATOSKR.imageLink), //
						userData.addExpAndMakeEmbed(fluffer10kFun.apiUtils, 2000, interaction.getUser(),
								interaction.getServer().get()), //
						makeEmbed("Obtained reward", "You got a magic ring!"))//
				.update();
	}
}
