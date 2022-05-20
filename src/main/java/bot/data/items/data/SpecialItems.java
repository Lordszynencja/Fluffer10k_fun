package bot.data.items.data;

import static bot.data.items.data.DefaultPrices.uncommonRingPrice;

import java.util.function.Consumer;

import bot.data.items.ItemBuilder;
import bot.data.items.ItemClass;

public class SpecialItems {
	public static final String RED_ACORN_RING = "RED_ACORN_RING";
	public static final String ORANGE_ACORN_RING = "ORANGE_ACORN_RING";
	public static final String YELLOW_ACORN_RING = "YELLOW_ACORN_RING";
	public static final String GREEN_ACORN_RING = "GREEN_ACORN_RING";
	public static final String BLUE_ACORN_RING = "BLUE_ACORN_RING";
	public static final String INDIGO_ACORN_RING = "INDIGO_ACORN_RING";
	public static final String VIOLET_ACORN_RING = "VIOLET_ACORN_RING";

	private static class SpecialItemBuilder extends ItemBuilder {
		public SpecialItemBuilder(final String id, final String name, final String namePlural,
				final String description) {
			super(id, name, namePlural, description);
			rarity(3);
			classes(ItemClass.SPECIAL);
		}
	}

	public static void addItems(final Consumer<ItemBuilder> itemAdder) {
		new SpecialItemBuilder(RED_ACORN_RING, "red acorn ring", "red acorn rings",
				"A special ring gifted by a grateful ratatoskr.")//
						.price(uncommonRingPrice)//
						.addClass(ItemClass.RING)//
						.strengthBonus(1)//
						.add(itemAdder);
		new SpecialItemBuilder(ORANGE_ACORN_RING, "orange acorn ring", "orange acorn rings",
				"A special ring gifted by a grateful ratatoskr.")//
						.price(uncommonRingPrice)//
						.addClass(ItemClass.RING)//
						.armorBonus(1)//
						.add(itemAdder);
		new SpecialItemBuilder(YELLOW_ACORN_RING, "yellow acorn ring", "yellow acorn rings",
				"A special ring gifted by a grateful ratatoskr.")//
						.price(uncommonRingPrice)//
						.addClass(ItemClass.RING)//
						.agilityBonus(1)//
						.add(itemAdder);
		new SpecialItemBuilder(GREEN_ACORN_RING, "green acorn ring", "green acorn rings",
				"A special ring gifted by a grateful ratatoskr.")//
						.price(uncommonRingPrice)//
						.addClass(ItemClass.RING)//
						.healthBonus(5)//
						.add(itemAdder);
		new SpecialItemBuilder(BLUE_ACORN_RING, "blue acorn ring", "blue acorn rings",
				"A special ring gifted by a grateful ratatoskr.")//
						.price(uncommonRingPrice)//
						.addClass(ItemClass.RING)//
						.intelligenceBonus(1)//
						.add(itemAdder);
		new SpecialItemBuilder(INDIGO_ACORN_RING, "indigo acorn ring", "indigo acorn rings",
				"A special ring gifted by a grateful ratatoskr.")//
						.price(uncommonRingPrice)//
						.addClass(ItemClass.RING)//
						.manaBonus(5)//
						.add(itemAdder);
		new SpecialItemBuilder(VIOLET_ACORN_RING, "violet acorn ring", "violet acorn rings",
				"A special ring gifted by a grateful ratatoskr.")//
						.price(uncommonRingPrice)//
						.addClass(ItemClass.RING)//
						.manaRegenBonus(15)//
						.add(itemAdder);
	}
}
