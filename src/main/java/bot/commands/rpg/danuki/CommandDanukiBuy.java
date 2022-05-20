package bot.commands.rpg.danuki;

import static bot.data.items.ItemUtils.getFormattedMonsterGold;
import static bot.data.items.Items.getName;
import static bot.util.CollectionUtils.mapToList;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;
import static java.util.stream.Collectors.toList;

import java.util.List;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.MessageComponentInteraction;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;

import bot.Fluffer10kFun;
import bot.data.items.ItemAmount;
import bot.userData.ServerUserData;
import bot.util.EmbedUtils.EmbedField;
import bot.util.pages.builders.PagedPickerMessageBuilder;
import bot.util.pages.messages.PagedMessage;
import bot.util.subcommand.Subcommand;

public class CommandDanukiBuy extends Subcommand {
	private final Fluffer10kFun fluffer10kFun;

	protected CommandDanukiBuy(final Fluffer10kFun fluffer10kFun) {
		super("buy", "Buy one of the items that Danuki offers", //
				SlashCommandOption.create(SlashCommandOptionType.LONG, "amount", "amount of items to buy"));

		this.fluffer10kFun = fluffer10kFun;
	}

	private EmbedField itemToField(final ItemAmount itemAmount) {
		return new EmbedField(itemAmount.item.name, getFormattedMonsterGold(itemAmount.item.mgPrice) + " per piece");
	}

	private EmbedBuilder itemAmountToEmbed(final ItemAmount itemAmount) {
		return makeEmbed("Buy " + getName(itemAmount) + "?", //
				"It will cost " + getFormattedMonsterGold(itemAmount.item.mgPrice * itemAmount.amount), //
				CommandDanuki.imgUrl);
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) throws Exception {
		final long amount = getOption(interaction).getOptionLongValueByName("amount").orElse(1L);
		if (amount < 1) {
			sendEphemeralMessage(interaction, "You can't buy no items");
			return;
		}

		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(interaction.getServer().get(),
				interaction.getUser());

		final List<ItemAmount> items = userData.danukiShopUnlocks.stream()//
				.filter(unlock -> userData.danukiShopUnlocks.contains(unlock))//
				.map(unlock -> fluffer10kFun.items.getItemAmount(unlock.itemId, amount))//
				.sorted()//
				.collect(toList());
		if (items.isEmpty()) {
			sendEphemeralMessage(interaction,
					"There are no items to buy (is error, you unlock items by unlocking the shop)");
			fluffer10kFun.apiUtils.messageUtils.sendMessageToMe(
					"Someone has no items in Danuki shop! server: " + interaction.getServer().get().getName()
							+ ", user: " + interaction.getUser().getDisplayName(interaction.getServer().get()));
			return;
		}

		final List<EmbedField> fields = mapToList(items, this::itemToField);

		final PagedMessage msg = new PagedPickerMessageBuilder<>("Buy items from Danuki", 5, fields, items)//
				.imgUrl(CommandDanuki.imgUrl)//
				.dataToEmbed(this::itemAmountToEmbed)//
				.onConfirm((interaction2, page, itemAmount) -> onConfirm(interaction2, userData, itemAmount))//
				.build();
		fluffer10kFun.pagedMessageUtils.addMessage(msg, interaction);
	}

	private void onConfirm(final MessageComponentInteraction interaction, final ServerUserData userData,
			final ItemAmount itemAmount) {
		final long price = itemAmount.item.mgPrice * itemAmount.amount;
		if (userData.monsterGold < price) {
			interaction.createOriginalMessageUpdater()
					.addEmbed(makeEmbed("Can't buy", "You don't have enough monster gold!")).update();
			return;
		}

		userData.monsterGold -= price;
		userData.addItem(itemAmount);

		interaction.createOriginalMessageUpdater()//
				.addEmbed(makeEmbed("Item bought!", "You bought " + getName(itemAmount), CommandDanuki.imgUrl))//
				.update();
	}
}
