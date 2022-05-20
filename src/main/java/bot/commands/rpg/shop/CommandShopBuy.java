package bot.commands.rpg.shop;

import static bot.commands.rpg.shop.CommandShop.shopkeeperImgUrl;
import static bot.data.items.ItemUtils.getFormattedMonies;
import static bot.data.items.data.QuestItems.TEDDY_BEAR_PLUSHIE;
import static bot.util.CollectionUtils.addToLongOnMap;
import static bot.util.CollectionUtils.mapToList;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;
import static bot.util.modularPrompt.ModularPrompt.prompt;
import static bot.util.modularPrompt.ModularPromptButton.button;
import static java.util.stream.Collectors.toList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.javacord.api.entity.message.component.ButtonStyle;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.MessageComponentInteraction;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.data.ServerData;
import bot.data.items.Item;
import bot.data.items.ItemAmount;
import bot.data.items.ItemClass;
import bot.data.quests.QuestType;
import bot.userData.ServerUserData;
import bot.userData.rpg.questData.QuestStep;
import bot.util.EmbedUtils.EmbedField;
import bot.util.modularPrompt.ModularPrompt;
import bot.util.pages.builders.PagedPickerMessageBuilder;
import bot.util.pages.messages.PagedMessage;
import bot.util.subcommand.Subcommand;

public class CommandShopBuy extends Subcommand {
	private final Fluffer10kFun fluffer10kFun;

	public CommandShopBuy(final Fluffer10kFun fluffer10kFun) {
		super("buy", "Buy something from the shop");

		this.fluffer10kFun = fluffer10kFun;
	}

	private Map<String, Long> getSpecialItems(final ServerUserData userData) {
		final Map<String, Long> items = new HashMap<>();

		if (userData.rpg.questIsOnStep(QuestType.SLEEPY_MOUSE_TEDDY_BEAR_QUEST, QuestStep.SEARCHING_FOR_TEDDY)//
				&& userData.items.getOrDefault(TEDDY_BEAR_PLUSHIE, 0L) == 0) {
			items.put(TEDDY_BEAR_PLUSHIE, 1L);
		}

		return items;
	}

	private List<ItemAmount> getUserShop(final ServerData serverData, final ServerUserData userData) {
		final List<ItemAmount> list = serverData.shopItems.entrySet().stream()//
				.filter(entry -> entry.getValue() > 0)//
				.map(entry -> fluffer10kFun.items.getItemAmount(entry.getKey(), entry.getValue()))//
				.collect(toList());
		list.addAll(mapToList(getSpecialItems(userData).entrySet(),
				entry -> fluffer10kFun.items.getItemAmount(entry.getKey(), entry.getValue())));
		list.sort(null);

		return list;
	}

	private PagedMessage makeList(final ServerData serverData, final ServerUserData userData) {
		final List<ItemAmount> itemAmounts = getUserShop(serverData, userData);
		final List<Item> items = mapToList(itemAmounts, itemAmount -> itemAmount.item);
		final List<EmbedField> fields = mapToList(itemAmounts, itemAmount -> itemAmount.getAsFieldWithPrice());

		return new PagedPickerMessageBuilder<>("What are you buying, stranger?", 5, fields, items)//
				.imgUrl(shopkeeperImgUrl)//
				.onPick((in, page, itemAmount) -> onPick(in, page, serverData, userData, itemAmount, ""))//
				.build();
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {
		final Server server = interaction.getServer().orElse(null);
		if (server == null) {
			sendEphemeralMessage(interaction, "This command can only be used on server");
			return;
		}

		final ServerData serverData = fluffer10kFun.botDataUtils.getServerData(server);
		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(interaction);
		fluffer10kFun.pagedMessageUtils.addMessage(makeList(serverData, userData), interaction);
	}

	private long getAmount(final ServerData serverData, final ServerUserData userData, final String itemId) {
		if (serverData.shopItems.containsKey(itemId)) {
			return serverData.shopItems.get(itemId);
		}

		return getSpecialItems(userData).getOrDefault(itemId, 0L);
	}

	private boolean itemAvailable(final ServerData serverData, final ServerUserData userData, final String itemId) {
		return getAmount(serverData, userData, itemId) > 0;
	}

	private void onPick(final MessageComponentInteraction interaction, final int page, final ServerData serverData,
			final ServerUserData userData, final Item item, final String append) {
		final String title = item.name + " (" + getAmount(serverData, userData, item.id) + ")";
		final String description = (userData.items.getOrDefault(item.id, 0L) > 0
				? "You have " + userData.items.get(item.id) + "\n"
				: "")//
				+ append;
		final EmbedBuilder embed = makeEmbed(title, description, shopkeeperImgUrl)//
				.addField("Price", getFormattedMonies(item.price));

		final boolean buyDisabled = userData.monies < item.price;

		final ModularPrompt prompt = prompt(embed, //
				button("Buy", ButtonStyle.PRIMARY, in -> onBuy(in, page, serverData, userData, item), buyDisabled), //
				button("Back", ButtonStyle.DANGER, in -> onBack(in, page, serverData, userData)));
		fluffer10kFun.modularPromptUtils.addMessage(prompt, interaction);
	}

	private void onBuy(final MessageComponentInteraction in, final int page, final ServerData serverData,
			final ServerUserData userData, final Item item) {
		if (!itemAvailable(serverData, userData, item.id)) {
			onBack(in, page, serverData, userData);
			return;
		}

		if (userData.monies < item.price) {
			onPick(in, page, serverData, userData, item, "You don't have enough money to buy it");
			return;
		}
		if (userData.rpg.fightId != null) {
			sendEphemeralMessage(in, "Can't use shop during a fight!");
			return;
		}

		userData.monies -= item.price;
		userData.addItem(item.id, 1);
		if (!item.classes.contains(ItemClass.QUEST)) {
			addToLongOnMap(serverData.shopItems, item.id, -1);
		}

		if (!itemAvailable(serverData, userData, item.id)) {
			onBack(in, page, serverData, userData);
		} else {
			onPick(in, page, serverData, userData, item, "You bought one");
		}
	}

	private void onBack(final MessageComponentInteraction in, final int page, final ServerData serverData,
			final ServerUserData userData) {
		fluffer10kFun.pagedMessageUtils.addMessage(makeList(serverData, userData).setPage(page), in);
	}
}
