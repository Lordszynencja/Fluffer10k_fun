package bot.data.items.data;

import static bot.data.items.data.DefaultPrices.commonCraftingMaterialPrice;
import static bot.data.items.data.DefaultPrices.rareCraftingMaterialPrice;
import static bot.data.items.data.DefaultPrices.uncommonCraftingMaterialPrice;

import java.util.function.Consumer;

import bot.data.items.ItemBuilder;
import bot.data.items.ItemClass;

public class OreItems {
	public static final long commonOrePrice = commonCraftingMaterialPrice / 5;
	public static final long uncommonOrePrice = uncommonCraftingMaterialPrice / 5;
	public static final long rareOrePrice = rareCraftingMaterialPrice / 5;
	public static final long superRareOrePrice = rareCraftingMaterialPrice;

	public static final String ORE_COAL = "ORE_COAL";
	public static final String ORE_COPPER = "ORE_COPPER";

	public static final String ORE_IRON = "ORE_IRON";
	public static final String ORE_SILVER = "ORE_SILVER";

	public static final String ORE_GOLD = "ORE_GOLD";
	public static final String ORE_DEMON_REALM_SILVER = "ORE_DEMON_REALM_SILVER";

	public static final String ORE_DRAGONIUM = "ORE_DRAGONIUM";

	private static class OreItemBuilder extends ItemBuilder {
		public OreItemBuilder(final String id, final String name, final String namePlural, final String description,
				final long price, final int rarity) {
			super(id, name, namePlural, description);
			price(price);
			rarity(rarity);
			classes(ItemClass.CRAFTING_MATERIAL, ItemClass.ORE);
		}
	}

	public static void addItems(final Consumer<ItemBuilder> itemAdder) {
		new OreItemBuilder(ORE_COAL, "nugget of coal ore", "nuggets of coal ore", "Used as heat source and material.",
				commonOrePrice, 1)//
						.add(itemAdder);
		new OreItemBuilder(ORE_COPPER, "nugget of copper ore", "nuggets of copper ore",
				"Used as material for tools and weapons.", commonOrePrice * 2, 1)//
						.add(itemAdder);

		new OreItemBuilder(ORE_IRON, "nugget of iron ore", "nuggets of iron ore",
				"Used as material for tools and weapons.", uncommonOrePrice, 2)//
						.add(itemAdder);
		new OreItemBuilder(ORE_SILVER, "nugget of silver ore", "nuggets of silver ore",
				"Used as material for rings and necklaces.", uncommonOrePrice * 2, 2)//

						.add(itemAdder);
		new OreItemBuilder(ORE_GOLD, "nugget of gold ore", "nuggets of gold ore",
				"Used as material for rings and necklaces.", rareOrePrice, 3)//
						.add(itemAdder);
		new OreItemBuilder(ORE_DEMON_REALM_SILVER, "nugget of demon realm silver ore",
				"nuggets of demon realm silver ore", "Special material used for magic tools.", rareOrePrice * 2, 3)//
						.add(itemAdder);

		new OreItemBuilder(ORE_DRAGONIUM, "nugget of dragonium ore", "nuggets of dragonium ore",
				"Special material used for bery durable tools and weapons.", superRareOrePrice, 4)//
						.add(itemAdder);
	}
}
