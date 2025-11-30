package bot.commands;

import static bot.data.items.ItemUtils.getFormattedPlayCoins;
import static bot.util.TimerUtils.startRepeatedTimedEvent;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;

import java.io.IOException;

import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.userData.ServerUserData;
import bot.util.subcommand.Command;

public class CommandDaily extends Command {
	private final Fluffer10kFun fluffer10kFun;

	private void refreshDailies() {
		try {
			fluffer10kFun.serverUserDataUtils.onEveryUser((serverId, userId, userData) -> {
				if (userData.dailyUsed) {
					userData.dailyBonus += 50_000;
					userData.dailyUsed = false;
				} else {
					userData.dailyBonus = 0;
				}
			});
		} catch (final Exception e) {
			fluffer10kFun.apiUtils.messageUtils.sendExceptionToMe(e);
		}
	}

	public CommandDaily(final Fluffer10kFun fluffer10kFun) throws IOException {
		super(fluffer10kFun.apiUtils, "daily", "Get some pocket money", false);

		this.fluffer10kFun = fluffer10kFun;

		startRepeatedTimedEvent(this::refreshDailies, 24 * 60 * 60, 24 * 60 * 60, "for daily refresh");
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {
		final Server server = interaction.getServer().orElse(null);
		if (server == null) {
			sendEphemeralMessage(interaction, "This command can only be used on server");
			return;
		}

		final long serverId = server.getId();
		final long userId = interaction.getUser().getId();

		final ServerUserData serverUserData = fluffer10kFun.serverUserDataUtils.getUserData(serverId, userId);

		final long amount = 1_000_000 + serverUserData.dailyBonus;
		final String amountString = getFormattedPlayCoins(amount);
		if (serverUserData.dailyUsed) {
			interaction.createImmediateResponder().append("You get " + amountString + "... NOT! hahahahaha!").respond();
			return;
		}

		serverUserData.dailyUsed = true;
		serverUserData.playCoins += amount;
		interaction.createImmediateResponder().append("You get " + amountString + ".").respond();
	}
}
