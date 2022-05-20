package bot.commands.casino;

import static bot.data.items.ItemUtils.playCoinName;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.RandomUtils.getRandomBoolean;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;

import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;

import bot.Fluffer10kFun;
import bot.userData.ServerUserData;

public class CasinoCoinFlip {
	private final Fluffer10kFun fluffer10kFun;

	public CasinoCoinFlip(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;
	}

	public static SlashCommandOption getCommandOption() {
		return SlashCommandOption.create(SlashCommandOptionType.SUB_COMMAND, "coin_flip", "Flip a coin");
	}

	public void handle(final SlashCommandInteraction interaction) {
		final long serverId = interaction.getServer().get().getId();
		final long userId = interaction.getUser().getId();
		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(serverId, userId);

		if (userData.playCoins < 1) {
			sendEphemeralMessage(interaction, "You don't have any " + playCoinName + " to flip.");
			return;
		}

		userData.playCoins -= 1;
		final boolean won = getRandomBoolean();

		final String msg;
		if (won) {
			userData.playCoins += 2;
			msg = "You won a " + playCoinName + "!";
		} else {
			fluffer10kFun.botDataUtils.getServerData(serverId).addJackpotStake(1);
			msg = "You lost a " + playCoinName + "!";
		}
		interaction.createImmediateResponder().addEmbed(makeEmbed(msg)).respond();
	}
}
