package bot.data.items.loot;

import static bot.util.RandomUtils.getRandomDouble;

import java.util.List;

import bot.util.Utils.Pair;

public class RandomWeightedLootListDouble<A> implements LootTable<A> {

	private final List<Pair<Double, A>> weightedItems;
	private final double weightSum;

	public RandomWeightedLootListDouble(final List<Pair<Double, A>> weightedItems) {
		this.weightedItems = weightedItems;
		double sum = 0;
		for (final Pair<Double, A> weightedLoot : weightedItems) {
			sum += weightedLoot.a;
		}
		weightSum = sum;
	}

	@Override
	public A getItem() {
		final double r = getRandomDouble() * weightSum;
		double countWeight = 0.0;

		for (final Pair<Double, A> weightedItem : weightedItems) {
			countWeight += weightedItem.a;
			if (countWeight > r) {
				return weightedItem.b;
			}
		}

		return weightedItems.get(weightedItems.size() - 1).b;
	}

	@Override
	public String toString() {
		String msg = "loot table double ";
		for (final Pair<Double, A> item : weightedItems) {
			msg += ", " + String.format("%.3f", item.a) + ":" + item.b;
		}
		return msg;
	}
}
