package bot.commands.utility;

import static bot.util.apis.MessageUtils.sendEphemeralMessage;

import java.util.HashMap;
import java.util.Map;

import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;

import bot.Fluffer10kFun;
import bot.util.subcommand.Command;

public class CommandAnswer extends Command {
	public interface AnswerHandler {
		void handle(SlashCommandInteraction interaction, String answer) throws Exception;
	}

	public CommandAnswer(final Fluffer10kFun fluffer10kFun) {
		super(fluffer10kFun.apiUtils, "answer", "Input for some places", //
				SlashCommandOption.create(SlashCommandOptionType.STRING, "answer", "the input", true));
	}

	Map<Long, Map<Long, AnswerHandler>> handlers = new HashMap<>();

	public void addAnswerHandler(final long channelId, final long userId, final AnswerHandler handler) {
		Map<Long, AnswerHandler> channelHandlers = handlers.get(channelId);
		if (channelHandlers == null) {
			channelHandlers = new HashMap<>();
			handlers.put(channelId, channelHandlers);
		}

		channelHandlers.put(userId, handler);
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) throws Exception {
		final long channelId = interaction.getChannel().get().getId();
		final Map<Long, AnswerHandler> channelHandlers = handlers.get(channelId);
		if (channelHandlers == null) {
			sendEphemeralMessage(interaction, "There's nothing waiting for your input here");
			return;
		}

		final long userId = interaction.getUser().getId();
		final AnswerHandler channelUserHandler = channelHandlers.remove(userId);
		if (channelUserHandler == null) {
			sendEphemeralMessage(interaction, "There's nothing waiting for your input here");
			return;
		}

		final String answer = interaction.getArgumentStringValueByName("answer").get();
		channelUserHandler.handle(interaction, answer);
	}

}
