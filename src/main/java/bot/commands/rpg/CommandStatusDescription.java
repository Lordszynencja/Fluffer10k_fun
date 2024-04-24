package bot.commands.rpg;

import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;

import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;

import bot.Fluffer10kFun;
import bot.data.fight.FighterStatus;
import bot.util.subcommand.Command;

public class CommandStatusDescription extends Command {
	public CommandStatusDescription(final Fluffer10kFun fluffer10kFun) {
		super(fluffer10kFun.apiUtils, "status_description", "Read about a fight status", //
				SlashCommandOption.create(SlashCommandOptionType.STRING, "statusName", "name of the status to look for",
						true));
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {
		final FighterStatus status = FighterStatus
				.findByName(interaction.getArgumentStringValueByName("statusName").orElse(null));
		if (status == null) {
			sendEphemeralMessage(interaction, "Status not found");
			return;
		}

		String description = status.description;

		if (status.preventsActions) {
			description += "\nPrevents all actions";
		}
		if (status.preventsItemUse) {
			description += "\nPrevents item use";
		}
		if (status.preventsDodge) {
			description += "\nPrevents dodging";
		}
		if (status.negativeExpiringFaster) {
			description += "\nExpires faster when anti-negative status effect works";
		}

		interaction.createImmediateResponder().addEmbed(makeEmbed(status.name, description)).respond();
	}
}
