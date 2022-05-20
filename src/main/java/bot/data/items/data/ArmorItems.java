package bot.data.items.data;

import static bot.data.items.data.DefaultPrices.commonArmorPrice;
import static bot.data.items.data.DefaultPrices.rareArmorPrice;
import static bot.data.items.data.DefaultPrices.uncommonArmorPrice;

import java.util.function.Consumer;

import bot.data.items.ItemBuilder;
import bot.data.items.ItemClass;

public class ArmorItems {
	public static final String LEATHER_ARMOR = "LEATHER_ARMOR";
	public static final String RING_MAIL = "RING_MAIL";
	public static final String CHAINMAIL = "CHAINMAIL";
	public static final String PLATE_ARMOR = "PLATE_ARMOR";

	private static class ArmorItemBuilder extends ItemBuilder {
		public ArmorItemBuilder(final String id, final String name, final String namePlural, final String description,
				final long price, final int rarity, final int armorBonus, final int requiredStrength,
				final int agilityBonus) {
			super(id, name, namePlural, description);
			price(price);
			rarity(rarity);
			armorBonus(armorBonus);
			requiredStrength(requiredStrength);
			agilityBonus(agilityBonus);
		}
	}

	public static void addItems(final Consumer<ItemBuilder> itemAdder) {
		new ArmorItemBuilder(LEATHER_ARMOR, "leather armor", "leather armors",
				"Simple armor giving basic protection while not restricting the user.", //
				commonArmorPrice, 1, 1, 1, 0)//
						.classes(ItemClass.LIGHT_ARMOR)//
						.add(itemAdder);

		new ArmorItemBuilder(RING_MAIL, "ring mail", "ring mails",
				"Simple armor with pieces of metal added on top for more protection.", //
				uncommonArmorPrice * 4 / 5, 2, 3, 6, -1)//
						.classes(ItemClass.MEDIUM_ARMOR)//
						.add(itemAdder);
		new ArmorItemBuilder(CHAINMAIL, "chainmail", "chainmails",
				"Offers good protection while being pretty flexible.", //
				uncommonArmorPrice * 6 / 5, 2, 4, 8, -2)//
						.classes(ItemClass.MEDIUM_ARMOR)//
						.add(itemAdder);

		new ArmorItemBuilder(PLATE_ARMOR, "plate armor", "plate armors", "Great but heavy armor.", //
				rareArmorPrice, 3, 9, 13, -4)//
						.classes(ItemClass.HEAVY_ARMOR)//
						.add(itemAdder);
	}
}
