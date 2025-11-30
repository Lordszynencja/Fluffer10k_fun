package bot.commands.rpg;

import static bot.util.CollectionUtils.mapToList;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.Utils.capitalize;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;
import static java.util.stream.Collectors.toList;

import java.util.List;

import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.MessageComponentInteraction;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.data.items.ItemAmount;
import bot.userData.ServerUserData;
import bot.util.EmbedUtils.EmbedField;
import bot.util.pages.builders.PagedPickerMessageBuilder;
import bot.util.pages.messages.PagedMessage;
import bot.util.subcommand.Command;

public class CommandEquip extends Command {
	private final Fluffer10kFun fluffer10kFun;

	public CommandEquip(final Fluffer10kFun fluffer10kFun) {
		super(fluffer10kFun.apiUtils, "equip", "Equip an item", false);

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

		final List<ItemAmount> items = userData.items.entrySet().stream()//
				.filter(entry -> entry.getValue() > 0)//
				.map(entry -> fluffer10kFun.items.getItemAmount(entry.getKey(), entry.getValue()))//
				.filter(itemAmount -> itemAmount.item.canBeEquippedBy(userData))//
				.sorted()//
				.collect(toList());

		final List<EmbedField> fields = mapToList(items, item -> item.getAsFieldWithDescription());

		final PagedMessage msg = new PagedPickerMessageBuilder<>("Choose item to equip", 5, fields, items)//
				.dataToEmbed(item -> item.getDetails())//
				.onConfirm(this::handlePick)//
				.build();
		fluffer10kFun.pagedMessageUtils.addMessage(msg, interaction);
	}

	private void handlePick(final MessageComponentInteraction interaction, final int page,
			final ItemAmount itemAmount) {
		final long serverId = interaction.getServer().get().getId();
		final User user = interaction.getUser();
		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(serverId, user.getId());
		if (userData.rpg.fightId != null) {
			interaction.createOriginalMessageUpdater()
					.addEmbed(makeEmbed("Can't equip " + itemAmount.item.name, "You are in a fight!")).update();
			return;
		}

		if (!userData.hasItem(itemAmount.item)) {
			interaction.createOriginalMessageUpdater()
					.addEmbed(makeEmbed("Can't equip " + itemAmount.item.name, "You do not have the item anymore"))
					.update();
			return;
		}

		userData.equip(fluffer10kFun, itemAmount.item);

		interaction.createOriginalMessageUpdater().addEmbed(makeEmbed(capitalize(itemAmount.item.name) + " equipped"))
				.update();
	}

}
