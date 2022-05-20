package bot.commands.goldenCookies;

import static bot.commands.goldenCookies.GoldenCookies.getGoldenCookiesTier;
import static bot.util.RandomUtils.getRandom;
import static bot.util.RandomUtils.getRandomInt;
import static bot.util.Utils.toMsFromSystemTime;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.MessageComponentInteraction;

import bot.commands.goldenCookies.GoldenCookies.GoldenCookieEffect;
import bot.userData.ServerUserData;
import bot.userData.UserStatusesData.UserStatusType;

public class GoldenCookieSillyStatus implements GoldenCookieEffect {
	private static class SillyStatusData {
		public final UserStatusType status;
		public final String description;
		public final String statusDescription;

		public SillyStatusData(final UserStatusType status, final String description, final String statusDescription) {
			this.status = status;
			this.description = description;
			this.statusDescription = statusDescription;
		}
	}

	private static final SillyStatusData[] sillyStatuses = {
			new SillyStatusData(UserStatusType.ADORABLENESS, "You got adorableness increased by %1$d%% for %2$s!",
					"Adorableness +%1$s%%"), //
			new SillyStatusData(UserStatusType.CHARM, "You got charm increased by %1$d%% for %2$s!", "Charm +%1$s%%"), //
			new SillyStatusData(UserStatusType.COOLNESS, "You got coolness increased by %1$d%% for %2$s!",
					"Coolness +%1$s%%"), //
			new SillyStatusData(UserStatusType.CUTENESS, "You got cuteness increased by %1$d%% for %2$s!",
					"Cuteness +%1$s%%"), //
			new SillyStatusData(UserStatusType.FLUFFINESS, "You got fluffiness increased by %1$d%% for %2$s!",
					"Fluffiness +%1$s%%"), //
			new SillyStatusData(UserStatusType.HOTNESS, "You got hotness increased by %1$d%% for %2$s!",
					"Hotness +%1$s%%"), //
			new SillyStatusData(UserStatusType.PRETTINESS, "You got prettiness increased by %1$d%% for %2$s!",
					"Prettiness +%1$s%%"), //
			new SillyStatusData(UserStatusType.SEXINESS, "You got sexiness increased by %1$d%% for %2$s!",
					"Sexiness +%1$s%%") };

	@Override
	public void apply(final MessageComponentInteraction interaction, final ServerUserData userData,
			final int goldenCookiesCaught, final EmbedBuilder embed) {
		final SillyStatusData statusData = getRandom(sillyStatuses);
		final int length = getRandomInt(1, 24);
		final int power = getRandomInt(1, 100 * getGoldenCookiesTier(goldenCookiesCaught));

		userData.statuses.addStatus(statusData.status, toMsFromSystemTime(0, length, 0, 0),
				String.format(statusData.statusDescription, power));

		final String lengthInfo = length + (length == 1 ? " hour" : " hours");
		embed.setDescription(String.format(statusData.description, power, lengthInfo));

		interaction.createOriginalMessageUpdater().addEmbed(embed).update();
	}

}
