package bot.commands.casino;

import static bot.util.apis.MessageUtils.sendEphemeralMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandBuilder;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandOption;

import bot.Fluffer10kFun;

public class CommandCasino {
	public static final long maxBet = 1_000_000_000_000L;

	public static interface CasinoGameHandler {
		void handle(SlashCommandInteraction interaction) throws Exception;
	}

	private final Map<String, CasinoGameHandler> handlers = new HashMap<>();
	private final CasinoBlackjack casinoBlackjack;
	private final CasinoCoinFlip casinoCoinFlip;
	private final CasinoJackpot casinoJackpot;
	private final CasinoJackpotPool casinoJackpotPool;
	private final CasinoRoulette casinoRoulette;
	private final CasinoWheelOfFortune casinoWheelOfFortune;

	public CommandCasino(final Fluffer10kFun fluffer10kFun) throws IOException {
		casinoBlackjack = new CasinoBlackjack(fluffer10kFun);
		casinoCoinFlip = new CasinoCoinFlip(fluffer10kFun);
		casinoJackpot = new CasinoJackpot(fluffer10kFun);
		casinoJackpotPool = new CasinoJackpotPool(fluffer10kFun);
		casinoRoulette = new CasinoRoulette(fluffer10kFun);
		casinoWheelOfFortune = new CasinoWheelOfFortune(fluffer10kFun);

		handlers.put("blackjack", casinoBlackjack::handle);
		handlers.put("coin_flip", casinoCoinFlip::handle);
		handlers.put("jackpot", casinoJackpot::handle);
		handlers.put("jackpot_pool", casinoJackpotPool::handle);
		handlers.put("roulette", casinoRoulette::handle);
		handlers.put("roulette_bets", CasinoRouletteBets::handle);
		handlers.put("wheel_of_fortune", casinoWheelOfFortune::handle);
		handlers.put("wheel_of_fortune_prizes", CasinoWheelOfFortunePrizes::handle);

		final List<SlashCommandOption> gameOptions = new ArrayList<>();
		gameOptions.add(CasinoBlackjack.getCommandOption());
		gameOptions.add(CasinoCoinFlip.getCommandOption());
		gameOptions.add(CasinoJackpot.getCommandOption());
		gameOptions.add(CasinoJackpotPool.getCommandOption());
		gameOptions.add(CasinoRoulette.getCommandOption());
		gameOptions.add(CasinoRouletteBets.getCommandOption());
		gameOptions.add(CasinoWheelOfFortune.getCommandOption());
		gameOptions.add(CasinoWheelOfFortunePrizes.getCommandOption());

		final SlashCommandBuilder scb = SlashCommand.with("casino", "Play in the casino", gameOptions)
				.setEnabledInDms(false);
		fluffer10kFun.apiUtils.commandHandlers.addSlashCommandHandler("casino", this::handle, scb);

		fluffer10kFun.apiUtils.commandHandlers.addMessageComponentHandler("casino_blackjack",
				casinoBlackjack::handleAction);
	}

	private void handle(final SlashCommandInteraction interaction) throws Exception {
		final Server server = interaction.getServer().orElse(null);
		if (server == null) {
			sendEphemeralMessage(interaction, "This command is only usable on a server.");
			return;
		}

		final String cmd = interaction.getOptionByIndex(0).get().getName();
		handlers.get(cmd).handle(interaction);
	}
}
