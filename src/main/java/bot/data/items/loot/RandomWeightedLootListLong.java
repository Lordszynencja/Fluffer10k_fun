package bot.data.items.loot;

import static bot.util.RandomUtils.getRandomLong;

import java.util.List;

import bot.util.Utils.Pair;

public class RandomWeightedLootListLong<A> implements LootTable<A> {

	private final List<Pair<Long, A>> weightedItems;
	private final long weightSum;

	public RandomWeightedLootListLong(final List<Pair<Long, A>> weightedItems) {
		this.weightedItems = weightedItems;
		long sum = 0;
		for (final Pair<Long, A> weightedLoot : weightedItems) {
			sum += weightedLoot.a;
		}
		weightSum = sum;
	}

	@Override
	public A getItem() {
		final long r = getRandomLong(weightSum);
		long countWeight = 0;
		for (final Pair<Long, A> weightedItem : weightedItems) {
			countWeight += weightedItem.a;
			if (countWeight > r) {
				return weightedItem.b;
			}
		}

		return weightedItems.get(weightedItems.size() - 1).b;
	}

}
