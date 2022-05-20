package bot.commands.casino;

import static bot.data.items.ItemUtils.getFormattedPlayCoins;
import static bot.data.items.ItemUtils.playCoinsName;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.RandomUtils.getRandom;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;
import static java.util.Arrays.asList;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandInteractionOption;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;

import bot.Fluffer10kFun;
import bot.userData.ServerUserData;

public class CasinoRoulette {
	private static final String[] rouletteNumbers = { "00", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", //
			"10", "11", "12", "13", "14", "15", "16", "17", "18", //
			"19", "20", "21", "22", "23", "24", "25", "26", "27", //
			"28", "29", "30", "31", "32", "33", "34", "35", "36" };

	private static interface NumberCheck {
		boolean check(String n);
	}

	private static class BetData {
		public final NumberCheck check;
		public final long multiplier;

		public BetData(final NumberCheck check, final long multiplier) {
			this.check = check;
			this.multiplier = multiplier;
		}
	}

	private static final Map<String, BetData> betTypes = new HashMap<>();

	static {
		final Set<String> oddNumbers = new HashSet<>();
		for (int i = 0; i < 18; i++) {
			oddNumbers.add("" + (2 * i + 1));
		}
		betTypes.put("odd", new BetData(n -> oddNumbers.contains(n), 2));

		final Set<String> evenNumbers = new HashSet<>();
		for (int i = 0; i < 18; i++) {
			evenNumbers.add("" + (2 * i + 2));
		}
		betTypes.put("even", new BetData(n -> evenNumbers.contains(n), 2));

		final Set<String> lowNumbers = new HashSet<>();
		for (int i = 0; i < 18; i++) {
			lowNumbers.add("" + (i + 1));
		}
		betTypes.put("low", new BetData(n -> lowNumbers.contains(n), 2));

		final Set<String> highNumbers = new HashSet<>();
		for (int i = 0; i < 18; i++) {
			highNumbers.add("" + (i + 19));
		}
		betTypes.put("high", new BetData(n -> highNumbers.contains(n), 2));

		final Set<String> redNumbers = new HashSet<>(asList("1", "3", "5", "7", "9", "12", "14", "16", "18", "19", "21",
				"23", "25", "27", "30", "32", "34", "36"));
		betTypes.put("red", new BetData(n -> redNumbers.contains(n), 2));

		final Set<String> blackNumbers = new HashSet<>(asList("2", "4", "6", "8", "10", "11", "13", "15", "17", "20",
				"22", "24", "26", "28", "29", "31", "33", "35"));
		betTypes.put("black", new BetData(n -> blackNumbers.contains(n), 2));

		final String[] dozenNames = { "1st dozen", "2nd dozen", "3rd dozen" };
		for (int i = 0; i < 3; i++) {
			final Set<String> dozen = new HashSet<>();
			for (int j = 0; j < 12; j++) {
				dozen.add("" + (i * 12 + j + 1));
			}
			betTypes.put(dozenNames[i], new BetData(n -> dozen.contains(n), 3));
		}

		final Set<String> basketNumbers = new HashSet<>(asList("00", "0", "1", "2", "3"));
		betTypes.put("basket", new BetData(n -> basketNumbers.contains(n), 7));

		for (final String number : rouletteNumbers) {
			betTypes.put(number, new BetData(n -> n.equals(number), 36));
		}
	}

	private final Fluffer10kFun fluffer10kFun;

	public CasinoRoulette(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;
	}

	public static SlashCommandOption getCommandOption() {
		return SlashCommandOption.createWithOptions(SlashCommandOptionType.SUB_COMMAND, "roulette", "Try roulette",
				asList(SlashCommandOption.create(SlashCommandOptionType.STRING, "bet_type", "Bet type", true),
						SlashCommandOption.create(SlashCommandOptionType.LONG, "bet",
								"Amount of " + playCoinsName + " to bet", true)));
	}

	public void handle(final SlashCommandInteraction interaction) {
		final long serverId = interaction.getServer().get().getId();
		final long userId = interaction.getUser().getId();

		final SlashCommandInteractionOption firstOption = interaction.getOptionByName("roulette").get();
		final String betType = firstOption.getOptionStringValueByName("bet_type").get();
		if (!betTypes.containsKey(betType)) {
			sendEphemeralMessage(interaction, "Wrong bet type");
			return;
		}
		final BetData betData = betTypes.get(betType);

		final long bet = firstOption.getOptionLongValueByName("bet").get();
		if (bet < 1) {
			sendEphemeralMessage(interaction, "Can't place negative or no bet");
			return;
		}
		if (bet > CommandCasino.maxBet) {
			sendEphemeralMessage(interaction, "Max bet is " + CommandCasino.maxBet);
			return;
		}

		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(serverId, userId);
		if (userData.playCoins < bet) {
			sendEphemeralMessage(interaction, "You don't have enough " + playCoinsName);
			return;
		}

		userData.playCoins -= bet;
		fluffer10kFun.botDataUtils.getServerData(serverId).addJackpotStake(bet / 2);

		final String result = getRandom(rouletteNumbers);

		String msg = "You bet " + getFormattedPlayCoins(bet) + " on " + betType + "\n\n"//
				+ "Dealer spins the roulette, aaaand...\n" //
				+ "It lands on " + result + "\n\n";
		if (betData.check.check(result)) {
			final long prize = betData.multiplier * bet;
			userData.playCoins += prize;
			msg += "You win " + getFormattedPlayCoins(prize) + "!";
		} else {
			msg += "You lose!";
		}

		interaction.createImmediateResponder().addEmbed(makeEmbed("Roulette", msg)).respond();
	}
}
