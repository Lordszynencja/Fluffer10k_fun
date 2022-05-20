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
import bot.data.items.data.GemItems.GemRefinement;
import bot.userData.ServerUserData;
import bot.util.EmbedUtils.EmbedField;
import bot.util.pages.builders.PagedPickerMessageBuilder;
import bot.util.pages.messages.PagedMessage;
import bot.util.subcommand.Subcommand;

public class CommandJewellerRefine extends Subcommand {
	private final Map<String, Long> refinePrices = new HashMap<>();

	private final Fluffer10kFun fluffer10kFun;

	public CommandJewellerRefine(final Fluffer10kFun fluffer10kFun) {
		super("refine", "refine gems", //
				SlashCommandOption.create(SlashCommandOptionType.LONG, "amount", "amount of gems you want to refine"));

		this.fluffer10kFun = fluffer10kFun;

		fluffer10kFun.items.items.values().forEach(item -> {
			if (item.gemRefinement != null && item.gemRefinement == GemRefinement.UNREFINED) {
				final long unrefinedPrice = item.price;
				final long refinedPrice = fluffer10kFun.items
						.getItem(GemItems.getId(item.gemSize, GemRefinement.REFINED, item.gemType)).price;
				refinePrices.put(item.id, refinedPrice - unrefinedPrice);
			}
		});
	}

	private long getRefinePrice(final Item item, final long refineAmount) {
		return refineAmount * refinePrices.get(item.id);
	}

	private EmbedBuilder itemAmountToEmbed(final ItemAmount itemAmount, final long refineAmount) {
		return makeEmbed("Refine " + refineAmount + " " + itemAmount.item.namePlural + "?", //
				"This will cost " + getFormattedMonies(getRefinePrice(itemAmount.item, refineAmount)), //
				CommandJeweller.jewellerImgUrl);
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) throws Exception {
		final long refineAmount = getOption(interaction).getOptionLongValueByName("amount").orElse(1L);
		if (refineAmount <= 0) {
			sendEphemeralMessage(interaction, "You can't refine less than one gem");
			return;
		}

		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(interaction.getServer().get(),
				interaction.getUser());
		final List<ItemAmount> items = userData.items.entrySet().stream()//
				.filter(entry -> entry.getValue() >= refineAmount)
				.map(entry -> fluffer10kFun.items.getItemAmount(entry.getKey(), entry.getValue()))//
				.filter(itemAmount -> itemAmount.item.gemType != null
						&& itemAmount.item.gemRefinement == GemRefinement.UNREFINED)//
				.sorted()//
				.collect(toList());

		if (items.isEmpty()) {
			sendEphemeralMessage(interaction, "You don't have gems to refine");
			return;
		}

		final List<EmbedField> fields = mapToList(items, itemAmount -> itemAmount.getAsField());

		final PagedMessage msg = new PagedPickerMessageBuilder<>("Pick gems to refine", 5, fields, items)//
				.imgUrl(CommandJeweller.jewellerImgUrl)//
				.dataToEmbed(itemAmount -> itemAmountToEmbed(itemAmount, refineAmount))//
				.onConfirm((interaction2, page, itemAmount) -> onPick(interaction2, userData, itemAmount, refineAmount))//
				.build();
		fluffer10kFun.pagedMessageUtils.addMessage(msg, interaction);
	}

	private void onPick(final MessageComponentInteraction interaction, final ServerUserData userData,
			final ItemAmount itemAmount, final long refineAmount) {
		final long price = refinePrices.get(itemAmount.item.id) * refineAmount;
		if (userData.monies < price) {
			sendEphemeralMessage(interaction, "You don't have enough money");
			return;
		}

		final String newGemId = getId(itemAmount.item.gemSize, GemRefinement.REFINED, itemAmount.item.gemType);
		final Item newGem = fluffer10kFun.items.getItem(newGemId);

		userData.monies -= price;
		userData.addItem(itemAmount.item, -refineAmount);
		userData.addItem(newGemId, refineAmount);

		final String description = "You refined " + Items.getName(itemAmount.item, refineAmount) + " into "
				+ Items.getName(newGem, refineAmount) + ".";
		interaction.createOriginalMessageUpdater()
				.addEmbed(makeEmbed("Gems refined", description, CommandJeweller.jewellerImgUrl)).update();

	}
}
