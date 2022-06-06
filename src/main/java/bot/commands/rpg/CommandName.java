package bot.commands.rpg;

import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;

import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;

import bot.Fluffer10kFun;
import bot.userData.ServerUserData;
import bot.util.subcommand.Command;

public class CommandName extends Command {
	private final Fluffer10kFun fluffer10kFun;

	public CommandName(final Fluffer10kFun fluffer10kFun) {
		super(fluffer10kFun.apiUtils, "name", "Change your name", //
				SlashCommandOption.create(SlashCommandOptionType.STRING, "new_name",
						"new name of your character (leave empty to clear and have user name as name)"));

		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {
		final Server server = interaction.getServer().orElse(null);
		if (server == null) {
			sendEphemeralMessage(interaction, "This command can only be used on server");
			return;
		}

		final long serverId = server.getId();
		final User user = interaction.getUser();
		final String newName = interaction.getOptionStringValueByName("new_name").orElse(null);

		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(serverId, user.getId());
		userData.rpg.name = newName;

		interaction.createImmediateResponder()
				.addEmbed(makeEmbed("Name changed to " + userData.rpg.getName(user, server))).respond();
	}
}
