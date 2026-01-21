package bot.commands;

import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;

import java.util.Random;

import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.util.apis.MessageUtils;
import bot.util.apis.commands.FlufferCommand;
import bot.util.apis.commands.FlufferCommandOption;
import bot.util.subcommand.Command;

public class CommandPp extends Command {
	private static final long dayMs = 24 * 60 * 60 * 1000;

	private final Fluffer10kFun fluffer10kFun;

	public CommandPp(final Fluffer10kFun fluffer10kFun) {
		super(fluffer10kFun.apiUtils, //
				new FlufferCommand("pp", "Measure your pp")//
						.addOption(FlufferCommandOption.string("target", "person whose pp you wanna see")));

		this.fluffer10kFun = fluffer10kFun;
	}

	private static double getSize(final String hashPart) {
		final long day = System.currentTimeMillis() / dayMs;
		return new Random((day + hashPart).hashCode()).nextDouble();
	}

	private static String getDescription(final double length) {
		if (length < 1) {
			return "is nonexistent";
		}
		if (length < 2) {
			return "is a clitty";
		}
		if (length < 5) {
			return "is a cute little boy";
		}
		if (length < 10) {
			return "is not very long";
		}
		if (length < 15) {
			return "might satisfy someone with some trying";
		}
		if (length < 20) {
			return "will satisfy someone for sure";
		}
		if (length < 25) {
			return "will make a bulge";
		}
		if (length < 30) {
			return "will make a visible bulge";
		}
		if (length < 35) {
			return "will turn hole into a gaping hole";
		}

		return "is a huge monster cock that will wreck the hole it's put into";
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {
		final Server server = interaction.getServer().orElse(null);
		if (!MessageUtils.isTextChannel(interaction)) {
			sendEphemeralMessage(interaction, "This command cannot be used here");
			return;
		}

		final String arg = interaction.getArgumentStringValueByName("target").orElse(null);

		final String name;
		if (arg != null) {
			name = arg;
		} else {
			name = fluffer10kFun.apiUtils.getUserName(interaction.getUser(), server);
		}
		final String hashPart = name;

		final double size = getSize(hashPart);
		final double ppLengthCentimetres = size * 40;

		final int cmLength = (int) ppLengthCentimetres;
		final int inchesLength = (int) (ppLengthCentimetres / 2.55);
		final String description = name + "'s pp " + getDescription(ppLengthCentimetres) + " (" + cmLength + "cm/"
				+ inchesLength + "in)";
		String pp = "8";
		for (int i = 0; i < inchesLength; i++) {
			pp += "=";
		}
		pp += "D";

		interaction.createImmediateResponder().addEmbed(makeEmbed(null, description + "\n\n" + pp)).respond();
	}
}
