package bot.commands.rpg.jeweller;

import static bot.data.items.ItemUtils.getFormattedMonies;
import static bot.data.items.data.GemItems.getId;
import static bot.util.CollectionUtils.mapToList;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;
import static java.util.stream.Collectors.toList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.MessageComponentInteraction;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;

import bot.Fluffer10kFun;
import bot.data.items.Item;
import bot.data.items.ItemAmount;
import bot.data.items.Items;
import bot.data.items.data.GemItems;
import bot.data.items.data.GemItems.GemSize;
import bot.userData.ServerUserData;
import bot.util.EmbedUtils.EmbedField;
import bot.util.pages.builders.PagedPickerMessageBuilder;
import bot.util.pages.messages.PagedMessage;
import bot.util.subcommand.Subcommand;

public class CommandJewellerJoin extends Subcommand {
	private final Map<String, Long> joinPrices = new HashMap<>();

	private final Fluffer10kFun fluffer10kFun;

	public CommandJewellerJoin(final Fluffer10kFun fluffer10kFun) {
		super("join", "join gems", //
				SlashCommandOption.create(SlashCommandOptionType.LONG, "amount",
						"amount of joins you want to make (2 gems for each)"));

		this.fluffer10kFun = fluffer10kFun;

		fluffer10kFun.items.items.values().forEach(item -> {
			if (item.gemSize != null && item.gemSize.sizeOrder < GemSize.LARGE.sizeOrder) {
				final long smallerPrice = item.price;
				final long biggerPrice = fluffer10kFun.items
						.getItem(GemItems.getId(item.gemSize.nextSize, item.gemRefinement, item.gemType)).price;
				joinPrices.put(item.id, biggerPrice - 2 * smallerPrice);
			}
		});
	}

	private long getJoinPrice(final Item item, final long joinAmount) {
		return joinAmount * joinPrices.get(item.id);
	}

	private EmbedBuilder itemAmountToEmbed(final ItemAmount itemAmount, final long joinAmount) {
		return makeEmbed("Join " + 2 * joinAmount + " " + itemAmount.item.namePlural + "?", //
				"This will cost " + getFormattedMonies(getJoinPrice(itemAmount.item, joinAmount)), //
				CommandJeweller.jewellerImgUrl);
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) throws Exception {
		final long joinAmount = getOption(interaction).getArgumentLongValueByName("amount").orElse(1L);
		if (joinAmount <= 0) {
			sendEphemeralMessage(interaction, "You can't join less than 2 gems");
			return;
		}

		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(interaction.getServer().get(),
				interaction.getUser());
		final List<ItemAmount> items = userData.items.entrySet().stream()//
				.filter(entry -> entry.getValue() >= joinAmount * 2)
				.map(entry -> fluffer10kFun.items.getItemAmount(entry.getKey(), entry.getValue()))//
				.filter(itemAmount -> itemAmount.item.gemType != null
						&& itemAmount.item.gemSize.sizeOrder < GemSize.LARGE.sizeOrder)//
				.sorted()//
				.collect(toList());

		if (items.isEmpty()) {
			sendEphemeralMessage(interaction, "You don't have gems to join");
			return;
		}

		final List<EmbedField> fields = mapToList(items, itemAmount -> itemAmount.getAsField());

		final PagedMessage msg = new PagedPickerMessageBuilder<>("Pick gems to join", 5, fields, items)//
				.imgUrl(CommandJeweller.jewellerImgUrl)//
				.dataToEmbed(itemAmount -> itemAmountToEmbed(itemAmount, joinAmount))//
				.onConfirm((interaction2, page, itemAmount) -> onPick(interaction2, userData, itemAmount, joinAmount))//
				.build();
		fluffer10kFun.pagedMessageUtils.addMessage(msg, interaction);
	}

	private void onPick(final MessageComponentInteraction interaction, final ServerUserData userData,
			final ItemAmount itemAmount, final long joinAmount) {
		final long price = joinPrices.get(itemAmount.item.id) * joinAmount;
		if (userData.monies < price) {
			sendEphemeralMessage(interaction, "You don't have enough money");
			return;
		}

		final String newGemId = getId(itemAmount.item.gemSize.nextSize, itemAmount.item.gemRefinement,
				itemAmount.item.gemType);
		final Item newGem = fluffer10kFun.items.getItem(newGemId);

		userData.monies -= price;
		userData.addItem(itemAmount.item, -joinAmount * 2);
		userData.addItem(newGemId, joinAmount);

		final String description = "You joined " + Items.getName(itemAmount.item, joinAmount * 2) + " to make "
				+ Items.getName(newGem, joinAmount) + ".";
		interaction.createOriginalMessageUpdater()
				.addEmbed(makeEmbed("Gems joined", description, CommandJeweller.jewellerImgUrl)).update();

	}
}
