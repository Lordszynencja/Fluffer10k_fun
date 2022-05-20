package bot.commands.rpg.blacksmith;

import static bot.commands.rpg.blacksmith.CommandBlacksmith.imgUrl;
import static bot.data.items.ItemUtils.getFormattedMonies;
import static bot.data.items.data.OreItems.ORE_COAL;
import static bot.data.items.data.OreItems.ORE_COPPER;
import static bot.data.items.data.OreItems.ORE_DEMON_REALM_SILVER;
import static bot.data.items.data.OreItems.ORE_DRAGONIUM;
import static bot.data.items.data.OreItems.ORE_GOLD;
import static bot.data.items.data.OreItems.ORE_IRON;
import static bot.data.items.data.OreItems.ORE_SILVER;
import static bot.util.CollectionUtils.mapToList;
import static bot.util.CollectionUtils.toMap;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.Utils.Pair.pair;
import static bot.util.modularPrompt.ModularPrompt.prompt;
import static bot.util.modularPrompt.ModularPromptButton.button;

import java.util.List;
import java.util.Map;

import org.javacord.api.entity.message.component.ButtonStyle;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.MessageComponentInteraction;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.data.items.Item;
import bot.userData.ServerUserData;
import bot.util.EmbedUtils.EmbedField;
import bot.util.SingleTimeSet;
import bot.util.modularPrompt.ModularPrompt;
import bot.util.pages.builders.PagedPickerMessageBuilder;
import bot.util.pages.messages.PagedMessage;
import bot.util.subcommand.Subcommand;

public class CommandBlacksmithBuy extends Subcommand {
	private final SingleTimeSet<Map<String, Long>> prices = new SingleTimeSet<>();

	private final Fluffer10kFun fluffer10kFun;

	private void setPrices() {
		prices.set(toMap(pair(ORE_COAL, fluffer10kFun.items.getItem(ORE_COAL).price), //
				pair(ORE_COPPER, fluffer10kFun.items.getItem(ORE_COPPER).price * 3), //
				pair(ORE_IRON, fluffer10kFun.items.getItem(ORE_IRON).price * 3), //
				pair(ORE_SILVER, fluffer10kFun.items.getItem(ORE_SILVER).price * 4), //
				pair(ORE_GOLD, fluffer10kFun.items.getItem(ORE_GOLD).price * 6), //
				pair(ORE_DEMON_REALM_SILVER, fluffer10kFun.items.getItem(ORE_DEMON_REALM_SILVER).price * 7), //
				pair(ORE_DRAGONIUM, fluffer10kFun.items.getItem(ORE_DRAGONIUM).price * 3)));
	}

	private long getPrice(final Item item) {
		return prices.get().get(item.id);
	}

	public CommandBlacksmithBuy(final Fluffer10kFun fluffer10kFun) {
		super("buy", "Buy some ore from the blacksmith");

		this.fluffer10kFun = fluffer10kFun;

		this.fluffer10kFun.apiUtils.initUtils.addInit("blacksmith buy prices", this::setPrices);
	}

	private PagedMessage makeList(final ServerUserData userData) {
		final List<Item> items = mapToList(CommandBlacksmith.oreIds, fluffer10kFun.items::getItem);
		items.sort(null);
		final List<EmbedField> fields = mapToList(items,
				item -> new EmbedField(item.name, getFormattedMonies(getPrice(item))));

		return new PagedPickerMessageBuilder<>("What will you buy?", 7, fields, items)//
				.imgUrl(imgUrl)//
				.onPick((in, page, itemAmount) -> onPick(in, page, userData, itemAmount, ""))//
				.build();
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {
		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(interaction);
		fluffer10kFun.pagedMessageUtils.addMessage(makeList(userData), interaction);
	}

	private void onPick(final MessageComponentInteraction interaction, final int page, final ServerUserData userData,
			final Item item, final String append) {
		final String title = item.name;
		final String description = (userData.items.getOrDefault(item.id, 0L) > 0
				? "You have " + userData.items.get(item.id) + "\n"
				: "")//
				+ append;
		final EmbedBuilder embed = makeEmbed(title, description, imgUrl)//
				.addField("Price", getFormattedMonies(getPrice(item)));

		final boolean buyDisabled = userData.monies < getPrice(item);

		final ModularPrompt prompt = prompt(embed, //
				button("Buy", ButtonStyle.PRIMARY, in -> onBuy(in, page, userData, item), buyDisabled), //
				button("Back", ButtonStyle.DANGER, in -> onBack(in, page, userData)));
		fluffer10kFun.modularPromptUtils.addMessage(prompt, interaction);
	}

	private void onBuy(final MessageComponentInteraction in, final int page, final ServerUserData userData,
			final Item item) {
		if (userData.monies < getPrice(item)) {
			onPick(in, page, userData, item, "You don't have enough money to buy it");
			return;
		}

		userData.monies -= getPrice(item);
		userData.addItem(item.id, 1);

		onPick(in, page, userData, item, "You bought one");
	}

	private void onBack(final MessageComponentInteraction in, final int page, final ServerUserData userData) {
		fluffer10kFun.pagedMessageUtils.addMessage(makeList(userData).setPage(page), in);
	}
}
