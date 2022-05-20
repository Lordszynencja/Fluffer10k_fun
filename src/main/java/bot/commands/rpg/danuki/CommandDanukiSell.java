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

public class CommandDanukiSell extends Subcommand {
	private final Fluffer10kFun fluffer10kFun;

	protected CommandDanukiSell(final Fluffer10kFun fluffer10kFun) {
		super("sell", "Sell one of the items that Danuki accepts", //
				SlashCommandOption.create(SlashCommandOptionType.LONG, "amount", "amount of items to buy"));

		this.fluffer10kFun = fluffer10kFun;
	}

	private EmbedField itemToField(final ItemAmount itemAmount, final ServerUserData userData) {
		return new EmbedField(itemAmount.item.name + " (" + userData.items.get(itemAmount.item.id) + ")",
				getFormattedMonsterGold(itemAmount.item.mgPrice) + " per piece");
	}

	private EmbedBuilder itemAmountToEmbed(final ItemAmount itemAmount) {
		return makeEmbed("Sell " + getName(itemAmount) + "?", //
				"It will give you " + getFormattedMonsterGold(itemAmount.item.mgPrice * itemAmount.amount), //
				CommandDanuki.imgUrl);
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) throws Exception {
		final long amount = getOption(interaction).getOptionLongValueByName("amount").orElse(1L);
		if (amount < 1) {
			sendEphemeralMessage(interaction, "You can't sell no items");
			return;
		}

		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(interaction.getServer().get(),
				interaction.getUser());

		final List<ItemAmount> items = userData.items.entrySet().stream()//
				.filter(entry -> entry.getValue() >= amount)//
				.map(entry -> fluffer10kFun.items.getItemAmount(entry.getKey(), amount))//
				.filter(itemAmount -> itemAmount.item.mgPrice > 0)//
				.sorted()//
				.collect(toList());
		if (items.isEmpty()) {
			sendEphemeralMessage(interaction, "There are no items to sell");
			return;
		}

		final List<EmbedField> fields = mapToList(items, itemAmount -> itemToField(itemAmount, userData));

		final PagedMessage msg = new PagedPickerMessageBuilder<>("Sell items to Danuki", 5, fields, items)//
				.imgUrl(CommandDanuki.imgUrl)//
				.dataToEmbed(this::itemAmountToEmbed)//
				.onConfirm((interaction2, page, itemAmount) -> onConfirm(interaction2, userData, itemAmount))//
				.build();
		fluffer10kFun.pagedMessageUtils.addMessage(msg, interaction);
	}

	private void onConfirm(final MessageComponentInteraction interaction, final ServerUserData userData,
			final ItemAmount itemAmount) {
		if (!userData.hasItem(itemAmount)) {
			interaction.createOriginalMessageUpdater().addEmbed(makeEmbed("Can't buy", "You don't have enough items!"))
					.update();
			return;
		}
		final long price = itemAmount.item.mgPrice * itemAmount.amount;

		userData.monsterGold += price;
		userData.addItem(itemAmount.minus());

		interaction.createOriginalMessageUpdater()//
				.addEmbed(makeEmbed("Item sold!", "You sold " + getName(itemAmount), CommandDanuki.imgUrl))//
				.update();
	}
}
