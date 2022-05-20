package bot.commands.casino;

import static bot.data.items.ItemUtils.getFormattedPlayCoins;
import static bot.util.EmbedUtils.makeEmbed;

import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;

import bot.Fluffer10kFun;

public class CasinoJackpotPool {
	private final Fluffer10kFun fluffer10kFun;

	public CasinoJackpotPool(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;
	}

	public static SlashCommandOption getCommandOption() {
		return SlashCommandOption.create(SlashCommandOptionType.SUB_COMMAND, "jackpot_pool",
				"Check the current Jackpot pool");
	}

	public void handle(final SlashCommandInteraction interaction) {
		final long serverId = interaction.getServer().get().getId();
		final long pool = fluffer10kFun.botDataUtils.getServerData(serverId).jackpotStake;

		interaction.createImmediateResponder().addEmbed(makeEmbed("Current Jackpot pool", getFormattedPlayCoins(pool)))
				.respond();
	}
}
