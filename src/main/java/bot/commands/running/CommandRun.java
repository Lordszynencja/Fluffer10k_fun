package bot.commands.running;

import static bot.commands.running.RunningUtils.addRunner;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.FileUtils.readFileLines;
import static bot.util.RandomUtils.getRandom;
import static bot.util.apis.MessageUtils.isServerTextChannel;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;

import java.io.IOException;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.util.subcommand.Command;

public class CommandRun extends Command {
	private final Fluffer10kFun fluffer10kFun;

	private final String[] imageLinks;

	public CommandRun(final Fluffer10kFun fluffer10kFun) throws IOException {
		super(fluffer10kFun.apiUtils, "run", "Start running");

		this.fluffer10kFun = fluffer10kFun;

		imageLinks = readFileLines(fluffer10kFun.apiUtils.config.getString("imageFolderPath") + "run/links.txt");
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {
		if (!isServerTextChannel(interaction)) {
			sendEphemeralMessage(interaction, "Can't run here");
			return;
		}

		final long channelId = interaction.getChannel().get().getId();
		final long userId = interaction.getUser().getId();
		final String userName = interaction.getUser().getDisplayName(interaction.getServer().get());
		final boolean added = addRunner(channelId, userId, userName);
		if (added) {
			fluffer10kFun.userDataUtils.getUserData(userId).runExp += 1;
		}

		final EmbedBuilder embed = makeEmbed(userName + " is running", null, getRandom(imageLinks));
		interaction.createImmediateResponder().addEmbed(embed).respond();
	}
}
