package bot.commands.goldenCookies;

import static bot.commands.goldenCookies.GoldenCookies.getGoldenCookiesTier;
import static bot.util.RandomUtils.getRandomLong;
import static bot.util.Utils.toMsFromSystemTime;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.MessageComponentInteraction;

import bot.commands.goldenCookies.GoldenCookies.GoldenCookieEffect;
import bot.userData.ServerUserData;
import bot.userData.UserStatusesData.UserStatusType;

public class GoldenCookieRunningAdvantage implements GoldenCookieEffect {

	private static final long[] runningAdvantageLenghtsPerTier = { 1, 2, 4, 8, 16, 24 };

	@Override
	public void apply(final MessageComponentInteraction interaction, final ServerUserData userData,
			final int goldenCookiesCaught, final EmbedBuilder embed) {
		final long length = getRandomLong(1, runningAdvantageLenghtsPerTier[getGoldenCookiesTier(goldenCookiesCaught)]);

		userData.statuses.addStatus(UserStatusType.RUNNING_ADVANTAGE, toMsFromSystemTime(0, length, 0, 0),
				"Running advantage");

		final String lengthInfo = length + (length == 1 ? " hour" : " hours");
		embed.setDescription("You have running advantage for " + lengthInfo + "!");

		interaction.createOriginalMessageUpdater().addEmbed(embed).update();
	}

}
