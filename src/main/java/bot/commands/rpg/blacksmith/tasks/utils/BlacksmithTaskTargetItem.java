package bot.commands.rpg.blacksmith.tasks.utils;

import bot.data.items.Items;
import bot.userData.ServerUserData;

public class BlacksmithTaskTargetItem implements BlacksmithTaskTarget {

	private final String itemId;
	private final long amount;

	public BlacksmithTaskTargetItem(final String itemId) {
		this.itemId = itemId;
		amount = 1;
	}

	public BlacksmithTaskTargetItem(final String itemId, final long amount) {
		this.itemId = itemId;
		this.amount = amount;
	}

	@Override
	public boolean isMet(final ServerUserData userData) {
		return userData.hasItem(itemId, amount);
	}

	@Override
	public String progressDescription(final ServerUserData userData, final Items items) {
		final long possessed = userData == null ? 0 : userData.items.getOrDefault(itemId, 0L);
		return items.getItem(itemId).namePlural + " collected: " + possessed + "/" + amount;
	}

	@Override
	public void apply(final ServerUserData userData) {
		userData.addItem(itemId, -amount);
	}
}
