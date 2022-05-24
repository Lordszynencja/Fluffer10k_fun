package bot.commands.rpg.blacksmith.blueprints.utils;

import java.util.List;

import bot.userData.ServerUserData;

public interface Payer {
	public static Payer multiPayer(final List<Payer> payers) {
		return new Payer() {
			@Override
			public boolean canPay(final ServerUserData userData) {
				for (final Payer payer : payers) {
					if (!payer.canPay(userData)) {
						return false;
					}
				}

				return true;
			}

			@Override
			public void pay(final ServerUserData userData) {
				for (final Payer payer : payers) {
					payer.pay(userData);
				}
			}

		};
	}

	public boolean canPay(ServerUserData userData);

	public void pay(ServerUserData userData);
}
