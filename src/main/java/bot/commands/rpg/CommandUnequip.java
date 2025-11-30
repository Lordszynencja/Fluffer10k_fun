package bot.commands.rpg;

import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import java.util.List;

import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.MessageComponentInteraction;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.data.items.ItemSlot;
import bot.userData.ServerUserData;
import bot.util.EmbedUtils.EmbedField;
import bot.util.pages.builders.PagedPickerMessageBuilder;
import bot.util.pages.messages.PagedMessage;
import bot.util.subcommand.Command;

public class CommandUnequip extends Command {

	private final Fluffer10kFun fluffer10kFun;

	public CommandUnequip(final Fluffer10kFun fluffer10kFun) {
		super(fluffer10kFun.apiUtils, "unequip", "Read about a status", false);

		this.fluffer10kFun = fluffer10kFun;
	}

	private EmbedField slotToField(final ItemSlot slot, final ServerUserData userData) {
		final String itemId = userData.rpg.eq.get(slot);
		return new EmbedField(slot.name, fluffer10kFun.items.getName(itemId));
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

		final List<ItemSlot> slots = asList(ItemSlot.values()).stream()//
				.filter(slot -> userData.rpg.eq.get(slot) != null)//
				.sorted()//
				.collect(toList());

		if (slots.isEmpty()) {
			sendEphemeralMessage(interaction, "You don't have anything equipped");
			return;
		}
		final List<EmbedField> fields = slots.stream()//
				.map(slot -> slotToField(slot, userData))//
				.collect(toList());

		final PagedMessage msg = new PagedPickerMessageBuilder<>("Choose slot to unequip", 10, fields, slots)//
				.onPick((interaction2, slot) -> handleSlotPicked(interaction2, slot, userData))//
				.build();
		fluffer10kFun.pagedMessageUtils.addMessage(msg, interaction);
	}

	private void handleSlotPicked(final MessageComponentInteraction interaction, final ItemSlot slot,
			final ServerUserData userData) {
		final String itemId = userData.rpg.eq.get(slot);
		if (itemId == null) {
			interaction.createOriginalMessageUpdater().addEmbed(makeEmbed("Slot was already empty")).update();
			return;
		}
		userData.unequip(slot);

		interaction.createOriginalMessageUpdater()
				.addEmbed(makeEmbed(
						"Unequipped " + fluffer10kFun.items.getName(itemId) + " from " + slot.name.toLowerCase()))
				.update();
	}
}
