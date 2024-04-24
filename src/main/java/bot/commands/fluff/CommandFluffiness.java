package bot.commands.fluff;

import static bot.commands.fluff.FluffyTailUtils.getTailsNumber;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.Utils.repeat;
import static bot.util.apis.MessageUtils.isServerTextChannel;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;
import static java.lang.String.format;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;

import bot.Fluffer10kFun;
import bot.util.subcommand.Command;

public class CommandFluffiness extends Command {
	private static final String tamamoImageUrl = "https://cdn.discordapp.com/attachments/831093717376172032/831105823252938822/Tamamo.png";

	private static final String[] tailNumberMessages = { "", //
			"%1$s is a kitsune with a tail fluffed %2$s times.", //
			"%1$s is a kitsune with two tails that were fluffed %2$s times.", //
			"%1$s is a kitsune with three tails that were fluffed %2$s times.", //
			"%1$s is a kitsune with four tails that were fluffed %2$s times.", //
			"%1$s is a kitsune with five tails that were fluffed %2$s times.", //
			"%1$s is a strong kitsune with six tails that were fluffed %2$s times!!", //
			"%1$s is a very strong kitsune with seven tails that were fluffed %2$s times!!", //
			"%1$s is a very strong and wise kitsune with eight tails that were fluffed %2$s times!!", //
			"%1$s is a legendary kitsune with nine tails that were fluffed %2$s times!!!" };

	private final Fluffer10kFun fluffer10kFun;

	public CommandFluffiness(final Fluffer10kFun fluffer10kFun) {
		super(fluffer10kFun.apiUtils, "fluffiness", "Shows current fluffiness", //
				SlashCommandOption.create(SlashCommandOptionType.USER, "fluff", "Check fluffiness of this person"));

		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {
		if (!isServerTextChannel(interaction)) {
			sendEphemeralMessage(interaction, "Cannot use this command here");
			return;
		}

		final Server server = interaction.getServer().get();
		final User user = interaction.getArgumentUserValueByName("fluff").orElse(interaction.getUser());
		final long userId = user.getId();
		final String userName = user.getDisplayName(server);
		final long fluffiness = fluffer10kFun.userDataUtils.getUserData(userId).fluffiness;

		if (fluffiness == 0) {
			interaction.createImmediateResponder()
					.addEmbed(makeEmbed("Do you even have a fluffy tail, " + userName + "?")).respond();
			return;
		}

		final int tails = getTailsNumber(fluffiness);
		final String tailsNumberMessage = format(tailNumberMessages[tails], userName, fluffiness);
		final String tailEmojis = repeat(fluffer10kFun.fluffyTailUtils.fluffyTailEmoji.getMentionTag(), tails);

		final EmbedBuilder embed = new EmbedBuilder()//
				.setTitle(tailsNumberMessage)//
				.setDescription(tailEmojis);

		if (tails >= 9) {
			embed.setImage(tamamoImageUrl);
		}

		interaction.createImmediateResponder().addEmbed(embed).respond();
	}
}
