package bot.commands.debug;

import static bot.util.CollectionUtils.addToLongOnMap;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;

import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandInteractionOption;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;

import bot.Fluffer10kFun;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.ServerData;
import bot.util.subcommand.Subcommand;

public class CommandDebugMarketAdd extends Subcommand {

	private final Fluffer10kFun fluffer10kFun;

	public CommandDebugMarketAdd(final Fluffer10kFun fluffer10kFun) {
		super("market_add", "add race to market", //
				SlashCommandOption.create(SlashCommandOptionType.STRING, "race", "race", true), //
				SlashCommandOption.create(SlashCommandOptionType.LONG, "amount", "amount", true));

		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {
		final Server server = interaction.getServer().get();
		final SlashCommandInteractionOption option = getOption(interaction);
		final String race = option.getOptionStringValueByName("race").get();
		final long amount = option.getOptionLongValueByName("amount").get();

		final MonsterGirlRace mgRace = MonsterGirlRace.valueOf(race);

		final ServerData serverData = fluffer10kFun.botDataUtils.getServerData(server);
		addToLongOnMap(serverData.monmusuMarket, mgRace, amount);
		sendEphemeralMessage(interaction, "Added " + amount + " " + mgRace.race);
	}
}
