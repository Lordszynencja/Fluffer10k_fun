package bot.data.items.loot;

import bot.data.items.Items;
import bot.userData.ServerUserData;

public interface Loot {
	public enum LootType {
		GOLD, //
		ITEM, //
		MIXED;
	}

	public static Loot gold(final long amount) {
		return new LootGold(amount);
	}

	public static Loot item(final String itemId) {
		return new LootItem(itemId, 1);
	}

	public static Loot item(final String itemId, final long amount) {
		return new LootItem(itemId, amount);
	}

	void addToUser(ServerUserData userData);

	String getDescription(Items items);

	LootType getType();

	long totalValue(Items items);
}
