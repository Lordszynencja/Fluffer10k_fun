package bot.commands.rpg.fight;

import static bot.util.apis.MessageUtils.sendEphemeralMessage;
import static java.util.Arrays.asList;

import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionChoice;
import org.javacord.api.interaction.SlashCommandOptionType;

import bot.Fluffer10kFun;
import bot.userData.UserData;
import bot.util.subcommand.Subcommand;

public class CommandFightAutoWait extends Subcommand {
	private final Fluffer10kFun fluffer10kFun;

	public CommandFightAutoWait(final Fluffer10kFun fluffer10kFun) {
		super("auto_wait", "Switch the auto wait flag", //
				SlashCommandOption.createWithChoices(SlashCommandOptionType.STRING, "value", "new flag value", true, //
						asList(SlashCommandOptionChoice.create("on", "on"), //
								SlashCommandOptionChoice.create("off", "off"))));

		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {
		final String value = interaction.getArgumentStringValueByName("value").get();

		final UserData userData = fluffer10kFun.userDataUtils.getUserData(interaction.getUser().getId());
		userData.autoWait = value.equals("on");
		sendEphemeralMessage(interaction, "Automatic waiting turned " + value);
	}
}
