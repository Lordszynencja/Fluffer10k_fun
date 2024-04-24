package bot.commands.utility;

import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;

import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;

import bot.Fluffer10kFun;
import bot.util.subcommand.Command;

public class CommandModRole extends Command {
	private final Fluffer10kFun fluffer10kFun;

	public CommandModRole(final Fluffer10kFun fluffer10kFun) {
		super(fluffer10kFun.apiUtils, "mod_role", "Set mod role (only server owner)", //
				SlashCommandOption.create(SlashCommandOptionType.ROLE, "role",
						"Role that will be set as mod role for bot commands on this server", true));

		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {
		final Server server = interaction.getServer().orElse(null);
		if (server == null) {
			sendEphemeralMessage(interaction, "Can't use this command here");
			return;
		}

		final User user = interaction.getUser();
		if (user.getId() != server.getOwnerId()) {
			sendEphemeralMessage(interaction, "Only server owner can set mod role");
			return;
		}

		final Role role = interaction.getArgumentRoleValueByName("role").orElse(null);
		if (role == null) {
			sendEphemeralMessage(interaction, "Unknown role");
			return;
		}
		if (role.isEveryoneRole()) {
			sendEphemeralMessage(interaction, "Can't set this role as mod role");
			return;
		}

		fluffer10kFun.botDataUtils.getServerData(server.getId()).modRoleId = role.getId();
		interaction.createImmediateResponder().append(makeEmbed("Role " + role.getMentionTag() + " set as mod role"))
				.respond();
	}
}
