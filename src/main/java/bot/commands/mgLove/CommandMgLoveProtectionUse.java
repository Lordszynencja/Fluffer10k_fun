package bot.commands.mgLove;

import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.apis.MessageUtils.isNSFWChannel;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;
import static java.util.Arrays.asList;

import java.io.IOException;

import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionChoice;
import org.javacord.api.interaction.SlashCommandOptionType;

import bot.Fluffer10kFun;
import bot.userData.ServerUserData;
import bot.util.subcommand.Command;

public class CommandMgLoveProtectionUse extends Command {
	private final Fluffer10kFun fluffer10kFun;

	public CommandMgLoveProtectionUse(final Fluffer10kFun fluffer10kFun) throws IOException {
		super(fluffer10kFun.apiUtils, "mg_love_protection_use",
				"Do you want to use the protection from buffs and level?", false, //
				SlashCommandOption.createWithChoices(SlashCommandOptionType.STRING, "value", "new flag value", true,
						asList(SlashCommandOptionChoice.create("on", "on"),
								SlashCommandOptionChoice.create("off", "off"))));

		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {
		final Server server = interaction.getServer().orElse(null);
		if (!isNSFWChannel(interaction) || server == null) {
			sendEphemeralMessage(interaction, "This command cannot be used here");
			return;
		}

		final boolean value = interaction.getArgumentStringValueByName("value").get().equals("on");

		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(server.getId(),
				interaction.getUser().getId());
		userData.useMgLoveProtection = value;

		interaction.createImmediateResponder()
				.addEmbed(makeEmbed("Usage of protection from monster girl love turned " + value)).respond();
	}
}
