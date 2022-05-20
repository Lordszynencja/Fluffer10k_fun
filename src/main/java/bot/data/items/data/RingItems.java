package bot.data.items.data;

import static bot.data.items.data.DefaultPrices.commonDecorationPrice;
import static bot.data.items.data.DefaultPrices.trashDecorationPrice;
import static bot.data.items.data.DefaultPrices.uncommonDecorationPrice;
import static bot.data.items.data.DefaultPrices.uncommonRingPrice;

import java.util.function.Consumer;

import bot.data.items.ItemBuilder;
import bot.data.items.ItemClass;

public class RingItems {
	public static final String WOODEN_RING = "WOODEN_RING";
	public static final String SILVER_RING = "SILVER_RING";
	public static final String GOLDEN_RING = "GOLDEN_RING";

	public static final String SPELL_VOID_RING = "SPELL_VOID_RING";
	public static final String STATUS_NEGATION_RING = "STATUS_NEGATION_RING";
	public static final String REGENERATION_RING = "REGENERATION_RING";

	private static class RingItemBuilder extends ItemBuilder {
		public RingItemBuilder(final String id, final String name, final String namePlural, final String description,
				final long price, final int rarity) {
			super(id, name, namePlural, description);
			price(price);
			rarity(rarity);
			classes(ItemClass.RING);
		}
	}

	public static void addItems(final Consumer<ItemBuilder> itemAdder) {
		new RingItemBuilder(WOODEN_RING, "wooden ring", "wooden rings",
				"Probably some child pretended to be rich and lost it.", trashDecorationPrice, 0)//
						.add(itemAdder);
		new RingItemBuilder(SILVER_RING, "silver ring", "silver rings", "Looks nice and is worth a nice amount.",
				commonDecorationPrice, 1)//
						.add(itemAdder);
		new RingItemBuilder(GOLDEN_RING, "golden ring", "golden rings", "Is worth quite a bit and looks very fancy.",
				uncommonDecorationPrice, 2)//
						.add(itemAdder);

		new RingItemBuilder(SPELL_VOID_RING, "ring of spell void", "rings of spell void",
				"Greatly reduces power of spells near the user.", uncommonRingPrice, 2)//
						.addClass(ItemClass.SPELL_VOID)//
						.add(itemAdder);
		new RingItemBuilder(STATUS_NEGATION_RING, "ring of negation", "rings of negation",
				"Reduces duration of negative effects.", uncommonRingPrice, 2)//
						.addClass(ItemClass.STATUS_NEGATION)//
						.add(itemAdder);
		new RingItemBuilder(REGENERATION_RING, "ring of regeneration", "rings of regeneration",
				"Gives user the ability to regenerate health during the fight.", uncommonRingPrice, 2)//
						.healthRegenerationBonus(1)//
						.add(itemAdder);
	}
}
