package bot.commands.rpg.blacksmith.blueprints.utils;

import org.javacord.api.interaction.MessageComponentInteraction;

import bot.Fluffer10kFun;
import bot.data.items.ItemAmount;
import bot.data.items.Items;
import bot.data.items.data.GemItems.GemSize;
import bot.data.items.data.GemItems.GemTier;
import bot.userData.ServerUserData;
import bot.util.pages.messages.PagedPickerMessage.OnPickHandler;

public interface BlacksmithBlueprintResource {
	public static BlacksmithBlueprintResource resourceGem(final GemSize size, final GemTier tier) {
		return new BlacksmithBlueprintResourceGemSizeTier(size, tier);
	}

	public static BlacksmithBlueprintResource resourceGold(final long amount) {
		return new BlacksmithBlueprintResourceGold(amount);
	}

	public static BlacksmithBlueprintResource resourceItem(final String itemId) {
		return new BlacksmithBlueprintResourceItem(itemId);
	}

	public static BlacksmithBlueprintResource resourceItem(final String itemId, final long amount) {
		return new BlacksmithBlueprintResourceItem(itemId, amount);
	}

	public String description(Items items, ServerUserData userData);

	public boolean payable(Items items, ServerUserData userData);

	public boolean isPickable();

	public void pick(Fluffer10kFun fluffer10kFun, MessageComponentInteraction interaction, ServerUserData userData,
			OnPickHandler<ItemAmount> onPick);

	public Payer getPayer();

	public long value(Items items);
}
