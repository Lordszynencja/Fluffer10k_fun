package bot.commands.races;

import static bot.util.EmbedUtils.makeEmbed;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.userData.ServerUserData;
import bot.userData.UserRaceSponsorData;
import bot.util.subcommand.Subcommand;

public class CommandRaceSponsor extends Subcommand {
	private final Fluffer10kFun fluffer10kFun;

	protected CommandRaceSponsor(final Fluffer10kFun fluffer10kFun) {
		super("sponsor", "Check your race sponsor");

		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) throws Exception {
		final long serverId = interaction.getServer().get().getId();
		final long userId = interaction.getUser().getId();

		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(serverId, userId);

		final UserRaceSponsorData sponsor = userData.raceSponsor;
		if (sponsor.sponsor == null) {
			interaction.createImmediateResponder().addEmbed(makeEmbed("You have no sponsor")).respond();
			return;
		}

		final EmbedBuilder embed = makeEmbed("Your sponsor is " + sponsor.sponsor.name,
				"Your current bonus is " + sponsor.bonus)//
						.setColor(sponsor.sponsor.color);
		interaction.createImmediateResponder().addEmbed(embed).respond();
	}

}
