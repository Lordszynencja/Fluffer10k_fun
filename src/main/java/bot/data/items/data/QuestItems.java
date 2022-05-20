package bot.data.items.data;

import java.util.function.Consumer;

import bot.data.items.ItemBuilder;
import bot.data.items.ItemClass;

public class QuestItems {
	public static final String BLACKSMITH_TOOLS_ADVANCED = "BLACKSMITH_TOOLS_ADVANCED";
	public static final String BLACKSMITH_TOOLS_SIMPLE = "BLACKSMITH_TOOLS_SIMPLE";
	public static final String NECROFILICON = "NECROFILICON";
	public static final String TEDDY_BEAR_PLUSHIE = "TEDDY_BEAR_PLUSHIE";
	public static final String WEIRD_MAGE_RING = "WEIRD_MAGE_RING";

	private static class QuestItemBuilder extends ItemBuilder {
		public QuestItemBuilder(final String id, final String name, final String namePlural, final String description) {
			super(id, name, namePlural, description);
			rarity(4);
			classes(ItemClass.QUEST);
		}
	}

	public static void addItems(final Consumer<ItemBuilder> itemAdder) {
		new QuestItemBuilder(BLACKSMITH_TOOLS_ADVANCED, "advanced blacksmith tool", "advanced blacksmith tools",
				"Stolen by hobgoblin")//
						.add(itemAdder);
		new QuestItemBuilder(BLACKSMITH_TOOLS_SIMPLE, "simple blacksmith tool", "simple blacksmith tools",
				"Stolen by goblins")//
						.add(itemAdder);
		new QuestItemBuilder(NECROFILICON, "Necrofilicon", "Necrofilicon", "That's... ew...\n"
				+ "It's sticky, you don't want to keep it in your hands for long. Maybe there is someone who will want this?")//
						.add(itemAdder);
		new QuestItemBuilder(TEDDY_BEAR_PLUSHIE, "teddy bear Plushie", "teddy bear Plushie",
				"It has its name written on a small identifier strapped to the thin collar that's on it.")//
						.price(500)//
						.add(itemAdder);
		new QuestItemBuilder(WEIRD_MAGE_RING, "golden ring with initials", "golden ring with initials",
				"A ring that was left in the tomb by a weird mage. It has his initials on it.")//
						.add(itemAdder);
	}
}
