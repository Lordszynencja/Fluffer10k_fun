package bot.data.items.data;

import static bot.data.items.data.DefaultPrices.commonWeaponPrice;
import static bot.data.items.data.DefaultPrices.rareWeaponPrice;
import static bot.data.items.data.DefaultPrices.uncommonWeaponPrice;

import java.util.function.Consumer;

import bot.data.items.ItemBuilder;
import bot.data.items.ItemClass;

public class PickaxeItems {
	public static final String PICKAXE_COPPER = "PICKAXE_COPPER";
	public static final String PICKAXE_IRON = "PICKAXE_IRON";
	public static final String PICKAXE_DRAGONIUM = "PICKAXE_DRAGONIUM";

	private static class PickaxeItemBuilder extends ItemBuilder {
		public PickaxeItemBuilder(final String id, final String name, final String namePlural, final String description,
				final long price, final int rarity, final int pickaxeTier) {
			super(id, name, namePlural, description);
			price(price);
			rarity(rarity);
			classes(ItemClass.PICKAXE);
			pickaxeTier(pickaxeTier);
		}
	}

	public static void addItems(final Consumer<ItemBuilder> itemAdder) {
		new PickaxeItemBuilder(PICKAXE_COPPER, "copper pickaxe", "copper pickaxes", "Basic tool used for mining.",
				commonWeaponPrice * 5 / 2, 1, 1)//
						.add(itemAdder);

		new PickaxeItemBuilder(PICKAXE_IRON, "iron pickaxe", "iron pickaxes", "Durable tool used for mining.",
				uncommonWeaponPrice * 5 / 2, 2, 2)//
						.add(itemAdder);

		new PickaxeItemBuilder(PICKAXE_DRAGONIUM, "dragonium pickaxe", "dragonium pickaxes", "Best tool for mining.",
				rareWeaponPrice * 5 / 2, 3, 3)//
						.add(itemAdder);
	}
}
