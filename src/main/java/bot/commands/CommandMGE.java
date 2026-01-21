package bot.commands;

import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.RandomUtils.getRandom;
import static bot.util.Utils.fixString;
import static bot.util.apis.MessageUtils.getServerTextChannel;
import static bot.util.apis.MessageUtils.isNSFWChannel;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;
import static bot.util.apis.MessageUtils.sendMessageToUser;
import static bot.util.apis.MessageUtils.splitLongMessage;
import static bot.util.apis.commands.FlufferCommandOption.string;

import org.javacord.api.entity.channel.ChannelType;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.data.MonsterGirls;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.util.apis.commands.FlufferCommand;
import bot.util.subcommand.Command;

public class CommandMGE extends Command {

	public CommandMGE(final Fluffer10kFun fluffer10kFun) {
		super(fluffer10kFun.apiUtils, //
				new FlufferCommand("mge", "get a page from Monster Girl Encyclopedia")//
						.addOption(string("race", "Race to show page for (or type \"all\" for all options)")));
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {
		if (!isNSFWChannel(interaction) && interaction.getChannel().get().getType() != ChannelType.PRIVATE_CHANNEL) {
			sendEphemeralMessage(interaction, "This command cannot be used here");
			return;
		}

		final String arg = fixString(interaction.getArgumentStringValueByName("race").orElse(null));
		if (arg != null && arg.equals("all")) {
			sendAll(interaction);
			return;
		}

		final MonsterGirlRace race = MonsterGirls.monsterGirlRaceByRaceName.get(arg);
		sendRace(interaction, race == null ? getRandom(MonsterGirlRace.values()) : race);
	}

	private void sendAll(final SlashCommandInteraction interaction) {
		sendEphemeralMessage(interaction, "List of all monster girl races:");

		final StringBuilder s = new StringBuilder("List of Monster Girl races:");
		for (final MonsterGirlRace race : MonsterGirls.monsterGirlRaces) {
			s.append("\n" + race.race);
		}

		final boolean sendToUser = !interaction.getServer().isPresent();

		final String msg = s.toString();
		for (final String msgPart : splitLongMessage(msg)) {
			if (sendToUser) {
				sendMessageToUser(interaction.getUser(), msgPart);
			} else {
				getServerTextChannel(interaction).sendMessage(msgPart);
			}
		}
	}

	private void sendRace(final SlashCommandInteraction interaction, final MonsterGirlRace race) {
		final EmbedBuilder embed = makeEmbed(race.race, //
				race.family + " Family - " + race.type + " Type", //
				race.mgePageLink);

		interaction.createImmediateResponder().addEmbed(embed).respond();
	}
}
