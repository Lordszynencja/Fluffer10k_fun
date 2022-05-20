package bot.commands.goldenCookies;

import static bot.commands.goldenCookies.GoldenCookies.getGoldenCookiesTier;
import static bot.data.items.ItemUtils.getFormattedPlayCoins;
import static bot.util.RandomUtils.getRandomLong;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.MessageComponentInteraction;

import bot.commands.goldenCookies.GoldenCookies.GoldenCookieEffect;
import bot.userData.ServerUserData;

public class GoldenCookiePlayCoins implements GoldenCookieEffect {

	private static final long[] playCoinTiers = { 50_000, 75_000, 100_000, 150_000, 200_000, 250_000 };

	@Override
	public void apply(final MessageComponentInteraction interaction, final ServerUserData userData,
			final int goldenCookiesCaught, final EmbedBuilder embed) {
		final long minAmount = playCoinTiers[getGoldenCookiesTier(goldenCookiesCaught)];
		final long amount = getRandomLong(minAmount, minAmount * 10);
		userData.playCoins += amount;

		embed.setDescription("You got " + getFormattedPlayCoins(amount) + "!");
		interaction.createOriginalMessageUpdater().addEmbed(embed).update();
	}

}
