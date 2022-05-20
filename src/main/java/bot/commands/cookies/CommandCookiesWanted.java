package bot.commands.cookies;

import static bot.util.EmbedUtils.makeEmbed;
import static java.util.stream.Collectors.toList;

import java.util.List;

import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.userData.ServerUserData;
import bot.util.subcommand.Subcommand;

public class CommandCookiesWanted extends Subcommand {
	private final Fluffer10kFun fluffer10kFun;

	public CommandCookiesWanted(final Fluffer10kFun fluffer10kFun) {
		super("wanted", "List the cookies you need");

		this.fluffer10kFun = fluffer10kFun;
	}

	private List<String> wantedCookies(final ServerUserData userData) {
		return fluffer10kFun.cookieUtils.cookieTypes.stream()//
				.filter(cookie -> userData.cookies.cookieCounts.getOrDefault(cookie, 0L) == 0L)//
				.collect(toList());
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {
		final long serverId = interaction.getServer().get().getId();
		final long userId = interaction.getUser().getId();

		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(serverId, userId);

		final List<String> wantedCookies = wantedCookies(userData);
		if (wantedCookies.isEmpty()) {
			interaction.createImmediateResponder().addEmbed(makeEmbed("You have all the cookies!")//
					.setColor(CookieUtils.cookiesColor))//
					.respond();
		} else if (wantedCookies.size() > 50) {
			interaction.createImmediateResponder()
					.addEmbed(makeEmbed("You are missing a whole lot of cookies... stop being so lazy!")//
							.setColor(CookieUtils.cookiesColor))//
					.respond();
		} else {
			interaction.createImmediateResponder()
					.addEmbed(makeEmbed("Your missing cookies", String.join(", ", wantedCookies))//
							.setColor(CookieUtils.cookiesColor))//
					.respond();
		}
	}
}
