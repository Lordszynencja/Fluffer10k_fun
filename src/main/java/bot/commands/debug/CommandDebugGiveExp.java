package bot.commands.debug;

import java.math.BigInteger;

import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandInteractionOption;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;

import bot.Fluffer10kFun;
import bot.userData.ServerUserData;
import bot.util.subcommand.Subcommand;

public class CommandDebugGiveExp extends Subcommand {

	private final Fluffer10kFun fluffer10kFun;

	public CommandDebugGiveExp(final Fluffer10kFun fluffer10kFun) {
		super("give_exp", "give exp", //
				SlashCommandOption.create(SlashCommandOptionType.STRING, "amount", "amount", true), //
				SlashCommandOption.create(SlashCommandOptionType.USER, "target", "target (default you)"));

		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {
		final Server server = interaction.getServer().get();
		final SlashCommandInteractionOption option = getOption(interaction);
		final User target = option.getArgumentUserValueByName("target").orElse(interaction.getUser());
		final BigInteger amount = new BigInteger(option.getArgumentStringValueByName("amount").get());

		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(server.getId(), target.getId());
		interaction.createImmediateResponder().addEmbed(userData.addExpAndMakeEmbed(amount, target, server)).respond();
	}
}
