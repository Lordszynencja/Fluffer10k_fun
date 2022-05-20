package bot.commands.goldenCookies;

import static bot.commands.goldenCookies.GoldenCookies.getGoldenCookiesTier;
import static bot.util.RandomUtils.getRandomLong;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.MessageComponentInteraction;

import bot.commands.goldenCookies.GoldenCookies.GoldenCookieEffect;
import bot.userData.ServerUserData;

public class GoldenCookieHaremHappiness implements GoldenCookieEffect {

	private static final long[] affectionBonusTiers = { 1, 2, 3, 5, 10, 20 };

	@Override
	public void apply(final MessageComponentInteraction interaction, final ServerUserData userData,
			final int goldenCookiesCaught, final EmbedBuilder embed) {
		final long amount = getRandomLong(1, affectionBonusTiers[getGoldenCookiesTier(goldenCookiesCaught)]);
		userData.harem.values().forEach(haremMember -> haremMember.addAffection(amount));

		embed.setDescription("Your harem got happier!");
		interaction.createOriginalMessageUpdater().addEmbed(embed).update();
	}

}
