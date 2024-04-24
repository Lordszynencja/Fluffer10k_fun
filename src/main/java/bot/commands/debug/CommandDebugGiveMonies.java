package bot.commands.debug;

import static bot.util.apis.MessageUtils.sendEphemeralMessage;

import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandInteractionOption;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;

import bot.Fluffer10kFun;
import bot.commands.rpg.CommandTransfer.Currency;
import bot.userData.ServerUserData;
import bot.util.subcommand.Subcommand;

public class CommandDebugGiveMonies extends Subcommand {

	private final Fluffer10kFun fluffer10kFun;

	public CommandDebugGiveMonies(final Fluffer10kFun fluffer10kFun) {
		super("give_monies", "give currency", //
				SlashCommandOption.create(SlashCommandOptionType.LONG, "amount", "amount", true), //
				SlashCommandOption.create(SlashCommandOptionType.USER, "target", "target"), //
				SlashCommandOption.createWithChoices(SlashCommandOptionType.STRING, "currency",
						"currency (default monies)", false, Currency.getOptions()));

		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {
		final Server server = interaction.getServer().get();
		final SlashCommandInteractionOption option = getOption(interaction);
		final User target = option.getArgumentUserValueByName("target").orElse(interaction.getUser());
		final long amount = option.getArgumentLongValueByName("amount").get();
		final Currency currency = Currency
				.valueOf(option.getArgumentStringValueByName("currency").orElse(Currency.MONIES.name()));

		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(server.getId(), target.getId());
		currency.adder.accept(userData, amount);

		sendEphemeralMessage(interaction,
				"Gave " + currency.formatter.apply(amount) + " to " + target.getDisplayName(server));
	}
}
