package bot.commands.rpg.blacksmith.blueprints.utils;

import java.util.List;
import java.util.function.BiConsumer;

import bot.Fluffer10kFun;
import bot.data.items.Item;
import bot.data.items.ItemBuilder;

public interface ItemGenerator {
	public static ItemGenerator normalItem(final String itemId) {
		return (fluffer10kFun, pickedItemsUsed) -> fluffer10kFun.items.getItem(itemId);
	}

	public static ItemGenerator specialItem(final String baseItemId,
			final BiConsumer<ItemBuilder, List<Item>> itemDecorator) {
		return (fluffer10kFun, pickedItemsUsed) -> {
			final ItemBuilder builder = fluffer10kFun.items.getItem(baseItemId).builder();
			itemDecorator.accept(builder, pickedItemsUsed);
			final Item item = fluffer10kFun.botDataUtils.addItem(builder);
			fluffer10kFun.items.addItem(item);
			return item;
		};
	}

	public Item generate(Fluffer10kFun fluffer10kFun, List<Item> pickedItemsUsed);
}
