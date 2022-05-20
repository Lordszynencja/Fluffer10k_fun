package bot.commands.rpg.saves;

import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;

import java.math.BigInteger;

import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;

import bot.Fluffer10kFun;
import bot.userData.ServerUserData;
import bot.util.subcommand.Subcommand;

public class CommandSavesLoad extends Subcommand {
	private final Fluffer10kFun fluffer10kFun;

	protected CommandSavesLoad(final Fluffer10kFun fluffer10kFun) {
		super("load", "load a save (replaces current one)", //
				SlashCommandOption.create(SlashCommandOptionType.STRING, "save_name", "name of save to delete", true));

		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) throws Exception {
		final long serverId = interaction.getServer().get().getId();
		final User user = interaction.getUser();

		final String saveName = getOption(interaction).getOptionStringValueByName("save_name").get();

		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(serverId, user.getId());
		if (userData.rpg.fightId != null) {
			sendEphemeralMessage(interaction, "Can't load during a fight");
			return;
		}
		if (userData.rpg.exp.compareTo(BigInteger.ZERO) > 0) {
			sendEphemeralMessage(interaction, "Can't load while other save is on, save first");
			return;
		}
		if (userData.saves.get(saveName) == null) {
			sendEphemeralMessage(interaction, "Save with this name doesn't exists");
			return;
		}

		userData.rpg = userData.saves.remove(saveName);

		interaction.createImmediateResponder().addEmbed(makeEmbed("Loaded game from " + saveName)).respond();
	}
}
