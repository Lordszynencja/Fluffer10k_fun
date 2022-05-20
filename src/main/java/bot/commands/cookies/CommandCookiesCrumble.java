package bot.commands.cookies;

import static bot.data.items.ItemUtils.getFormattedPlayCoins;
import static bot.util.CollectionUtils.addToLongOnMap;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;
import static java.util.stream.Collectors.toList;

import java.util.List;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.userData.ServerUserData;
import bot.util.subcommand.Subcommand;

public class CommandCookiesCrumble extends Subcommand {
	private static final long pcReward = 1_000_000;

	private final EmbedBuilder embed;

	private final Fluffer10kFun fluffer10kFun;

	public CommandCookiesCrumble(final Fluffer10kFun fluffer10kFun) {
		super("crumble", "Crumble your cookies for a crumble point");

		this.fluffer10kFun = fluffer10kFun;

		embed = makeEmbed("You crumbled your cookies and got a crumble point!\n" //
				+ "You also got " + getFormattedPlayCoins(pcReward) + "!")//
						.setImage(fluffer10kFun.cookieUtils.getCookieFile("Cookie crumbs"))//
						.setColor(CookieUtils.cookiesColor);
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
		if (!wantedCookies.isEmpty()) {
			sendEphemeralMessage(interaction, "You don't have every cookie yet");
			return;
		}

		fluffer10kFun.cookieUtils.cookieTypes
				.forEach(cookie -> addToLongOnMap(userData.cookies.cookieCounts, cookie, -1));
		userData.playCoins += pcReward;
		userData.cookies.crumbles++;

		interaction.respondLater().join().addEmbed(embed).update();
	}
}
