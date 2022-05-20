package bot.commands;

import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;

import bot.Fluffer10kFun;
import bot.util.subcommand.Command;

public class CommandOwoify extends Command {
	public CommandOwoify(final Fluffer10kFun fluffer10kFun) {
		super(fluffer10kFun.apiUtils, "owoify", "OwO", //
				SlashCommandOption.create(SlashCommandOptionType.STRING, "text", "not vewy OwO text", true));
	}

	private static final String[][] replacements = { { "r", "w" }, { "l", "w" }, //
			{ "owo", "OwO" }, { " wo", " OwO" }, { "ow ", "OwO " }, //
			{ "uwu", "UwU" }, { " wu", " UwU" }, { "uw ", "UwU " } };

	public void handle(final SlashCommandInteraction interaction) {
		String msg = " " + interaction.getOptionStringValueByName("text").get() + " ";
		for (final String[] replacement : replacements) {
			msg = msg.replaceAll(replacement[0], replacement[1]);
		}

		interaction.createImmediateResponder().append(msg.trim()).respond();
	}
}
