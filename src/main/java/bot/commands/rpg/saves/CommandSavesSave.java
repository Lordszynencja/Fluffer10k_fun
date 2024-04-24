package bot.commands.rpg.saves;

import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;

import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;

import bot.Fluffer10kFun;
import bot.userData.ServerUserData;
import bot.userData.rpg.UserRPGData;
import bot.util.subcommand.Subcommand;

public class CommandSavesSave extends Subcommand {
	public static final int maxSaves = 20;

	private final Fluffer10kFun fluffer10kFun;

	protected CommandSavesSave(final Fluffer10kFun fluffer10kFun) {
		super("save", "save your data", //
				SlashCommandOption.create(SlashCommandOptionType.STRING, "save_name", "name of save to delete", true));

		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) throws Exception {
		final long serverId = interaction.getServer().get().getId();
		final User user = interaction.getUser();

		final String saveName = getOption(interaction).getArgumentStringValueByName("save_name").get();
		if (!saveName.matches("[0-9a-zA-Z \\-_]+")) {
			sendEphemeralMessage(interaction,
					"Wrong save name, it can only contain letters, numbers, spaces, dashes and underscores");
			return;
		}

		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(serverId, user.getId());
		if (userData.rpg.fightId != null) {
			sendEphemeralMessage(interaction, "Can't save during a fight");
			return;
		}
		if (userData.saves.get(saveName) != null) {
			sendEphemeralMessage(interaction, "Save with this name already exists");
			return;
		}
		if (userData.saves.size() >= maxSaves) {
			sendEphemeralMessage(interaction,
					"You have maximum amount of saves (" + maxSaves + "), delete one of them first");
			return;
		}

		userData.saves.put(saveName, userData.rpg);
		userData.rpg = new UserRPGData();

		interaction.createImmediateResponder().addEmbed(makeEmbed("Saved game to " + saveName)).respond();
	}

}
