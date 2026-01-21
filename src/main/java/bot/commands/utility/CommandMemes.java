package bot.commands.utility;

import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.commands.imageManipulation.CommandMeme;
import bot.util.subcommand.Command;

public class CommandMemes extends Command {
	private static final String memesListMessage = String.join("\n", CommandMeme.getMemeDescriptions());

	public CommandMemes(final Fluffer10kFun fluffer10kFun) {
		super(fluffer10kFun.apiUtils, "memes", "List of available templates for /meme command", true);
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) throws Exception {
		interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL)
				.setContent("**Available meme templates:**\n" + memesListMessage).respond();
	}

}
