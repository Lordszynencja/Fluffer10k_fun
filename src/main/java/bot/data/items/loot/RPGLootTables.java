package bot.data.items.loot;

import static bot.data.items.data.ArmorItems.CHAINMAIL;
import static bot.data.items.data.ArmorItems.LEATHER_ARMOR;
import static bot.data.items.data.ArmorItems.PLATE_ARMOR;
import static bot.data.items.data.ArmorItems.RING_MAIL;
import static bot.data.items.data.MagicScrollItems.BOOK_OF_OFFENSE_BASH;
import static bot.data.items.data.MagicScrollItems.BOOK_OF_OFFENSE_DOUBLE_STRIKE;
import static bot.data.items.data.MagicScrollItems.BOOK_OF_OFFENSE_GOUGE;
import static bot.data.items.data.MagicScrollItems.BOOK_OF_OFFENSE_PRECISE_STRIKE;
import static bot.data.items.data.MagicScrollItems.MAGIC_SCROLL_BLIZZARD;
import static bot.data.items.data.MagicScrollItems.MAGIC_SCROLL_FIERY_WEAPON;
import static bot.data.items.data.MagicScrollItems.MAGIC_SCROLL_FIREBALL;
import static bot.data.items.data.MagicScrollItems.MAGIC_SCROLL_FORCE_HIT;
import static bot.data.items.data.MagicScrollItems.MAGIC_SCROLL_FREEZE;
import static bot.data.items.data.MagicScrollItems.MAGIC_SCROLL_HEAL;
import static bot.data.items.data.MagicScrollItems.MAGIC_SCROLL_HOLY_AURA;
import static bot.data.items.data.MagicScrollItems.MAGIC_SCROLL_ICE_BOLT;
import static bot.data.items.data.MagicScrollItems.MAGIC_SCROLL_LIGHTNING;
import static bot.data.items.data.MagicScrollItems.MAGIC_SCROLL_MAGIC_SHIELD;
import static bot.data.items.data.MagicScrollItems.MAGIC_SCROLL_METEORITE;
import static bot.data.items.data.MagicScrollItems.MAGIC_SCROLL_RAGE;
import static bot.data.items.data.MagicScrollItems.MAGIC_SCROLL_SLEEP;
import static bot.data.items.data.MagicScrollItems.MAGIC_SCROLL_SPEED_OF_WIND;
import static bot.data.items.data.MagicScrollItems.MAGIC_SCROLL_WHIRLPOOL;
import static bot.data.items.data.MonmusuDropItems.COUPLES_FRUIT;
import static bot.data.items.data.MonmusuDropItems.INTOXICATION_FRUIT;
import static bot.data.items.data.MonmusuDropItems.PRISONER_FRUIT;
import static bot.data.items.data.MonmusuDropItems.RAGING_MUSHROOM;
import static bot.data.items.data.MonmusuDropItems.STICKY_MUSHROOM;
import static bot.data.items.data.MonmusuDropItems.SUCCUBUS_NOSTRUM;
import static bot.data.items.data.PotionItems.AGILITY_1_POTION;
import static bot.data.items.data.PotionItems.AGILITY_2_POTION;
import static bot.data.items.data.PotionItems.AGILITY_3_POTION;
import static bot.data.items.data.PotionItems.DOPPELGANGER_POTION;
import static bot.data.items.data.PotionItems.HEALTH_POTION;
import static bot.data.items.data.PotionItems.HEALTH_POTION_MAJOR;
import static bot.data.items.data.PotionItems.HEALTH_POTION_MINOR;
import static bot.data.items.data.PotionItems.INTELLIGENCE_1_POTION;
import static bot.data.items.data.PotionItems.INTELLIGENCE_2_POTION;
import static bot.data.items.data.PotionItems.INTELLIGENCE_3_POTION;
import static bot.data.items.data.PotionItems.MANA_POTION;
import static bot.data.items.data.PotionItems.MANA_POTION_MAJOR;
import static bot.data.items.data.PotionItems.MANA_POTION_MINOR;
import static bot.data.items.data.PotionItems.STRENGTH_1_POTION;
import static bot.data.items.data.PotionItems.STRENGTH_2_POTION;
import static bot.data.items.data.PotionItems.STRENGTH_3_POTION;
import static bot.data.items.data.RingItems.GOLDEN_RING;
import static bot.data.items.data.RingItems.REGENERATION_RING;
import static bot.data.items.data.RingItems.SILVER_RING;
import static bot.data.items.data.RingItems.SPELL_VOID_RING;
import static bot.data.items.data.RingItems.STATUS_NEGATION_RING;
import static bot.data.items.data.RingItems.WOODEN_RING;
import static bot.data.items.data.WeaponItems.BATTLE_AXE;
import static bot.data.items.data.WeaponItems.BLOWGUN;
import static bot.data.items.data.WeaponItems.BOLAS;
import static bot.data.items.data.WeaponItems.BROADSWORD;
import static bot.data.items.data.WeaponItems.CHAINED_KUNAI;
import static bot.data.items.data.WeaponItems.CLAYMORE;
import static bot.data.items.data.WeaponItems.DAGGER;
import static bot.data.items.data.WeaponItems.DARTS;
import static bot.data.items.data.WeaponItems.DWARVEN_AXE;
import static bot.data.items.data.WeaponItems.DWARVEN_BOW;
import static bot.data.items.data.WeaponItems.ELVEN_BOW;
import static bot.data.items.data.WeaponItems.ESTOC;
import static bot.data.items.data.WeaponItems.GAUNTLET;
import static bot.data.items.data.WeaponItems.GLAIVE;
import static bot.data.items.data.WeaponItems.HALBERD;
import static bot.data.items.data.WeaponItems.HEAVY_CROSSBOW;
import static bot.data.items.data.WeaponItems.HOOK_SWORD;
import static bot.data.items.data.WeaponItems.JAVELIN;
import static bot.data.items.data.WeaponItems.KAMA;
import static bot.data.items.data.WeaponItems.KATANA;
import static bot.data.items.data.WeaponItems.KNIFE;
import static bot.data.items.data.WeaponItems.KUKRI;
import static bot.data.items.data.WeaponItems.KUNAI;
import static bot.data.items.data.WeaponItems.KUSARIGAMA;
import static bot.data.items.data.WeaponItems.LIGHT_CROSSBOW;
import static bot.data.items.data.WeaponItems.LONG_BOW;
import static bot.data.items.data.WeaponItems.LONG_SWORD;
import static bot.data.items.data.WeaponItems.LUMBERJACK_AXE;
import static bot.data.items.data.WeaponItems.MACHETE;
import static bot.data.items.data.WeaponItems.MAGIC_SWORD;
import static bot.data.items.data.WeaponItems.METAL_SHIELD;
import static bot.data.items.data.WeaponItems.NECRONOMICON;
import static bot.data.items.data.WeaponItems.NUNCHUCK;
import static bot.data.items.data.WeaponItems.PALADIN_SHIELD;
import static bot.data.items.data.WeaponItems.RAPIER;
import static bot.data.items.data.WeaponItems.RITUAL_DAGGER;
import static bot.data.items.data.WeaponItems.RUNIC_SCIMITAR;
import static bot.data.items.data.WeaponItems.SCYTHE;
import static bot.data.items.data.WeaponItems.SHORT_BOW;
import static bot.data.items.data.WeaponItems.SHORT_SWORD;
import static bot.data.items.data.WeaponItems.SHURIKENS;
import static bot.data.items.data.WeaponItems.SLINGSHOT;
import static bot.data.items.data.WeaponItems.SPEAR;
import static bot.data.items.data.WeaponItems.SPIKED_KNUCKLES;
import static bot.data.items.data.WeaponItems.SPIKED_MACE;
import static bot.data.items.data.WeaponItems.THROWING_KNIVES;
import static bot.data.items.data.WeaponItems.TOMAHAWK;
import static bot.data.items.data.WeaponItems.TRIDENT;
import static bot.data.items.data.WeaponItems.WHIP;
import static bot.data.items.data.WeaponItems.WOODEN_SHIELD;
import static bot.data.items.data.WeaponItems.WOODEN_SWORD;
import static bot.data.items.data.WeaponItems.getApprenticeBookId;
import static bot.data.items.data.WeaponItems.getApprenticeStaffId;
import static bot.data.items.data.WeaponItems.getArchmageSpellbookId;
import static bot.data.items.data.WeaponItems.getArchmageStaffId;
import static bot.data.items.data.WeaponItems.getMageSpellbookId;
import static bot.data.items.data.WeaponItems.getMageStaffId;
import static bot.data.items.loot.LootTable.listT;
import static bot.data.items.loot.LootTable.weightedI;
import static bot.data.items.loot.LootTable.weightedTI;
import static bot.util.CollectionUtils.mapToList;
import static bot.util.Utils.Pair.pair;
import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

import bot.data.items.data.GemItems.GemType;
import bot.util.Utils.Pair;

public class RPGLootTables {
	private static LootTable<Loot> lootTable(final List<String> itemIds) {
		return LootTable.list(mapToList(itemIds, Loot::item));
	}

	private static LootTable<Loot> lootTable(final String... itemIds) {
		return lootTable(asList(itemIds));
	}

	/////////////
	// WEAPONS //
	/////////////
	private static final List<String> commonWeapons = new ArrayList<>(asList(//
			CHAINED_KUNAI, //
			DARTS, //
			JAVELIN, //
			KNIFE, //
			KUNAI, //
			LUMBERJACK_AXE, //
			SCYTHE, //
			SLINGSHOT, //
			SHORT_BOW, //
			SHORT_SWORD, //
			WHIP));
	private static final List<String> uncommonWeapons = new ArrayList<>(asList(//
			BATTLE_AXE, //
			BLOWGUN, //
			BROADSWORD, //
			CLAYMORE, //
			DAGGER, //
			ESTOC, //
			GLAIVE, //
			HALBERD, //
			KAMA, //
			KUKRI, //
			KUSARIGAMA, //
			LIGHT_CROSSBOW, //
			LONG_BOW, //
			LONG_SWORD, //
			MACHETE, //
			NUNCHUCK, //
			RAPIER, //
			SPEAR, //
			SPIKED_KNUCKLES, //
			SPIKED_MACE, //
			THROWING_KNIVES, //
			TOMAHAWK));
	private static final List<String> rareWeapons = new ArrayList<>(asList(//
			BOLAS, //
			DWARVEN_AXE, //
			DWARVEN_BOW, //
			ELVEN_BOW, //
			GAUNTLET, //
			HEAVY_CROSSBOW, //
			HOOK_SWORD, //
			KATANA, //
			MAGIC_SWORD, //
			NECRONOMICON, //
			RITUAL_DAGGER, //
			RUNIC_SCIMITAR, //
			SHURIKENS, //
			TRIDENT));
	static {
		for (final GemType gemType : GemType.values()) {
			commonWeapons.add(getApprenticeBookId(gemType));
			commonWeapons.add(getApprenticeStaffId(gemType));
			uncommonWeapons.add(getMageSpellbookId(gemType));
			uncommonWeapons.add(getMageStaffId(gemType));
			rareWeapons.add(getArchmageSpellbookId(gemType));
			rareWeapons.add(getArchmageStaffId(gemType));
		}
	}

	private static final LootTable<Loot> trashWeaponsLootTable = lootTable(WOODEN_SWORD);
	private static final LootTable<Loot> commonWeaponsLootTable = lootTable(commonWeapons);
	private static final LootTable<Loot> uncommonWeaponsLootTable = lootTable(uncommonWeapons);
	private static final LootTable<Loot> rareWeaponsLootTable = lootTable(rareWeapons);

	////////
	// EQ //
	////////
	private static final LootTable<Loot> trashEqLootTable = lootTable(//
			WOODEN_RING);
	private static final LootTable<Loot> commonEqLootTable = lootTable(//
			LEATHER_ARMOR, //
			WOODEN_SHIELD);
	private static final LootTable<Loot> uncommonEqLootTable = lootTable(//
			CHAINMAIL, //
			METAL_SHIELD, //
			REGENERATION_RING, //
			RING_MAIL, //
			SILVER_RING, //
			SPELL_VOID_RING, //
			STATUS_NEGATION_RING);
	private static final LootTable<Loot> rareEqLootTable = lootTable(//
			GOLDEN_RING, //
			PALADIN_SHIELD, //
			PLATE_ARMOR);

	/////////////
	// POTIONS //
	/////////////
	private static final LootTable<Loot> minorPotionsLootTable = weightedTI(//
			pair(4, lootTable(//
					HEALTH_POTION_MINOR, //
					MANA_POTION_MINOR)), //
			pair(1, lootTable(//
					AGILITY_1_POTION, //
					STRENGTH_1_POTION, //
					INTELLIGENCE_1_POTION)));
	private static final LootTable<Loot> mediumPotionsLootTable = weightedTI(//
			pair(4, lootTable(//
					HEALTH_POTION, //
					MANA_POTION)), //
			pair(1, lootTable(//
					AGILITY_2_POTION, //
					STRENGTH_2_POTION, //
					INTELLIGENCE_2_POTION, //
					DOPPELGANGER_POTION)));
	private static final LootTable<Loot> majorPotionsLootTable = weightedTI(//
			pair(4, lootTable(//
					HEALTH_POTION_MAJOR, //
					MANA_POTION_MAJOR)), //
			pair(1, lootTable(//
					AGILITY_3_POTION, //
					STRENGTH_3_POTION, //
					INTELLIGENCE_3_POTION)));

	/////////////
	// SCROLLS //
	/////////////
	private static final LootTable<Loot> commonScrollsLootTable = lootTable(//
			BOOK_OF_OFFENSE_GOUGE, //
			BOOK_OF_OFFENSE_PRECISE_STRIKE, //
			MAGIC_SCROLL_HEAL, //
			MAGIC_SCROLL_FORCE_HIT);
	private static final LootTable<Loot> uncommonScrollsLootTable = lootTable(//
			BOOK_OF_OFFENSE_BASH, //
			BOOK_OF_OFFENSE_DOUBLE_STRIKE, //
			MAGIC_SCROLL_FIERY_WEAPON, //
			MAGIC_SCROLL_FIREBALL, //
			MAGIC_SCROLL_HOLY_AURA, //
			MAGIC_SCROLL_ICE_BOLT, //
			MAGIC_SCROLL_LIGHTNING, //
			MAGIC_SCROLL_MAGIC_SHIELD, //
			MAGIC_SCROLL_RAGE, //
			MAGIC_SCROLL_SLEEP, //
			MAGIC_SCROLL_SPEED_OF_WIND);
	private static final LootTable<Loot> rareScrollsLootTable = lootTable(//
			MAGIC_SCROLL_BLIZZARD, //
			MAGIC_SCROLL_FREEZE, //
			MAGIC_SCROLL_METEORITE, //
			MAGIC_SCROLL_WHIRLPOOL);

	//////////
	// GOLD //
	//////////
	private static final LootTable<Loot> trashGoldLootTable = weightedI(//
			pair(1, Loot.gold(20)), //
			pair(5, Loot.gold(10)), //
			pair(10, Loot.gold(5)), //
			pair(5, Loot.gold(2)), //
			pair(1, Loot.gold(1)));
	private static final LootTable<Loot> commonGoldLootTable = weightedI(//
			pair(1, Loot.gold(200)), //
			pair(5, Loot.gold(100)), //
			pair(10, Loot.gold(50)), //
			pair(5, Loot.gold(20)), //
			pair(1, Loot.gold(10)));
	private static final LootTable<Loot> uncommonGoldLootTable = weightedI(//
			pair(1, Loot.gold(1000)), //
			pair(5, Loot.gold(500)), //
			pair(10, Loot.gold(200)), //
			pair(5, Loot.gold(100)), //
			pair(1, Loot.gold(50)));
	private static final LootTable<Loot> rareGoldLootTable = weightedI(//
			pair(1, Loot.gold(5000)), //
			pair(5, Loot.gold(2000)), //
			pair(10, Loot.gold(1000)), //
			pair(5, Loot.gold(500)), //
			pair(1, Loot.gold(200)));

	/////////////
	// SPECIAL //
	/////////////
	private static final LootTable<Loot> commonSpecialLootTable = lootTable(//
			COUPLES_FRUIT, //
			PRISONER_FRUIT);
	private static final LootTable<Loot> uncommonSpecialLootTable = lootTable(//
			INTOXICATION_FRUIT);
	private static final LootTable<Loot> rareSpecialLootTable = lootTable(//
			SUCCUBUS_NOSTRUM, //
			RAGING_MUSHROOM, //
			STICKY_MUSHROOM);

	////////////
	// TABLES //
	////////////
	private static final LootTable<Loot> trashItemsTable = listT(//
			trashWeaponsLootTable, //
			trashEqLootTable);
	private static final LootTable<Loot> commonItemsTable = listT(//
			commonWeaponsLootTable, //
			commonEqLootTable, //
			minorPotionsLootTable, //
			commonScrollsLootTable, //
			commonSpecialLootTable);
	private static final LootTable<Loot> uncommonItemsTable = listT(//
			uncommonWeaponsLootTable, //
			uncommonEqLootTable, //
			mediumPotionsLootTable, //
			uncommonScrollsLootTable, //
			uncommonSpecialLootTable);
	private static final LootTable<Loot> rareItemsTable = listT(//
			rareWeaponsLootTable, //
			rareEqLootTable, //
			majorPotionsLootTable, //
			rareScrollsLootTable, //
			rareSpecialLootTable);

	private static final LootTable<Loot> trashLootTable = weightedTI(//
			pair(1, trashItemsTable), //
			pair(15, trashGoldLootTable));
	private static final LootTable<Loot> commonLootTable = weightedTI(//
			pair(1, commonItemsTable), //
			pair(14, commonGoldLootTable));
	private static final LootTable<Loot> uncommonLootTable = weightedTI(//
			pair(1, uncommonItemsTable), //
			pair(13, uncommonGoldLootTable));
	private static final LootTable<Loot> rareLootTable = weightedTI(//
			pair(1, rareItemsTable), //
			pair(12, rareGoldLootTable));
	private static final LootTable<Loot> superRare1LootTable = weightedTI(//
			pair(1, rareItemsTable), //
			pair(11, rareGoldLootTable));
	private static final LootTable<Loot> superRare2LootTable = weightedTI(//
			pair(1, rareItemsTable), //
			pair(10, rareGoldLootTable));
	private static final LootTable<Loot> superRare3LootTable = weightedTI(//
			pair(1, rareItemsTable), //
			pair(9, rareGoldLootTable));

	private static final List<LootTable<Loot>> tableOrder = asList(//
			trashLootTable, //
			commonLootTable, //
			uncommonLootTable, //
			rareLootTable, //
			superRare1LootTable, //
			superRare2LootTable, //
			superRare3LootTable);

	public static Loot getRandomLoot(final int[] tableWeights) {
		final List<Pair<Integer, LootTable<Loot>>> weightedTables = new ArrayList<>();
		for (int i = 0; i < tableWeights.length && i < tableOrder.size(); i++) {
			weightedTables.add(pair(tableWeights[i], tableOrder.get(i)));
		}

		return weightedTI(weightedTables).getItem();
	}
}
