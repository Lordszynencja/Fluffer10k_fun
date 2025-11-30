package bot.commands.rpg.quests;

import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.Utils.bold;

import org.javacord.api.interaction.MessageComponentInteraction;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.data.items.data.WeaponItems;
import bot.data.quests.QuestType;
import bot.userData.ServerUserData;
import bot.userData.rpg.questData.QuestStep;
import bot.userData.rpg.questData.UserQuestData;
import bot.util.apis.APIUtils;

public class QuestLittleHeroes extends Quest {
	public QuestLittleHeroes() {
		super(QuestType.LITTLE_HEROES_QUEST, 0);

		continueQuestHandlers.put(QuestStep.COLLECTING_SWORDS, this::continueCollectingSwordsStep);
	}

	private static final String startStepText = String.join("\n", //
			description(
					"When walking through a village, you hear a group of children talking about wanting to be heroes."), //
			dialogue("We can't be heroes, mom won't let me go away!"), //
			description(
					"Intrigued, you stand behind a corner of the nearby building and listen some more. As you listen, one of the boys spots you and comes closer."), //
			dialogue("Are you a hero? you look like one!"), //
			description("His eyes start to shine when you confirm that you are a hero."), //
			dialogue("How is it to be a hero? Can I be a hero like you?"), //
			description(
					"You tell him that being hero is not easy because of many dangers that await in the wild. You also describe some places you have visited, which makes the boy's eyes shine even more. Then you ponder for a moment before telling him that if he wants to be a hero too, he needs to practice fighting and get stronger."), //
			dialogue(
					"Hmmmm, but how can I practice? I don't have a weapon to practice with. Mom would probably say it's not for me anyway because I can hurt myself with it..."), //
			description(
					"You tell the boy that you will think of something and that you will return once you find a solution."), //
			dialogue("Ok!"), //
			description("Says the boy before returning to his friends."));
	private static final String startStepDescription = String.join("\n", //
			"Collect three wooden swords and bring them to the little heroes!", //
			"Use the " + bold("/quest continue") + " when you get them.");

	@Override
	public void start(final SlashCommandInteraction interaction, final ServerUserData userData) {
		final UserQuestData questData = new UserQuestData(type, QuestStep.COLLECTING_SWORDS, startStepDescription,
				true);
		userData.rpg.setQuest(questData);
		interaction.createImmediateResponder().addEmbed(newQuestMessage(startStepText)).respond();
	}

	private static final String stepFinishedText = String.join("\n", //
			description(
					"Children are playing on the playground as you walk up to them. They are surprised when you give them the swords, but they take them and thank you."), //
			dialogue("I'm gonna be a hero!"), //
			description(
					"They say as they start to fight playfully. That's not much but that's all you could do for now, and possibly all they need to continue their dream."));
	private static final String stepFinishedDescription = "The children happily practice with wooden swords to become real heroes one day.";

	private void continueCollectingSwordsStep(final APIUtils apiUtils, final MessageComponentInteraction interaction,
			final ServerUserData userData) {
		if (!userData.hasItem(WeaponItems.WOODEN_SWORD, 3)) {
			interaction.createOriginalMessageUpdater()
					.addEmbed(makeEmbed(type.name, "You don't have the necessary items.")).update();
			return;
		}

		userData.rpg.setQuest(new UserQuestData(type, QuestStep.FINISHED, stepFinishedDescription));
		userData.addItem(WeaponItems.WOODEN_SWORD, -3);

		interaction.createOriginalMessageUpdater().addEmbeds(makeEmbed(type.name, stepFinishedText), //
				userData.addExpAndMakeEmbed(apiUtils, 250, interaction.getUser(), interaction.getServer().get()))//
				.update();
	}
}
