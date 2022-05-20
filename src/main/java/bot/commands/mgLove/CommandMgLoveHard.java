package bot.commands.mgLove;

import static bot.data.items.ItemUtils.getFormattedPlayCoins;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.apis.MessageUtils.isNSFWChannel;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;

import java.io.IOException;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;
import org.javacord.api.interaction.callback.InteractionImmediateResponseBuilder;

import bot.Fluffer10kFun;
import bot.userData.ServerUserData;
import bot.util.subcommand.Command;

public class CommandMgLoveHard extends Command {
	private static final long price = 50_000;

	private final Fluffer10kFun fluffer10kFun;

	public CommandMgLoveHard(final Fluffer10kFun fluffer10kFun) throws IOException {
		super(fluffer10kFun.apiUtils, "mg_love_hard", "Lots of segs", //
				SlashCommandOption.create(SlashCommandOptionType.USER, "target", "target of your love", true));

		this.fluffer10kFun = fluffer10kFun;
	}

	private EmbedBuilder getEmbed(final MgLoveData mgloveData, final Server server) {
		if (mgloveData.protectedFromLove) {
			return makeEmbed(mgloveData.target.getDisplayName(server) + " is protected from lewd love!");
		}
		if (mgloveData.savedFromLove) {
			return makeEmbed(mgloveData.target.getDisplayName(server) + " is saved from lewd love!");
		}

		return fluffer10kFun.commandMgLove.makeMgLoveEmbed(server, mgloveData.target, mgloveData.cums);
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {
		final Server server = interaction.getServer().orElse(null);
		if (!isNSFWChannel(interaction) || server == null) {
			sendEphemeralMessage(interaction, "This command cannot be used here");
			return;
		}

		final User target = interaction.getOptionUserValueByName("target").get();
		final User user = interaction.getUser();

		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(server.getId(), user.getId());
		if (userData.playCoins < price) {
			sendEphemeralMessage(interaction,
					"You don't have enough money for that (need " + getFormattedPlayCoins(price) + ")");
			return;
		}
		userData.playCoins -= price;

		final InteractionImmediateResponseBuilder responder = interaction.createImmediateResponder()
				.append(target.getMentionTag());
		for (int i = 0; i < 10; i++) {
			final MgLoveData mgFuckData = fluffer10kFun.commandMgLove.calculateMgLove(target, user, server);
			responder.addEmbed(getEmbed(mgFuckData, server));
		}
		responder.respond();
	}

}
