package bot.data.items.loot;

import bot.data.items.Items;
import bot.userData.ServerUserData;

public class LootItem implements Loot {

	public final String itemId;
	public final long amount;

	public LootItem(final String itemId) {
		this(itemId, 1);
	}

	public LootItem(final String itemId, final long amount) {
		this.itemId = itemId;
		this.amount = amount;
	}

	@Override
	public void addToUser(final ServerUserData userData) {
		userData.addItem(itemId, amount);
	}

	@Override
	public String getDescription(final Items items) {
		return items.getName(itemId, amount);
	}

	@Override
	public LootType getType() {
		return LootType.ITEM;
	}

	@Override
	public long totalValue(final Items items) {
		return items.getItem(itemId).price;
	}

}
