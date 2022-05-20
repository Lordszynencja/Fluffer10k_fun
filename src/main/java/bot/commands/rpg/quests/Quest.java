package bot.commands.rpg.quests;

import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.Utils.cursive;

import java.util.HashMap;
import java.util.Map;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.MessageComponentInteraction;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.data.quests.QuestType;
import bot.userData.ServerUserData;
import bot.userData.rpg.questData.QuestStep;
import bot.userData.rpg.questData.UserQuestData;

public abstract class Quest {
	protected static interface ContinueQuestHandler {
		void handle(MessageComponentInteraction interaction, ServerUserData userData);
	}

	public static String description(final String text) {
		return text;
	}

	public static String dialogue(final String text) {
		return cursive("\"" + text + "\"");
	}

	protected static EmbedBuilder newQuestMessage(final String description) {
		return makeEmbed("New quest!", description);
	}

	protected static void defaultGiveItemStep(final MessageComponentInteraction interaction,
			final ServerUserData userData, final QuestType type, final String itemId, final long amount,
			final QuestStep nextStep, final boolean nextStepContinued, final String text, final String description,
			final String img, final long exp) {
		if (!userData.hasItem(itemId, amount)) {
			interaction.createOriginalMessageUpdater().addEmbed(
					makeEmbed(type.name, "You don't have the necessary " + (amount == 1 ? "item" : "items") + "."))
					.update();
			return;
		}

		userData.rpg.setQuest(new UserQuestData(type, nextStep, description, nextStepContinued));
		userData.addItem(itemId, -amount);

		interaction.createOriginalMessageUpdater().addEmbeds(makeEmbed(type.name, text, img), //
				userData.addExpAndMakeEmbed(exp, interaction.getUser(), interaction.getServer().get())).update();
	}

	public final QuestType type;
	public final int minimumLevel;
	public final QuestType[] prerequisiteQuests;

	protected Quest(final QuestType type, final int minimumLevel, final QuestType... prerequisiteQuests) {
		this.type = type;
		this.minimumLevel = minimumLevel;
		this.prerequisiteQuests = prerequisiteQuests;
	}

	protected boolean fitsCriteria(final ServerUserData userData) {
		return true;
	}

	public boolean available(final ServerUserData userData) {
		if (userData.rpg.level < minimumLevel) {
			return false;
		}
		if (userData.rpg.quests.get(type) != null) {
			return false;
		}

		for (final QuestType prerequisiteQuest : prerequisiteQuests) {
			if (!userData.rpg.questIsOnStep(prerequisiteQuest, QuestStep.FINISHED)) {
				return false;
			}
		}

		return fitsCriteria(userData);
	}

	public abstract void start(SlashCommandInteraction interaction, final ServerUserData userData);

	protected final Map<QuestStep, ContinueQuestHandler> continueQuestHandlers = new HashMap<>();

	private void defaultQuestHandler(final MessageComponentInteraction interaction, final ServerUserData userData) {
		interaction.createOriginalMessageUpdater()
				.addEmbed(makeEmbed("Continue quest " + type.name, "Quest can't be continued this way")).update();
	}

	public void continueQuest(final MessageComponentInteraction interaction, final ServerUserData userData) {
		continueQuestHandlers.getOrDefault(userData.rpg.quests.get(type).step, this::defaultQuestHandler)
				.handle(interaction, userData);
	}
}
