package bot.commands.rpg.blacksmith.blueprints.data;

import static bot.commands.rpg.blacksmith.blueprints.utils.BlacksmithBlueprintResource.resourceGem;
import static bot.commands.rpg.blacksmith.blueprints.utils.BlacksmithBlueprintResource.resourceGold;
import static bot.commands.rpg.blacksmith.blueprints.utils.BlacksmithBlueprintResource.resourceItem;
import static bot.commands.rpg.blacksmith.blueprints.utils.ItemGenerator.normalItem;
import static bot.data.items.data.ArmorItems.PLATE_ARMOR;
import static bot.data.items.data.CraftingItems.EMPTY_BOOK;
import static bot.data.items.data.CraftingItems.WOOD;
import static bot.data.items.data.OreItems.ORE_COAL;
import static bot.data.items.data.OreItems.ORE_DEMON_REALM_SILVER;
import static bot.data.items.data.OreItems.ORE_DRAGONIUM;
import static bot.data.items.data.OreItems.ORE_GOLD;
import static bot.data.items.data.OreItems.ORE_IRON;
import static bot.data.items.data.OreItems.ORE_SILVER;
import static bot.data.items.data.PickaxeItems.PICKAXE_DRAGONIUM;
import static bot.data.items.data.WeaponItems.BOLAS;
import static bot.data.items.data.WeaponItems.DWARVEN_AXE;
import static bot.data.items.data.WeaponItems.DWARVEN_BOW;
import static bot.data.items.data.WeaponItems.ELVEN_BOW;
import static bot.data.items.data.WeaponItems.GAUNTLET;
import static bot.data.items.data.WeaponItems.HEAVY_CROSSBOW;
import static bot.data.items.data.WeaponItems.HOOK_SWORD;
import static bot.data.items.data.WeaponItems.KATANA;
import static bot.data.items.data.WeaponItems.MAGIC_SWORD;
import static bot.data.items.data.WeaponItems.PALADIN_SHIELD;
import static bot.data.items.data.WeaponItems.RITUAL_DAGGER;
import static bot.data.items.data.WeaponItems.RUNIC_SCIMITAR;
import static bot.data.items.data.WeaponItems.SHURIKENS;
import static bot.data.items.data.WeaponItems.TRIDENT;
import static bot.data.items.data.WeaponItems.getArchmageSpellbookId;
import static bot.data.items.data.WeaponItems.getArchmageStaffId;
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

public class Tier3BlacksmithBlueprints {
	private static void add(final String id, final String name, final ItemGenerator itemGenerator,
			final List<BlacksmithBlueprintResource> resources) {
		new BlacksmithBlueprintBuilder("BLUEPRINT_TIER_3_" + id)//
				.name(name)//
				.tier(BlacksmithTier.TIER_3)//
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

	private static void addArchmageSpellbooks(final Items items) {
		for (final GemTier tier : GemTier.values()) {
			final GemType exampleType = Stream.of(GemType.values()).filter(type -> type.tier == tier).findAny().get();

			final List<BlacksmithBlueprintResource> resources = new ArrayList<>();
			resources.add(resourceItem(EMPTY_BOOK, 1));
			resources.add(resourceItem(ORE_COAL, 10));
			resources.add(resourceItem(ORE_SILVER, 5));
			resources.add(resourceItem(ORE_GOLD, 3));
			resources.add(resourceItem(ORE_DEMON_REALM_SILVER, 2));
			resources.add(resourceGem(GemSize.LARGE, tier));
			addPayment(items, getArchmageSpellbookId(exampleType), resources);

			new BlacksmithBlueprintBuilder("BLUEPRINT_TIER_3_ARCHMAGE_SPELLBOOK_" + tier.name())//
					.name(tier.name + " tier archmage spellbook blueprint")//
					.tier(BlacksmithTier.TIER_3)//
					.itemGenerator((fluffer10kFun, pickedItemsUsed) -> {
						final GemType gemType = pickedItemsUsed.get(0).gemType;
						return items.getItem(getArchmageSpellbookId(gemType));
					})//
					.resources(resources)//
					.add();
		}
	}

	private static void addArchmageStaffs(final Items items) {
		for (final GemTier tier : GemTier.values()) {
			final GemType exampleType = Stream.of(GemType.values()).filter(type -> type.tier == tier).findAny().get();

			final List<BlacksmithBlueprintResource> resources = new ArrayList<>();
			resources.add(resourceItem(WOOD, 10));
			resources.add(resourceItem(ORE_COAL, 10));
			resources.add(resourceItem(ORE_SILVER, 5));
			resources.add(resourceItem(ORE_GOLD, 3));
			resources.add(resourceItem(ORE_DEMON_REALM_SILVER, 2));
			resources.add(resourceGem(GemSize.LARGE, tier));
			addPayment(items, getArchmageStaffId(exampleType), resources);

			new BlacksmithBlueprintBuilder("BLUEPRINT_TIER_3_ARCHMAGE_STAFF_" + tier.name())//
					.name(tier.name + " tier archmage staff blueprint")//
					.tier(BlacksmithTier.TIER_3)//
					.itemGenerator((fluffer10kFun, pickedItemsUsed) -> {
						final GemType gemType = pickedItemsUsed.get(0).gemType;
						return items.getItem(getArchmageStaffId(gemType));
					})//
					.resources(resources)//
					.add();
		}
	}

	public static void addBlueprints(final Items items) {
		addArchmageSpellbooks(items);
		addArchmageStaffs(items);

		add(items, BOLAS, //
				pair(ORE_COAL, 25L), //
				pair(ORE_IRON, 20L), //
				pair(ORE_DRAGONIUM, 5L));
		add(items, DWARVEN_AXE, //
				pair(ORE_COAL, 20L), //
				pair(ORE_IRON, 15L), //
				pair(ORE_DRAGONIUM, 5L));
		add(items, DWARVEN_BOW, //
				pair(WOOD, 10L), //
				pair(ORE_COAL, 15L), //
				pair(ORE_IRON, 10L), //
				pair(ORE_DRAGONIUM, 5L));
		add(items, ELVEN_BOW, //
				pair(WOOD, 20L), //
				pair(ORE_COAL, 20L), //
				pair(ORE_IRON, 10L), //
				pair(ORE_SILVER, 3L), //
				pair(ORE_GOLD, 2L), //
				pair(ORE_DRAGONIUM, 5L));
		add(items, GAUNTLET, //
				pair(ORE_COAL, 35L), //
				pair(ORE_IRON, 30L), //
				pair(ORE_DRAGONIUM, 5L));
		add(items, HEAVY_CROSSBOW, //
				pair(WOOD, 20L), //
				pair(ORE_COAL, 25L), //
				pair(ORE_IRON, 20L), //
				pair(ORE_DRAGONIUM, 5L));
		add(items, HOOK_SWORD, //
				pair(ORE_COAL, 25L), //
				pair(ORE_IRON, 20L), //
				pair(ORE_DRAGONIUM, 5L));
		add(items, KATANA, //
				pair(ORE_COAL, 20L), //
				pair(ORE_IRON, 15L), //
				pair(ORE_DRAGONIUM, 5L));
		add(items, MAGIC_SWORD, //
				pair(ORE_COAL, 25L), //
				pair(ORE_IRON, 10L), //
				pair(ORE_GOLD, 2L), //
				pair(ORE_DEMON_REALM_SILVER, 3L), //
				pair(ORE_DRAGONIUM, 5L));
		add(items, PALADIN_SHIELD, //
				pair(ORE_COAL, 50L), //
				pair(ORE_IRON, 40L), //
				pair(ORE_SILVER, 3L), //
				pair(ORE_GOLD, 2L), //
				pair(ORE_DRAGONIUM, 5L));
		add(items, RITUAL_DAGGER, //
				pair(ORE_COAL, 25L), //
				pair(ORE_IRON, 10L), //
				pair(ORE_GOLD, 2L), //
				pair(ORE_DEMON_REALM_SILVER, 3L), //
				pair(ORE_DRAGONIUM, 5L));
		add(items, RUNIC_SCIMITAR, //
				pair(ORE_COAL, 30L), //
				pair(ORE_IRON, 15L), //
				pair(ORE_GOLD, 2L), //
				pair(ORE_DEMON_REALM_SILVER, 3L), //
				pair(ORE_DRAGONIUM, 5L));
		add(items, SHURIKENS, //
				pair(ORE_COAL, 20L), //
				pair(ORE_IRON, 15L), //
				pair(ORE_DRAGONIUM, 5L));
		add(items, TRIDENT, //
				pair(ORE_COAL, 30L), //
				pair(ORE_IRON, 25L), //
				pair(ORE_DRAGONIUM, 5L));

		add(items, PLATE_ARMOR, //
				pair(ORE_COAL, 55L), //
				pair(ORE_IRON, 40L), //
				pair(ORE_SILVER, 2L), //
				pair(ORE_GOLD, 2L), //
				pair(ORE_DEMON_REALM_SILVER, 1L), //
				pair(ORE_DRAGONIUM, 10L));

		add(items, PICKAXE_DRAGONIUM, //
				pair(ORE_COAL, 50L), //
				pair(ORE_IRON, 40L), //
				pair(ORE_DRAGONIUM, 10L));
	}
}