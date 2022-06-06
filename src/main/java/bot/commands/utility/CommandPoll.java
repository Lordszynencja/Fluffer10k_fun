package bot.commands.utility;

import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.util.subcommand.Command;

public class CommandPoll extends Command {
	public class Poll {

	}

	private final Fluffer10kFun fluffer10kFun;

	public CommandPoll(final Fluffer10kFun fluffer10kFun) {
		super(fluffer10kFun.apiUtils, "poll", "Make a poll");

		this.fluffer10kFun = fluffer10kFun;
	}

	private void makePollMessage() {

	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {

	}
	// TODO buttons:
	// title, description, time
	// option 1
	// option 2
	// add option (max 5), remove option (min 2)
	// create

	// TODO title uses /answer to change title
	// TODO description uses /answer to change description
	// TODO time uses /answer to change time
	// TODO option X uses /answer to change option X
	// TODO add option uses /answer to add new option
	// TODO create sends working poll

}
