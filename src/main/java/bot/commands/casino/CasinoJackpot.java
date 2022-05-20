package bot.commands.casino;

import static bot.data.items.ItemUtils.formatNumber;
import static bot.data.items.ItemUtils.playCoinsName;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.RandomUtils.getRandomBoolean;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;

import bot.Fluffer10kFun;
import bot.data.ServerData;
import bot.userData.ServerUserData;

public class CasinoJackpot {
	public static final long ticketPrice = 2000;
	public static final long maxStake = ticketPrice * 1_000_000_000_000L;

	private static double getWinChance(final long amount) {
		return Math.log(amount) / 200;
	}

	private final Fluffer10kFun fluffer10kFun;

	public CasinoJackpot(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;
	}

	public static SlashCommandOption getCommandOption() {
		return SlashCommandOption.create(SlashCommandOptionType.SUB_COMMAND, "jackpot",
				"Try the Jackpot (" + ticketPrice + " PC)");
	}

	public void handle(final SlashCommandInteraction interaction) {
		final long serverId = interaction.getServer().get().getId();
		final long userId = interaction.getUser().getId();
		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(serverId, userId);

		if (userData.playCoins < ticketPrice) {
			sendEphemeralMessage(interaction,
					"You don't have enough " + playCoinsName + " (need " + ticketPrice + " to play).");
			return;
		}

		userData.playCoins -= ticketPrice;
		final ServerData serverData = fluffer10kFun.botDataUtils.getServerData(serverId);
		serverData.addJackpotStake(ticketPrice - 1);
		final boolean won = getRandomBoolean(getWinChance(serverData.jackpotStake));
		if (!won) {
			sendEphemeralMessage(interaction, "You put a token in the Jackpot machine, aaaand...\n\nYou lose...");
			return;
		}

		final long prize = serverData.jackpotStake;
		userData.playCoins += prize;
		serverData.jackpotStake = 0;

		final EmbedBuilder embed = makeEmbed("You put a token in the Jackpot machine, aaaand...",
				"YOU WIN " + formatNumber(prize) + " " + playCoinsName.toUpperCase() + "!!!");
		interaction.createImmediateResponder().addEmbed(embed).respond();
	}
}
