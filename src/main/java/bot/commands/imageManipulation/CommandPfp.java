package bot.commands.imageManipulation;

import static bot.util.apis.commands.FlufferCommandOption.user;

import org.javacord.api.entity.Icon;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.util.apis.commands.FlufferCommand;
import bot.util.subcommand.Command;

public class CommandPfp extends Command {
	public CommandPfp(final Fluffer10kFun fluffer10kFun) {
		super(fluffer10kFun.apiUtils, //
				new FlufferCommand("pfp", "check pfp")//
						.addOption(user("target", "person whose pfp you wanna see").required()));
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {
		final User target = interaction.getArgumentUserValueByName("target").orElse(interaction.getUser());
		final Server server = interaction.getServer().orElse(null);
		final Icon avatar = server == null ? target.getAvatar()
				: target.getServerAvatar(server).orElse(target.getAvatar());

		interaction.createImmediateResponder().append(avatar.getUrl()).respond();
	}

}
