package bot.commands.goldenCookies;

import static bot.commands.goldenCookies.GoldenCookies.getGoldenCookiesTier;
import static bot.data.items.ItemUtils.getFormattedMonies;
import static bot.util.RandomUtils.getRandomLong;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.MessageComponentInteraction;

import bot.commands.goldenCookies.GoldenCookies.GoldenCookieEffect;
import bot.userData.ServerUserData;

public class GoldenCookieGold implements GoldenCookieEffect {

	private static final long[] goldTiers = { 10, 25, 50, 100, 250, 500 };

	@Override
	public void apply(final MessageComponentInteraction interaction, final ServerUserData userData,
			final int goldenCookiesCaught, final EmbedBuilder embed) {
		final long minAmount = goldTiers[getGoldenCookiesTier(goldenCookiesCaught)];
		final long amount = getRandomLong(minAmount, minAmount * 10);
		userData.monies += amount;

		embed.setDescription("You got " + getFormattedMonies(amount) + "!");
		interaction.createOriginalMessageUpdater().addEmbed(embed).update();
	}

}
