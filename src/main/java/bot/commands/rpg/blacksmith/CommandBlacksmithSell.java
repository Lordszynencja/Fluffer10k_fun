package bot.commands.rpg.blacksmith;

import static bot.commands.rpg.blacksmith.CommandBlacksmith.imgUrl;
import static bot.commands.rpg.blacksmith.CommandBlacksmith.oreIds;
import static bot.data.items.ItemUtils.calculatePrice;
import static bot.data.items.ItemUtils.formatNumber;
import static bot.data.items.ItemUtils.getFormattedMonies;
import static bot.util.CollectionUtils.mapToList;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.Utils.capitalize;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;
import static bot.util.modularPrompt.ModularPrompt.prompt;
import static bot.util.modularPrompt.ModularPromptButton.button;
import static java.util.stream.Collectors.toList;

import java.util.List;

import org.javacord.api.entity.message.component.ButtonStyle;
import org.javacord.api.interaction.MessageComponentInteraction;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.data.items.Item;
import bot.userData.ServerUserData;
import bot.util.EmbedUtils.EmbedField;
import bot.util.modularPrompt.ModularPrompt;
import bot.util.pages.builders.PagedPickerMessageBuilder;
import bot.util.pages.messages.PagedMessage;
import bot.util.subcommand.Subcommand;

public class CommandBlacksmithSell extends Subcommand {

	private final Fluffer10kFun fluffer10kFun;

	public CommandBlacksmithSell(final Fluffer10kFun fluffer10kFun) {
		super("sell", "Sell ores to the blacksmith");

		this.fluffer10kFun = fluffer10kFun;
	}

	private PagedMessage makeList(final ServerUserData userData) {
		final List<Item> items = userData.items.entrySet().stream()//
				.filter(entry -> entry.getValue() > 0 && oreIds.contains(entry.getKey()))//
				.map(entry -> fluffer10kFun.items.getItem(entry.getKey()))//
				.sorted()//
				.collect(toList());
		final List<EmbedField> fields = mapToList(items,
				item -> new EmbedField(item.name, formatNumber(userData.items.get(item.id))));

		return new PagedPickerMessageBuilder<>("Selling ore", 5, fields, items)//
				.imgUrl(imgUrl)//
				.onPick((in, page, item) -> onPick(in, page, userData, item, ""))//
				.build();
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {
		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(interaction);
		fluffer10kFun.pagedMessageUtils.addMessage(makeList(userData), interaction);
	}

	private void onPick(final MessageComponentInteraction in, final int page, final ServerUserData userData,
			final Item item, final String append) {
		final String title = capitalize(item.name) + " (" + userData.items.get(item.id) + ")";
		final String description = "You will get " + getFormattedMonies(calculatePrice(userData, item)) + " for it"
				+ append;

		final boolean sellDisabled = userData.items.get(item.id) <= 0;

		final ModularPrompt prompt = prompt(makeEmbed(title, description, item.image), //
				button("Sell", ButtonStyle.PRIMARY, in2 -> onSell(in2, page, userData, item), sellDisabled), //
				button("Back", ButtonStyle.DANGER, in2 -> onBack(in2, page, userData)));

		fluffer10kFun.modularPromptUtils.addMessage(prompt, in);
	}

	private void onSell(final MessageComponentInteraction in, final int page, final ServerUserData userData,
			final Item item) {
		if (userData.items.getOrDefault(item.id, 0L) <= 0) {
			onBack(in, page, userData);
			return;
		}
		if (userData.rpg.fightId != null) {
			sendEphemeralMessage(in, "Can't sell during a fight!");
			return;
		}

		final long price = calculatePrice(userData, item);

		userData.monies += price;
		userData.addItem(item.id, -1);

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
