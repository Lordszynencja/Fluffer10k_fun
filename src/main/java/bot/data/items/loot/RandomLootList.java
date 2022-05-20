package bot.data.items.loot;

import static bot.util.RandomUtils.getRandom;

import java.util.List;

public class RandomLootList<A> implements LootTable<A> {

	private final List<A> items;

	public RandomLootList(final List<A> items) {
		this.items = items;
	}

	@Override
	public A getItem() {
		return getRandom(items);
	}

}
