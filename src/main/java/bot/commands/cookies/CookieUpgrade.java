package bot.commands.cookies;

import java.util.function.Function;

public enum CookieUpgrade {
	TARGETTED_CHANCE("Targeted cookie drop", "Chance of getting targeted cookie when baking specific cookie", //
			0, 100, i -> (i + 1) * 10_000),
	MULTIPLE_DROP_CHANCE("Multiple drop chance", "Chance of multiple drop", //
			0, 100, i -> (i + 1) * 10_000),
	MULTIPLE_DROP_MULTIPLIER("Multiple drop multiplier", "Multiple drop multiplier", //
			1, 5, i -> i * i * 1_000_000),
	AUTO_BAKE("Automatic baking",
			"Automatic baking (cookies get added to your collection periodically (120 minutes at level 1, 20 minutes at level 100))", //
			0, 100, i -> (i + 1) * 10_000);

	public final String name;
	public final String description;
	public final long min;
	public final long max;
	public final Function<Long, Long> priceCreator;

	private CookieUpgrade(final String name, final String description, final long min, final long max,
			final Function<Long, Long> priceCreator) {
		this.name = name;
		this.description = description;
		this.min = min;
		this.max = max;
		this.priceCreator = priceCreator;
	}

}
