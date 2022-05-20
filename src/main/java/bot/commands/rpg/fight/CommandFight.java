package bot.commands.rpg.fight;

import static bot.util.apis.MessageUtils.sendEphemeralMessage;

import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.util.subcommand.Command;

public class CommandFight extends Command {
	public CommandFight(final Fluffer10kFun fluffer10kFun) {
		super(fluffer10kFun.apiUtils, "fight", "for bot owner or test server only", //
				new CommandFightAutoWait(fluffer10kFun), //
				new CommandFightRefresh(fluffer10kFun), //
				new CommandFightSpellHotbar(fluffer10kFun));
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
