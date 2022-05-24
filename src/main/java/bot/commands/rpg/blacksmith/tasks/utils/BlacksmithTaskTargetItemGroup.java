package bot.commands.rpg.blacksmith.tasks.utils;

import static bot.util.CollectionUtils.mapToList;

import java.util.ArrayList;
import java.util.List;

import org.javacord.api.interaction.MessageComponentInteraction;

import bot.Fluffer10kFun;
import bot.commands.rpg.blacksmith.CommandBlacksmith;
import bot.commands.rpg.blacksmith.blueprints.utils.ItemPayer;
import bot.commands.rpg.blacksmith.blueprints.utils.Payer;
import bot.data.items.ItemAmount;
import bot.data.items.Items;
import bot.userData.ServerUserData;
import bot.util.EmbedUtils.EmbedField;
import bot.util.pages.builders.PagedPickerMessageBuilder;
import bot.util.pages.messages.PagedMessage;
import bot.util.pages.messages.PagedPickerMessage.OnPickHandler;

public class BlacksmithTaskTargetItemGroup implements BlacksmithTaskTarget {
	private final List<String> itemIds;
	private final String name;

	public BlacksmithTaskTargetItemGroup(final List<String> itemIds, final String name) {
		this.itemIds = itemIds;
		this.name = name;
	}

	@Override
	public String taskDescription(final Items items) {
		return "collect " + name;
	}

	@Override
	public String progressDescription(final ServerUserData userData, final Items items) {
		long total = 0;
		for (final String itemId : itemIds) {
			total += userData == null ? 0 : userData.items.getOrDefault(itemId, 0L);
		}
		return name + " collected: " + (total >= 0 ? "yes" : "no");
	}

	@Override
	public boolean isPickable() {
		return true;
	}

	@Override
	public Payer getPayer() {
		return null;
	}

	@Override
	public void pick(final Fluffer10kFun fluffer10kFun, final MessageComponentInteraction in,
			final ServerUserData userData, final OnPickHandler<Payer> onPick) {
		final List<ItemAmount> items = new ArrayList<>();
		userData.items.forEach((id, amount) -> {
			if (amount >= 1 && itemIds.contains(id)) {
				items.add(fluffer10kFun.items.getItemAmount(id, amount));
			}
		});
		items.sort(null);

		final List<EmbedField> fields = mapToList(items, itemAmount -> itemAmount.getAsField());

		final PagedMessage msg = new PagedPickerMessageBuilder<>("Pick items to give", 5, fields, items)//
				.imgUrl(CommandBlacksmith.imgUrl)//
				.dataToEmbed(itemAmount -> itemAmount.getDetails())//
				.onConfirm((interaction2, page, itemAmount) -> onPick.handle(interaction2, page,
						new ItemPayer(itemAmount.item.id, 1)))//
				.build();
		fluffer10kFun.pagedMessageUtils.addMessage(msg, in);
	}
}
