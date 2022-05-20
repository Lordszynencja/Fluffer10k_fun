package bot.data.items.data;

import static bot.data.items.data.DefaultPrices.commonCraftingMaterialPrice;
import static bot.data.items.data.DefaultPrices.uncommonCraftingMaterialPrice;

import java.util.function.Consumer;

import bot.data.items.ItemBuilder;
import bot.data.items.ItemClass;

public class CraftingItems {
	public static final String EMPTY_BOOK = "EMPTY_BOOK";
	public static final String WOOD = "WOOD";

	private static class CraftingItemBuilder extends ItemBuilder {
		public CraftingItemBuilder(final String id, final String name, final String namePlural,
				final String description, final int rarity) {
			super(id, name, namePlural, description);
			rarity(rarity);
			classes(ItemClass.CRAFTING_MATERIAL);
		}
	}

	public static void addItems(final Consumer<ItemBuilder> itemAdder) {
		new CraftingItemBuilder(EMPTY_BOOK, "empty book", "empty books", //
				"An empty book to write in.", 2)//
						.price(uncommonCraftingMaterialPrice)//
						.add(itemAdder);
		new CraftingItemBuilder(WOOD, "wood", "wood", //
				"Wood from a tree.", 1)//
						.price(commonCraftingMaterialPrice)//
						.add(itemAdder);
	}
}
