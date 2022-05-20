package bot.commands.rpg.blacksmith.blueprints.utils;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;

import java.util.List;

import bot.data.items.Items;
import bot.userData.ServerUserData;

public class BlacksmithBlueprintResources {
	public final List<BlacksmithBlueprintResource> resources;

	public BlacksmithBlueprintResources(final List<BlacksmithBlueprintResource> resources) {
		this.resources = resources;
	}

	public BlacksmithBlueprintResources(final BlacksmithBlueprintResource... resources) {
		this(asList(resources));
	}

	public String description(final Items items, final ServerUserData userData) {
		return resources.stream()//
				.map(resource -> resource.description(items, userData))//
				.collect(joining("\n"));
	}

	public boolean payable(final Items items, final ServerUserData userData) {
		return resources.stream().allMatch(resource -> resource.payable(items, userData));
	}
}
