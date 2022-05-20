package bot.data;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.summingInt;

import java.util.List;

public enum ExplorationType {
	FIGHT(4, 30), //
	NOTHING(1, 5), //
	QUEST(1, 0), //
	STASH(1, 15), //
	TRAVELING_MERCHANT(1, 5);

	public static final List<ExplorationType> activeExplorations = asList(//
			FIGHT, //
			NOTHING, //
			QUEST, //
			STASH, //
			TRAVELING_MERCHANT);

	public static final int totalWeights = activeExplorations.stream()//
			.collect(summingInt(type -> type.basicWeight));

	public final int basicWeight;
	public final int staminaConsumption;

	ExplorationType(final int basicWeight, final int staminaConsumption) {
		this.basicWeight = basicWeight;
		this.staminaConsumption = staminaConsumption;
	}
}
