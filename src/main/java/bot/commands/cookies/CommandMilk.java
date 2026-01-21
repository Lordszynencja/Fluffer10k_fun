package bot.commands.cookies;

import static bot.util.CollectionUtils.addToLongOnMap;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.RandomUtils.getRandom;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;

import java.util.HashMap;
import java.util.Map;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.userData.ServerUserData;
import bot.util.apis.commands.FlufferCommand;
import bot.util.apis.commands.FlufferCommandOption;
import bot.util.subcommand.Command;

public class CommandMilk extends Command {

	private final Fluffer10kFun fluffer10kFun;
	private final Map<Long, Long> timeouts = new HashMap<>();

	public CommandMilk(final Fluffer10kFun fluffer10kFun) {
		super(fluffer10kFun.apiUtils, //
				new FlufferCommand("milk", "Get a bottle of milk")
						.addOption(FlufferCommandOption.user("target", "Person to get milk for")));

		this.fluffer10kFun = fluffer10kFun;
	}

	private String getTitle(final User maker, final User receiver, final Server server) {
		if (maker.getId() == receiver.getId()) {
			return "Got a bottle of milk!";
		}
		return fluffer10kFun.apiUtils.getUserName(receiver, server) + " got a bottle of milk from "
				+ fluffer10kFun.apiUtils.getUserName(maker, server) + "!";
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) throws Exception {
		final Server server = interaction.getServer().orElse(null);
		if (server == null) {
			sendEphemeralMessage(interaction, "This command is only usable on a server.");
			return;
		}

		final long serverId = server.getId();
		final User maker = interaction.getUser();
		final User receiver = interaction.getArgumentUserValueByName("target").orElse(maker);
		final String milkName = getRandom(fluffer10kFun.cookieUtils.milkTypes);
		final ServerUserData receiverData = fluffer10kFun.serverUserDataUtils.getUserData(serverId, receiver.getId());

		if (timeouts.getOrDefault(maker.getId(), 0L) > System.currentTimeMillis()) {
			sendEphemeralMessage(interaction, "You can't have another bottle of milk yet.");
			return;
		}

		timeouts.put(maker.getId(), System.currentTimeMillis() + 5 * 1000);

		addToLongOnMap(receiverData.cookies.milkCounts, milkName, 1);

		final String title = getTitle(maker, receiver, server);

		final EmbedBuilder embed = makeEmbed(title, milkName)//
				.setImage(fluffer10kFun.cookieUtils.getMilkFile(milkName))//
				.setColor(CookieUtils.milkColor);
		interaction.respondLater().join().addEmbed(embed).update();
	}
}
