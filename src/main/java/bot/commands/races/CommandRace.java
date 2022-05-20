package bot.commands.races;

import static bot.util.apis.MessageUtils.sendEphemeralMessage;

import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.util.subcommand.Command;

public class CommandRace extends Command {
	public CommandRace(final Fluffer10kFun fluffer10kFun) {
		super(fluffer10kFun.apiUtils, "race", "Zoooom", //
				new CommandRaceSponsor(fluffer10kFun), //
				new CommandRaceStart(fluffer10kFun));
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) throws Exception {
		final Server server = interaction.getServer().orElse(null);
		if (server == null) {
			sendEphemeralMessage(interaction, "Can't race here");
			return;
		}

		subcommandHandler.handle(interaction);
	}
}
