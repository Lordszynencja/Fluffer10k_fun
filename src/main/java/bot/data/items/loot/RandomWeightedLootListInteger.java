package bot.data.items.loot;

import static bot.util.RandomUtils.getRandomInt;

import java.util.List;

import bot.util.Utils.Pair;

public class RandomWeightedLootListInteger<A> implements LootTable<A> {

	private final List<Pair<Integer, A>> weightedItems;
	private final int weightSum;

	public RandomWeightedLootListInteger(final List<Pair<Integer, A>> weightedItems) {
		this.weightedItems = weightedItems;
		int sum = 0;
		for (final Pair<Integer, A> weightedLoot : weightedItems) {
			sum += weightedLoot.a;
		}
		weightSum = sum;
	}

	@Override
	public A getItem() {
		final int r = getRandomInt(weightSum);
		int countWeight = 0;
		for (final Pair<Integer, A> weightedItem : weightedItems) {
			countWeight += weightedItem.a;
			if (countWeight > r) {
				return weightedItem.b;
			}
		}

		return weightedItems.get(weightedItems.size() - 1).b;
	}

}
