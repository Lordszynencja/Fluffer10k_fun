package bot.commands.rpg.danuki;

import static bot.util.apis.MessageUtils.sendEphemeralMessage;

import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.userData.ServerUserData;
import bot.util.subcommand.Command;

public class CommandDanuki extends Command {
	public static final String imgUrl = MonsterGirlRace.GYOUBU_DANUKI.imageLink;

	private final Fluffer10kFun fluffer10kFun;

	public CommandDanuki(final Fluffer10kFun fluffer10kFun) {
		super(fluffer10kFun.apiUtils, "danuki", "Danuki shop", false, //
				new CommandDanukiBuy(fluffer10kFun), //
				new CommandDanukiList(fluffer10kFun), //
				new CommandDanukiSell(fluffer10kFun), //
				new CommandDanukiUnlock(fluffer10kFun));

		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) throws Exception {
		final Server server = interaction.getServer().orElse(null);
		if (server == null) {
			sendEphemeralMessage(interaction, "This command can only be used on server");
			return;
		}

		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(server, interaction.getUser());
		if (!userData.danukiShopAvailable) {
			sendEphemeralMessage(interaction, "You didn't unlock this feature yet");
			return;
		}
		if (userData.rpg.fightId != null) {
			sendEphemeralMessage(interaction, "You are in a fight!");
			return;
		}

		subcommandHandler.handle(interaction);
	}

}
