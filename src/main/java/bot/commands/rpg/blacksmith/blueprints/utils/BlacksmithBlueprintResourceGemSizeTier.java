package bot.commands.rpg.blacksmith.blueprints.utils;

import static bot.util.CollectionUtils.mapToList;
import static bot.util.Utils.Pair.pair;
import static java.util.stream.Collectors.summingLong;

import java.util.ArrayList;
import java.util.List;

import org.javacord.api.interaction.MessageComponentInteraction;

import bot.Fluffer10kFun;
import bot.commands.rpg.blacksmith.CommandBlacksmith;
import bot.data.items.Item;
import bot.data.items.ItemAmount;
import bot.data.items.Items;
import bot.data.items.data.GemItems;
import bot.data.items.data.GemItems.GemRefinement;
import bot.data.items.data.GemItems.GemSize;
import bot.data.items.data.GemItems.GemTier;
import bot.userData.ServerUserData;
import bot.util.EmbedUtils.EmbedField;
import bot.util.pages.builders.PagedPickerMessageBuilder;
import bot.util.pages.messages.PagedMessage;
import bot.util.pages.messages.PagedPickerMessage.OnPickHandler;

public class BlacksmithBlueprintResourceGemSizeTier extends BlacksmithBlueprintResourcePickable {
	private final GemSize size;
	private final GemTier tier;
	private final long amount;

	public BlacksmithBlueprintResourceGemSizeTier(final GemSize size, final GemTier tier) {
		this(size, tier, 1);
	}

	public BlacksmithBlueprintResourceGemSizeTier(final GemSize size, final GemTier tier, final long amount) {
		this.size = size;
		this.tier = tier;
		this.amount = amount;
	}

	@Override
	public String description(final Items items, final ServerUserData userData) {
		final long userGems = userData.items.entrySet().stream()//
				.map(entry -> pair(items.getItem(entry.getKey()), entry.getValue()))//
				.filter(pair -> pair.b > 0 //
						&& pair.a.gemSize == size//
						&& pair.a.gemType.tier == tier//
						&& pair.a.gemRefinement == GemRefinement.REFINED)//
				.collect(summingLong(pair -> pair.b));
		final String neededGems = amount + " " + size.name + (amount == 1 ? " refined gem" : " refined gems");
		return userGems + "/" + neededGems;
	}

	@Override
	public boolean payable(final Items items, final ServerUserData userData) {
		final long userGems = userData.items.entrySet().stream()//
				.map(entry -> pair(items.getItem(entry.getKey()), entry.getValue()))//
				.filter(pair -> pair.b > 0//
						&& pair.a.gemSize == size//
						&& pair.a.gemType.tier == tier//
						&& pair.a.gemRefinement == GemRefinement.REFINED)//
				.collect(summingLong(pair -> pair.b));
		return userGems >= amount;
	}

	@Override
	public void pick(final Fluffer10kFun fluffer10kFun, final MessageComponentInteraction interaction,
			final ServerUserData userData, final OnPickHandler<ItemAmount> onPick) {
		final List<ItemAmount> items = new ArrayList<>();
		userData.items.forEach((id, amount) -> {
			if (amount >= this.amount) {
				final Item item = fluffer10kFun.items.getItem(id);
				if (item.gemRefinement == GemRefinement.REFINED && item.gemSize == size && item.gemType.tier == tier) {
					items.add(new ItemAmount(item, amount));
				}
			}
		});
		items.sort(null);

		final List<EmbedField> fields = mapToList(items, itemAmount -> itemAmount.getAsField());

		final PagedMessage msg = new PagedPickerMessageBuilder<>("Pick gems for crafting", 5, fields, items)//
				.imgUrl(CommandBlacksmith.imgUrl)//
				.dataToEmbed(itemAmount -> itemAmount.getDetails())//
				.onConfirm((interaction2, page, itemAmount) -> onPick.handle(interaction2, page,
						new ItemAmount(itemAmount.item, amount)))//
				.build();
		fluffer10kFun.pagedMessageUtils.addMessage(msg, interaction);
	}

	@Override
	public long value(final Items items) {
		return GemItems.getGemPrice(size, tier, GemRefinement.REFINED);
	}
}
