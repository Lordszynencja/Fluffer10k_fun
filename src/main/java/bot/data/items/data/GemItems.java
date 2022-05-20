package bot.data.items.data;

import static bot.util.CollectionUtils.toMap;
import static bot.util.Utils.capitalize;
import static bot.util.Utils.Pair.pair;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import bot.data.items.ItemBuilder;

public class GemItems {
	public static enum GemTier {
		LOW("low", 1), //
		MEDIUM("medium", 2), //
		HIGH("high", 3), //
		TOP("top", 4);

		public final String name;
		public final int id;

		private GemTier(final String name, final int id) {
			this.name = name;
			this.id = id;
		}
	}

	public static enum GemType {
		AMETHYST("amethyst", "amethysts", "violet", GemTier.LOW), //
		JADE("jade", "jades", "green", GemTier.LOW), //
		QUARTZ("quartz", "quartzs", "white", GemTier.LOW), //
		TOPAZ("topaz", "topazes", "orange", GemTier.LOW), //

		GARNET("garnet", "garnets", "dark red", GemTier.MEDIUM), //
		OPAL("opal", "opals", "iridescent", GemTier.MEDIUM), //

		EMERALD("emerald", "emeralds", "green", GemTier.HIGH), //
		RUBY("ruby", "rubies", "red", GemTier.HIGH), //
		SAPPHIRE("sapphire", "sapphires", "blue", GemTier.HIGH), //

		DIAMOND("diamond", "diamonds", "clear", GemTier.TOP);

		public final String name;
		public final String namePlural;
		public final String color;
		public final GemTier tier;

		private GemType(final String name, final String namePlural, final String color, final GemTier tier) {
			this.name = name;
			this.namePlural = namePlural;
			this.color = color;
			this.tier = tier;
		}
	}

	public static enum GemRefinement {
		REFINED("refined"), //
		UNREFINED("unrefined");

		public final String name;

		private GemRefinement(final String name) {
			this.name = name;
		}
	}

	public static enum GemSize {
		LARGE("large", 3), //
		MEDIUM("medium", 2, LARGE), //
		SMALL("small", 1, MEDIUM);

		public final String name;
		public final int sizeOrder;
		public final GemSize nextSize;

		private GemSize(final String name, final int sizeOrder) {
			this.name = name;
			this.sizeOrder = sizeOrder;
			nextSize = null;
		}

		private GemSize(final String name, final int sizeOrder, final GemSize nextSize) {
			this.name = name;
			this.sizeOrder = sizeOrder;
			this.nextSize = nextSize;
		}
	}

	public static String getId(final GemSize gemSize, final GemRefinement gemRefinement, final GemType gemType) {
		return "GEM_" + gemSize.name() + "_" + gemRefinement.name() + "_" + gemType.name();
	}

	private static final Map<GemRefinement, Map<GemSize, Long>> priceMultipliers = new HashMap<>();

	static {
		final long refinementPriceBase = 15;
		final long joinPriceBase = 10;

		final Map<GemSize, Long> unrefinedPrices = new HashMap<>();
		unrefinedPrices.put(GemSize.SMALL, 10L);
		unrefinedPrices.put(GemSize.MEDIUM, 2 * unrefinedPrices.get(GemSize.SMALL) + joinPriceBase);
		unrefinedPrices.put(GemSize.LARGE, 2 * unrefinedPrices.get(GemSize.MEDIUM) + joinPriceBase);
		priceMultipliers.put(GemRefinement.UNREFINED, unrefinedPrices);

		final Map<GemSize, Long> refinedPrices = new HashMap<>();
		refinedPrices.put(GemSize.SMALL, unrefinedPrices.get(GemSize.SMALL) + refinementPriceBase);
		refinedPrices.put(GemSize.MEDIUM, 2 * refinedPrices.get(GemSize.SMALL) + joinPriceBase);
		refinedPrices.put(GemSize.LARGE, 2 * refinedPrices.get(GemSize.MEDIUM) + joinPriceBase);
		priceMultipliers.put(GemRefinement.REFINED, refinedPrices);
	}

	private static final Map<GemTier, Long> gemTierPriceMultipliers = toMap(//
			pair(GemTier.LOW, 5L), //
			pair(GemTier.MEDIUM, 10L), //
			pair(GemTier.HIGH, 20L), //
			pair(GemTier.TOP, 50L));

	public static long getGemPrice(final GemSize size, final GemTier tier, final GemRefinement refinement) {
		return priceMultipliers.get(refinement).get(size) * gemTierPriceMultipliers.get(tier);
	}

	private static class GemItemBuilder extends ItemBuilder {
		public GemItemBuilder(final GemType gemType, final GemSize gemSize, final GemRefinement gemRefinement) {
			id(getId(gemSize, gemRefinement, gemType));
			name(gemSize.name + " " + gemRefinement.name + " " + gemType.name);
			namePlural(gemSize.name + " " + gemRefinement.name + " " + gemType.namePlural);
			description(capitalize(gemSize.name) + " " + gemRefinement.name + " " + gemType.color
					+ " gem, can be sold or used for magical enhancement.");
			price(getGemPrice(gemSize, gemType.tier, gemRefinement));
			rarity(gemType.tier.id);
			gemType(gemType);
			gemSize(gemSize);
			gemRefinement(gemRefinement);
		}
	}

	public static void addItems(final Consumer<ItemBuilder> itemAdder) {
		for (final GemType gemType : GemType.values()) {
			for (final GemSize gemSize : GemSize.values()) {
				for (final GemRefinement gemRefinement : GemRefinement.values()) {
					new GemItemBuilder(gemType, gemSize, gemRefinement)//
							.add(itemAdder);
				}
			}
		}
	}
}
