package bot.commands.rpg;

import static bot.data.items.ItemUtils.formatNumber;
import static bot.data.items.ItemUtils.playCoinsName;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.Utils.capitalize;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;

import java.io.IOException;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.userData.ServerUserData;
import bot.util.subcommand.Command;

public class CommandBalance extends Command {
	private final Fluffer10kFun fluffer10kFun;

	public CommandBalance(final Fluffer10kFun fluffer10kFun) throws IOException {
		super(fluffer10kFun.apiUtils, "balance", "Check your wallet", false);

		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {
		final Server server = interaction.getServer().orElse(null);
		if (server == null) {
			sendEphemeralMessage(interaction, "This command can only be used on server");
			return;
		}

		final long serverId = server.getId();
		final User user = interaction.getUser();

		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(serverId, user.getId());

		final EmbedBuilder embed = makeEmbed(fluffer10kFun.apiUtils.getUserName(user, server) + "'s balance")//
				.addField("Gold coins", formatNumber(userData.monies))//
				.addField(capitalize(playCoinsName), formatNumber(userData.playCoins));

		if (userData.danukiShopAvailable) {
			embed.addField("Monster Gold", formatNumber(userData.monsterGold));
		}

		interaction.createImmediateResponder().addEmbed(embed).respond();
	}
}
