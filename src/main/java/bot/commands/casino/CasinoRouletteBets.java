package bot.commands.casino;

import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;

import bot.util.EmbedUtils;

public class CasinoRouletteBets {

	public static SlashCommandOption getCommandOption() {
		return SlashCommandOption.create(SlashCommandOptionType.SUB_COMMAND, "roulette_bets", "Check bets on Roulette");
	}

	private static final String msg = "odd - odd numbers (2:1)\n"//
			+ "even - even numbers, excluding zeroes (2:1)\n"//
			+ "red - 1, 3, 5, 7, 9, 12, 14, 16, 18, 19, 21, 23, 25, 27, 30, 32, 34, 36 (2:1)\n"//
			+ "black - 2, 4, 6, 8, 10, 11, 13, 15, 17, 20, 22, 24, 26, 28, 29, 31, 33, 35 (2:1)\n"//
			+ "low - numbers from 1 to 18 (2:1)\n"//
			+ "high - numbers from 19 to 36 (2:1)\n"//
			+ "1st dozen - numbers from 1 to 12 (3:1)\n"//
			+ "2nd dozen - numbers from 13 to 24 (3:1)\n"//
			+ "3rd dozen - numbers from 25 to 36 (3:1)\n"//
			+ "basket - 00, 0, 1, 2, 3 (7:1)\n"//
			+ "any of the numbers on roulette wheel (00, 0-36) - exactly the number (36:1)";

	public static void handle(final SlashCommandInteraction interaction) {
		interaction.createImmediateResponder().addEmbed(EmbedUtils.makeEmbed("Roulette bets", msg)).respond();
	}
}
