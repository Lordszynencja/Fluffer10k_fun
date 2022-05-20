package bot.commands.goldenCookies;

import static bot.commands.goldenCookies.GoldenCookies.getGoldenCookiesTier;
import static bot.util.CollectionUtils.addToLongOnMap;
import static bot.util.RandomUtils.getRandom;
import static bot.util.RandomUtils.getRandomInt;
import static bot.util.Utils.joinNames;

import java.util.ArrayList;
import java.util.List;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.MessageComponentInteraction;

import bot.Fluffer10kFun;
import bot.commands.goldenCookies.GoldenCookies.GoldenCookieEffect;
import bot.userData.ServerUserData;

public class GoldenCookieCookieBox implements GoldenCookieEffect {
	private enum GCCookieBoxSizes {
		SMALL("small", 10), //
		MEDIUM("medium", 20), //
		BIG("big", 30), //
		LARGE("large", 40), //
		HUGE("huge", 50), //
		GIGANTIC("gigantic", 100);

		public static final GCCookieBoxSizes[] list = { SMALL, MEDIUM, BIG, LARGE, HUGE, GIGANTIC };

		public final String name;
		public final int size;

		GCCookieBoxSizes(final String name, final int size) {
			this.name = name;
			this.size = size;
		}
	}

	private static final String[] cookieBoxImages = {
			"https://cdn.discordapp.com/attachments/831093717376172032/886761751973093397/Box_of_brand_biscuits.png", //
			"https://cdn.discordapp.com/attachments/831093717376172032/886761754607124530/Box_of_macarons.png", //
			"https://cdn.discordapp.com/attachments/831093717376172032/886761755739578448/Box_of_maybe_cookies.png", //
			"https://cdn.discordapp.com/attachments/831093717376172032/886761758000300142/Box_of_not_cookies.png", //
			"https://cdn.discordapp.com/attachments/831093717376172032/886761759048876042/Box_of_pastries.png", //
			"https://cdn.discordapp.com/attachments/831093717376172032/886761760890167296/Tin_of_british_tea_biscuits.png", //
			"https://cdn.discordapp.com/attachments/831093717376172032/886761763067011102/Tin_of_butter_cookies.png" };

	private final Fluffer10kFun fluffer10kFun;

	public GoldenCookieCookieBox(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void apply(final MessageComponentInteraction interaction, final ServerUserData userData,
			final int goldenCookiesCaught, final EmbedBuilder embed) {
		final int boxNo = getRandomInt(getGoldenCookiesTier(goldenCookiesCaught) + 1);
		final GCCookieBoxSizes box = GCCookieBoxSizes.list[boxNo];

		final List<String> cookies = new ArrayList<>();
		for (int i = 0; i < box.size; i++) {
			final String cookieName = getRandom(fluffer10kFun.cookieUtils.cookieTypes);
			addToLongOnMap(userData.cookies.cookieCounts, cookieName, 1);
			cookies.add(cookieName);
		}

		embed.setDescription("You found " + box.name + " box of cookies containing " + joinNames(cookies) + "!")//
				.setImage(getRandom(cookieBoxImages));

		interaction.createOriginalMessageUpdater().addEmbed(embed).update();
	}

}
