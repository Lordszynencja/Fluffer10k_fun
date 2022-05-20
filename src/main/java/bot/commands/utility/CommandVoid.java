package bot.commands.utility;

import static bot.util.apis.MessageUtils.sendEphemeralMessage;

import java.util.Arrays;

import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.util.apis.MessageUtils;
import bot.util.subcommand.Command;

public class CommandVoid extends Command {
	private final Fluffer10kFun fluffer10kFun;

	public CommandVoid(final Fluffer10kFun fluffer10kFun) {
		super(fluffer10kFun.apiUtils, "void", "Creates void (mod only)");

		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {
		final Server server = interaction.getServer().orElse(null);
		if (server == null) {
			MessageUtils.sendEphemeralMessage(interaction, "Can't use this command here");
			return;
		}

		final User user = interaction.getUser();
		if (!fluffer10kFun.botDataUtils.isUserMod(user, server)) {
			sendEphemeralMessage(interaction, "This command is only for mods (mod role is set with /mod_role command)");
			return;
		}

		final char[] chars = new char[50];
		Arrays.fill(chars, '\n');
		chars[0] = '_';
		chars[49] = '_';
		interaction.createImmediateResponder().append(new String(chars)).respond();
	}
}
