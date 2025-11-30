package bot.commands.running;

import static bot.util.EmbedUtils.makeEmbed;

import java.io.IOException;

import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;

import bot.Fluffer10kFun;
import bot.userData.UserData;
import bot.util.subcommand.Command;

public class CommandCatchLevel extends Command {
	private final Fluffer10kFun fluffer10kFun;

	public CommandCatchLevel(final Fluffer10kFun fluffer10kFun) throws IOException {
		super(fluffer10kFun.apiUtils, "catch_level", "Check catching level", //
				SlashCommandOption.create(SlashCommandOptionType.USER, "target", "person to check catching level for"));

		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {
		final User user = interaction.getArgumentUserValueByName("target").orElse(interaction.getUser());
		final UserData userData = fluffer10kFun.userDataUtils.getUserData(user.getId());
		final long level = userData.getCatchingLevel();
		final String userName = fluffer10kFun.apiUtils.getUserName(user, interaction.getServer().orElse(null));

		interaction.createImmediateResponder().addEmbed(makeEmbed(userName + "'s catching level is " + level))
				.respond();
	}
}
