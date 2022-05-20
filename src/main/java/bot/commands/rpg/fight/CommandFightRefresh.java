package bot.commands.rpg.fight;

import static bot.util.apis.MessageUtils.sendEphemeralMessage;

import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.data.fight.FightData;
import bot.userData.ServerUserData;
import bot.util.subcommand.Subcommand;

public class CommandFightRefresh extends Subcommand {
	private final Fluffer10kFun fluffer10kFun;

	public CommandFightRefresh(final Fluffer10kFun fluffer10kFun) {
		super("refresh", "refreshes your current fight");

		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {
		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(interaction.getServer().get(),
				interaction.getUser());
		if (userData.rpg.fightId == null) {
			sendEphemeralMessage(interaction, "You aren't in a fight");
			return;
		}
		final FightData fight = fluffer10kFun.botDataUtils.fights.get(userData.rpg.fightId);
		if (fight == null) {
			userData.rpg.fightId = null;
			sendEphemeralMessage(interaction, "Error, fight disappeared, cleared \"in fight\" status");
			return;
		}

		sendEphemeralMessage(interaction, "Fight refreshed");
		fluffer10kFun.fightActionsHandler.startFight(interaction.getChannel().get(), fight);
	}
}
