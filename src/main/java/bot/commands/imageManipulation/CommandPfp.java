package bot.commands.imageManipulation;

import org.javacord.api.entity.Icon;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;

import bot.Fluffer10kFun;
import bot.util.subcommand.Command;

public class CommandPfp extends Command {
	public CommandPfp(final Fluffer10kFun fluffer10kFun) {
		super(fluffer10kFun.apiUtils, "pfp", "check pfp", //
				SlashCommandOption.create(SlashCommandOptionType.USER, "target", "person whose pfp you wanna see"));
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {
		final User target = interaction.getOptionUserValueByName("target").orElse(interaction.getUser());
		final Icon avatar = target.getServerAvatar(interaction.getServer().orElse(null)).orElse(target.getAvatar());

		interaction.createImmediateResponder().append(avatar.getUrl()).respond();
	}

}
