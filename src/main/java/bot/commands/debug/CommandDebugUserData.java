package bot.commands.debug;

import static bot.util.FileUtils.writeJSON;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;

import java.util.Map;

import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandInteractionOption;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import bot.Fluffer10kFun;
import bot.userData.ServerUserData;
import bot.util.apis.APIUtils;
import bot.util.subcommand.Subcommand;

public class CommandDebugUserData extends Subcommand {

	private final Fluffer10kFun fluffer10kFun;

	public CommandDebugUserData(final Fluffer10kFun fluffer10kFun) {
		super("user_data", "gets user data", //
				SlashCommandOption.create(SlashCommandOptionType.USER, "user", "user", true), //
				SlashCommandOption.create(SlashCommandOptionType.STRING, "path", "path"));

		this.fluffer10kFun = fluffer10kFun;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handle(final SlashCommandInteraction interaction) throws JsonMappingException, JsonProcessingException {
		final Server server = interaction.getServer().get();
		final SlashCommandInteractionOption option = getOption(interaction);
		final User user = option.getArgumentUserValueByName("user").orElse(interaction.getUser());
		final String path = option.getArgumentStringValueByName("path").orElse(null);
		final String[] pathTokens = path == null ? new String[0] : path.split(" ");

		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(server, user);
		Map<String, Object> map = userData.toMap();
		for (int i = 0; i < pathTokens.length; i++) {
			map = (Map<String, Object>) map.get(pathTokens[i]);
		}

		String json = writeJSON(map);
		if (json.length() > 1900) {
			json = "options: " + String.join(", ", map.keySet());
		}

		sendEphemeralMessage(interaction, APIUtils.getUserName(user, server) + " data " + path + "\n" + json);
	}
}
