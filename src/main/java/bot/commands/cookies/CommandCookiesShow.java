package bot.commands.cookies;

import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;

import bot.Fluffer10kFun;
import bot.userData.ServerUserData;
import bot.util.subcommand.Subcommand;

public class CommandCookiesShow extends Subcommand {
	private final Fluffer10kFun fluffer10kFun;

	public CommandCookiesShow(final Fluffer10kFun fluffer10kFun) {
		super("show", "Show one of your cookies", //
				SlashCommandOption.create(SlashCommandOptionType.STRING, "cookie_name", "Cookie to show", true));

		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {
		final long serverId = interaction.getServer().get().getId();
		final long userId = interaction.getUser().getId();

		final String cookieName = getOption(interaction).getArgumentStringValueByName("cookie_name").get();

		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(serverId, userId);
		if (userData.cookies.cookieCounts.getOrDefault(cookieName, 0L) == 0) {
			sendEphemeralMessage(interaction, "Couldn't find such cookie in your collection");
			return;
		}

		final EmbedBuilder embed = makeEmbed("Your " + cookieName)//
				.setImage(fluffer10kFun.cookieUtils.getCookieFile(cookieName))//
				.setColor(CookieUtils.cookiesColor);

		interaction.respondLater().join().addEmbed(embed).update();
	}
}
