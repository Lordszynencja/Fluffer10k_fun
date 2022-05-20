package bot.userData;

import static java.util.Arrays.asList;

import java.util.List;

public enum House {
	SLEEPING_BAG("sleeping bag", 0, 0L), //
	TENT("tent", 1, 1000L), //
	WOODEN_HUT("wooden hut", 2, 4000L), //
	TREEHOUSE("treehouse", 3, 10000L), //
	COTTAGE("cottage", 4, 30000L), //
	STONE_HOUSE("stone house", 5, 70000L), //
	FARM("farm", 6, 150000L), //
	PENTHOUSE("penthouse", 7, 350000L), //
	MANSION("mansion", 8, 500000L), //
	CASTLE("castle", 9, 1000000L), //
	PALACE("palace", 10, 1500000L);

	public static final List<House> sorted = asList(SLEEPING_BAG, //
			TENT, //
			WOODEN_HUT, //
			TREEHOUSE, //
			COTTAGE, //
			STONE_HOUSE, //
			FARM, //
			PENTHOUSE, //
			MANSION, //
			CASTLE, //
			PALACE);

	public final String name;
	public final int size;
	public final long price;

	private House(final String name, final int size, final long price) {
		this.name = name;
		this.size = size;
		this.price = price;
	}
}
