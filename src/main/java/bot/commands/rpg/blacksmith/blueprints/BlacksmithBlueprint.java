package bot.commands.rpg.blacksmith.blueprints;

import bot.commands.rpg.blacksmith.blueprints.utils.BlacksmithBlueprintResources;
import bot.commands.rpg.blacksmith.blueprints.utils.ItemGenerator;
import bot.commands.rpg.blacksmith.tasks.BlacksmithTier;

public class BlacksmithBlueprint {
	public final String id;
	public final String name;
	public final BlacksmithTier tier;
	public final ItemGenerator itemGenerator;
	public final BlacksmithBlueprintResources resources;

	public BlacksmithBlueprint(final String id, final String name, final BlacksmithTier tier,
			final ItemGenerator itemGenerator, final BlacksmithBlueprintResources resources) {
		this.id = id;
		this.name = name;
		this.tier = tier;
		this.itemGenerator = itemGenerator;
		this.resources = resources;
	}
}
