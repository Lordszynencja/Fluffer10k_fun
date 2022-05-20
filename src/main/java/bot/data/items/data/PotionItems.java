package bot.data.items.data;

import static bot.data.items.data.DefaultPrices.commonPotionPrice;
import static bot.data.items.data.DefaultPrices.rarePotionMGPrice;
import static bot.data.items.data.DefaultPrices.rarePotionPrice;
import static bot.data.items.data.DefaultPrices.uncommonPotionMGPrice;
import static bot.data.items.data.DefaultPrices.uncommonPotionPrice;

import java.util.function.Consumer;

import bot.data.items.ItemBuilder;
import bot.data.items.ItemClass;

public class PotionItems {
	public static final String HEALTH_POTION_MINOR = "HEALTH_POTION_MINOR";
	public static final String HEALTH_POTION = "HEALTH_POTION";
	public static final String HEALTH_POTION_MAJOR = "HEALTH_POTION_MAJOR";

	public static final String MANA_POTION_MINOR = "MANA_POTION_MINOR";
	public static final String MANA_POTION = "MANA_POTION";
	public static final String MANA_POTION_MAJOR = "MANA_POTION_MAJOR";

	public static final String STRENGTH_1_POTION = "STRENGTH_1_POTION";
	public static final String STRENGTH_2_POTION = "STRENGTH_2_POTION";
	public static final String STRENGTH_3_POTION = "STRENGTH_3_POTION";

	public static final String AGILITY_1_POTION = "AGILITY_1_POTION";
	public static final String AGILITY_2_POTION = "AGILITY_2_POTION";
	public static final String AGILITY_3_POTION = "AGILITY_3_POTION";

	public static final String INTELLIGENCE_1_POTION = "INTELLIGENCE_1_POTION";
	public static final String INTELLIGENCE_2_POTION = "INTELLIGENCE_2_POTION";
	public static final String INTELLIGENCE_3_POTION = "INTELLIGENCE_3_POTION";

	public static final String DOPPELGANGER_POTION = "DOPPELGANGER_POTION";

	private static class PotionItemBuilder extends ItemBuilder {
		public PotionItemBuilder(final String id, final String name, final String namePlural, final String description,
				final long price, final int rarity) {
			super(id, name, namePlural, description);
			price(price);
			rarity(rarity);
			classes(ItemClass.SINGLE_USE, ItemClass.USE_IN_COMBAT);
		}
	}

	public static void addItems(final Consumer<ItemBuilder> itemAdder) {
		new PotionItemBuilder(HEALTH_POTION_MINOR, "minor health potion", "minor health potions",
				"Heals you when you drink it.", commonPotionPrice, 1)//
						.add(itemAdder);
		new PotionItemBuilder(HEALTH_POTION, "medium health potion", "medium health potions",
				"Heals you when you drink it.", commonPotionPrice * 2, 1)//
						.add(itemAdder);
		new PotionItemBuilder(HEALTH_POTION_MAJOR, "major health potion", "major health potions",
				"Heals you when you drink it.", uncommonPotionPrice, 2)//
						.add(itemAdder);

		new PotionItemBuilder(MANA_POTION_MINOR, "minor mana potion", "minor mana potions",
				"Gives you mana when you drink it.", commonPotionPrice, 1)//
						.add(itemAdder);
		new PotionItemBuilder(MANA_POTION, "medium mana potion", "medium mana potions",
				"Gives you mana when you drink it.", commonPotionPrice * 2, 1)//
						.add(itemAdder);
		new PotionItemBuilder(MANA_POTION_MAJOR, "major mana potion", "major mana potions",
				"Gives you mana when you drink it.", uncommonPotionPrice, 2)//
						.add(itemAdder);

		new PotionItemBuilder(STRENGTH_1_POTION, "potion of strength", "potions of strength",
				"Gives you weak strength buff until the end of combat.", uncommonPotionPrice, 2)//
						.mgPrice(uncommonPotionMGPrice)//
						.add(itemAdder);
		new PotionItemBuilder(STRENGTH_2_POTION, "potion of greater strength", "potions of greater strength",
				"Gives you medium strength buff until the end of combat.", uncommonPotionPrice * 2, 2)//
						.mgPrice(uncommonPotionMGPrice * 2)//
						.add(itemAdder);
		new PotionItemBuilder(STRENGTH_3_POTION, "potion of inhuman strength", "potions of inhuman strength",
				"Gives you huge strength buff until the end of combat.", rarePotionPrice, 3)//
						.mgPrice(rarePotionMGPrice)//
						.add(itemAdder);

		new PotionItemBuilder(AGILITY_1_POTION, "potion of swiftness", "potions of swiftness",
				"Gives you agility buff until the end of combat.", uncommonPotionPrice, 2)//
						.mgPrice(uncommonPotionMGPrice)//
						.add(itemAdder);
		new PotionItemBuilder(AGILITY_2_POTION, "potion of agility", "potions of agility",
				"Gives you greater agility buff until the end of combat.", uncommonPotionPrice * 2, 2)//
						.mgPrice(uncommonPotionMGPrice * 2)//
						.add(itemAdder);
		new PotionItemBuilder(AGILITY_3_POTION, "potion of acrobatics", "potions of acrobatics",
				"Gives you huge agility buff until the end of combat.", rarePotionPrice, 3)//
						.mgPrice(rarePotionMGPrice)//
						.add(itemAdder);

		new PotionItemBuilder(INTELLIGENCE_1_POTION, "potion of potence", "potions of potence",
				"Gives you intelligence buff until the end of combat.", uncommonPotionPrice, 2)//
						.mgPrice(uncommonPotionMGPrice)//
						.add(itemAdder);
		new PotionItemBuilder(INTELLIGENCE_2_POTION, "potion of intelligence", "potions of intelligence",
				"Gives you great intelligence buff until the end of combat.", uncommonPotionPrice * 2, 2)//
						.mgPrice(uncommonPotionMGPrice * 2)//
						.add(itemAdder);
		new PotionItemBuilder(INTELLIGENCE_3_POTION, "potion of mastermind", "potions of mastermind",
				"Gives you huge intelligence buff until the end of combat.", rarePotionPrice, 3)//
						.mgPrice(rarePotionMGPrice)//
						.add(itemAdder);

		new PotionItemBuilder(DOPPELGANGER_POTION, "doppelganger potion", "doppelganger potions",
				"Rare potion that multiplies the user who drinks it.", rarePotionPrice, 3)////
						.mgPrice(rarePotionMGPrice)//
						.addClass(ItemClass.GIFT)//
						.affectionBonus(50)//
						.add(itemAdder);
	}
}
