package bot.commands.debug;

import static bot.util.CollectionUtils.toMap;
import static bot.util.Utils.Pair.pair;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;

import java.util.Map;

import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandInteractionOption;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;

import bot.Fluffer10kFun;
import bot.util.apis.CommandHandlers.SlashCommandHandler;
import bot.util.subcommand.Subcommand;

public class CommandDebugOther extends Subcommand {

	private final Fluffer10kFun fluffer10kFun;

	public CommandDebugOther(final Fluffer10kFun fluffer10kFun) {
		super("other", "do other stuff", //
				SlashCommandOption.create(SlashCommandOptionType.STRING, "cmd", "cmd", true));

		this.fluffer10kFun = fluffer10kFun;
	}

	private void handleResetBlacksmith(final SlashCommandInteraction interaction) {
		fluffer10kFun.serverUserDataUtils.onEveryUser((serverId, userId, userData) -> {
			userData.blacksmith.setToday = false;
		});
	}

	private final Map<String, SlashCommandHandler> handlers = toMap(//
			pair("reset_blacksmith", this::handleResetBlacksmith));

	@Override
	public void handle(final SlashCommandInteraction interaction) throws Exception {
		final SlashCommandInteractionOption option = getOption(interaction);
		final String cmd = option.getArgumentStringValueByName("cmd").orElse(null);

		if (handlers.containsKey(cmd)) {
			handlers.get(cmd).handle(interaction);
			return;
		}

		sendEphemeralMessage(interaction, "Unknown command " + cmd);
	}
}
