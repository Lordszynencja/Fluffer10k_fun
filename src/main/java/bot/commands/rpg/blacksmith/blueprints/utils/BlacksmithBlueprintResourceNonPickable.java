package bot.commands.rpg.blacksmith.blueprints.utils;

import org.javacord.api.interaction.MessageComponentInteraction;

import bot.Fluffer10kFun;
import bot.data.items.ItemAmount;
import bot.userData.ServerUserData;
import bot.util.pages.messages.PagedPickerMessage.OnPickHandler;

public abstract class BlacksmithBlueprintResourceNonPickable implements BlacksmithBlueprintResource {
	@Override
	public final boolean isPickable() {
		return false;
	}

	@Override
	public final void pick(final Fluffer10kFun fluffer10kFun, final MessageComponentInteraction interaction,
			final ServerUserData userData, final OnPickHandler<ItemAmount> onPick) {
	}
}
