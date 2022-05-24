package bot.commands.rpg.blacksmith.tasks.utils;

import org.javacord.api.interaction.MessageComponentInteraction;

import bot.Fluffer10kFun;
import bot.commands.rpg.blacksmith.blueprints.utils.ItemPayer;
import bot.commands.rpg.blacksmith.blueprints.utils.Payer;
import bot.data.items.Items;
import bot.userData.ServerUserData;
import bot.util.pages.messages.PagedPickerMessage.OnPickHandler;

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
	public String taskDescription(final Items items) {
		return "collect " + items.getName(itemId, amount);
	}

	@Override
	public String progressDescription(final ServerUserData userData, final Items items) {
		final long possessed = userData.items.getOrDefault(itemId, 0L);
		return items.getItem(itemId).namePlural + " collected: " + possessed + "/" + amount;
	}

	@Override
	public boolean isPickable() {
		return false;
	}

	@Override
	public Payer getPayer() {
		return new ItemPayer(itemId, amount);
	}

	@Override
	public void pick(final Fluffer10kFun fluffer10kFun, final MessageComponentInteraction in,
			final ServerUserData userData, final OnPickHandler<Payer> onPick) {
	}

}
