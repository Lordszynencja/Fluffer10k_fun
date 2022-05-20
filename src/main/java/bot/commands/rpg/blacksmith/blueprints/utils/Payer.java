package bot.commands.rpg.blacksmith.blueprints.utils;

import bot.userData.ServerUserData;

public interface Payer {
	public boolean canPay(ServerUserData userData);

	public void pay(ServerUserData userData);
}
