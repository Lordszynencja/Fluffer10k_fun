package bot.commands.debug;

import static bot.util.CollectionUtils.mapToList;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;
import static java.util.Arrays.asList;

import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;

import bot.Fluffer10kFun;
import bot.data.quests.QuestType;
import bot.userData.ServerUserData;
import bot.util.subcommand.Subcommand;

public class CommandDebugSetQuestContinuable extends Subcommand {
	private static final String questNames = String.join("\n",
			mapToList(asList(QuestType.values()), q -> q.name() + " - " + q.name));

	private final Fluffer10kFun fluffer10kFun;

	public CommandDebugSetQuestContinuable(final Fluffer10kFun fluffer10kFun) {
		super("set_quest_continuable", "set a quest to be continuable", //
				SlashCommandOption.create(SlashCommandOptionType.STRING, "quest", "quest id", true), //
				SlashCommandOption.create(SlashCommandOptionType.USER, "user", "user", true));

		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {
		final Server server = interaction.getServer().get();
		final String questId = getOption(interaction).getOptionStringValueByName("quest").get();
		final User user = getOption(interaction).getOptionUserValueByName("user").get();
		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(server, user);

		try {
			final QuestType quest = QuestType.valueOf(questId);
			userData.rpg.quests.get(quest).continuable = true;

			sendEphemeralMessage(interaction,
					"You set continuable on " + quest.name + " for " + user.getDiscriminatedName());
		} catch (final Exception e) {
			sendEphemeralMessage(interaction, "Wrong quest id, available: " + questNames);
		}
	}
}
