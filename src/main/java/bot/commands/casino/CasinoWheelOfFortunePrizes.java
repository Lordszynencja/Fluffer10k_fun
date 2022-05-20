package bot.commands.casino;

import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;

import bot.util.EmbedUtils;
import bot.util.Utils.Pair;

public class CasinoWheelOfFortunePrizes {

	public static SlashCommandOption getCommandOption() {
		return SlashCommandOption.create(SlashCommandOptionType.SUB_COMMAND, "wheel_of_fortune_prizes",
				"Check prizes on Wheel of Fortune");
	}

	public static void handle(final SlashCommandInteraction interaction) {
		final StringBuilder prizesBuilder = new StringBuilder();
		for (final Pair<Integer, Long> prize : CasinoWheelOfFortune.prizes) {
			prizesBuilder.append("weight: " + prize.a + ", reward: " + prize.b + "\n");
		}

		interaction.createImmediateResponder()
				.addEmbed(EmbedUtils.makeEmbed("Wheel of Fortune prizes", prizesBuilder.toString())).respond();
	}
}
