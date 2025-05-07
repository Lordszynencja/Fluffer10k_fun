package bot.commands.fluff;

import static bot.commands.fluff.FluffyTailUtils.getTailsNumber;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.FileUtils.readFileLines;
import static bot.util.RandomUtils.getRandom;
import static bot.util.apis.MessageUtils.isNSFWChannel;
import static bot.util.apis.MessageUtils.isServerTextChannel;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;
import static bot.util.modularPrompt.ModularPromptButton.button;

import java.io.IOException;

import org.javacord.api.entity.message.component.ButtonStyle;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.MessageComponentInteraction;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.userData.UserData;
import bot.util.apis.APIUtils;
import bot.util.modularPrompt.ModularPrompt;
import bot.util.subcommand.Command;

public class CommandWag extends Command {
	private final Fluffer10kFun fluffer10kFun;

	private final String[] imageLinksSFW;
	private final String[] imageLinksNSFW;

	public CommandWag(final Fluffer10kFun fluffer10kFun) throws IOException {
		super(fluffer10kFun.apiUtils, "wag", "Wags your tail");

		this.fluffer10kFun = fluffer10kFun;

		imageLinksSFW = readFileLines(fluffer10kFun.apiUtils.config.getString("imageFolderPath") + "wag/links.txt");
		imageLinksNSFW = readFileLines(
				fluffer10kFun.apiUtils.config.getString("imageFolderPath") + "wag/linksNSFW.txt");
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {
		if (!isServerTextChannel(interaction)) {
			sendEphemeralMessage(interaction, "Cannot use this command here");
			return;
		}

		final User user = interaction.getUser();
		final String userName = APIUtils.getUserName(user, interaction.getServer().get());
		final long userId = user.getId();
		final UserData userData = fluffer10kFun.userDataUtils.getUserData(userId);
		final int tailsNumber = getTailsNumber(userData.fluffiness);

		final String tailsMsg = fluffer10kFun.fluffyTailUtils.getTailsMsg(tailsNumber)
				+ (tailsNumber == 1 ? " is" : " are");
		final String title = userName + "'s " + tailsMsg + " waiting to be fluffed now";
		final String imgUrl = getRandom(isNSFWChannel(interaction) ? imageLinksNSFW : imageLinksSFW);
		final EmbedBuilder embed = makeEmbed(title, null, imgUrl);

		final ModularPrompt prompt = new ModularPrompt(embed, //
				button(fluffer10kFun.fluffyTailUtils.fluffyTailEmoji, ButtonStyle.SECONDARY,
						in -> handleFluffButton(in, userData, userId, userName)));
		fluffer10kFun.modularPromptUtils.addMessageForEveryone(prompt, interaction);
	}

	private void handleFluffButton(final MessageComponentInteraction interaction, final UserData userData,
			final long userId, final String userName) {
		final Server server = interaction.getServer().get();
		final User fluffer = interaction.getUser();

		if (fluffer.getId() == userId) {
			sendEphemeralMessage(interaction, "Can't fluff your own tail!");
			return;
		}

		fluffer10kFun.commandFluff.fluffTailOnButton(interaction, APIUtils.getUserName(fluffer, server), userId,
				userData, userName);
	}
}
