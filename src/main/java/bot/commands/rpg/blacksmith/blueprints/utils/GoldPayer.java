package bot.commands.rpg.blacksmith.blueprints.utils;

import bot.userData.ServerUserData;

public class GoldPayer implements Payer {
	private final long amount;

	public GoldPayer(final long amount) {
		this.amount = amount;
	}

	@Override
	public boolean canPay(final ServerUserData userData) {
		return userData.monies >= amount;
	}

	@Override
	public void pay(final ServerUserData userData) {
		userData.monies -= amount;
	}

}
