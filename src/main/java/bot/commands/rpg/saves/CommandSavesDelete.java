package bot.commands.rpg.saves;

import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;

import java.util.Arrays;

import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.Button;
import org.javacord.api.entity.message.component.ButtonStyle;
import org.javacord.api.interaction.MessageComponentInteraction;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;

import bot.Fluffer10kFun;
import bot.userData.ServerUserData;
import bot.util.subcommand.Subcommand;

public class CommandSavesDelete extends Subcommand {
	private final Fluffer10kFun fluffer10kFun;

	protected CommandSavesDelete(final Fluffer10kFun fluffer10kFun) {
		super("delete", "delete a save", //
				SlashCommandOption.create(SlashCommandOptionType.STRING, "save_name", "name of save to delete", true));

		this.fluffer10kFun = fluffer10kFun;

		fluffer10kFun.apiUtils.commandHandlers.addMessageComponentHandler("saves_delete_yes", this::handleDeleteYes);
		fluffer10kFun.apiUtils.commandHandlers.addMessageComponentHandler("saves_delete_no", this::handleDeleteNo);
	}

	private void handleDeleteYes(final MessageComponentInteraction interaction) {
		final String[] tokens = interaction.getCustomId().split(" ");
		final long userId = Long.valueOf(tokens[1]);
		final long serverId = Long.valueOf(tokens[2]);
		final String saveName = String.join(" ", Arrays.copyOfRange(tokens, 3, tokens.length));

		if (userId != interaction.getUser().getId()) {
			sendEphemeralMessage(interaction, "Not your decision!");
			return;
		}

		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(serverId, userId);
		userData.saves.remove(saveName);

		interaction.createOriginalMessageUpdater().addEmbed(makeEmbed("Save " + saveName + " deleted")).update();
	}

	private void handleDeleteNo(final MessageComponentInteraction interaction) {
		final long userId = Long.valueOf(interaction.getCustomId().split(" ")[1]);
		if (interaction.getUser().getId() != userId) {
			sendEphemeralMessage(interaction, "Not your decision!");
			return;
		}

		interaction.createOriginalMessageUpdater().addEmbed(makeEmbed("Save deletion cancelled")).update();
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) throws Exception {
		final long serverId = interaction.getServer().get().getId();
		final long userId = interaction.getUser().getId();

		final String saveName = interaction.getOptionByName("delete").get().getArgumentStringValueByName("save_name")
				.get();
		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(serverId, userId);
		if (userData.saves.get(saveName) == null) {
			sendEphemeralMessage(interaction, "Save with this name doesn't exists");
			return;
		}

		final String title = "Are you sure you want to delete " + saveName + "?";
		final String description = userData.saves.get(saveName).getSaveDescription(fluffer10kFun.apiUtils,
				interaction.getUser(), interaction.getServer().get());
		interaction.createImmediateResponder().addEmbed(makeEmbed(title, description))//
				.addComponents(ActionRow.of(
						Button.create("saves_delete_yes " + userId + " " + serverId + " " + saveName,
								ButtonStyle.SUCCESS, "Yes"),
						Button.create("saves_delete_no " + userId, ButtonStyle.DANGER, "No")))
				.respond();
	}
}
