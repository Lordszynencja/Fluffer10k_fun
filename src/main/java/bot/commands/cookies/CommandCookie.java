package bot.commands.cookies;

import static bot.util.CollectionUtils.addToLongOnMap;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.RandomUtils.getRandom;
import static bot.util.RandomUtils.getRandomBoolean;
import static bot.util.Utils.capitalize;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;

import java.util.HashMap;
import java.util.Map;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;
import org.javacord.api.interaction.callback.InteractionOriginalResponseUpdater;

import bot.Fluffer10kFun;
import bot.userData.ServerUserData;
import bot.util.subcommand.Command;

public class CommandCookie extends Command {

	private static final Map<Long, Long> timeouts = new HashMap<>();

	private static final String[] cookieDenialReasons = { //
			"you will get fat", //
			"you will get lazy", //
			"your tummy will hurt", //
			"your mum will be angry", //
			"your dad ate them all", //
			"you got money this time" };

	private final Fluffer10kFun fluffer10kFun;

	public CommandCookie(final Fluffer10kFun fluffer10kFun) {
		super(fluffer10kFun.apiUtils, "cookie", "Bake a cookie", //
				SlashCommandOption.create(SlashCommandOptionType.STRING, "target_cookie", "Cookie name to try to make"), //
				SlashCommandOption.create(SlashCommandOptionType.USER, "receiver", "Person to make cookie for"));

		this.fluffer10kFun = fluffer10kFun;
	}

	private static int getCookiesBaked(final ServerUserData bakerData) {
		final long multiDropChanceLevel = bakerData.cookies.getUpgradeLevel(CookieUpgrade.MULTIPLE_DROP_CHANCE);
		return getRandomBoolean(multiDropChanceLevel / 100.0)
				? (int) bakerData.cookies.getUpgradeLevel(CookieUpgrade.MULTIPLE_DROP_MULTIPLIER)
				: 1;
	}

	private static String getTitle(final int cookiesBaked, final User baker, final User receiver, final Server server) {
		if (cookiesBaked == 1) {
			if (baker.getId() == receiver.getId()) {
				return "Got a cookie!";
			}
			return receiver.getDisplayName(server) + " got a cookie from " + baker.getDisplayName(server) + "!";
		}
		if (baker.getId() == receiver.getId()) {
			return "Got " + cookiesBaked + " cookies!";
		}
		return receiver.getDisplayName(server) + " got " + cookiesBaked + " cookies from "
				+ baker.getDisplayName(server) + "!";
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) throws Exception {
		final Server server = interaction.getServer().orElse(null);
		if (server == null) {
			sendEphemeralMessage(interaction, "This command is only usable on a server.");
			return;
		}

		final long serverId = server.getId();
		final User baker = interaction.getUser();
		final User receiver = interaction.getArgumentUserValueByName("receiver").orElse(baker);
		String cookieName = capitalize(interaction.getArgumentStringValueByName("target_cookie").orElse(""));

		final ServerUserData bakerData = fluffer10kFun.serverUserDataUtils.getUserData(serverId, baker.getId());
		final ServerUserData receiverData = fluffer10kFun.serverUserDataUtils.getUserData(serverId, receiver.getId());

		if (fluffer10kFun.cookieUtils.cookieExists(cookieName)) {
			final long targetedLevel = bakerData.cookies.getUpgradeLevel(CookieUpgrade.TARGETTED_CHANCE);
			if (targetedLevel > 0) {
				final double chance = targetedLevel * 1.0 / CookieUpgrade.TARGETTED_CHANCE.max;
				if (!getRandomBoolean(chance)) {
					cookieName = getRandom(fluffer10kFun.cookieUtils.cookieTypes);
				}
			}
		} else {
			if (!cookieName.equals("")) {
				sendEphemeralMessage(interaction, "Misspelled cookie name?");
				return;
			}
			cookieName = getRandom(fluffer10kFun.cookieUtils.cookieTypes);
		}

		if (timeouts.getOrDefault(baker.getId(), 0L) > System.currentTimeMillis()) {
			sendEphemeralMessage(interaction,
					"You can't have another cookie yet, " + getRandom(cookieDenialReasons) + ".");
			return;
		}

		timeouts.put(baker.getId(), System.currentTimeMillis() + 5 * 1000);

		final int cookiesBaked = getCookiesBaked(bakerData);
		addToLongOnMap(receiverData.cookies.cookieCounts, cookieName, cookiesBaked);

		final String title = getTitle(cookiesBaked, baker, receiver, server);

		final EmbedBuilder embed = makeEmbed(title, cookieName)//
				.setImage(fluffer10kFun.cookieUtils.getCookieFile(cookieName))//
				.setColor(CookieUtils.cookiesColor);
		final InteractionOriginalResponseUpdater updater = interaction.respondLater().join().addEmbed(embed);
		if (receiver.getId() != baker.getId()) {
			updater.append(receiver.getMentionTag());
		}
		updater.update();
	}
}
