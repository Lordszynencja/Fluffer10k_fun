package bot.commands.rpg.blacksmith.blueprints.utils;

import bot.data.items.Items;
import bot.userData.ServerUserData;

public class BlacksmithBlueprintResourceItem extends BlacksmithBlueprintResourceNonPickable {
	private final String itemId;
	private final long amount;
	private final Payer payer;

	public BlacksmithBlueprintResourceItem(final String itemId) {
		this.itemId = itemId;
		amount = 1;
		payer = new ItemPayer(itemId);
	}

	public BlacksmithBlueprintResourceItem(final String itemId, final long amount) {
		this.itemId = itemId;
		this.amount = amount;
		payer = new ItemPayer(itemId, amount);
	}

	@Override
	public String description(final Items items, final ServerUserData userData) {
		return userData.items.getOrDefault(itemId, 0L) + "/" + amount + " " + items.getName(itemId);
	}

	@Override
	public boolean payable(final Items items, final ServerUserData userData) {
		return userData.items.getOrDefault(itemId, 0L) >= amount;
	}

	@Override
	public Payer getPayer() {
		return payer;
	}

	@Override
	public long value(final Items items) {
		return items.getItem(itemId).price * amount;
	}
}
