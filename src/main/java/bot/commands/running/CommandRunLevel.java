package bot.commands.running;

import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.apis.commands.FlufferCommandOption.user;

import java.io.IOException;

import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.userData.UserData;
import bot.util.apis.commands.FlufferCommand;
import bot.util.subcommand.Command;

public class CommandRunLevel extends Command {
	private final Fluffer10kFun fluffer10kFun;

	public CommandRunLevel(final Fluffer10kFun fluffer10kFun) throws IOException {
		super(fluffer10kFun.apiUtils, //
				new FlufferCommand("run_level", "Check running level")//
						.addOption(user("target", "person to check running level for")));

		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {
		final User user = interaction.getArgumentUserValueByName("target").orElse(interaction.getUser());
		final UserData userData = fluffer10kFun.userDataUtils.getUserData(user.getId());
		final long level = userData.getRunningLevel();
		final String userName = fluffer10kFun.apiUtils.getUserName(user, interaction.getServer().orElse(null));

		interaction.createImmediateResponder().addEmbed(makeEmbed(userName + "'s running level is " + level)).respond();
	}
}
