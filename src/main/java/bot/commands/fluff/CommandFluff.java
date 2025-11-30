package bot.commands.fluff;

import static bot.commands.fluff.FluffyTailUtils.getTailsNumber;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.FileUtils.readFileLines;
import static bot.util.RandomUtils.getRandom;
import static bot.util.Utils.toMention;
import static bot.util.apis.MessageUtils.getTextChannel;
import static bot.util.apis.MessageUtils.isNSFWChannel;
import static bot.util.apis.MessageUtils.isTextChannel;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;

import java.io.IOException;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.MessageComponentInteraction;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.data.Emojis;
import bot.userData.UserData;
import bot.util.apis.commands.FlufferCommand;
import bot.util.apis.commands.FlufferCommandOption;
import bot.util.subcommand.Command;

public class CommandFluff extends Command {
	private final Fluffer10kFun fluffer10kFun;
	private final Emojis emojis;

	private final String[] imageLinksSFW;
	private final String[] imageLinksNSFW;

	public CommandFluff(final Fluffer10kFun fluffer10kFun) throws IOException {
		super(fluffer10kFun.apiUtils, new FlufferCommand("fluff", "Fluff the tail!")//
				.addOption(FlufferCommandOption.user("target", "Lets you fluff the tail you want")//
						.required()));

		this.fluffer10kFun = fluffer10kFun;
		emojis = fluffer10kFun.emojis;

		imageLinksSFW = readFileLines(fluffer10kFun.apiUtils.config.getString("imageFolderPath") + "fluff/links.txt");
		imageLinksNSFW = readFileLines(
				fluffer10kFun.apiUtils.config.getString("imageFolderPath") + "fluff/linksNSFW.txt");
	}

	private String getDescription(final boolean isNew, final int newTailsNumber, final int oldTailsNumber,
			final String fluffedNick) {
		final String fluffTailEmoji = emojis.fluffytail.getMentionTag();
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
		final String title = fluffedNick + "'s " + tailMsg + " fluffed by " + flufferNick + " "
				+ emojis.tamawoo.getMentionTag();
		final String description = getDescription(isNew, newTailsNumber, tailsNumber, fluffedNick);
		return makeEmbed(title, description, getRandom(isNSFW ? imageLinksNSFW : imageLinksSFW));
	}

	public void fluffTailOnButton(final MessageComponentInteraction interaction, final String flufferNick,
			final long fluffedId, final UserData fluffedData, final String fluffedNick) {
		final EmbedBuilder embed = fluffTailAndMakeEmbed(flufferNick, fluffedData, fluffedNick,
				getTextChannel(interaction).getId(), isNSFWChannel(interaction));

		if (interaction.getChannel().get().getType().isServerChannelType()) {
			interaction.createOriginalMessageUpdater()//
					.removeAllComponents()//
					.update();
			interaction.getChannel().get().sendMessage(toMention(fluffedId), embed);
		} else {
			interaction.createOriginalMessageUpdater()//
					.removeAllComponents()//
					.removeAllEmbeds()//
					.addEmbed(embed)//
					.update();
		}
	}

	public void fluffTail(final SlashCommandInteraction interaction, final String flufferNick, final long fluffedId,
			final String fluffedNick) {
		final UserData userData = fluffer10kFun.userDataUtils.getUserData(fluffedId);

		final EmbedBuilder embed = fluffTailAndMakeEmbed(flufferNick, userData, fluffedNick,
				getTextChannel(interaction).getId(), isNSFWChannel(interaction));
		interaction.createImmediateResponder().append(toMention(fluffedId)).addEmbed(embed).respond();
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {
		if (!isTextChannel(interaction)) {
			sendEphemeralMessage(interaction, "This command cannot be used here");
			return;
		}

		final User user = interaction.getUser();
		final Server server = interaction.getServer().orElse(null);
		final User mentionedUser = interaction.getArgumentUserValueByName("target").orElse(null);

		if (mentionedUser != null && mentionedUser.getId() != user.getId()) {
			fluffTail(interaction, fluffer10kFun.apiUtils.getUserName(user, server), mentionedUser.getId(),
					fluffer10kFun.apiUtils.getUserName(mentionedUser, server));
			return;
		}

		final String title = fluffer10kFun.apiUtils.getUserName(user, server) + " fluffed some very fluffy tails "
				+ emojis.tamawoo;
		interaction.createImmediateResponder()
				.addEmbed(
						makeEmbed(title, null, getRandom(isNSFWChannel(interaction) ? imageLinksNSFW : imageLinksSFW)))
				.respond();

	}
}
