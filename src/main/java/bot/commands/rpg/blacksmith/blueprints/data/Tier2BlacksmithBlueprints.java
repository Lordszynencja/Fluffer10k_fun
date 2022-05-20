package bot.commands.rpg.blacksmith.blueprints.data;

import static bot.commands.rpg.blacksmith.blueprints.utils.BlacksmithBlueprintResource.resourceGem;
import static bot.commands.rpg.blacksmith.blueprints.utils.BlacksmithBlueprintResource.resourceGold;
import static bot.commands.rpg.blacksmith.blueprints.utils.BlacksmithBlueprintResource.resourceItem;
import static bot.commands.rpg.blacksmith.blueprints.utils.ItemGenerator.normalItem;
import static bot.data.items.data.ArmorItems.CHAINMAIL;
import static bot.data.items.data.ArmorItems.RING_MAIL;
import static bot.data.items.data.CraftingItems.EMPTY_BOOK;
import static bot.data.items.data.CraftingItems.WOOD;
import static bot.data.items.data.OreItems.ORE_COAL;
import static bot.data.items.data.OreItems.ORE_GOLD;
import static bot.data.items.data.OreItems.ORE_IRON;
import static bot.data.items.data.OreItems.ORE_SILVER;
import static bot.data.items.data.PickaxeItems.PICKAXE_IRON;
import static bot.data.items.data.RingItems.GOLDEN_RING;
import static bot.data.items.data.RingItems.SILVER_RING;
import static bot.data.items.data.WeaponItems.BATTLE_AXE;
import static bot.data.items.data.WeaponItems.BROADSWORD;
import static bot.data.items.data.WeaponItems.CHAIN_WHIP;
import static bot.data.items.data.WeaponItems.CLAYMORE;
import static bot.data.items.data.WeaponItems.DAGGER;
import static bot.data.items.data.WeaponItems.ESTOC;
import static bot.data.items.data.WeaponItems.GLAIVE;
import static bot.data.items.data.WeaponItems.HALBERD;
import static bot.data.items.data.WeaponItems.KUKRI;
import static bot.data.items.data.WeaponItems.KUSARIGAMA;
import static bot.data.items.data.WeaponItems.LIGHT_CROSSBOW;
import static bot.data.items.data.WeaponItems.LONG_SWORD;
import static bot.data.items.data.WeaponItems.MACHETE;
import static bot.data.items.data.WeaponItems.METAL_SHIELD;
import static bot.data.items.data.WeaponItems.NUNCHUCK;
import static bot.data.items.data.WeaponItems.RAPIER;
import static bot.data.items.data.WeaponItems.SPEAR;
import static bot.data.items.data.WeaponItems.SPIKED_KNUCKLES;
import static bot.data.items.data.WeaponItems.SPIKED_MACE;
import static bot.data.items.data.WeaponItems.THROWING_KNIVES;
import static bot.data.items.data.WeaponItems.TOMAHAWK;
import static bot.data.items.data.WeaponItems.getMageSpellbookId;
import static bot.data.items.data.WeaponItems.getMageStaffId;
import static bot.util.Utils.Pair.pair;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import bot.commands.rpg.blacksmith.blueprints.utils.BlacksmithBlueprintBuilder;
import bot.commands.rpg.blacksmith.blueprints.utils.BlacksmithBlueprintResource;
import bot.commands.rpg.blacksmith.blueprints.utils.ItemGenerator;
import bot.commands.rpg.blacksmith.tasks.BlacksmithTier;
import bot.data.items.Items;
import bot.data.items.data.GemItems.GemSize;
import bot.data.items.data.GemItems.GemTier;
import bot.data.items.data.GemItems.GemType;
import bot.util.Utils.Pair;

public class Tier2BlacksmithBlueprints {
	private static void add(final String id, final String name, final ItemGenerator itemGenerator,
			final List<BlacksmithBlueprintResource> resources) {
		new BlacksmithBlueprintBuilder("BLUEPRINT_TIER_2_" + id)//
				.name(name)//
				.tier(BlacksmithTier.TIER_2)//
				.itemGenerator(itemGenerator)//
				.resources(resources)//
				.add();
	}

	private static void addPayment(final Items items, final String itemId,
			final List<BlacksmithBlueprintResource> resources) {
		long totalPrice = (long) (items.getItem(itemId).price * 0.75);
		for (final BlacksmithBlueprintResource resource : resources) {
			totalPrice -= resource.value(items);
		}
		resources.add(resourceGold(totalPrice));
	}

	@SafeVarargs
	private static List<BlacksmithBlueprintResource> getResourcesList(final Items items, final String itemId,
			final Pair<String, Long>... resources) {
		final List<BlacksmithBlueprintResource> resourcesList = new ArrayList<>();
		for (final Pair<String, Long> resource : resources) {
			resourcesList.add(resourceItem(resource.a, resource.b));
		}
		addPayment(items, itemId, resourcesList);

		return resourcesList;
	}

	@SafeVarargs
	private static void add(final Items items, final String itemId, final Pair<String, Long>... resources) {
		add(itemId, items.getName(itemId) + " blueprint", normalItem(itemId),
				getResourcesList(items, itemId, resources));
	}

	private static void addMageSpellbooks(final Items items) {
		for (final GemTier tier : GemTier.values()) {
			final GemType exampleType = Stream.of(GemType.values()).filter(type -> type.tier == tier).findAny().get();

			final List<BlacksmithBlueprintResource> resources = new ArrayList<>();
			resources.add(resourceItem(EMPTY_BOOK, 1));
			resources.add(resourceItem(ORE_COAL, 2));
			resources.add(resourceItem(ORE_SILVER, 2));
			resources.add(resourceGem(GemSize.MEDIUM, tier));
			addPayment(items, getMageSpellbookId(exampleType), resources);

			new BlacksmithBlueprintBuilder("BLUEPRINT_TIER_2_MAGE_SPELLBOOK_" + tier.name())//
					.name(tier.name + " tier mage spellbook blueprint")//
					.tier(BlacksmithTier.TIER_2)//
					.itemGenerator((fluffer10kFun, pickedItemsUsed) -> {
						final GemType gemType = pickedItemsUsed.get(0).gemType;
						return items.getItem(getMageSpellbookId(gemType));
					})//
					.resources(resources)//
					.add();
		}
	}

	private static void addMageStaffs(final Items items) {
		for (final GemTier tier : GemTier.values()) {
			final GemType exampleType = Stream.of(GemType.values()).filter(type -> type.tier == tier).findAny().get();

			final List<BlacksmithBlueprintResource> resources = new ArrayList<>();
			resources.add(resourceItem(WOOD, 10));
			resources.add(resourceItem(ORE_COAL, 2));
			resources.add(resourceItem(ORE_SILVER, 2));
			resources.add(resourceGem(GemSize.MEDIUM, tier));
			addPayment(items, getMageStaffId(exampleType), resources);

			new BlacksmithBlueprintBuilder("BLUEPRINT_TIER_2_MAGE_STAFF_" + tier.name())//
					.name(tier.name + " tier mage staff blueprint")//
					.tier(BlacksmithTier.TIER_2)//
					.itemGenerator((fluffer10kFun, pickedItemsUsed) -> {
						final GemType gemType = pickedItemsUsed.get(0).gemType;
						return items.getItem(getMageStaffId(gemType));
					})//
					.resources(resources)//
					.add();
		}
	}

	public static void addBlueprints(final Items items) {
		addMageSpellbooks(items);
		addMageStaffs(items);

		add(items, BATTLE_AXE, //
				pair(ORE_COAL, 15L), //
				pair(ORE_IRON, 15L));
		add(items, BROADSWORD, //
				pair(ORE_COAL, 12L), //
				pair(ORE_IRON, 12L));
		add(items, CHAIN_WHIP, //
				pair(ORE_COAL, 15L), //
				pair(ORE_IRON, 15L));
		add(items, CLAYMORE, //
				pair(ORE_COAL, 15L), //
				pair(ORE_IRON, 15L));
		add(items, DAGGER, //
				pair(ORE_COAL, 7L), //
				pair(ORE_IRON, 7L));
		add(items, ESTOC, //
				pair(ORE_COAL, 12L), //
				pair(ORE_IRON, 12L));
		add(items, GLAIVE, //
				pair(WOOD, 5L), //
				pair(ORE_COAL, 10L), //
				pair(ORE_IRON, 10L));
		add(items, HALBERD, //
				pair(WOOD, 5L), //
				pair(ORE_COAL, 10L), //
				pair(ORE_IRON, 10L));
		add(items, KUKRI, //
				pair(ORE_COAL, 12L), //
				pair(ORE_IRON, 12L));
		add(items, KUSARIGAMA, //
				pair(ORE_COAL, 12L), //
				pair(ORE_IRON, 12L));
		add(items, LIGHT_CROSSBOW, //
				pair(WOOD, 8L), //
				pair(ORE_COAL, 10L), //
				pair(ORE_IRON, 10L));
		add(items, LONG_SWORD, //
				pair(ORE_COAL, 12L), //
				pair(ORE_IRON, 12L));
		add(items, MACHETE, //
				pair(ORE_COAL, 12L), //
				pair(ORE_IRON, 12L));
		add(items, METAL_SHIELD, //
				pair(ORE_COAL, 25L), //
				pair(ORE_IRON, 25L));
		add(items, NUNCHUCK, //
				pair(ORE_COAL, 15L), //
				pair(ORE_IRON, 15L));
		add(items, RAPIER, //
				pair(ORE_COAL, 10L), //
				pair(ORE_IRON, 10L));
		add(items, SPEAR, //
				pair(WOOD, 10L), //
				pair(ORE_COAL, 5L), //
				pair(ORE_IRON, 5L));
		add(items, SPIKED_KNUCKLES, //
				pair(ORE_COAL, 20L), //
				pair(ORE_IRON, 20L));
		add(items, SPIKED_MACE, //
				pair(ORE_COAL, 15L), //
				pair(ORE_IRON, 15L));
		add(items, THROWING_KNIVES, //
				pair(ORE_COAL, 10L), //
				pair(ORE_IRON, 10L));
		add(items, TOMAHAWK, //
				pair(WOOD, 2L), //
				pair(ORE_COAL, 10L), //
				pair(ORE_IRON, 10L));

		add(items, SILVER_RING, //
				pair(ORE_COAL, 1L), //
				pair(ORE_SILVER, 1L));

		add(items, GOLDEN_RING, //
				pair(ORE_COAL, 1L), //
				pair(ORE_GOLD, 1L));

		add(items, RING_MAIL, //
				pair(ORE_COAL, 30L), //
				pair(ORE_IRON, 30L));
		add(items, CHAINMAIL, //
				pair(ORE_COAL, 60L), //
				pair(ORE_IRON, 60L));

		add(items, PICKAXE_IRON, //
				pair(WOOD, 5L), //
				pair(ORE_COAL, 25L), //
				pair(ORE_IRON, 25L));
	}
}
