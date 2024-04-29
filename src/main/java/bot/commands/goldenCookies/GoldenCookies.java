package bot.commands.goldenCookies;

import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.RandomUtils.getRandomLong;
import static bot.util.TimerUtils.startRepeatedTimedEvent;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.component.Button;
import org.javacord.api.entity.message.component.ButtonStyle;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.MessageComponentInteraction;

import bot.Fluffer10kFun;
import bot.data.ServerData;
import bot.userData.ServerUserData;
import bot.util.RandomUtils;

public class GoldenCookies {

	public static interface GoldenCookieEffect {
		void apply(MessageComponentInteraction interaction, ServerUserData userData, int cookiesCaught,
				EmbedBuilder embed);
	}

	private static final long[] goldenCookieTiers = { 7, 27, 77, 277, 777 };

	public static int getGoldenCookiesTier(final long goldenCookiesCaught) {
		for (int i = 0; i < goldenCookieTiers.length; i++) {
			if (goldenCookieTiers[i] > goldenCookiesCaught) {
				return i;
			}
		}

		return goldenCookieTiers.length;
	}

	private static final String goldenCookieImgUrl = "https://cdn.discordapp.com/attachments/831093717376172032/886745536001093682/goldenCookie.png";

	private final GoldenCookieEffect[] effects;

	private static String countString(final long count) {
		if (count % 10 == 1) {
			return count + "st";
		}
		if (count % 10 == 2) {
			return count + "nd";
		}
		if (count % 10 == 3) {
			return count + "rd";
		}
		return count + "th";
	}

	private static EmbedBuilder prepareTemplateEmbed(final String userName, final long cookiesCaught) {
		final String title = "Golden cookie caught by " + userName + "!";
		final String footer = "That's " + countString(cookiesCaught) + " golden cookie caught";
		return makeEmbed(title).setFooter(footer);
	}

	private final Fluffer10kFun fluffer10kFun;

	private void sendGoldenCookie(final long serverId, final ServerData serverData) {
		try {
			final MessageBuilder msg = new MessageBuilder()
					.addEmbed(makeEmbed("Golden Cookie!", null, goldenCookieImgUrl))//
					.addActionRow(Button.create("golden_cookie", ButtonStyle.PRIMARY, "Catch!"));

			final CompletableFuture<Message> newMessage = serverData.sendMessageOnBotChannel(serverId,
					fluffer10kFun.apiUtils, msg);
			serverData.lastGoldenCookieMessageId = newMessage == null ? null : newMessage.get().getId();
		} catch (final Exception e) {
			fluffer10kFun.apiUtils.messageUtils.sendExceptionToMe(e);
		}
	}

	private void removeLastGoldenCookieMessage(final long serverId, final ServerData serverData) {
		if (serverData.lastGoldenCookieMessageId == null) {
			return;
		}

		serverData.removeMessageFromBotChannel(serverId, fluffer10kFun.apiUtils, serverData.lastGoldenCookieMessageId);
		serverData.lastGoldenCookieMessageId = null;
	}

	private void tickServerGoldenCookies(final Long serverId, final ServerData serverData) {
		removeLastGoldenCookieMessage(serverId, serverData);

		serverData.goldenCookieCountdown--;
		if (serverData.goldenCookieCountdown <= 0) {
			final int tier = getGoldenCookiesTier(serverData.goldenCookiesCaught);
			final int minTime = tier < 5 ? 6 : 3;
			final int randomness = tier < 3 ? 12 : tier < 5 ? 6 : 3;
			serverData.goldenCookieCountdown = minTime + getRandomLong(randomness);
			sendGoldenCookie(serverId, serverData);
		}
	}

	private void tickGoldenCookies() {
		fluffer10kFun.botDataUtils.forEachServer(this::tickServerGoldenCookies, fluffer10kFun.apiUtils.messageUtils);
	}

	public GoldenCookies(final Fluffer10kFun fluffer10kFun) throws IOException {
		this.fluffer10kFun = fluffer10kFun;

		effects = new GoldenCookieEffect[] { //
				new GoldenCookieBonusExp(), //
				new GoldenCookieCookieBox(fluffer10kFun), //
				new GoldenCookieExpMultiplier(), //
				new GoldenCookieGold(), //
				new GoldenCookieHaremHappiness(), //
				new GoldenCookieMgFuckProtection(), //
				new GoldenCookieNoEffect(), //
				new GoldenCookiePlayCoins(), //
				new GoldenCookieRunningAdvantage(), //
				new GoldenCookieSillyStatus(), //
				new GoldenCookieSuperExpMultiplier() };

		startRepeatedTimedEvent(this::tickGoldenCookies, 5 * 60, 0, "shows golden cookies");

		fluffer10kFun.apiUtils.commandHandlers.addMessageComponentHandler("golden_cookie", this::handleAction);
	}

	private void handleAction(final MessageComponentInteraction interaction) {
		final Server server = interaction.getServer().get();
		final ServerData serverData = fluffer10kFun.botDataUtils.getServerData(server.getId());
		serverData.goldenCookiesCaught++;
		serverData.lastGoldenCookieMessageId = null;

		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(server.getId(),
				interaction.getUser().getId());
		final EmbedBuilder embed = prepareTemplateEmbed(interaction.getUser().getDisplayName(server),
				serverData.goldenCookiesCaught);

		RandomUtils.getRandom(effects).apply(interaction, userData, serverData.goldenCookiesCaught, embed);
	}
}
