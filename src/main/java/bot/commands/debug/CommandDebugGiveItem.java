package bot.commands.debug;

import static bot.util.apis.MessageUtils.sendEphemeralMessage;

import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandInteractionOption;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;

import bot.Fluffer10kFun;
import bot.userData.ServerUserData;
import bot.util.subcommand.Subcommand;

public class CommandDebugGiveItem extends Subcommand {

	private final Fluffer10kFun fluffer10kFun;

	public CommandDebugGiveItem(final Fluffer10kFun fluffer10kFun) {
		super("give_item", "gives an item", //
				SlashCommandOption.create(SlashCommandOptionType.STRING, "item_id", "item id", true), //
				SlashCommandOption.create(SlashCommandOptionType.LONG, "amount", "amount"), //
				SlashCommandOption.create(SlashCommandOptionType.USER, "user", "user"));

		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {
		final Server server = interaction.getServer().get();
		final SlashCommandInteractionOption option = getOption(interaction);
		final String itemId = option.getOptionStringValueByName("item_id").get();
		final long amount = option.getOptionLongValueByName("amount").orElse(1L);
		final User user = option.getOptionUserValueByName("user").orElse(interaction.getUser());

		if (!fluffer10kFun.items.items.containsKey(itemId)) {
			System.out.println(itemId);
			sendEphemeralMessage(interaction, "Wrong item id");
			return;
		}

		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(server, user);
		userData.addItem(itemId, amount);

		sendEphemeralMessage(interaction, "You obtained " + fluffer10kFun.items.getName(itemId, amount));
	}
}
