package bot.commands.rpg.quests;

import static bot.commands.rpg.quests.QuestUtils.getImage;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.Utils.bold;

import org.javacord.api.interaction.MessageComponentInteraction;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.data.items.data.PotionItems;
import bot.data.items.data.QuestItems;
import bot.data.quests.QuestType;
import bot.userData.ServerUserData;
import bot.userData.rpg.questData.QuestStep;
import bot.userData.rpg.questData.UserQuestData;

public class QuestChromeBook extends Quest {
	public QuestChromeBook() {
		super(QuestType.CHROME_BOOK_QUEST, 3);

		continueQuestHandlers.put(QuestStep.SEARCHING_FOR_THE_BOOK, this::continueSearchingForTheBookStep);
	}

	private static final String startStepText = String.join("\n", //
			description(
					"While roaming through the forest, you walk by the road to some old mansion, on which you can see a short, weird-looking girl."), //
			dialogue("Oh, hey, you! You look like someone who can help me! Come here!"), //
			description("Intruigued by this, you walk closer, to listen to her."), //
			dialogue(
					"I'm Chrome! Now, see, I need you to find my book. I think one of my zombies could have it with her, but I can't seem to find the right one. Bring the book to me, and I'll reward you~"), //
			description("Girl smirks at you as you accept the quest."), //
			dialogue("Maybe also check if any of the other undead has it, even I don't know everyone my zombies meet."), //
			description("After that, you continue your journey."));
	private static final String startStepDescription = String.join("\n", //
			"Find Chrome's book and return it to her.", //
			"Use the " + bold("/quest continue") + " to give her the book once you found it");

	@Override
	public void start(final SlashCommandInteraction interaction, final ServerUserData userData) {
		final UserQuestData questData = new UserQuestData(type, QuestStep.SEARCHING_FOR_THE_BOOK, startStepDescription,
				true);
		userData.rpg.setQuest(questData);
		interaction.createImmediateResponder().addEmbed(newQuestMessage(startStepText).setImage(getImage("Chrome")))
				.respond();
	}

	private static final String stepFinishedText = String.join("\n", //
			description(
					"Chrome looks at you as you approach her and her eyes start to shine when you show her the weird book."), //
			dialogue("Thank you so much! I've sent so many zombies to search for it but couldn't find it!"), //
			dialogue("She smiles and hugs the book."), //
			dialogue(
					"Now, for the payment, I could give you a zombie to help you! No? Hmmm, then I guess you can have this"), //
			description("She gives you a sack of coins and a few potions."), //
			dialogue("Visit me if you want to read the book~"), //
			description("She says while grinning."));
	private static final String stepFinishedDescription = "Chrome is happily using the book you gave her for... things you probably don't want to see.";

	private void continueSearchingForTheBookStep(final MessageComponentInteraction interaction,
			final ServerUserData userData) {
		if (!userData.hasItem(QuestItems.NECROFILICON)) {
			interaction.createOriginalMessageUpdater()
					.addEmbed(makeEmbed(type.name, "You don't have the necessary item.")).update();
			return;
		}

		userData.rpg.setQuest(new UserQuestData(type, QuestStep.FINISHED, stepFinishedDescription));
		userData.addItem(QuestItems.NECROFILICON, -1);
		userData.monies += 500;
		userData.addItem(PotionItems.HEALTH_POTION, 2);
		userData.addItem(PotionItems.MANA_POTION, 2);
		userData.addItem(PotionItems.STRENGTH_1_POTION);
		userData.addItem(PotionItems.AGILITY_1_POTION);
		userData.addItem(PotionItems.INTELLIGENCE_1_POTION);

		interaction.createOriginalMessageUpdater().addEmbeds(makeEmbed(type.name, stepFinishedText, getImage("Chrome")), //
				userData.addExpAndMakeEmbed(500, interaction.getUser(), interaction.getServer().get()), //
				makeEmbed("Obtained items", "You got 500 gold and bunch of potions"))//
				.update();
	}
}
