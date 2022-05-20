package bot.commands.rpg.blacksmith.tasks;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

import bot.util.CollectionUtils.ValueFrom;

public enum BlacksmithTier {
	TIER_1(0, "Tier 1"), //
	TIER_2(1, "Tier 2"), //
	TIER_3(2, "Tier 3"), //
	TIER_ENCHANTMENT(3, "Tier Enchantment");

	public static final List<BlacksmithTier> tiers = new ArrayList<>(asList(values()));
	static {
		tiers.sort(new ValueFrom<>(tier -> tier.order));
	}

	public final int order;
	public final String name;

	private BlacksmithTier(final int order, final String name) {
		this.order = order;
		this.name = name;
	}
}
