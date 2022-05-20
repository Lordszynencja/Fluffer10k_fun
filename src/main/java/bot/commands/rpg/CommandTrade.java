package bot.commands.rpg;

import static bot.data.items.ItemUtils.getFormattedMonies;
import static bot.util.CollectionUtils.mapToList;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.RandomUtils.getRandomLong;
import static bot.util.TimerUtils.startTimedEvent;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.Button;
import org.javacord.api.entity.message.component.ButtonStyle;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.MessageComponentInteraction;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;

import bot.Fluffer10kFun;
import bot.data.items.Item;
import bot.data.items.ItemAmount;
import bot.userData.ServerUserData;
import bot.util.EmbedUtils.EmbedField;
import bot.util.pages.builders.PagedPickerMessageBuilder;
import bot.util.pages.messages.PagedMessage;
import bot.util.subcommand.Command;

public class CommandTrade extends Command {
	private static class TradeData {
		public final User buyer;
		public final long price;
		public final ItemAmount itemAmount;

		public TradeData(final User buyer, final long price, final ItemAmount itemAmount) {
			this.buyer = buyer;
			this.price = price;
			this.itemAmount = itemAmount;
		}
	}

	private static class TradeAcceptanceData {
		public final User seller;
		public final User buyer;
		public final long price;
		public final ItemAmount itemAmount;

		public TradeAcceptanceData(final User seller, final TradeData tradeData) {
			this.seller = seller;
			buyer = tradeData.buyer;
			price = tradeData.price;
			itemAmount = tradeData.itemAmount;
		}
	}

	private final Fluffer10kFun fluffer10kFun;

	private long nextTradeId = getRandomLong(Long.MAX_VALUE);
	private final Map<String, TradeAcceptanceData> trades = new HashMap<>();

	public CommandTrade(final Fluffer10kFun fluffer10kFun) {
		super(fluffer10kFun.apiUtils, "trade", "Trade items, using the command will make you choose item to sell", //
				SlashCommandOption.create(SlashCommandOptionType.USER, "buyer", "person to trade with", true), //
				SlashCommandOption.create(SlashCommandOptionType.LONG, "price", "price for items you will sell", true), //
				SlashCommandOption.create(SlashCommandOptionType.LONG, "amount",
						"amount of items to trade (default 1)"));

		this.fluffer10kFun = fluffer10kFun;

		fluffer10kFun.apiUtils.commandHandlers.addMessageComponentHandler("trade_yes", this::handleTradeYes);
		fluffer10kFun.apiUtils.commandHandlers.addMessageComponentHandler("trade_no", this::handleTradeNo);
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {
		final Server server = interaction.getServer().orElse(null);
		if (server == null) {
			sendEphemeralMessage(interaction, "This command can only be used on server");
			return;
		}

		final long serverId = server.getId();
		final User user = interaction.getUser();
		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(serverId, user.getId());
		if (userData.rpg.fightId != null) {
			sendEphemeralMessage(interaction, "Can't sell during a fight!");
			return;
		}

		final User buyer = interaction.getOptionUserValueByName("buyer").get();
		final ServerUserData buyerData = fluffer10kFun.serverUserDataUtils.getUserData(serverId, buyer.getId());
		if (buyerData.rpg.fightId != null) {
			sendEphemeralMessage(interaction, "Target can't buy during a fight!");
			return;
		}

		final long price = interaction.getOptionLongValueByName("price").get();
		final long amount = interaction.getOptionLongValueByName("amount").orElse(1L);
		if (price < 0) {
			sendEphemeralMessage(interaction, "You can't sell for negative price");
			return;
		}
		if (amount <= 0) {
			sendEphemeralMessage(interaction, "You can't sell negative amount of items or nothing");
			return;
		}

		final List<TradeData> data = new ArrayList<>();
		userData.items.forEach((itemId, itemAmount) -> {
			if (itemAmount >= amount) {
				final Item item = fluffer10kFun.items.getItem(itemId);
				if (item.isSellable()) {
					data.add(new TradeData(buyer, price, fluffer10kFun.items.getItemAmount(itemId, amount)));
				}
			}
		});
		if (data.isEmpty()) {
			sendEphemeralMessage(interaction,
					"You don't have items that meet the criteria (maybe you want to sell less items?)");
			return;
		}

		data.sort((a, b) -> a.itemAmount.compareTo(b.itemAmount));

		final List<EmbedField> fields = mapToList(data, tradeData -> tradeData.itemAmount.getAsField());

		final PagedMessage msg = new PagedPickerMessageBuilder<>("Pick item to sell", 10, fields, data)//
				.description("You want to sell for " + getFormattedMonies(price))//
				.dataToEmbed(tradeData -> tradeData.itemAmount.getDetails())//
				.onConfirm(this::onPick)//
				.build();
		fluffer10kFun.pagedMessageUtils.addMessage(msg, interaction);
	}

	private ActionRow makeButtons(final String tradeId) {
		return ActionRow.of(Button.create("trade_yes " + tradeId, ButtonStyle.SUCCESS, "Accept"), //
				Button.create("trade_no " + tradeId, ButtonStyle.DANGER, "Decline"));
	}

	private void onPick(final MessageComponentInteraction interaction, final int page, final TradeData tradeData) {
		final Server server = interaction.getServer().get();
		final User seller = interaction.getUser();

		final String description = seller.getDisplayName(server) + " sells "
				+ (tradeData.itemAmount.amount == 1 ? "item" : "items") + " to "
				+ tradeData.buyer.getDisplayName(server);

		final EmbedBuilder embed = makeEmbed("Trade", description)//
				.addField("Item", tradeData.itemAmount.getDescription())//
				.addField("Price", getFormattedMonies(tradeData.price));

		final String tradeId = "" + nextTradeId++;
		final TradeAcceptanceData acceptanceData = new TradeAcceptanceData(seller, tradeData);
		trades.put(tradeId, acceptanceData);
		startTimedEvent(() -> trades.remove(tradeId), System.currentTimeMillis() + 5 * 60 * 1000);

		interaction.createOriginalMessageUpdater().addEmbed(embed).addComponents(makeButtons(tradeId)).update();
	}

	private void handleTradeNo(final MessageComponentInteraction interaction) {
		final String tradeId = interaction.getCustomId().split(" ")[1];
		final TradeAcceptanceData trade = trades.get(tradeId);
		if (trade == null) {
			interaction.createOriginalMessageUpdater().addEmbed(makeEmbed("Trade timed out")).update();
			return;
		}

		final User user = interaction.getUser();
		if (user.getId() != trade.seller.getId() && user.getId() != trade.buyer.getId()) {
			sendEphemeralMessage(interaction, "Not your trade!");
			return;
		}

		trades.remove(tradeId);

		interaction.createOriginalMessageUpdater()
				.addEmbed(
						makeEmbed("Trade cancelled by " + (user.getId() == trade.seller.getId() ? "seller" : "buyer")))
				.update();
	}

	private void handleTradeYes(final MessageComponentInteraction interaction) {
		final String tradeId = interaction.getCustomId().split(" ")[1];
		final TradeAcceptanceData trade = trades.get(tradeId);
		if (trade == null) {
			interaction.createOriginalMessageUpdater().addEmbed(makeEmbed("Trade timed out")).update();
			return;
		}

		final User user = interaction.getUser();
		if (user.getId() != trade.buyer.getId()) {
			sendEphemeralMessage(interaction, "Only buyer can accept the trade");
			return;
		}
		final Server server = interaction.getServer().get();

		final ServerUserData sellerData = fluffer10kFun.serverUserDataUtils.getUserData(server.getId(),
				trade.seller.getId());
		if (sellerData.rpg.fightId != null) {
			sendEphemeralMessage(interaction, "Seller can't sell during a fight!");
			return;
		}
		if (sellerData.items.getOrDefault(trade.itemAmount.item.id, 0L) < trade.itemAmount.amount) {
			interaction.createOriginalMessageUpdater().addEmbed(makeEmbed("Seller doesn't have the item anymore"))
					.update();
			return;
		}

		final ServerUserData buyerData = fluffer10kFun.serverUserDataUtils.getUserData(server.getId(),
				trade.buyer.getId());
		if (buyerData.rpg.fightId != null) {
			sendEphemeralMessage(interaction, "Buyer can't buy during a fight!");
			return;
		}
		if (buyerData.monies < trade.price) {
			sendEphemeralMessage(interaction, "You don't have enough money");
			return;
		}

		buyerData.addItem(trade.itemAmount);
		buyerData.monies -= trade.price;
		sellerData.addItem(trade.itemAmount.minus());
		sellerData.monies += trade.price;

		trades.remove(tradeId);

		final String description = trade.buyer.getDisplayName(server) + " bought " + trade.itemAmount.getDescription()
				+ " from " + trade.seller.getDisplayName(server) + " for " + getFormattedMonies(trade.price);
		interaction.createOriginalMessageUpdater().addEmbed(makeEmbed("Trade successful!", description)).update();
	}
}
