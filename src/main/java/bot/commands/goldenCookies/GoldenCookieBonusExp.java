package bot.commands.goldenCookies;

import static bot.commands.goldenCookies.GoldenCookies.getGoldenCookiesTier;
import static bot.util.RandomUtils.getRandomBigInteger;
import static java.lang.Math.pow;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.MessageComponentInteraction;
import org.javacord.api.interaction.callback.ComponentInteractionOriginalMessageUpdater;

import bot.Fluffer10kFun;
import bot.commands.goldenCookies.GoldenCookies.GoldenCookieEffect;
import bot.userData.ServerUserData;

public class GoldenCookieBonusExp implements GoldenCookieEffect {

	private static final long[] baseExpMultiplierPerTier = { 5, 7, 10, 15, 20, 25 };

	private static BigInteger getRandomExp(int level, final int tier) {
		final long baseExpMult = baseExpMultiplierPerTier[tier];

		double multiplier = 1;
		if (level > 30) {
			multiplier *= pow(1.05, level - 30);
			level = 30;
		}
		if (level > 20) {
			multiplier *= pow(1.1, level - 20);
			level = 20;
		}
		if (level > 10) {
			multiplier *= pow(1.2, level - 10);
			level = 10;
		}
		if (level > 0) {
			multiplier *= pow(1.25, level);
		}

		final BigInteger minExp = new BigDecimal(baseExpMult * multiplier).toBigInteger();
		final BigInteger maxExp = minExp.multiply(new BigInteger("2"));

		return getRandomBigInteger(minExp, maxExp);
	}

	private final Fluffer10kFun fluffer10kFun;

	public GoldenCookieBonusExp(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void apply(final MessageComponentInteraction interaction, final ServerUserData userData,
			final int goldenCookiesCaught, final EmbedBuilder embed) {
		embed.setDescription("You got bonus experience!");

		final BigInteger exp = getRandomExp(userData.rpg.level, getGoldenCookiesTier(goldenCookiesCaught));
		final EmbedBuilder embedExp = userData.addExpAndMakeEmbed(fluffer10kFun.apiUtils, exp, interaction.getUser(),
				interaction.getServer().get());

		final ComponentInteractionOriginalMessageUpdater updater = interaction.createOriginalMessageUpdater()
				.addEmbed(embed);
		if (embedExp != null) {
			updater.addEmbed(embedExp);
		}
		updater.update();
	}

}
