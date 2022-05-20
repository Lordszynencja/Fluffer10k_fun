package bot.commands.cookies;

import static bot.util.EmbedUtils.makeEmbed;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.userData.ServerUserData;
import bot.util.subcommand.Subcommand;

public class CommandCookiesCrumbles extends Subcommand {
	private final Fluffer10kFun fluffer10kFun;

	public CommandCookiesCrumbles(final Fluffer10kFun fluffer10kFun) {
		super("crumbles", "Check how many times you crumbled your cookies");

		this.fluffer10kFun = fluffer10kFun;
	}

	public void handle(final SlashCommandInteraction interaction) {
		final long serverId = interaction.getServer().get().getId();
		final long userId = interaction.getUser().getId();

		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(serverId, userId);
		final long crumbles = userData.cookies.crumbles;

		EmbedBuilder embed;
		if (crumbles == 0) {
			embed = makeEmbed("You didn't crumble cookies yet");
		} else if (crumbles == 1) {
			embed = makeEmbed("You crumbled cookies one time");
		} else {
			embed = makeEmbed("You crumbled cookies " + crumbles + " times");
		}
		embed.setColor(CookieUtils.cookiesColor)//
				.setImage(fluffer10kFun.cookieUtils.getCookieFile("Cookie crumbs"));

		interaction.respondLater().join().addEmbed(embed).update();
	}
}
