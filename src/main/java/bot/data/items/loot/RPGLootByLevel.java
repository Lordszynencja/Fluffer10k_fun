package bot.data.items.loot;

import static bot.data.items.loot.RPGLootTables.getRandomLoot;
import static bot.util.RandomUtils.getRandomInt;

import java.util.Arrays;

public class RPGLootByLevel {
	private static final int[][] tiers = { //
			{ 4, 5, 1 }, // 0
			{ 3, 5, 2 }, //
			{ 2, 5, 3 }, //
			{ 1, 5, 4 }, //
			{ 0, 4, 5, 1 }, // 4
			{ 0, 3, 5, 2 }, //
			{ 0, 2, 5, 3 }, //
			{ 0, 1, 5, 4 }, //
			{ 0, 0, 4, 5, 1 }, // 8
			{ 0, 0, 3, 5, 2 }, //
			{ 0, 0, 2, 5, 3 }, //
			{ 0, 0, 1, 5, 4 }, //
			{ 0, 0, 0, 4, 5, 1 }, // 12
			{ 0, 0, 0, 3, 5, 2 }, //
			{ 0, 0, 0, 2, 5, 3 }, //
			{ 0, 0, 0, 1, 5, 4 }, //
			{ 0, 0, 0, 0, 4, 5, 1 }, // 16
			{ 0, 0, 0, 0, 3, 5, 2 }, //
			{ 0, 0, 0, 0, 2, 5, 3 }, //
			{ 0, 0, 0, 0, 1, 5, 4 }, //
			{ 0, 0, 0, 0, 0, 5, 5 } };// 20

	private static int[] generateDistributionForTier(int tier) {
		if (tier > 20) {
			tier = 20;
		}
		if (tier < 0) {
			tier = 0;
		}
		return tiers[tier];
	}

	public static LootList getLoot(final int level) {
		final int rewards = 2 + getRandomInt(4);
		final int tier = level / 5;
		final int[] tierDistribution = generateDistributionForTier(tier);

		final LootList loot = new LootList();
		for (int i = 0; i < rewards; i++) {
			loot.add(getRandomLoot(tierDistribution));
		}

		return loot;
	}

	public static void main(final String[] args) {
		for (int i = -1; i <= 21; i++) {
			System.out.println("level " + i * 5 + ": " + Arrays.toString(generateDistributionForTier(i)));
		}
	}
}
