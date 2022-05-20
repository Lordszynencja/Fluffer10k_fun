package bot.commands.rpg.blacksmith.blueprints.utils;

import java.util.List;

import bot.commands.rpg.blacksmith.blueprints.BlacksmithBlueprint;
import bot.commands.rpg.blacksmith.blueprints.BlacksmithBlueprintsList;
import bot.commands.rpg.blacksmith.tasks.BlacksmithTier;

public class BlacksmithBlueprintBuilder {
	public String id;
	public String name;
	public BlacksmithTier tier;
	public ItemGenerator itemGenerator;
	public BlacksmithBlueprintResources resources;

	public BlacksmithBlueprintBuilder(final String id) {
		this.id = id;
	}

	public BlacksmithBlueprintBuilder name(final String x) {
		name = x;
		return this;
	}

	public BlacksmithBlueprintBuilder tier(final BlacksmithTier x) {
		tier = x;
		return this;
	}

	public BlacksmithBlueprintBuilder itemGenerator(final ItemGenerator x) {
		itemGenerator = x;
		return this;
	}

	public BlacksmithBlueprintBuilder resources(final List<BlacksmithBlueprintResource> x) {
		resources = new BlacksmithBlueprintResources(x);
		return this;
	}

	public BlacksmithBlueprintBuilder resources(final BlacksmithBlueprintResources x) {
		resources = x;
		return this;
	}

	public void add() {
		BlacksmithBlueprintsList.addBlueprint(new BlacksmithBlueprint(id, name, tier, itemGenerator, resources));
	}
}
