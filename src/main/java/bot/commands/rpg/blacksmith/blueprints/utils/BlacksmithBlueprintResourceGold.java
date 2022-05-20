package bot.commands.rpg.blacksmith.blueprints.utils;

import static bot.data.items.ItemUtils.formatNumber;
import static bot.data.items.ItemUtils.getFormattedMonies;

import bot.data.items.Items;
import bot.userData.ServerUserData;

public class BlacksmithBlueprintResourceGold extends BlacksmithBlueprintResourceNonPickable {
	private final long amount;
	private final Payer payer;

	public BlacksmithBlueprintResourceGold(final long amount) {
		this.amount = amount;
		payer = new GoldPayer(amount);
	}

	@Override
	public String description(final Items items, final ServerUserData userData) {
		return formatNumber(userData.monies) + "/" + getFormattedMonies(amount);
	}

	@Override
	public boolean payable(final Items items, final ServerUserData userData) {
		return userData.monies >= amount;
	}

	@Override
	public Payer getPayer() {
		return payer;
	}

	@Override
	public long value(final Items items) {
		return amount;
	}
}
