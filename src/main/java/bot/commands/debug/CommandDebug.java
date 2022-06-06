package bot.commands.debug;

import static bot.util.apis.APIUtils.isTestServer;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;

import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.util.subcommand.Command;

public class CommandDebug extends Command {
	public CommandDebug(final Fluffer10kFun fluffer10kFun) {
		super(fluffer10kFun.apiUtils, "debug", "for bot owner or test server only", //
				new CommandDebugFight(fluffer10kFun), //
				new CommandDebugGiveExp(fluffer10kFun), //
				new CommandDebugGiveItem(fluffer10kFun), //
				new CommandDebugGiveMonies(fluffer10kFun), //
				new CommandDebugItems(fluffer10kFun), //
				new CommandDebugOther(fluffer10kFun), //
				new CommandDebugMarketAdd(fluffer10kFun), //
				new CommandDebugResetQuest(fluffer10kFun), //
				new CommandDebugSetQuestContinuable(fluffer10kFun), //
				new CommandDebugUserData(fluffer10kFun));
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) throws Exception {
		final Server server = interaction.getServer().orElse(null);
		if (!isTestServer(server) && interaction.getUser().getId() != 289105579077664768L) {
			sendEphemeralMessage(interaction, "This command can only be used on test server or by the bot owner");
			return;
		}

		subcommandHandler.handle(interaction);
	}
}
