package bot.data.items.loot;

import static java.util.Arrays.asList;

import java.util.List;

import bot.util.Utils.Pair;

public interface LootTable<A> {
	public static <A> LootTable<A> single(final A loot) {
		return () -> loot;
	}

	@SafeVarargs
	public static <A> LootTable<A> list(final A... items) {
		return new RandomLootList<>(asList(items));
	}

	public static <A> LootTable<A> list(final List<A> items) {
		return new RandomLootList<>(items);
	}

	@SafeVarargs
	public static <A> LootTable<A> listT(final LootTable<A>... lootTables) {
		final LootTable<LootTable<A>> lootTable = list(lootTables);
		return () -> lootTable.getItem().getItem();
	}

	public static <A> RandomWeightedLootListDouble<A> weightedD(final List<Pair<Double, A>> weightedItems) {
		return new RandomWeightedLootListDouble<>(weightedItems);
	}

	@SafeVarargs
	public static <A> LootTable<A> weightedD(final Pair<Double, A>... weightedItems) {
		return weightedD(asList(weightedItems));
	}

	public static <A> LootTable<A> weightedL(final List<Pair<Long, A>> weightedItems) {
		return new RandomWeightedLootListLong<>(weightedItems);
	}

	@SafeVarargs
	public static <A> LootTable<A> weightedL(final Pair<Long, A>... weightedItems) {
		return weightedL(asList(weightedItems));
	}

	public static <A> LootTable<A> weightedI(final List<Pair<Integer, A>> weightedItems) {
		return new RandomWeightedLootListInteger<>(weightedItems);
	}

	@SafeVarargs
	public static <A> LootTable<A> weightedI(final Pair<Integer, A>... weightedItems) {
		return weightedI(asList(weightedItems));
	}

	public static <A> LootTable<A> weightedTD(final List<Pair<Double, LootTable<A>>> weightedLootTables) {
		final LootTable<LootTable<A>> lootTable = weightedD(weightedLootTables);
		return () -> lootTable.getItem().getItem();
	}

	@SafeVarargs
	public static <A> LootTable<A> weightedTD(final Pair<Double, LootTable<A>>... weightedLootTables) {
		return weightedTD(asList(weightedLootTables));
	}

	public static <A> LootTable<A> weightedTL(final List<Pair<Long, LootTable<A>>> weightedLootTables) {
		final LootTable<LootTable<A>> lootTable = weightedL(weightedLootTables);
		return () -> lootTable.getItem().getItem();
	}

	@SafeVarargs
	public static <A> LootTable<A> weightedTL(final Pair<Long, LootTable<A>>... weightedLootTables) {
		return weightedTL(asList(weightedLootTables));
	}

	public static <A> LootTable<A> weightedTI(final List<Pair<Integer, LootTable<A>>> weightedLootTables) {
		final LootTable<LootTable<A>> lootTable = weightedI(weightedLootTables);
		return () -> lootTable.getItem().getItem();
	}

	@SafeVarargs
	public static <A> LootTable<A> weightedTI(final Pair<Integer, LootTable<A>>... weightedLootTables) {
		return weightedTI(asList(weightedLootTables));
	}

	A getItem();
}
