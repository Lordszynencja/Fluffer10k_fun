package bot.commands.utility;

import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.summingInt;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;

import bot.Fluffer10kFun;
import bot.util.subcommand.Command;

public class CommandRoll extends Command {
	private static final String numberRegex = "[0-9]{1,2}";

	private static final Random r = new Random();

	private static class DiceThrowResult {
		public final String description;
		public final int total;

		public DiceThrowResult(final String description, final int total) {
			this.description = description;
			this.total = total;
		}
	}

	private static class Bonus {
		private static String regex = "\\+" + numberRegex;

		public static boolean applicable(final String token) {
			return token.matches(regex);
		}

		public static DiceThrowResult getResult(final String token) {
			final int bonus = Integer.valueOf(token.substring(1));

			return new DiceThrowResult("+" + bonus + " - " + bonus, bonus);
		}
	}

	private static class SingleDice {
		private static String regex = "d" + numberRegex;

		public static boolean applicable(final String token) {
			return token.matches(regex);
		}

		public static DiceThrowResult getResult(final String token) {
			final int sides = Integer.valueOf(token.substring(1));

			final int throwResult = r.nextInt(sides) + 1;
			return new DiceThrowResult("d" + sides + " - " + throwResult, throwResult);
		}
	}

	private static class MultipleDice {
		private static String regex = numberRegex + "d" + numberRegex;

		public static boolean applicable(final String token) {
			return token.matches(regex);
		}

		public static DiceThrowResult getResult(final String token) {
			final String[] parts = token.split("d");
			final int dices = Integer.valueOf(parts[0]);
			final int sides = Integer.valueOf(parts[1]);

			final String[] results = new String[dices];
			int total = 0;
			for (int i = 0; i < dices; i++) {
				final int throwResult = r.nextInt(sides) + 1;
				results[i] = throwResult + "";
				total += throwResult;
			}

			final String description = dices + "d" + sides + " - " + String.join(", ", results) + " (" + total + ")";
			return new DiceThrowResult(description, total);
		}
	}

	public CommandRoll(final Fluffer10kFun fluffer10kFun) {
		super(fluffer10kFun.apiUtils, "roll", "roll some dice", //
				SlashCommandOption.create(SlashCommandOptionType.STRING, "dice_expression",
						"expression that contains XdX, dX or +X", true));
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {
		final String dice_expression = interaction.getOptionStringValueByName("dice_expression").orElse(null);
		if (dice_expression == null) {
			sendEphemeralMessage(interaction, "Argument is required");
			return;
		}

		final List<DiceThrowResult> dicesToThrow = new ArrayList<>();

		for (final String token : dice_expression.split(" ")) {
			if (Bonus.applicable(token)) {
				dicesToThrow.add(Bonus.getResult(token));
			} else if (SingleDice.applicable(token)) {
				dicesToThrow.add(SingleDice.getResult(token));
			} else if (MultipleDice.applicable(token)) {
				dicesToThrow.add(MultipleDice.getResult(token));
			}
		}

		if (dicesToThrow.isEmpty()) {
			interaction.createImmediateResponder().addEmbed(new EmbedBuilder().setTitle("Nothing to calculate"))
					.respond();
			return;
		}

		final String description = dicesToThrow.stream().map(result -> result.description).collect(joining("\n"));
		final int total = dicesToThrow.stream().map(result -> result.total).collect(summingInt(i -> i));

		final EmbedBuilder embed = makeEmbed("Dice roll result", description)//
				.addField("Total", total + "");

		interaction.createImmediateResponder().addEmbed(embed).respond();
	}
}
