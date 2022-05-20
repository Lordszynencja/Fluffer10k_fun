package bot.commands.rpg.blacksmith.blueprints.data;

import static bot.commands.rpg.blacksmith.blueprints.utils.BlacksmithBlueprintResource.resourceGem;
import static bot.commands.rpg.blacksmith.blueprints.utils.BlacksmithBlueprintResource.resourceGold;
import static bot.commands.rpg.blacksmith.blueprints.utils.BlacksmithBlueprintResource.resourceItem;
import static bot.commands.rpg.blacksmith.blueprints.utils.ItemGenerator.normalItem;
import static bot.data.items.data.CraftingItems.EMPTY_BOOK;
import static bot.data.items.data.CraftingItems.WOOD;
import static bot.data.items.data.OreItems.ORE_COAL;
import static bot.data.items.data.OreItems.ORE_COPPER;
import static bot.data.items.data.PickaxeItems.PICKAXE_COPPER;
import static bot.data.items.data.WeaponItems.CHAINED_KUNAI;
import static bot.data.items.data.WeaponItems.DARTS;
import static bot.data.items.data.WeaponItems.JAVELIN;
import static bot.data.items.data.WeaponItems.KAMA;
import static bot.data.items.data.WeaponItems.KNIFE;
import static bot.data.items.data.WeaponItems.KUNAI;
import static bot.data.items.data.WeaponItems.LUMBERJACK_AXE;
import static bot.data.items.data.WeaponItems.SCYTHE;
import static bot.data.items.data.WeaponItems.SHORT_SWORD;
import static bot.data.items.data.WeaponItems.getApprenticeBookId;
import static bot.data.items.data.WeaponItems.getApprenticeStaffId;
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

public class Tier1BlacksmithBlueprints {
	private static void add(final String id, final String name, final ItemGenerator itemGenerator,
			final List<BlacksmithBlueprintResource> resources) {
		new BlacksmithBlueprintBuilder("BLUEPRINT_TIER_1_" + id)//
				.name(name)//
				.tier(BlacksmithTier.TIER_1)//
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

	private static void addApprenticeBooks(final Items items) {
		for (final GemTier tier : GemTier.values()) {
			final GemType exampleType = Stream.of(GemType.values()).filter(type -> type.tier == tier).findAny().get();

			final List<BlacksmithBlueprintResource> resources = new ArrayList<>();
			resources.add(resourceItem(EMPTY_BOOK, 1));
			resources.add(resourceGem(GemSize.SMALL, tier));
			addPayment(items, getApprenticeBookId(exampleType), resources);

			new BlacksmithBlueprintBuilder("BLUEPRINT_TIER_1_APPRENTICE_BOOK_" + tier.name())//
					.name(tier.name + " tier apprentice book blueprint")//
					.tier(BlacksmithTier.TIER_1)//
					.itemGenerator((fluffer10kFun, pickedItemsUsed) -> {
						final GemType gemType = pickedItemsUsed.get(0).gemType;
						return items.getItem(getApprenticeBookId(gemType));
					})//
					.resources(resources)//
					.add();
		}
	}

	private static void addApprenticeStaffs(final Items items) {
		for (final GemTier tier : GemTier.values()) {
			final GemType exampleType = Stream.of(GemType.values()).filter(type -> type.tier == tier).findAny().get();

			final List<BlacksmithBlueprintResource> resources = new ArrayList<>();
			resources.add(resourceItem(WOOD, 10));
			resources.add(resourceGem(GemSize.SMALL, tier));
			addPayment(items, getApprenticeStaffId(exampleType), resources);

			new BlacksmithBlueprintBuilder("BLUEPRINT_TIER_1_APPRENTICE_STAFF_" + tier.name())//
					.name(tier.name + " tier apprentice staff blueprint")//
					.tier(BlacksmithTier.TIER_1)//
					.itemGenerator((fluffer10kFun, pickedItemsUsed) -> {
						final GemType gemType = pickedItemsUsed.get(0).gemType;
						return items.getItem(getApprenticeStaffId(gemType));
					})//
					.resources(resources)//
					.add();
		}
	}

	public static void addBlueprints(final Items items) {
		addApprenticeBooks(items);
		addApprenticeStaffs(items);

		add(items, CHAINED_KUNAI, //
				pair(ORE_COAL, 15L), //
				pair(ORE_COPPER, 15L));
		add(items, DARTS, //
				pair(ORE_COAL, 15L), //
				pair(ORE_COPPER, 15L));
		add(items, JAVELIN, //
				pair(WOOD, 2L), //
				pair(ORE_COAL, 5L), //
				pair(ORE_COPPER, 5L));
		add(items, KAMA, //
				pair(WOOD, 1L), //
				pair(ORE_COAL, 7L), //
				pair(ORE_COPPER, 7L));
		add(items, KNIFE, //
				pair(WOOD, 1L), //
				pair(ORE_COAL, 7L), //
				pair(ORE_COPPER, 7L));
		add(items, KUNAI, //
				pair(ORE_COAL, 10L), //
				pair(ORE_COPPER, 10L));
		add(items, LUMBERJACK_AXE, //
				pair(WOOD, 2L), //
				pair(ORE_COAL, 10L), //
				pair(ORE_COPPER, 10L));
		add(items, SCYTHE, //
				pair(WOOD, 2L), //
				pair(ORE_COAL, 10L), //
				pair(ORE_COPPER, 10L));
		add(items, SHORT_SWORD, //
				pair(WOOD, 1L), //
				pair(ORE_COAL, 15L), //
				pair(ORE_COPPER, 15L));

		add(items, PICKAXE_COPPER, //
				pair(WOOD, 5L), //
				pair(ORE_COAL, 25L), //
				pair(ORE_COPPER, 25L));
	}
}