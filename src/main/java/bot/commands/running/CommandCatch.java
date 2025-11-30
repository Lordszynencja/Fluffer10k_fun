package bot.commands.running;

import static bot.commands.running.RunningUtils.removeRunner;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.FileUtils.readFileLines;
import static bot.util.RandomUtils.getRandom;
import static bot.util.RandomUtils.getRandomBoolean;
import static bot.util.apis.MessageUtils.getTextChannel;
import static bot.util.apis.MessageUtils.isTextChannel;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;

import java.io.IOException;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.commands.running.RunningUtils.RunnerData;
import bot.userData.ServerUserData;
import bot.userData.UserData;
import bot.userData.UserStatusesData.UserStatusType;
import bot.util.subcommand.Command;

public class CommandCatch extends Command {
	private static final String chasingItselfImageUrl = "https://cdn.discordapp.com/attachments/831126151044136970/831126800443637800/chasingYourself.gif";

	private final String[] imageLinks;

	private final Fluffer10kFun fluffer10kFun;

	public CommandCatch(final Fluffer10kFun fluffer10kFun) throws IOException {
		super(fluffer10kFun.apiUtils, "catch", "Catch someone!");

		imageLinks = readFileLines(fluffer10kFun.apiUtils.config.getString("imageFolderPath") + "catch/links.txt");

		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {
		if (!isTextChannel(interaction)) {
			sendEphemeralMessage(interaction, "Can't catch in here");
			return;
		}

		final Server server = interaction.getServer().orElse(null);
		final long channelId = getTextChannel(interaction).getId();
		final RunnerData runner = RunningUtils.getCurrentRunner(channelId);
		final User catcher = interaction.getUser();

		if (runner == null) {
			sendEphemeralMessage(interaction, "No one is running in your sight.");
			return;
		}

		if (runner.userId == catcher.getId()) {
			final EmbedBuilder embed = makeEmbed(runner.nick + " chases themselves", null, chasingItselfImageUrl);
			interaction.createImmediateResponder().addEmbed(embed).respond();
			return;
		}

		final UserData runnerUserData = fluffer10kFun.userDataUtils.getUserData(runner.userId);
		final UserData catcherUserData = fluffer10kFun.userDataUtils.getUserData(catcher.getId());

		final boolean runnerAdvantage = server == null ? false : getAdvantage(server.getId(), runner.userId);
		final boolean catcherAdvantage = server == null ? false : getAdvantage(server.getId(), catcher.getId());

		final boolean isCaught;
		if (runnerAdvantage && !catcherAdvantage) {
			isCaught = false;
		} else if (!runnerAdvantage && catcherAdvantage) {
			isCaught = true;
		} else {
			final long runnerLevel = runnerUserData.getRunningLevel();
			final long catcherLevel = catcherUserData.getCatchingLevel();

			final double levelMultiplier = 0.5
					+ ((double) (catcherLevel - runnerLevel)) / Math.max(catcherLevel, runnerLevel) / 2;
			final long t = System.currentTimeMillis();
			final double timeMultiplier = (runner.timeout - t) / 1000.0 / RunningUtils.runningTimeout;
			final double catchChance = levelMultiplier * timeMultiplier;

			isCaught = getRandomBoolean(catchChance);
		}

		if (isCaught) {
			catcherUserData.catchExp += 2;

			final EmbedBuilder embed = makeEmbed(
					fluffer10kFun.apiUtils.getUserName(catcher, interaction.getServer().get()) + " caught "
							+ runner.nick + "!")//
					.setImage(getRandom(imageLinks));

			interaction.createImmediateResponder().addEmbed(embed).respond();
		} else {
			runnerUserData.runExp += 1;
			catcherUserData.catchExp += 1;

			interaction.createImmediateResponder().addEmbed(makeEmbed(runner.nick + " escaped!")).respond();
		}
		removeRunner(channelId, runner);
	}

	private boolean getAdvantage(final long serverId, final long userId) {
		final ServerUserData serverUserData = fluffer10kFun.serverUserDataUtils.getUserData(serverId, userId);

		return serverUserData.statuses.isStatus(UserStatusType.RUNNING_ADVANTAGE);
	}
}
