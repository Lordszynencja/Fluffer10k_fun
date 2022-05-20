package bot.commands.goldenCookies;

import static bot.commands.goldenCookies.GoldenCookies.getGoldenCookiesTier;
import static bot.util.RandomUtils.getRandomLong;
import static bot.util.Utils.toMsFromSystemTime;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.MessageComponentInteraction;

import bot.commands.goldenCookies.GoldenCookies.GoldenCookieEffect;
import bot.userData.ServerUserData;
import bot.userData.UserStatusesData.UserStatusType;

public class GoldenCookieExpMultiplier implements GoldenCookieEffect {

	private static final long[] expMultiplierLenghtsPerTier = { 1, 2, 3, 4, 6, 8 };

	@Override
	public void apply(final MessageComponentInteraction interaction, final ServerUserData userData,
			final int goldenCookiesCaught, final EmbedBuilder embed) {
		final long length = getRandomLong(1, expMultiplierLenghtsPerTier[getGoldenCookiesTier(goldenCookiesCaught)]);

		userData.statuses.addStatus(UserStatusType.EXP_MULTIPLIER_2, toMsFromSystemTime(0, length, 0, 0),
				"Experience x2");

		final String lengthInfo = length + (length == 1 ? " hour" : " hours");
		embed.setDescription("You have experience x2 for " + lengthInfo + "!");

		interaction.createOriginalMessageUpdater().addEmbed(embed).update();
	}

}
