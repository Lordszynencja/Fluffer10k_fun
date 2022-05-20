package bot.data.items.data;

import static bot.data.items.data.DefaultPrices.commonMonmusuDropMGPrice;
import static bot.data.items.data.DefaultPrices.commonMonmusuDropPrice;
import static bot.data.items.data.DefaultPrices.rareMonmusuDropMGPrice;
import static bot.data.items.data.DefaultPrices.rareMonmusuDropPrice;
import static bot.data.items.data.DefaultPrices.uncommonMonmusuDropMGPrice;
import static bot.data.items.data.DefaultPrices.uncommonMonmusuDropPrice;

import java.util.function.Consumer;

import bot.data.items.ItemBuilder;
import bot.data.items.ItemClass;

public class MonmusuDropItems {
	public static final String ALRAUNE_NECTAR = "ALRAUNE_NECTAR";
	public static final String ARACHNE_SILK = "ARACHNE_SILK";
	public static final String BAROMETZ_COTTON = "BAROMETZ_COTTON";
	public static final String BAROMETZ_FRUIT = "BAROMETZ_FRUIT";
	public static final String COUPLES_FRUIT = "COUPLES_FRUIT";
	public static final String HOLSTAUR_MILK = "HOLSTAUR_MILK";
	public static final String INTOXICATION_FRUIT = "INTOXICATION_FRUIT";
	public static final String MERMAID_BLOOD = "MERMAID_BLOOD";
	public static final String MERROW_BLOOD = "MERROW_BLOOD";
	public static final String PRISONER_FRUIT = "PRISONER_FRUIT";
	public static final String RAGING_MUSHROOM = "RAGING_MUSHROOM";
	public static final String SLIME_JELLY = "SLIME_JELLY";
	public static final String SLIME_JELLY_BUBBLE = "SLIME_JELLY_BUBBLE";
	public static final String SLIME_JELLY_DARK = "SLIME_JELLY_DARK";
	public static final String SLIME_JELLY_HUMPTY_EGG = "SLIME_JELLY_HUMPTY_EGG";
	public static final String SLIME_JELLY_NUREONAGO = "SLIME_JELLY_NUREONAGO";
	public static final String SLIME_JELLY_RED = "SLIME_JELLY_RED";
	public static final String STICKY_MUSHROOM = "STICKY_MUSHROOM";
	public static final String SUCCUBUS_NOSTRUM = "SUCCUBUS_NOSTRUM";
	public static final String WERESHEEP_WOOL = "WERESHEEP_WOOL";

	private static class MonmusuDropItemBuilder extends ItemBuilder {
		private static final long[] prices = { 0, //
				commonMonmusuDropPrice, //
				uncommonMonmusuDropPrice, //
				rareMonmusuDropPrice };
		private static final long[] mgPrices = { 0, //
				commonMonmusuDropMGPrice, //
				uncommonMonmusuDropMGPrice, //
				rareMonmusuDropMGPrice };

		public MonmusuDropItemBuilder(final String id, final String name, final String namePlural,
				final String description, final int rarity) {
			super(id, name, namePlural, description);
			price(prices[rarity]);
			mgPrice(mgPrices[rarity]);
			rarity(rarity);
			classes(ItemClass.MONMUSU_DROP);
		}
	}

	public static void addItems(final Consumer<ItemBuilder> itemAdder) {
		new MonmusuDropItemBuilder(ALRAUNE_NECTAR, "bottle of alraune nectar", "bottles of alraune nectar",
				"Nectar gathered from the alraune type monster.", 3)//
						.image("https://cdn.discordapp.com/attachments/831299601675845682/831659326460264468/Alraune_Nectar.jpg")//
						.addClass(ItemClass.GIFT)//
						.affectionBonus(5)//
						.add(itemAdder);
		new MonmusuDropItemBuilder(ARACHNE_SILK, "string of arachne silk", "strings of arachne silk",
				"Silk made by arachne, it can be used to make clothing.", 3)//
						.image("https://cdn.discordapp.com/attachments/831299601675845682/831659424065519616/Arachne_Silk.jpg")//
						.add(itemAdder);
		new MonmusuDropItemBuilder(BAROMETZ_COTTON, "piece of barometz's cotton", "pieces of barometz's cotton",
				"Cotton made by barometz, it's always fluffy.", 3)//
						.image("https://cdn.discordapp.com/attachments/831299601675845682/831659483934621756/Barometzs_Cotton.png")//
						.add(itemAdder);
		new MonmusuDropItemBuilder(BAROMETZ_FRUIT, "bit of barometz's fruit", "bits of barometz's fruit",
				"Part of fruit grown by barometz.", 3)//
						.addClass(ItemClass.GIFT)//
						.affectionBonus(5)//
						.add(itemAdder);
		new MonmusuDropItemBuilder(COUPLES_FRUIT, "couple's fruit", "couple's fruits",
				"A fruit made of two fruits from two interconnected trees, representing male and female joining together.",
				1)//
						.image("https://cdn.discordapp.com/attachments/831299601675845682/831659526586892348/Couples_Fruit.jpg")//
						.addClass(ItemClass.GIFT)//
						.affectionBonus(2)//
						.add(itemAdder);
		new MonmusuDropItemBuilder(HOLSTAUR_MILK, "bottle of holstaur milk", "bottles of holstaur milk",
				"A sweet drink produced by holstaur, refreshes anyone.", 3)//
						.price(rareMonmusuDropPrice * 2)
						.image("https://cdn.discordapp.com/attachments/831299601675845682/831659577379913728/Holstaur_Milk.jpg")//
						.addClasses(ItemClass.GIFT, ItemClass.SINGLE_USE, ItemClass.USE_IN_COMBAT)//
						.affectionBonus(4)//
						.add(itemAdder);
		new MonmusuDropItemBuilder(INTOXICATION_FRUIT, "intoxication fruit", "intoxication fruits",
				"A fruit that looks like grape, but has half-transparent skin and it's filled with red liquid.", 2)//
						.image("https://cdn.discordapp.com/attachments/831299601675845682/831659614549966918/Intoxication_Fruit.jpg")//
						.addClass(ItemClass.GIFT)//
						.affectionBonus(3)//
						.add(itemAdder);
		new MonmusuDropItemBuilder(MERMAID_BLOOD, "vial of mermaid's blood", "vials of mermaid's blood",
				"A red liquid that increases lifespan, it's quite rare.", 3)//
						.addClass(ItemClass.GIFT)//
						.affectionBonus(7)//
						.add(itemAdder);
		new MonmusuDropItemBuilder(MERROW_BLOOD, "vial of merrow's blood", "vials of merrow's blood",
				"A pink liquid that is used to make medicines and lust potions, it also increases lifespan.", 3)//
						.addClass(ItemClass.GIFT)//
						.affectionBonus(5)//
						.add(itemAdder);
		new MonmusuDropItemBuilder(PRISONER_FRUIT, "prisoner fruit", "prisoner fruits",
				"An addicting fruit that makes those who eat it appear more attractive.", 1)//
						.image("https://cdn.discordapp.com/attachments/831299601675845682/831659648574685244/Prisoner_Fruit.jpg")//
						.addClass(ItemClass.GIFT)//
						.affectionBonus(2)//
						.add(itemAdder);
		new MonmusuDropItemBuilder(RAGING_MUSHROOM, "raging mushroom", "raging mushrooms",
				"A mushroom that turns human men into lustful predators, has very phallic appearance.", 3)//
						.image("https://cdn.discordapp.com/attachments/831299601675845682/960652702868705310/Raging_Mushroom.jpg")//
						.addClass(ItemClass.GIFT)//
						.affectionBonus(10)//
						.add(itemAdder);
		new MonmusuDropItemBuilder(SLIME_JELLY, "piece of slime jelly", "pieces of slime jelly", "A piece of a slime.",
				1)//
						.add(itemAdder);
		new MonmusuDropItemBuilder(SLIME_JELLY_BUBBLE, "piece of bubble slime jelly", "pieces of bubble slime jelly",
				"A piece of a bubble slime. Very addictive.", 1)//
						.add(itemAdder);
		new MonmusuDropItemBuilder(SLIME_JELLY_DARK, "piece of dark slime jelly", "pieces of dark slime jelly",
				"A piece of a dark slime.", 2)//
						.add(itemAdder);
		new MonmusuDropItemBuilder(SLIME_JELLY_HUMPTY_EGG, "piece of humpty egg jelly", "pieces of humpty egg jelly",
				"A piece of a humpty egg, it is similar in flavor to normal eggs. Makes man produce much more semen.",
				2)//
						.addClass(ItemClass.GIFT)//
						.affectionBonus(4)//
						.add(itemAdder);
		new MonmusuDropItemBuilder(SLIME_JELLY_NUREONAGO, "piece of nureonago slime Jelly",
				"pieces of nureonago slime Jelly", "A piece of a nureonago slime.", 2)//
						.add(itemAdder);
		new MonmusuDropItemBuilder(SLIME_JELLY_RED, "piece of red slime Jelly", "pieces of red slime Jelly",
				"A piece of a red slime.", 2)//
						.add(itemAdder);
		new MonmusuDropItemBuilder(STICKY_MUSHROOM, "sticky mushroom", "sticky mushrooms",
				"A mushroom covered in sticky, viscous liquid, that is constantly secreted, causes semen to persist for long time.",
				3)//
						.image("https://cdn.discordapp.com/attachments/831299601675845682/960653532002930698/Sticky_Mushroom.jpg")//
						.addClass(ItemClass.GIFT)//
						.affectionBonus(10)//
						.add(itemAdder);
		new MonmusuDropItemBuilder(SUCCUBUS_NOSTRUM, "bottle of Succubus Nostrum", "bottles of Succubus Nostrum",
				"A potion containing mamono mana.", 3)//
						.image("https://cdn.discordapp.com/attachments/831299601675845682/831659692538200075/Succubus_Nostrum.jpg")//
						.addClass(ItemClass.GIFT)//
						.affectionBonus(10)//
						.add(itemAdder);
		new MonmusuDropItemBuilder(WERESHEEP_WOOL, "bit of weresheep wool", "bits of weresheep wool",
				"A type of wool grown by Weresheep. It induces sleep and is used in production of beds, pyjamas and pillows.",
				2)//
						.image("https://cdn.discordapp.com/attachments/831299601675845682/831659752052097044/Weresheep_Wool.jpg")//
						.addClasses(ItemClass.SINGLE_USE, ItemClass.USE_IN_COMBAT)//
						.add(itemAdder);
	}
}
