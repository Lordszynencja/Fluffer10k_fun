package bot.commands.rpg.shop;

import static bot.data.items.ItemUtils.calculatePrice;
import static bot.data.items.ItemUtils.formatNumber;
import static bot.data.items.ItemUtils.getFormattedMonies;
import static bot.util.CollectionUtils.addToLongOnMap;
import static bot.util.CollectionUtils.mapToList;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.Utils.capitalize;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;
import static bot.util.modularPrompt.ModularPrompt.prompt;
import static bot.util.modularPrompt.ModularPromptButton.button;
import static java.util.stream.Collectors.toList;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.javacord.api.entity.message.component.ButtonStyle;
import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.MessageComponentInteraction;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.data.ServerData;
import bot.data.items.Item;
import bot.userData.ServerUserData;
import bot.util.EmbedUtils.EmbedField;
import bot.util.modularPrompt.ModularPrompt;
import bot.util.pages.builders.PagedPickerMessageBuilder;
import bot.util.pages.messages.PagedMessage;
import bot.util.subcommand.Subcommand;

public class CommandShopSell extends Subcommand {
	private static final Set<String> excludedItems = new HashSet<>();

	private final Fluffer10kFun fluffer10kFun;

	private void init() {
		excludedItems.addAll(fluffer10kFun.commandJeweller.includedItems.get());
	}

	public CommandShopSell(final Fluffer10kFun fluffer10kFun) {
		super("sell", "Sell item to the shop");

		this.fluffer10kFun = fluffer10kFun;

		fluffer10kFun.apiUtils.initUtils.addInit("shop sell", this::init, "jeweller");
	}

	private PagedMessage makeList(final ServerUserData userData) {
		final List<Item> items = userData.items.entrySet().stream()//
				.filter(entry -> entry.getValue() > 0 && !excludedItems.contains(entry.getKey()))//
				.map(entry -> fluffer10kFun.items.getItem(entry.getKey()))//
				.filter(item -> item.isSellable())//
				.sorted()//
				.collect(toList());
		final List<EmbedField> fields = mapToList(items,
				item -> new EmbedField(item.name, formatNumber(userData.items.get(item.id))));

		return new PagedPickerMessageBuilder<>("Selling item", 5, fields, items)//
				.imgUrl(CommandShop.shopkeeperImgUrl)//
				.onPick((in, page, item) -> onPick(in, page, userData, item, ""))//
				.build();
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {
		final Server server = interaction.getServer().orElse(null);
		if (server == null) {
			sendEphemeralMessage(interaction, "This command can only be used on server");
			return;
		}

		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(server, interaction.getUser());
		fluffer10kFun.pagedMessageUtils.addMessage(makeList(userData), interaction);
	}

	private void onPick(final MessageComponentInteraction in, final int page, final ServerUserData userData,
			final Item item, final String append) {
		final String title = capitalize(item.name) + " (" + userData.items.get(item.id) + ")";
		final String description = "You will get " + getFormattedMonies(calculatePrice(userData, item)) + " for it"
				+ append;

		final ModularPrompt prompt = prompt(makeEmbed(title, description, item.image));

		int amount = 1;
		for (int i = 0; i < 4; i++) {
			if (userData.items.get(item.id) >= amount) {
				final int sellAmount = amount;
				prompt.addButton(button("Sell " + amount, ButtonStyle.PRIMARY,
						in2 -> onSell(in2, page, userData, item, sellAmount)));
			}
			amount *= 10;
		}
		prompt.addButton(button("Back", ButtonStyle.DANGER, in2 -> onBack(in2, page, userData)));

		fluffer10kFun.modularPromptUtils.addMessage(prompt, in);
	}

	private void onSell(final MessageComponentInteraction in, final int page, final ServerUserData userData,
			final Item item, final long amount) {
		if (userData.items.getOrDefault(item.id, 0L) < amount) {
			onBack(in, page, userData);
			return;
		}
		if (userData.rpg.fightId != null) {
			sendEphemeralMessage(in, "Can't sell during a fight!");
			return;
		}

		final long price = calculatePrice(userData, item);

		userData.monies += price * amount;
		userData.addItem(item.id, -amount);

		final ServerData serverData = fluffer10kFun.botDataUtils.getServerData(in.getServer().get());
		addToLongOnMap(serverData.shopItems, item.id, amount);

		if (userData.items.getOrDefault(item.id, 0L) <= 0) {
			onBack(in, page, userData);
		} else {
			onPick(in, page, userData, item, "\n\nItem sold!");
		}
	}

	private void onBack(final MessageComponentInteraction in, final int page, final ServerUserData userData) {
		fluffer10kFun.pagedMessageUtils.addMessage(makeList(userData).setPage(page), in);
	}
}
