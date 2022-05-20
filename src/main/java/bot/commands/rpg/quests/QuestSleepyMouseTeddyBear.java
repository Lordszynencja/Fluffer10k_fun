package bot.commands.rpg.quests;

import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.Utils.bold;

import org.javacord.api.interaction.MessageComponentInteraction;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.items.data.QuestItems;
import bot.data.items.data.RingItems;
import bot.data.quests.QuestType;
import bot.userData.ServerUserData;
import bot.userData.rpg.questData.QuestStep;
import bot.userData.rpg.questData.UserQuestData;

public class QuestSleepyMouseTeddyBear extends Quest {
	public QuestSleepyMouseTeddyBear() {
		super(QuestType.SLEEPY_MOUSE_TEDDY_BEAR_QUEST, 0);

		continueQuestHandlers.put(QuestStep.SEARCHING_FOR_TEDDY, this::continueSearchingForTeddyStep);
	}

	private static final String startStepText = String.join("\n", //
			description(
					"As you roam the city, you notice a dormouse slowly walking down the street. What is weird to you is that her eyes are closed, and her head is leaning forward like if she was asleep, but still she doesn't bump into anything or anyone. You come to her and shake her by the shoulder, asking if she's ok."), //
			dialogue("Huh? Ah yes, I'm fine. Just looking for my teddy."), //
			description("She answers without opening her eyes."), //
			dialogue("His name is Plushie, if you find him, please bring him to me, ok?"), //
			description("You agree and leave her roaming the street."));
	private static final String startStepDescription = String.join("\n", //
			"Help the dormouse find her teddy.", //
			"Use the " + bold("/quest continue") + " to give it to her once you find it.");

	@Override
	public void start(final SlashCommandInteraction interaction, final ServerUserData userData) {
		final UserQuestData questData = new UserQuestData(type, QuestStep.SEARCHING_FOR_TEDDY, startStepDescription,
				true);
		userData.rpg.setQuest(questData);
		interaction.createImmediateResponder()
				.addEmbed(newQuestMessage(startStepText).setImage(MonsterGirlRace.DORMOUSE.imageLink)).respond();
	}

	private static final String stepFinishedText = String.join("\n", //
			description("You find the dormouse and show her the teddy."), //
			dialogue("Plushie!"), //
			description("She smiles, grabs him and hugs him tightly."), //
			dialogue("I don't know how to thank you!"), //
			description("She then hugs you too."), //
			dialogue(
					"Mmm, maybe I can give you something that I found on the street once. Looks like an magical ring."), //
			description(
					"She gives you a ring and then goes home nuzzling her teddy, waving at you and thanking you again."));
	private static final String stepFinishedDescription = "Dormouse sleeps happily in her house, hugging her teddy.";

	private void continueSearchingForTeddyStep(final MessageComponentInteraction interaction,
			final ServerUserData userData) {
		if (!userData.hasItem(QuestItems.TEDDY_BEAR_PLUSHIE)) {
			interaction.createOriginalMessageUpdater()
					.addEmbed(makeEmbed(type.name, "You don't have the necessary item.")).update();
			return;
		}

		userData.rpg.setQuest(new UserQuestData(type, QuestStep.FINISHED, stepFinishedDescription));
		userData.addItem(QuestItems.TEDDY_BEAR_PLUSHIE, -1);
		userData.addItem(RingItems.STATUS_NEGATION_RING);

		interaction.createOriginalMessageUpdater()
				.addEmbeds(makeEmbed(type.name, stepFinishedText, MonsterGirlRace.DORMOUSE.imageLink), //
						userData.addExpAndMakeEmbed(500, interaction.getUser(), interaction.getServer().get()), //
						makeEmbed("Obtained reward", "You got a ring of negation"))//
				.update();
	}
}
