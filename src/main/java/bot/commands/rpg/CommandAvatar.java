package bot.commands.rpg;

import static bot.util.apis.MessageUtils.sendEphemeralMessage;

import java.io.IOException;

import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;

import bot.Fluffer10kFun;
import bot.userData.ServerUserData;
import bot.util.subcommand.Command;

public class CommandAvatar extends Command {

	private final Fluffer10kFun fluffer10kFun;

	public CommandAvatar(final Fluffer10kFun fluffer10kFun) throws IOException {
		super(fluffer10kFun.apiUtils, "avatar", "Set your rpg avatar", false, //
				SlashCommandOption.create(SlashCommandOptionType.STRING, "avatar_url", "url of the image"));

		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {
		final Server server = interaction.getServer().orElse(null);
		if (server == null) {
			sendEphemeralMessage(interaction, "This command can only be used on server");
			return;
		}

		final User user = interaction.getUser();
		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(server.getId(), user.getId());

		userData.rpg.avatar = interaction.getArgumentStringValueByName("avatar_url").orElse(null);

		sendEphemeralMessage(interaction, "Image changed successfully");
	}
}
