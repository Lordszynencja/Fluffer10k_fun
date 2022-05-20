package bot.data.items.data;

import static bot.data.items.data.DefaultPrices.commonMagicScrollPrice;
import static bot.data.items.data.DefaultPrices.rareMagicScrollPrice;
import static bot.data.items.data.DefaultPrices.uncommonMagicScrollPrice;
import static bot.util.Utils.joinNames;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import bot.commands.rpg.spells.ActiveSkill;
import bot.data.items.ItemBuilder;
import bot.data.items.ItemClass;

public class MagicScrollItems {
	public static final String BOOK_OF_OFFENSE_GOUGE = "BOOK_OF_OFFENSE_GOUGE";
	public static final String BOOK_OF_OFFENSE_PRECISE_STRIKE = "BOOK_OF_OFFENSE_PRECISE_STRIKE";
	public static final String MAGIC_SCROLL_FORCE_HIT = "MAGIC_SCROLL_FORCE_HIT";
	public static final String MAGIC_SCROLL_HEAL = "MAGIC_SCROLL_HEAL";

	public static final String BOOK_OF_OFFENSE_BASH = "BOOK_OF_OFFENSE_BASH";
	public static final String BOOK_OF_OFFENSE_DOUBLE_STRIKE = "BOOK_OF_OFFENSE_DOUBLE_STRIKE";
	public static final String MAGIC_SCROLL_FIERY_WEAPON = "MAGIC_SCROLL_FIERY_WEAPON";
	public static final String MAGIC_SCROLL_FIREBALL = "MAGIC_SCROLL_FIREBALL";
	public static final String MAGIC_SCROLL_HOLY_AURA = "MAGIC_SCROLL_HOLY_AURA";
	public static final String MAGIC_SCROLL_ICE_BOLT = "MAGIC_SCROLL_ICE_BOLT";
	public static final String MAGIC_SCROLL_LIGHTNING = "MAGIC_SCROLL_LIGHTNING";
	public static final String MAGIC_SCROLL_MAGIC_SHIELD = "MAGIC_SCROLL_MAGIC_SHIELD";
	public static final String MAGIC_SCROLL_RAGE = "MAGIC_SCROLL_RAGE";
	public static final String MAGIC_SCROLL_SLEEP = "MAGIC_SCROLL_SLEEP";
	public static final String MAGIC_SCROLL_SPEED_OF_WIND = "MAGIC_SCROLL_SPEED_OF_WIND";

	public static final String MAGIC_SCROLL_BLIZZARD = "MAGIC_SCROLL_BLIZZARD";
	public static final String MAGIC_SCROLL_FREEZE = "MAGIC_SCROLL_FREEZE";
	public static final String MAGIC_SCROLL_METEORITE = "MAGIC_SCROLL_METEORITE";
	public static final String MAGIC_SCROLL_WHIRLPOOL = "MAGIC_SCROLL_WHIRLPOOL";

	private static class MagicScrollItemBuilder extends ItemBuilder {
		private static final long[] pricesForTiers = { 0, //
				commonMagicScrollPrice, //
				uncommonMagicScrollPrice, //
				rareMagicScrollPrice };
		private static final String[] descriptionsForTiers = { "", //
				"Use it to learn the basic %s spell", //
				"Use it to learn the %s spell", //
				"Use it to learn the powerful %s spell" };

		public MagicScrollItemBuilder(final String id) {
			final ActiveSkill spell = ActiveSkill.valueOf(id.replace("MAGIC_SCROLL_", ""));
			id(id);
			name("magic scroll (" + spell.name + ")");
			namePlural("magic scrolls (" + spell.name + ")");
			typeName("magic scroll");

			final String description = String.format(descriptionsForTiers[spell.tier], spell.name);
			description(description);
			price(pricesForTiers[spell.tier]);
			rarity(spell.tier);
			classes(ItemClass.SINGLE_USE);
		}
	}

	private static class BookOfOffenseItemBuilder extends ItemBuilder {
		private static final long[] pricesForTiers = { 0, //
				commonMagicScrollPrice, //
				uncommonMagicScrollPrice, //
				rareMagicScrollPrice };
		private static final String[] descriptionsForTiers = { "", //
				"Use it to learn the basic %s move", //
				"Use it to learn the %s move", //
				"Use it to learn the powerful %s move" };

		public BookOfOffenseItemBuilder(final String id) {
			final ActiveSkill spell = ActiveSkill.valueOf(id.replace("BOOK_OF_OFFENSE_", ""));
			id(id);
			name("book of offense (" + spell.name + ")");
			namePlural("books of offense (" + spell.name + ")");
			typeName("book of offense");

			String description = String.format(descriptionsForTiers[spell.tier], spell.name);
			final List<String> requirements = new ArrayList<>();
			if (spell.requiredStrength > 0) {
				requirements.add(spell.requiredStrength + " strength");
			}
			if (spell.requiredAgility > 0) {
				requirements.add(spell.requiredAgility + " agility");
			}
			if (spell.requiredIntelligence > 0) {
				requirements.add(spell.requiredIntelligence + " intelligence");
			}
			if (!requirements.isEmpty()) {
				description += "\nThis move requires " + joinNames(requirements) + " to use";
			}
			description(description);
			spellTaught(spell);
			price(pricesForTiers[spell.tier]);
			rarity(spell.tier);
			classes(ItemClass.SINGLE_USE, ItemClass.USE_OUTSIDE_COMBAT);
		}
	}

	public static void addItems(final Consumer<ItemBuilder> itemAdder) {
		new BookOfOffenseItemBuilder(BOOK_OF_OFFENSE_GOUGE).add(itemAdder);
		new BookOfOffenseItemBuilder(BOOK_OF_OFFENSE_PRECISE_STRIKE).add(itemAdder);
		new MagicScrollItemBuilder(MAGIC_SCROLL_FORCE_HIT).add(itemAdder);
		new MagicScrollItemBuilder(MAGIC_SCROLL_HEAL).add(itemAdder);

		new BookOfOffenseItemBuilder(BOOK_OF_OFFENSE_BASH).add(itemAdder);
		new BookOfOffenseItemBuilder(BOOK_OF_OFFENSE_DOUBLE_STRIKE).add(itemAdder);
		new MagicScrollItemBuilder(MAGIC_SCROLL_FIERY_WEAPON).add(itemAdder);
		new MagicScrollItemBuilder(MAGIC_SCROLL_FIREBALL).add(itemAdder);
		new MagicScrollItemBuilder(MAGIC_SCROLL_HOLY_AURA).add(itemAdder);
		new MagicScrollItemBuilder(MAGIC_SCROLL_ICE_BOLT).add(itemAdder);
		new MagicScrollItemBuilder(MAGIC_SCROLL_LIGHTNING).add(itemAdder);
		new MagicScrollItemBuilder(MAGIC_SCROLL_MAGIC_SHIELD).add(itemAdder);
		new MagicScrollItemBuilder(MAGIC_SCROLL_RAGE).add(itemAdder);
		new MagicScrollItemBuilder(MAGIC_SCROLL_SLEEP).add(itemAdder);
		new MagicScrollItemBuilder(MAGIC_SCROLL_SPEED_OF_WIND).add(itemAdder);

		new MagicScrollItemBuilder(MAGIC_SCROLL_BLIZZARD).add(itemAdder);
		new MagicScrollItemBuilder(MAGIC_SCROLL_FREEZE).add(itemAdder);
		new MagicScrollItemBuilder(MAGIC_SCROLL_METEORITE).add(itemAdder);
		new MagicScrollItemBuilder(MAGIC_SCROLL_WHIRLPOOL).add(itemAdder);
	}
}
