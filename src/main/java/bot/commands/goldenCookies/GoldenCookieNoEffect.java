package bot.commands.goldenCookies;

import static bot.util.RandomUtils.getRandom;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.MessageComponentInteraction;

import bot.commands.goldenCookies.GoldenCookies.GoldenCookieEffect;
import bot.userData.ServerUserData;

public class GoldenCookieNoEffect implements GoldenCookieEffect {

	private static final String[] descriptions = {
			"However, it seems to just be painted in gold as you grab it and the paint rubs onto your fingers. Fool's Gold, I tell you.", //
			"Just as you're about to take a bite, a Wurm steals it before digging straight into the ground!", //
			"Just as you're about to take a bite, you drop it into some kitsune's fluff. Guess you're not getting that back again...", //
			"Just as you're about to take a bite, a dog steals it thinking it's a frisbee! Talk about barking up the wrong tree..." };

	@Override
	public void apply(final MessageComponentInteraction interaction, final ServerUserData userData,
			final int goldenCookiesCaught, final EmbedBuilder embed) {
		embed.setDescription(getRandom(descriptions));
		interaction.createOriginalMessageUpdater().addEmbed(embed).update();
	}

}
