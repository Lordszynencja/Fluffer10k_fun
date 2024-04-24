package bot.commands.mgLove;

import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.apis.MessageUtils.isNSFWChannel;
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

public class CommandMgLoveCount extends Command {
	private final Fluffer10kFun fluffer10kFun;

	public CommandMgLoveCount(final Fluffer10kFun fluffer10kFun) throws IOException {
		super(fluffer10kFun.apiUtils, "mg_love_count", "Check how much fun you had with monster girls", //
				SlashCommandOption.create(SlashCommandOptionType.USER, "user", "user to check"));

		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {
		final Server server = interaction.getServer().orElse(null);
		if (!isNSFWChannel(interaction) || server == null) {
			sendEphemeralMessage(interaction, "This command cannot be used here");
			return;
		}

		final User user = interaction.getArgumentUserValueByName("user").orElse(interaction.getUser());

		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(server.getId(), user.getId());
		final String countString = userData.cums == 1 ? "1 time" : userData.cums + " times";
		interaction.createImmediateResponder().addEmbed(makeEmbed(user.getDisplayName(server) + " came " + countString))
				.respond();
	}
}
