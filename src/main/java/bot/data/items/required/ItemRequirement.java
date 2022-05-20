package bot.data.items.required;

import bot.data.items.Items;
import bot.userData.ServerUserData;

public interface ItemRequirement {

	boolean isMet(ServerUserData userData, String itemId);

	void use(ServerUserData userData, String itemId);

	boolean successful(ServerUserData userData, String itemId);

	String description(Items items);
}
