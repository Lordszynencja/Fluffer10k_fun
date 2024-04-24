package bot.commands.utility;

import static bot.util.DateUtils.formatDateFromMilis;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;

import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;

import bot.Fluffer10kFun;
import bot.util.subcommand.Command;

public class CommandReminder extends Command {
	private static final String timeRegexp = "(((\\d{1,3}:)?([01]?[0-9]|2[0-3]):)?[0-5]?[0-9]:)?[0-5]?[0-9]";

	private final Fluffer10kFun fluffer10kFun;

	public CommandReminder(final Fluffer10kFun fluffer10kFun) {
		super(fluffer10kFun.apiUtils, "reminder", "Set reminder", //
				SlashCommandOption.create(SlashCommandOptionType.STRING, "time",
						"in what time you want to be reminded? Format is numbers separated by :, 999:23:59:59 max",
						true), //
				SlashCommandOption.create(SlashCommandOptionType.STRING, "message",
						"Message that will be sent as reminder", true));

		this.fluffer10kFun = fluffer10kFun;
	}

	private static long readTime(final String expression) {
		final String[] parts = expression.split(":");
		final int seconds = Integer.valueOf(parts[parts.length - 1]);

		final int minutes = parts.length >= 2 ? Integer.valueOf(parts[parts.length - 2]) : 0;
		final int hours = parts.length >= 3 ? Integer.valueOf(parts[parts.length - 3]) : 0;
		final int days = parts.length >= 4 ? Integer.valueOf(parts[parts.length - 4]) : 0;

		return ((days * 24 + hours) * 60 + minutes) * 60 + seconds;
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {
		final String timeExpression = interaction.getArgumentStringValueByName("time").get();
		if (!timeExpression.matches(timeRegexp)) {
			sendEphemeralMessage(interaction, "Wrong time expression");
			return;
		}

		final String msg = interaction.getArgumentStringValueByName("message").get();
		final long userId = interaction.getUser().getId();

		final long time = System.currentTimeMillis() + readTime(timeExpression) * 1000;
		fluffer10kFun.botDataUtils.botDataReminders.addReminder(userId, time, msg);

		sendEphemeralMessage(interaction, "Reminder will fire on " + formatDateFromMilis(time));
	}
}
