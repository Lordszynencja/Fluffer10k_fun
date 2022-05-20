package bot.data.items.required;

import bot.data.items.Items;
import bot.data.items.SimpleItemAmount;
import bot.userData.ServerUserData;
import bot.util.CollectionUtils;

public class SingleItemRequirement implements ItemRequirement {
	private final SimpleItemAmount itemAmount;

	public SingleItemRequirement(final SimpleItemAmount itemAmount) {
		this.itemAmount = itemAmount;
	}

	@Override
	public boolean isMet(final ServerUserData userData, final String itemId) {
		return itemAmount.id.equals(itemId) && userData.items.getOrDefault(itemId, 0L) >= itemAmount.amount;
	}

	@Override
	public String description(final Items items) {
		return items.getName(itemAmount);
	}

	@Override
	public void use(final ServerUserData userData, final String itemId) {
		CollectionUtils.addToLongOnMap(userData.items, itemAmount.id, -itemAmount.amount);
	}

	@Override
	public boolean successful(final ServerUserData userData, final String itemId) {
		return true;
	}

}
