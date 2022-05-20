package bot.commands.rpg.saves;

import static bot.util.apis.MessageUtils.sendEphemeralMessage;

import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.util.subcommand.Command;

public class CommandSaves extends Command {
	public CommandSaves(final Fluffer10kFun fluffer10kFun) {
		super(fluffer10kFun.apiUtils, "saves", "Handle saves", //
				new CommandSavesDelete(fluffer10kFun), //
				new CommandSavesLoad(fluffer10kFun), //
				new CommandSavesList(fluffer10kFun), //
				new CommandSavesSave(fluffer10kFun));
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) throws Exception {
		final Server server = interaction.getServer().orElse(null);
		if (server == null) {
			sendEphemeralMessage(interaction, "This command can only be used on server");
			return;
		}

		subcommandHandler.handle(interaction);
	}
}
