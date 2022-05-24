package bot.commands.rpg.blacksmith.tasks;

import static java.util.Arrays.asList;

import java.util.List;

public enum BlacksmithTier {
	TIER_1("Tier 1"), //
	TIER_2("Tier 2"), //
	TIER_3("Tier 3"), //
	TIER_ENCHANTMENT("Tier Enchantment");

	public static final List<BlacksmithTier> tiers = asList(TIER_3, TIER_2, TIER_1);

	public final String name;

	private BlacksmithTier(final String name) {
		this.name = name;
	}
}
