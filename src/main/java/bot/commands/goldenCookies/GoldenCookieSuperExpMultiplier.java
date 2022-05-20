package bot.commands.goldenCookies;

import static bot.util.Utils.toMsFromSystemTime;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.MessageComponentInteraction;

import bot.commands.goldenCookies.GoldenCookies.GoldenCookieEffect;
import bot.userData.ServerUserData;
import bot.userData.UserStatusesData.UserStatusType;

public class GoldenCookieSuperExpMultiplier implements GoldenCookieEffect {

	@Override
	public void apply(final MessageComponentInteraction interaction, final ServerUserData userData,
			final int goldenCookiesCaught, final EmbedBuilder embed) {
		userData.statuses.addStatus(UserStatusType.SUPER_EXP_MULTIPLIER, toMsFromSystemTime(0, 0, 5, 0),
				"Experience x7");

		embed.setDescription("You have x7 experience for 5 minutes!");
		interaction.createOriginalMessageUpdater().addEmbed(embed).update();
	}

}
