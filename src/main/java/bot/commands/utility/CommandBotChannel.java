package bot.commands.utility;

import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.apis.MessageUtils.getServerTextChannel;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;

import java.io.IOException;

import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.util.subcommand.Command;

public class CommandBotChannel extends Command {
	private final Fluffer10kFun fluffer10kFun;

	public CommandBotChannel(final Fluffer10kFun fluffer10kFun) throws IOException {
		super(fluffer10kFun.apiUtils, "bot_channel", "Mark channel as bot channel (owner only)");

		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {
		final Server server = interaction.getServer().orElse(null);
		if (server == null) {
			sendEphemeralMessage(interaction, "Can't use this command here");
			return;
		}

		final User user = interaction.getUser();
		if (user.getId() != server.getOwnerId()) {
			sendEphemeralMessage(interaction, "Only server owner can set bot channel");
			return;
		}

		fluffer10kFun.botDataUtils.getServerData(server.getId()).botChannelId = getServerTextChannel(interaction)
				.getId();
		interaction.createImmediateResponder().addEmbed(makeEmbed("Channel marked as bot channel")).respond();
	}
}
