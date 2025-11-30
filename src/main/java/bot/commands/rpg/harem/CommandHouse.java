package bot.commands.rpg.harem;

import static bot.util.apis.MessageUtils.isNSFWChannel;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;

import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.util.subcommand.Command;

public class CommandHouse extends Command {
	public CommandHouse(final Fluffer10kFun fluffer10kFun) {
		super(fluffer10kFun.apiUtils, "house", "House commands", false, //
				new CommandHouseList(fluffer10kFun), //
				new CommandHouseShow(fluffer10kFun));
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) throws Exception {
		final Server server = interaction.getServer().orElse(null);
		if (!isNSFWChannel(interaction) || server == null) {
			sendEphemeralMessage(interaction, "This command cannot be used here");
			return;
		}

		subcommandHandler.handle(interaction);
	}
}
