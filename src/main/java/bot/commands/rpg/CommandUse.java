package bot.commands.rpg;

import static bot.util.CollectionUtils.mapToList;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;

import java.util.ArrayList;
import java.util.List;

import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.MessageComponentInteraction;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.data.items.Item;
import bot.data.items.ItemAmount;
import bot.data.items.ItemClass;
import bot.userData.ServerUserData;
import bot.util.EmbedUtils.EmbedField;
import bot.util.pages.builders.PagedPickerMessageBuilder;
import bot.util.pages.messages.PagedMessage;
import bot.util.subcommand.Command;

public class CommandUse extends Command {
	private final Fluffer10kFun fluffer10kFun;

	public CommandUse(final Fluffer10kFun fluffer10kFun) {
		super(fluffer10kFun.apiUtils, "use", "Use an item", false);

		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {
		final Server server = interaction.getServer().orElse(null);
		if (server == null) {
			sendEphemeralMessage(interaction, "This command can only be used on server");
			return;
		}

		final long serverId = server.getId();
		final User user = interaction.getUser();
		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(serverId, user.getId());

		final List<ItemAmount> items = new ArrayList<>();
		userData.items.forEach((itemId, amount) -> {
			if (amount != 0) {
				final Item item = fluffer10kFun.items.getItem(itemId);
				if (item.classes.contains(ItemClass.USE_OUTSIDE_COMBAT)) {
					items.add(fluffer10kFun.items.getItemAmount(itemId, amount));
				}
			}
		});
		items.sort(null);

		final List<EmbedField> fields = mapToList(items, item -> item.getAsField());

		final PagedMessage msg = new PagedPickerMessageBuilder<>("Use item", 10, fields, items)//
				.dataToEmbed(itemAmount -> itemAmount.getDetails())//
				.onConfirm(this::onPick)//
				.build();
		fluffer10kFun.pagedMessageUtils.addMessage(msg, interaction);
	}

	@SuppressWarnings("unused")
	private void onPick(final MessageComponentInteraction interaction, final ItemAmount itemAmount) {
		final Server server = interaction.getServer().get();
		final User user = interaction.getUser();
		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(server.getId(), user.getId());

		interaction.createOriginalMessageUpdater().addEmbed(makeEmbed("Item " + itemAmount.item.name + " #"
				+ itemAmount.item.id + " has unknown use, contact developer because this is a bug (reward for that~)"))
				.update();
	}
}
