package bot.commands.imageManipulation;

import static bot.util.ImageUtils.getImageFromUrl;
import static bot.util.ImageUtils.rescaleToMaxSize;
import static bot.util.apis.MessageUtils.getServerTextChannel;

import java.awt.image.BufferedImage;
import java.util.NavigableSet;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageAttachment;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;

import bot.Fluffer10kFun;
import bot.util.ImageUtils.ImageData;
import bot.util.ImageUtils.PixelColorRGBA;
import bot.util.subcommand.Command;

public class CommandInvert extends Command {

	public CommandInvert(final Fluffer10kFun fluffer10kFun) {
		super(fluffer10kFun.apiUtils, "invert", "Inverts image that is in parameter, or last pic on the channel", //
				SlashCommandOption.create(SlashCommandOptionType.STRING, "image_url", "image to invert", true));
	}

	private static ImageData getImageData(final SlashCommandInteraction interaction) {
		final String url = interaction.getArgumentStringValueByName("image_url").get();
		if (url != null) {
			return getImageFromUrl(url);
		}

		final TextChannel channel = getServerTextChannel(interaction);
		final NavigableSet<Message> messages = channel.getMessages(10).join().descendingSet();
		for (final Message message : messages) {
			for (final MessageAttachment attachment : message.getAttachments()) {
				if (attachment.isImage()) {
					return new ImageData(attachment.asImage().join());
				}
			}
		}

		return null;
	}

	private static BufferedImage transform(final BufferedImage img) {
		final BufferedImage scaled = img.getWidth() > 1000 || img.getHeight() > 1000 ? rescaleToMaxSize(img, 1000, 1000)
				: img;
		final int w = scaled.getWidth();
		final int h = scaled.getHeight();

		final BufferedImage transformed = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				final PixelColorRGBA pixelColor = new PixelColorRGBA(scaled.getRGB(x, y));
				pixelColor.r = 255 - pixelColor.r;
				pixelColor.g = 255 - pixelColor.g;
				pixelColor.b = 255 - pixelColor.b;
				transformed.setRGB(x, y, pixelColor.to32Bit());
			}
		}

		return transformed;
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {
		interaction.respondLater();

		final ImageData imageData = getImageData(interaction);
		if (imageData == null) {
			interaction.createFollowupMessageBuilder().setFlags(MessageFlag.EPHEMERAL)
					.setContent("Couldn't read the image").send();
			return;
		}

		final BufferedImage transformed = transform(imageData.images[0]);
		interaction.createFollowupMessageBuilder().addAttachment(transformed, "inverted.png").send();
	}
}
