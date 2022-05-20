package bot.commands.fluff;

import static bot.commands.fluff.FluffyTailUtils.getTailsNumber;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.FileUtils.readFileLines;
import static bot.util.RandomUtils.getRandom;
import static bot.util.Utils.toMention;
import static bot.util.apis.MessageUtils.isNSFWChannel;
import static bot.util.apis.MessageUtils.isServerTextChannel;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;

import java.io.IOException;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.MessageComponentInteraction;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;

import bot.Fluffer10kFun;
import bot.userData.UserData;
import bot.util.subcommand.Command;

public class CommandFluff extends Command {
	private final Fluffer10kFun fluffer10kFun;

	private final String tamawooEmoji;

	private final String[] imageLinksSFW;
	private final String[] imageLinksNSFW;

	public CommandFluff(final Fluffer10kFun fluffer10kFun) throws IOException {
		super(fluffer10kFun.apiUtils, "fluff", "Fluff the tail!", //
				SlashCommandOption.create(SlashCommandOptionType.USER, "target", "Lets you fluff the tail you want",
						true));

		this.fluffer10kFun = fluffer10kFun;

		tamawooEmoji = fluffer10kFun.apiUtils.getEmojiStringByNameFromMyServer("tamawoo");

		imageLinksSFW = readFileLines(fluffer10kFun.apiUtils.config.getString("imageFolderPath") + "fluff/links.txt");
		imageLinksNSFW = readFileLines(
				fluffer10kFun.apiUtils.config.getString("imageFolderPath") + "fluff/linksNSFW.txt");
	}

	private String getDescription(final boolean isNew, final int newTailsNumber, final int oldTailsNumber,
			final String fluffedNick) {
		final String fluffTailEmoji = fluffer10kFun.fluffyTailUtils.fluffyTailEmoji.getMentionTag();
		if (isNew) {
			return fluffedNick + " grew a " + fluffTailEmoji + ", welcome to the fluffy family :3";
		}

		if (oldTailsNumber == newTailsNumber) {
			return null;
		}
		if (newTailsNumber != 9) {
			return fluffedNick + " grew another " + fluffTailEmoji + "!";
		}

		return fluffTailEmoji + " " + fluffedNick + " grew a ninth " + fluffTailEmoji + " and became LEGENDARY!!! "
				+ fluffTailEmoji;
	}

	private EmbedBuilder fluffTailAndMakeEmbed(final String flufferNick, final UserData fluffedData,
			final String fluffedNick, final long channelId, final boolean isNSFW) {
		final int tailsNumber = getTailsNumber(fluffedData.fluffiness);
		final boolean isNew = fluffedData.fluffiness == 0;
		fluffedData.fluffiness += tailsNumber;
		final int newTailsNumber = getTailsNumber(fluffedData.fluffiness);

		final String tailMsg = fluffer10kFun.fluffyTailUtils.getTailsMsg(tailsNumber)
				+ (tailsNumber == 1 ? " was" : " were");
		final String title = fluffedNick + "'s " + tailMsg + " fluffed by " + flufferNick + " " + tamawooEmoji;
		final String description = getDescription(isNew, newTailsNumber, tailsNumber, fluffedNick);
		return makeEmbed(title, description, getRandom(isNSFW ? imageLinksNSFW : imageLinksSFW));
	}

	public void fluffTailOnButton(final MessageComponentInteraction interaction, final String flufferNick,
			final long fluffedId, final UserData fluffedData, final String fluffedNick) {
		interaction.createOriginalMessageUpdater().removeAllComponents().update();

		final EmbedBuilder embed = fluffTailAndMakeEmbed(flufferNick, fluffedData, fluffedNick,
				interaction.getChannel().get().getId(), isNSFWChannel(interaction));

		interaction.getChannel().get().sendMessage(toMention(fluffedId), embed);
	}

	public void fluffTail(final SlashCommandInteraction interaction, final String flufferNick, final long fluffedId,
			final String fluffedNick) {
		final UserData userData = fluffer10kFun.userDataUtils.getUserData(fluffedId);

		final EmbedBuilder embed = fluffTailAndMakeEmbed(flufferNick, userData, fluffedNick,
				interaction.getChannel().get().getId(), isNSFWChannel(interaction));
		interaction.createImmediateResponder().append(toMention(fluffedId)).addEmbed(embed).respond();
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {
		if (!isServerTextChannel(interaction)) {
			sendEphemeralMessage(interaction, "This command cannot be used here");
			return;
		}

		final User user = interaction.getUser();
		final Server server = interaction.getServer().get();
		final User mentionedUser = interaction.getOptionUserValueByName("target").orElse(null);

		if (mentionedUser != null && mentionedUser.getId() != user.getId()) {
			fluffTail(interaction, user.getDisplayName(server), mentionedUser.getId(),
					mentionedUser.getDisplayName(server));
			return;
		}

		final String title = user.getDisplayName(server) + " fluffed some very fluffy tails " + tamawooEmoji;
		interaction.createImmediateResponder()
				.addEmbed(
						makeEmbed(title, null, getRandom(isNSFWChannel(interaction) ? imageLinksNSFW : imageLinksSFW)))
				.respond();

	}
}
