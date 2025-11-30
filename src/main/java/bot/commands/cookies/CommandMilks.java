package bot.commands.cookies;

import static bot.util.apis.MessageUtils.sendEphemeralMessage;

import java.io.IOException;

import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.util.subcommand.Command;

public class CommandMilks extends Command {
	public CommandMilks(final Fluffer10kFun fluffer10kFun) throws IOException {
		super(fluffer10kFun.apiUtils, "milks", "Use various milk related things", false, //
				new CommandMilksGive(fluffer10kFun), //
				new CommandMilksList(fluffer10kFun), //
				new CommandMilksShow(fluffer10kFun));
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) throws Exception {
		final Server server = interaction.getServer().orElse(null);
		if (server == null) {
			sendEphemeralMessage(interaction, "This command is only usable on a server.");
			return;
		}

		subcommandHandler.handle(interaction);
	}
}
