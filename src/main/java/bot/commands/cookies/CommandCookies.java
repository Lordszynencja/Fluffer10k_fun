package bot.commands.cookies;

import static bot.util.apis.MessageUtils.sendEphemeralMessage;

import java.io.IOException;

import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.util.subcommand.Command;

public class CommandCookies extends Command {
	public CommandCookies(final Fluffer10kFun fluffer10kFun) throws IOException {
		super(fluffer10kFun.apiUtils, "cookies", "Use various cookie related things", //
				new CommandCookiesCrumble(fluffer10kFun), //
				new CommandCookiesCrumbles(fluffer10kFun), //
				new CommandCookiesGive(fluffer10kFun), //
				new CommandCookiesList(fluffer10kFun), //
				new CommandCookiesShow(fluffer10kFun), //
				new CommandCookiesUpgrade(fluffer10kFun), //
				new CommandCookiesWanted(fluffer10kFun));
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
