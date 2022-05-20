package bot.commands.casino;

import static bot.data.items.ItemUtils.getFormattedPlayCoins;
import static bot.data.items.ItemUtils.playCoinsName;
import static bot.data.items.loot.LootTable.weightedI;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.Utils.Pair.pair;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;
import static java.util.Arrays.asList;

import java.util.List;

import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;

import bot.Fluffer10kFun;
import bot.data.items.loot.LootTable;
import bot.userData.ServerUserData;
import bot.util.Utils.Pair;

public class CasinoWheelOfFortune {
	private static final long ticketPrice = 177_777;

	public static final List<Pair<Integer, Long>> prizes = asList(//
			pair(1, 0L), //
			pair(2, 500L), //
			pair(3, 1_000L), //
			pair(5, 2_500L), //
			pair(10, 5_000L), //
			pair(20, 10_000L), //
			pair(50, 25_000L), //
			pair(100, 50_000L), //
			pair(50, 100_000L), //
			pair(20, 250_000L), //
			pair(10, 500_000L), //
			pair(5, 1_000_000L), //
			pair(3, 2_500_000L), //
			pair(2, 5_000_000L), //
			pair(1, 10_000_000L));

	private static double ratio() {
		long totalChances = 0;
		long totalWins = 0;
		for (final Pair<Integer, Long> p : prizes) {
			totalChances += p.a;
			totalWins += p.a * p.b;
		}

		return 1.0 * totalWins / totalChances / ticketPrice;
	}

	public static void main(final String[] args) {
		System.out.println("Average return ratio: " + ratio());
	}

	public static final LootTable<Long> prizesLootTable = weightedI(prizes);

	public static SlashCommandOption getCommandOption() {
		return SlashCommandOption.create(SlashCommandOptionType.SUB_COMMAND, "wheel_of_fortune",
				"Spin a wheel of fortune (" + getFormattedPlayCoins(ticketPrice) + ")");
	}

	private final Fluffer10kFun fluffer10kFun;

	public CasinoWheelOfFortune(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;
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
		fluffer10kFun.botDataUtils.getServerData(serverId).addJackpotStake(ticketPrice / 2);
		final long prize = prizesLootTable.getItem();
		userData.playCoins += prize;

		final String msg = "You put a token in the Wheel of Fortune machine, aaaand...\n\n"//
				+ "You win " + getFormattedPlayCoins(prize) + "!";

		interaction.createImmediateResponder().addEmbed(makeEmbed("Wheel of Fortune", msg)).respond();
	}
}
