package bot.commands.cookies;

import static bot.util.CollectionUtils.addToLongOnMap;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandInteractionOption;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;

import bot.Fluffer10kFun;
import bot.userData.ServerUserData;
import bot.util.apis.APIUtils;
import bot.util.subcommand.Subcommand;

public class CommandMilksGive extends Subcommand {
	private final Fluffer10kFun fluffer10kFun;

	public CommandMilksGive(final Fluffer10kFun fluffer10kFun) {
		super("give", "Give one of your milk bottles", //
				SlashCommandOption.create(SlashCommandOptionType.STRING, "milk_name", "Milk to give", true), //
				SlashCommandOption.create(SlashCommandOptionType.USER, "milk_receiver",
						"Person you want to give the bottle of milk to", true));

		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {
		final Server server = interaction.getServer().get();
		final long serverId = server.getId();
		final User giver = interaction.getUser();

		final SlashCommandInteractionOption option = getOption(interaction);
		final String milkName = option.getArgumentStringValueByName("milk_name").orElse(null);
		final User receiver = option.getArgumentUserValueByName("milk_receiver").orElse(null);

		final ServerUserData giverData = fluffer10kFun.serverUserDataUtils.getUserData(serverId, giver.getId());
		if (giverData.cookies.milkCounts.getOrDefault(milkName, 0L) == 0) {
			sendEphemeralMessage(interaction, "Couldn't find such milk in your collection");
			return;
		}

		final ServerUserData receiverData = fluffer10kFun.serverUserDataUtils.getUserData(serverId, receiver.getId());

		addToLongOnMap(giverData.cookies.milkCounts, milkName, -1);
		addToLongOnMap(receiverData.cookies.milkCounts, milkName, 1);

		final String title = APIUtils.getUserName(receiver, server) + " gets " + milkName + " from "
				+ APIUtils.getUserName(giver, server) + "!";
		final EmbedBuilder embed = makeEmbed(title)//
				.setImage(fluffer10kFun.cookieUtils.getMilkFile(milkName))//
				.setColor(CookieUtils.milkColor);

		interaction.respondLater().join().addEmbed(embed).update();
	}
}
