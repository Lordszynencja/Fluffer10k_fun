package bot.commands.rpg;

import static bot.util.CollectionUtils.mapToList;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.Utils.toTimeString;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;

import java.util.List;

import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.userData.ServerUserData;
import bot.userData.UserStatusesData.UserStatusData;
import bot.util.EmbedUtils.EmbedField;
import bot.util.pages.builders.PagedMessageBuilder;
import bot.util.pages.messages.PagedMessage;
import bot.util.subcommand.Command;

public class CommandStatuses extends Command {
	private final Fluffer10kFun fluffer10kFun;

	public CommandStatuses(final Fluffer10kFun fluffer10kFun) {
		super(fluffer10kFun.apiUtils, "statuses", "Check your statuses");

		this.fluffer10kFun = fluffer10kFun;
	}

	public void handle(final SlashCommandInteraction interaction) {
		final Server server = interaction.getServer().orElse(null);
		if (server == null) {
			sendEphemeralMessage(interaction, "This command can only be used on server");
			return;
		}

		final User user = interaction.getUser();
		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(server.getId(), user.getId());

		final List<UserStatusData> statuses = userData.statuses.getSortedStatuses();
		if (statuses.isEmpty()) {
			interaction.createImmediateResponder().addEmbed(makeEmbed("You have no statuses on")).respond();
			return;
		}

		final long systemTime = System.currentTimeMillis();
		final List<EmbedField> fields = mapToList(statuses,
				status -> new EmbedField(status.description, toTimeString((status.timeout - systemTime) / 1000)));

		final PagedMessage msg = new PagedMessageBuilder<>("Your statuses", 7, fields).build();
		fluffer10kFun.pagedMessageUtils.addMessage(msg, interaction);
	}
}
