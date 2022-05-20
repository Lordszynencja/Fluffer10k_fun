package bot.data.items.data;

import static bot.data.items.data.DefaultPrices.commonWeaponPrice;
import static bot.data.items.data.DefaultPrices.rareWeaponPrice;
import static bot.data.items.data.DefaultPrices.trashWeaponPrice;
import static bot.data.items.data.DefaultPrices.uncommonWeaponPrice;

import java.util.function.Consumer;

import bot.data.items.Item;
import bot.data.items.ItemBuilder;
import bot.data.items.ItemClass;
import bot.data.items.Items;
import bot.data.items.data.GemItems.GemRefinement;
import bot.data.items.data.GemItems.GemSize;
import bot.data.items.data.GemItems.GemType;

public class WeaponItems {
	public static String getApprenticeBookId(final GemType gemType) {
		return "APPRENTICE_BOOK_" + gemType.name();
	}

	public static String getApprenticeStaffId(final GemType gemType) {
		return "APPRENTICE_STAFF_" + gemType.name();
	}

	public static String getArchmageSpellbookId(final GemType gemType) {
		return "ARCHMAGE_SPELLBOOK_" + gemType.name();
	}

	public static String getArchmageStaffId(final GemType gemType) {
		return "ARCHMAGE_STAFF_" + gemType.name();
	}

	public static String getMageSpellbookId(final GemType gemType) {
		return "MAGE_SPELLBOOK_" + gemType.name();
	}

	public static String getMageStaffId(final GemType gemType) {
		return "MAGE_STAFF_" + gemType.name();
	}

	public static final String CHAINED_KUNAI = "CHAINED_KUNAI";
	public static final String DARTS = "DARTS";
	public static final String JAVELIN = "JAVELIN";
	public static final String KAMA = "KAMA";
	public static final String KNIFE = "KNIFE";
	public static final String KUNAI = "KUNAI";
	public static final String LUMBERJACK_AXE = "LUMBERJACK_AXE";
	public static final String SCYTHE = "SCYTHE";
	public static final String SHORT_BOW = "SHORT_BOW";
	public static final String SHORT_SWORD = "SHORT_SWORD";
	public static final String SLINGSHOT = "SLINGSHOT";
	public static final String WHIP = "WHIP";

	public static final String BATTLE_AXE = "BATTLE_AXE";
	public static final String BLOWGUN = "BLOWGUN";
	public static final String BROADSWORD = "BROADSWORD";
	public static final String CHAIN_WHIP = "CHAIN_WHIP";
	public static final String CLAYMORE = "CLAYMORE";
	public static final String DAGGER = "DAGGER";
	public static final String ESTOC = "ESTOC";
	public static final String GLAIVE = "GLAIVE";
	public static final String HALBERD = "HALBERD";
	public static final String KUKRI = "KUKRI";
	public static final String KUSARIGAMA = "KUSARIGAMA";
	public static final String LIGHT_CROSSBOW = "LIGHT_CROSSBOW";
	public static final String LONG_BOW = "LONG_BOW";
	public static final String LONG_SWORD = "LONG_SWORD";
	public static final String MACHETE = "MACHETE";
	public static final String NUNCHUCK = "NUNCHUCK";
	public static final String RAPIER = "RAPIER";
	public static final String SPEAR = "SPEAR";
	public static final String SPIKED_KNUCKLES = "SPIKED_KNUCKLES";
	public static final String SPIKED_MACE = "SPIKED_MACE";
	public static final String THROWING_KNIVES = "THROWING_KNIVES";
	public static final String TOMAHAWK = "TOMAHAWK";

	public static final String BOLAS = "BOLAS";
	public static final String DWARVEN_AXE = "DWARVEN_AXE";
	public static final String DWARVEN_BOW = "DWARVEN_BOW";
	public static final String ELVEN_BOW = "ELVEN_BOW";
	public static final String GAUNTLET = "GAUNTLET";
	public static final String HEAVY_CROSSBOW = "HEAVY_CROSSBOW";
	public static final String HOOK_SWORD = "HOOK_SWORD";
	public static final String KATANA = "KATANA";
	public static final String MAGIC_SWORD = "MAGIC_SWORD";
	public static final String NECRONOMICON = "NECRONOMICON";
	public static final String RITUAL_DAGGER = "RITUAL_DAGGER";
	public static final String RUNIC_SCIMITAR = "RUNIC_SCIMITAR";
	public static final String SHURIKENS = "SHURIKENS";
	public static final String TRIDENT = "TRIDENT";

	public static final String METAL_SHIELD = "METAL_SHIELD";
	public static final String PALADIN_SHIELD = "PALADIN_SHIELD";
	public static final String WOODEN_SHIELD = "WOODEN_SHIELD";

	public static final String WOODEN_SWORD = "WOODEN_SWORD";

	private static enum AttackType {
		AGILITY(ItemClass.AGILITY_BASED), //
		STRENGTH(ItemClass.STRENGTH_BASED);

		public final ItemClass itemClass;

		private AttackType(final ItemClass itemClass) {
			this.itemClass = itemClass;
		}
	}

	private static enum Handedness {
		BOTH(ItemClass.BOTH_HANDS), //
		LEFT(ItemClass.LEFT_HAND), //
		RIGHT(ItemClass.RIGHT_HAND);

		public final ItemClass itemClass;

		private Handedness(final ItemClass itemClass) {
			this.itemClass = itemClass;
		}
	}

	private static class WeaponItemBuilder extends ItemBuilder {
		public WeaponItemBuilder(final String id, final String name, final String namePlural, final String description,
				final long price, final int rarity, final AttackType attackType, final Handedness handedness) {
			super(id, name, namePlural, description);
			price(price);
			rarity(rarity);
			classes(ItemClass.WEAPON, attackType.itemClass, handedness.itemClass);
		}
	}

	private static class ShieldItemBuilder extends ItemBuilder {
		public ShieldItemBuilder(final String id, final String name, final String namePlural, final String description,
				final long price, final int rarity) {
			super(id, name, namePlural, description);
			price(price);
			rarity(rarity);
			classes(ItemClass.SHIELD, ItemClass.LEFT_HAND);
		}
	}

	private static void addTier1Items(final Items items, final Consumer<ItemBuilder> itemAdder) {
		for (final GemType gemType : GemType.values()) {
			final Item gem = items.getItem(GemItems.getId(GemSize.SMALL, GemRefinement.REFINED, gemType));

			new WeaponItemBuilder(getApprenticeBookId(gemType), "apprentice book with " + gemType.color + " gem",
					"apprentice books with " + gemType.color + " gems", //
					"Book with magic gem on the cover, containing basic spells and incantations to help one focus.", //
					commonWeaponPrice + gem.price, 1, AttackType.STRENGTH, Handedness.BOTH)//
							.magicPower(2)//
							.add(itemAdder);
			new WeaponItemBuilder(getApprenticeStaffId(gemType), "apprentice staff with " + gemType.color + " gem",
					"apprentice staffs with " + gemType.color + " gems", //
					"Staff with low grade magical gem.", //
					commonWeaponPrice + gem.price, 1, AttackType.STRENGTH, Handedness.BOTH)//
							.damageRollBonus(1)//
							.magicPower(1)//
							.add(itemAdder);
		}

		new WeaponItemBuilder(CHAINED_KUNAI, "chained kunai", "chained kunais",
				"A long chain that has a kunai at its end.", //
				commonWeaponPrice, 1, AttackType.AGILITY, Handedness.BOTH)//
						.addClass(ItemClass.LONG)//
						.damageRollBonus(1)//
						.add(itemAdder);
		new WeaponItemBuilder(DARTS, "set of darts", "sets of darts", "Throwable darts.", //
				commonWeaponPrice, 1, AttackType.AGILITY, Handedness.BOTH)//
						.addClasses(ItemClass.RANGED)//
						.damageRollBonus(1)//
						.add(itemAdder);
		new WeaponItemBuilder(JAVELIN, "javelin", "javelins", "Light spear that you can throw.", //
				commonWeaponPrice, 1, AttackType.AGILITY, Handedness.RIGHT)//
						.addClass(ItemClass.RANGED)//
						.damageRollBonus(1)//
						.requiredAgility(2)//
						.add(itemAdder);
		new WeaponItemBuilder(KAMA, "kama", "kamas", "A sickle-like short weapon with straight blade.", //
				commonWeaponPrice, 1, AttackType.AGILITY, Handedness.RIGHT)//
						.damageRollBonus(1)//
						.criticalStrikeBonus(5)//
						.add(itemAdder);
		new WeaponItemBuilder(KNIFE, "knife", "knives", "Simple knife.", //
				commonWeaponPrice, 1, AttackType.AGILITY, Handedness.RIGHT)//
						.damageRollBonus(1)//
						.criticalStrikeBonus(5)//
						.add(itemAdder);
		new WeaponItemBuilder(KUNAI, "kunai", "kunais",
				"A weapon with a leaf-shaped blade, with ring on the end, near the handle.", //
				commonWeaponPrice, 1, AttackType.AGILITY, Handedness.RIGHT)//
						.damageRollBonus(1)//
						.criticalStrikeBonus(5)//
						.add(itemAdder);
		new WeaponItemBuilder(LUMBERJACK_AXE, "lumberjack axe", "lumberjack axes", "Axe used to cut down trees.", //
				commonWeaponPrice, 1, AttackType.STRENGTH, Handedness.BOTH)//
						.damageRollBonus(3)//
						.requiredStrength(2)//
						.add(itemAdder);
		new WeaponItemBuilder(SCYTHE, "scythe", "scythes",
				"Used mostly to mow plants, but it's sharp enough to be used in fight.", //
				commonWeaponPrice, 1, AttackType.STRENGTH, Handedness.BOTH)//
						.addClass(ItemClass.LONG)//
						.damageRollBonus(2)//
						.add(itemAdder);
		new WeaponItemBuilder(SHORT_BOW, "short bow", "short bows", "Usually used for practice or hunting.", //
				commonWeaponPrice, 1, AttackType.AGILITY, Handedness.BOTH)//
						.addClass(ItemClass.RANGED)//
						.damageRollBonus(1)//
						.add(itemAdder);
		new WeaponItemBuilder(SHORT_SWORD, "short sword", "short swords", "A sword used by infantry.", //
				commonWeaponPrice, 1, AttackType.STRENGTH, Handedness.RIGHT)//
						.damageRollBonus(2)//
						.add(itemAdder);
		new WeaponItemBuilder(SLINGSHOT, "slingshot", "slingshots",
				"Y-shaped piece of wood with elastic string attached to sling small rocks at enemy.", //
				commonWeaponPrice, 1, AttackType.AGILITY, Handedness.BOTH)//
						.addClass(ItemClass.RANGED)//
						.damageRollBonus(1)//
						.add(itemAdder);
		new WeaponItemBuilder(WHIP, "whip", "whips", "You can use it to strike enemies, dark elves like to use it.", //
				commonWeaponPrice, 1, AttackType.AGILITY, Handedness.RIGHT)//
						.addClass(ItemClass.LONG)//
						.add(itemAdder);
	}

	private static void addTier2Items(final Items items, final Consumer<ItemBuilder> itemAdder) {
		for (final GemType gemType : GemType.values()) {
			final Item gem = items.getItem(GemItems.getId(GemSize.MEDIUM, GemRefinement.REFINED, gemType));

			new WeaponItemBuilder(getMageSpellbookId(gemType), "mage spellbook with " + gemType.color + " gem",
					"mage books with " + gemType.color + " gems", //
					"Spellbook filled with spells and incantations.", //
					commonWeaponPrice + gem.price, 2, AttackType.STRENGTH, Handedness.BOTH)//
							.magicPower(5)//
							.requiredIntelligence(12)//
							.intelligenceBonus(2)//
							.add(itemAdder);
			new WeaponItemBuilder(getMageStaffId(gemType), "mage staff with " + gemType.color + " gem",
					"mage staffs with " + gemType.color + " gems", //
					"Staff with magical gem socketed in it. Used by mages all over the world.", //
					uncommonWeaponPrice + gem.price, 2, AttackType.STRENGTH, Handedness.BOTH)//
							.damageRollBonus(3)//
							.magicPower(2)//
							.requiredStrength(5).requiredIntelligence(5)//
							.add(itemAdder);
		}

		new WeaponItemBuilder(BATTLE_AXE, "battle axe", "battle axes", "Weapon of choice for most dwarves.", //
				uncommonWeaponPrice, 2, AttackType.STRENGTH, Handedness.BOTH)//
						.damageRollBonus(8)//
						.requiredStrength(12)//
						.add(itemAdder);
		new WeaponItemBuilder(BLOWGUN, "blowgun", "blowguns",
				"A small pipe that is used to shoot darts by blowing in it.", //
				uncommonWeaponPrice, 2, AttackType.AGILITY, Handedness.BOTH)//
						.addClass(ItemClass.RANGED)//
						.damageRollBonus(2)//
						.requiredAgility(7)//
						.add(itemAdder);
		new WeaponItemBuilder(BROADSWORD, "broadsword", "broadswords", "Sword with a guard for user's hand.", //
				uncommonWeaponPrice, 2, AttackType.STRENGTH, Handedness.RIGHT)//
						.damageRollBonus(3)//
						.armorBonus(1)//
						.requiredStrength(6)//
						.add(itemAdder);
		new WeaponItemBuilder(CHAIN_WHIP, "chain whip", "chain whips", "A long chain that can be used to whip enemies.", //
				uncommonWeaponPrice, 2, AttackType.STRENGTH, Handedness.BOTH)//
						.addClass(ItemClass.LONG)//
						.damageRollBonus(4)//
						.requiredStrength(7)//
						.add(itemAdder);
		new WeaponItemBuilder(CLAYMORE, "claymore", "claymores", "A two-handed sword.", //
				uncommonWeaponPrice, 2, AttackType.STRENGTH, Handedness.BOTH)//
						.damageRollBonus(7)//
						.requiredStrength(10)//
						.add(itemAdder);
		new WeaponItemBuilder(DAGGER, "dagger", "daggers", "Dagger of a stealthy killer.", //
				uncommonWeaponPrice, 2, AttackType.AGILITY, Handedness.RIGHT)//
						.damageRollBonus(2)//
						.criticalStrikeBonus(10)//
						.requiredAgility(7)//
						.add(itemAdder);
		new WeaponItemBuilder(ESTOC, "estoc", "estocs", "Two-handed sword used to pierce enemy armor.", //
				uncommonWeaponPrice, 2, AttackType.STRENGTH, Handedness.BOTH)//
						.addClass(ItemClass.ARMOR_PIERCING)//
						.damageRollBonus(4)//
						.requiredStrength(9).requiredAgility(2)//
						.add(itemAdder);
		new WeaponItemBuilder(GLAIVE, "glaive", "glaives", "Spear-like weapon with one-sided blade at the end.", //
				uncommonWeaponPrice, 2, AttackType.STRENGTH, Handedness.BOTH).addClass(ItemClass.LONG)//
						.damageRollBonus(5)//
						.requiredStrength(7).requiredAgility(3)//
						.add(itemAdder);
		new WeaponItemBuilder(HALBERD, "halberd", "halberds",
				"A weapon that consists of axe blade mounted on long wooden shaft.", //
				uncommonWeaponPrice, 2, AttackType.STRENGTH, Handedness.BOTH).addClass(ItemClass.LONG)//
						.damageRollBonus(5)//
						.requiredStrength(8).requiredAgility(2)//
						.add(itemAdder);
		new WeaponItemBuilder(KUKRI, "kukri", "kukris", "A type of machete with downward curved blade.", //
				uncommonWeaponPrice, 2, AttackType.STRENGTH, Handedness.RIGHT)//
						.damageRollBonus(5)//
						.requiredStrength(8)//
						.add(itemAdder);
		new WeaponItemBuilder(KUSARIGAMA, "kusarigama", "kusarigamas",
				"A kama with weight on a chain attached to the handle.", //
				uncommonWeaponPrice, 2, AttackType.AGILITY, Handedness.BOTH)//
						.damageRollBonus(3)//
						.requiredStrength(4).requiredAgility(7)//
						.add(itemAdder);
		new WeaponItemBuilder(LIGHT_CROSSBOW, "light crossbow", "light crossbows",
				"Lighter version of crossbow, can still pack a punch.", //
				uncommonWeaponPrice, 2, AttackType.AGILITY, Handedness.BOTH)//
						.addClasses(ItemClass.ARMOR_PIERCING, ItemClass.RANGED)//
						.damageRollBonus(3)//
						.requiredStrength(7).requiredAgility(4)//
						.add(itemAdder);
		new WeaponItemBuilder(LONG_BOW, "long bow", "long bows",
				"Standard military bow, can pierce armor from far away.", //
				uncommonWeaponPrice, 2, AttackType.AGILITY, Handedness.BOTH)//
						.addClasses(ItemClass.ARMOR_PIERCING, ItemClass.RANGED)//
						.damageRollBonus(3)//
						.requiredAgility(9)//
						.add(itemAdder);
		new WeaponItemBuilder(LONG_SWORD, "long sword", "long swords", "A sword used usually by proficient fighters.", //
				uncommonWeaponPrice, 2, AttackType.STRENGTH, Handedness.RIGHT)//
						.damageRollBonus(5)//
						.requiredStrength(7)//
						.add(itemAdder);
		new WeaponItemBuilder(MACHETE, "machete", "machetes", "A short weapon with single-edged blade.", //
				uncommonWeaponPrice, 2, AttackType.STRENGTH, Handedness.RIGHT)//
						.damageRollBonus(5)//
						.requiredStrength(7)//
						.add(itemAdder);
		new WeaponItemBuilder(NUNCHUCK, "nunchuck", "nunchucks", "Weapon of the legendary Bruce Lee, karate master.", //
				uncommonWeaponPrice, 2, AttackType.STRENGTH, Handedness.BOTH)//
						.damageRollBonus(4)//
						.requiredStrength(4).requiredAgility(6)//
						.add(itemAdder);
		new WeaponItemBuilder(RAPIER, "rapier", "rapiers", "Elegant weapon used usually for dueling.", //
				uncommonWeaponPrice, 2, AttackType.AGILITY, Handedness.RIGHT)//
						.damageRollBonus(3)//
						.criticalStrikeBonus(5)//
						.requiredStrength(4).requiredAgility(5)//
						.add(itemAdder);
		new WeaponItemBuilder(SPEAR, "spear", "spears", "Long stick with a sharp piece of metal at the end.", //
				uncommonWeaponPrice, 2, AttackType.AGILITY, Handedness.BOTH)//
						.addClass(ItemClass.LONG)//
						.damageRollBonus(2)//
						.requiredStrength(3).requiredAgility(4)//
						.add(itemAdder);
		new WeaponItemBuilder(SPIKED_KNUCKLES, "pair of spiked knuckles", "pairs of spiked knuckles",
				"Gloves with spikes protruding out of them.", //
				uncommonWeaponPrice, 2, AttackType.STRENGTH, Handedness.BOTH)//
						.damageRollBonus(3)//
						.requiredStrength(5)//
						.add(itemAdder);
		new WeaponItemBuilder(SPIKED_MACE, "spiked mace", "spiked maces", "Mace with spikes.", //
				uncommonWeaponPrice, 2, AttackType.STRENGTH, Handedness.BOTH).damageRollBonus(5)//
						.requiredStrength(7)//
						.add(itemAdder);
		new WeaponItemBuilder(THROWING_KNIVES, "set of throwing knives", "sets of throwing knives",
				"Well-balanced, throwable knives.", //
				uncommonWeaponPrice, 2, AttackType.AGILITY, Handedness.BOTH)//
						.addClass(ItemClass.RANGED)//
						.damageRollBonus(2)//
						.requiredAgility(6)//
						.add(itemAdder);
		new WeaponItemBuilder(TOMAHAWK, "tomahawk", "tomahawks", "A small axe that can be thrown.", //
				uncommonWeaponPrice, 2, AttackType.AGILITY, Handedness.RIGHT)//
						.addClasses(ItemClass.RANGED)//
						.damageRollBonus(2)//
						.requiredStrength(3).requiredAgility(5)//
						.add(itemAdder);
	}

	private static void addTier3Items(final Items items, final Consumer<ItemBuilder> itemAdder) {
		for (final GemType gemType : GemType.values()) {
			final Item gem = items.getItem(GemItems.getId(GemSize.LARGE, GemRefinement.REFINED, gemType));

			new WeaponItemBuilder(getArchmageSpellbookId(gemType), "archmage spellbook with " + gemType.color + " gem",
					"archmage books with " + gemType.color + " gems", //
					"Spellbook of an archmage, the only better thing would be something cursed.", //
					rareWeaponPrice + gem.price, 3, AttackType.STRENGTH, Handedness.BOTH)//
							.magicPower(8)//
							.requiredIntelligence(20)//
							.intelligenceBonus(5)//
							.add(itemAdder);
			new WeaponItemBuilder(getArchmageStaffId(gemType), "archmage staff with " + gemType.color + " gem",
					"archmage staffs with " + gemType.color + " gems", //
					"Staff with powerful gem, that concentrates energy, incrusted into the tip. Only the strongest warlocks can use it.", //
					rareWeaponPrice + gem.price, 3, AttackType.STRENGTH, Handedness.BOTH)//
							.damageRollBonus(7)//
							.magicPower(5)//
							.requiredStrength(9).requiredIntelligence(11)//
							.add(itemAdder);
		}

		new WeaponItemBuilder(BOLAS, "bolas", "bolas", "Three heavy balls attached to interjoined cord.", //
				rareWeaponPrice, 3, AttackType.STRENGTH, Handedness.RIGHT)//
						.addClass(ItemClass.RANGED)//
						.damageRollBonus(7)//
						.requiredStrength(9).requiredAgility(4)//
						.add(itemAdder);
		new WeaponItemBuilder(DWARVEN_AXE, "dwarven axe", "dwarven axes",
				"Axe of a dwarven fighter, the most brutal weapon you can wield if you are strong enough.", //
				rareWeaponPrice, 3, AttackType.STRENGTH, Handedness.BOTH)//
						.damageRollBonus(16)//
						.requiredStrength(20)//
						.add(itemAdder);
		new WeaponItemBuilder(DWARVEN_BOW, "dwarven bow", "dwarven bows",
				"Axe with short handle. But you can throw it!", //
				rareWeaponPrice, 3, AttackType.STRENGTH, Handedness.BOTH)//
						.image("https://cdn.discordapp.com/attachments/831093717376172032/963796258542215229/dwarven_bow.webp")//
						.addClass(ItemClass.RANGED)//
						.damageRollBonus(11)//
						.requiredStrength(15).requiredAgility(5)//
						.add(itemAdder);
		new WeaponItemBuilder(ELVEN_BOW, "elven bow", "elven bows",
				"Can be used only by the most skilled archers, but it can pierce heavy armor.", //
				rareWeaponPrice, 3, AttackType.AGILITY, Handedness.BOTH)//
						.addClasses(ItemClass.ARMOR_PIERCING, ItemClass.RANGED)//
						.damageRollBonus(8)//
						.requiredAgility(20)//
						.add(itemAdder);
		new WeaponItemBuilder(GAUNTLET, "gauntlet", "gauntlets", "Heavy metal gauntlet, providing substantial defense.", //
				rareWeaponPrice, 3, AttackType.STRENGTH, Handedness.RIGHT)//
						.damageRollBonus(7)//
						.armorBonus(2)//
						.requiredStrength(15)//
						.add(itemAdder);
		new WeaponItemBuilder(HEAVY_CROSSBOW, "heavy crossbow", "heavy crossbows",
				"Needs both strength and agility to use, but it can pierce heavy armor.", //
				rareWeaponPrice, 3, AttackType.AGILITY, Handedness.BOTH)//
						.addClasses(ItemClass.ARMOR_PIERCING, ItemClass.RANGED)//
						.damageRollBonus(10)//
						.requiredStrength(15).requiredAgility(5)//
						.add(itemAdder);
		new WeaponItemBuilder(HOOK_SWORD, "hook sword", "hook swords",
				"A sword with single-sided edge and hook at the end, as well as the crescent guard.", //
				rareWeaponPrice, 3, AttackType.AGILITY, Handedness.RIGHT)//
						.damageRollBonus(5)//
						.armorBonus(1)//
						.requiredStrength(6).requiredAgility(15)//
						.add(itemAdder);
		new WeaponItemBuilder(KATANA, "katana", "katanas", "A weapon with long, curved, single-edged blade.", //
				rareWeaponPrice, 3, AttackType.AGILITY, Handedness.BOTH)//
						.damageRollBonus(9)//
						.criticalStrikeBonus(10)//
						.requiredStrength(10).requiredAgility(12)//
						.add(itemAdder);
		new WeaponItemBuilder(MAGIC_SWORD, "magic sword", "magic swords",
				"Powerful sword that emanates magic, you can possibly kill gods with it.", //
				rareWeaponPrice, 3, AttackType.STRENGTH, Handedness.RIGHT)//
						.damageRollBonus(7)//
						.magicOnHitDamageBonus(2)//
						.requiredStrength(15)//
						.add(itemAdder);
		new WeaponItemBuilder(NECRONOMICON, "Necronomicon", "Necronomicons",
				"Copy of the famous dark magic book. Gives the user huge magic powers in exchange for his life energy.", //
				rareWeaponPrice * 5, 4, AttackType.STRENGTH, Handedness.BOTH)//
						.magicPower(20)//
						.requiredIntelligence(25)//
						.strengthBonus(-13).agilityBonus(-13).intelligenceBonus(13)//
						.add(itemAdder);
		new WeaponItemBuilder(RITUAL_DAGGER, "ritual dagger", "ritual daggers",
				"Dagger with runic inscriptions on it, better not check what it was used for before.", //
				rareWeaponPrice, 3, AttackType.AGILITY, Handedness.RIGHT)//
						.damageRollBonus(3)//
						.criticalStrikeBonus(20)//
						.requiredAgility(10).requiredIntelligence(5)//
						.add(itemAdder);
		new WeaponItemBuilder(RUNIC_SCIMITAR, "runic scimitar", "runic scimitars",
				"Scimitar with runes engraved on its handle.", //
				rareWeaponPrice, 3, AttackType.STRENGTH, Handedness.RIGHT)//
						.damageRollBonus(6)//
						.magicOnHitDamageBonus(2)//
						.armorBonus(1)//
						.requiredStrength(12).requiredAgility(4).requiredIntelligence(4)//
						.add(itemAdder);
		new WeaponItemBuilder(SHURIKENS, "set of shurikens", "sets of shurikens",
				"Small pieces of metal with sharp edges, used to be thrown at enemy.", //
				rareWeaponPrice, 3, AttackType.AGILITY, Handedness.BOTH)//
						.addClass(ItemClass.RANGED)//
						.damageRollBonus(7)//
						.requiredStrength(4).requiredAgility(11)//
						.add(itemAdder);
		new WeaponItemBuilder(TRIDENT, "trident", "tridents", "Spear-like weapon with three spikes on its end.", //
				rareWeaponPrice, 3, AttackType.STRENGTH, Handedness.RIGHT)//
						.addClass(ItemClass.RANGED)//
						.damageRollBonus(8)//
						.requiredStrength(9).requiredAgility(6)//
						.add(itemAdder);
	}

	private static void addShieldItems(final Consumer<ItemBuilder> itemAdder) {
		new ShieldItemBuilder(WOODEN_SHIELD, "wooden shield", "wooden shields",
				"Pretty sturdy and easy to wield shield.", //
				commonWeaponPrice, 1)//
						.armorBonus(1)//
						.agilityBonus(-1)//
						.add(itemAdder);

		new ShieldItemBuilder(METAL_SHIELD, "metal shield", "metal shields", "Heavy and sturdy shield.", //
				uncommonWeaponPrice, 2)//
						.armorBonus(3)//
						.requiredStrength(10)//
						.agilityBonus(-2)//
						.add(itemAdder);

		new ShieldItemBuilder(PALADIN_SHIELD, "paladin shield", "paladin shields",
				"Impenetrable shield of a true paladin, blessed by the goddess (there are still speculations about which one). Lighter than the usual shield.", //
				rareWeaponPrice, 3)//
						.armorBonus(4)//
						.requiredStrength(15)//
						.agilityBonus(-1)//
						.add(itemAdder);
	}

	public static void addItems(final Items items, final Consumer<ItemBuilder> itemAdder) {
		addTier1Items(items, itemAdder);
		addTier2Items(items, itemAdder);
		addTier3Items(items, itemAdder);
		addShieldItems(itemAdder);

		new WeaponItemBuilder(WOODEN_SWORD, "wooden sword", "wooden swords",
				"Used for practice, it's hard to hurt someone with it.", //
				trashWeaponPrice, 0, AttackType.STRENGTH, Handedness.RIGHT)//
						.add(itemAdder);
	}

}
