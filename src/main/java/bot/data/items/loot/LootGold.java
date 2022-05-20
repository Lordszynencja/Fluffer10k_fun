package bot.data.items.loot;

import static bot.data.items.ItemUtils.getFormattedMonies;

import bot.data.items.Items;
import bot.userData.ServerUserData;

public class LootGold implements Loot {

	public final long amount;

	public LootGold(final long amount) {
		this.amount = amount;
	}

	@Override
	public void addToUser(final ServerUserData userData) {
		userData.monies += amount;
	}

	@Override
	public String getDescription(final Items items) {
		return getFormattedMonies(amount);
	}

	@Override
	public LootType getType() {
		return LootType.GOLD;
	}

	@Override
	public long totalValue(final Items items) {
		return amount;
	}

}
