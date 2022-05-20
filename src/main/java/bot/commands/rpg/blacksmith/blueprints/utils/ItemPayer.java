package bot.commands.rpg.blacksmith.blueprints.utils;

import bot.userData.ServerUserData;

public class ItemPayer implements Payer {
	private final String itemId;
	private final long amount;

	public ItemPayer(final String itemId) {
		this.itemId = itemId;
		amount = 1;
	}

	public ItemPayer(final String itemId, final long amount) {
		this.itemId = itemId;
		this.amount = amount;
	}

	@Override
	public boolean canPay(final ServerUserData userData) {
		return userData.hasItem(itemId, amount);
	}

	@Override
	public void pay(final ServerUserData userData) {
		userData.addItem(itemId, -amount);
	}

}
