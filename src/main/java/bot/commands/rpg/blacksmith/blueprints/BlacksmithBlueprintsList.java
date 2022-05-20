package bot.commands.rpg.blacksmith.blueprints;

import static bot.util.RandomUtils.getRandom;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bot.commands.rpg.blacksmith.blueprints.data.Tier1BlacksmithBlueprints;
import bot.commands.rpg.blacksmith.blueprints.data.Tier2BlacksmithBlueprints;
import bot.commands.rpg.blacksmith.blueprints.data.Tier3BlacksmithBlueprints;
import bot.commands.rpg.blacksmith.tasks.BlacksmithTier;
import bot.data.items.Items;

public class BlacksmithBlueprintsList {
	public static final Map<String, BlacksmithBlueprint> blueprints = new HashMap<>();
	private static final Map<BlacksmithTier, List<BlacksmithBlueprint>> blueprintsByTier = asList(//
			BlacksmithTier.TIER_1, //
			BlacksmithTier.TIER_2, //
			BlacksmithTier.TIER_3)//
					.stream()//
					.collect(toMap(tier -> tier, tier -> new ArrayList<>()));

	public static void addBlueprint(final BlacksmithBlueprint blueprint) {
		if (blueprints.get(blueprint.id) != null) {
			throw new RuntimeException("double blueprint id " + blueprint.id);
		}

		blueprints.put(blueprint.id, blueprint);
		blueprintsByTier.get(blueprint.tier).add(blueprint);
	}

	public static void addBlueprints(final Items items) {
		Tier1BlacksmithBlueprints.addBlueprints(items);
		Tier2BlacksmithBlueprints.addBlueprints(items);
		Tier3BlacksmithBlueprints.addBlueprints(items);
	}

	public static BlacksmithBlueprint getRandomBlueprint(final BlacksmithTier tier) {
		return getRandom(blueprintsByTier.get(tier));
	}

	public static BlacksmithBlueprint getBlueprint(final String id) {
		return blueprints.get(id);
	}
}
