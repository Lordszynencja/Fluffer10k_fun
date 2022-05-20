package bot.commands.rpg.jeweller;

import static bot.commands.rpg.jeweller.CommandJeweller.jewellerImgUrl;
import static bot.commands.rpg.shop.CommandShop.shopkeeperImgUrl;
import static bot.data.items.ItemUtils.getFormattedMonies;
import static bot.util.CollectionUtils.mapToList;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;
import static bot.util.modularPrompt.ModularPrompt.prompt;
import static bot.util.modularPrompt.ModularPromptButton.button;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.javacord.api.entity.message.component.ButtonStyle;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.MessageComponentInteraction;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.data.items.Item;
import bot.data.items.data.GemItems;
import bot.data.items.data.GemItems.GemRefinement;
import bot.data.items.data.GemItems.GemSize;
import bot.data.items.data.GemItems.GemType;
import bot.userData.ServerUserData;
import bot.util.EmbedUtils.EmbedField;
import bot.util.modularPrompt.ModularPrompt;
import bot.util.pages.builders.PagedPickerMessageBuilder;
import bot.util.pages.messages.PagedMessage;
import bot.util.subcommand.Subcommand;

public class CommandJewellerBuy extends Subcommand {
	private static final Set<String> gemIds = new HashSet<>();
	static {
		for (final GemSize size : GemSize.values()) {
			for (final GemType type : GemType.values()) {
				gemIds.add(GemItems.getId(size, GemRefinement.REFINED, type));
			}
		}
	}

	private final Fluffer10kFun fluffer10kFun;

	public CommandJewellerBuy(final Fluffer10kFun fluffer10kFun) {
		super("buy", "Buy a gem from the jeweller");

		this.fluffer10kFun = fluffer10kFun;
	}

	private PagedMessage makeList(final ServerUserData userData) {
		final List<Item> items = mapToList(gemIds, fluffer10kFun.items::getItem);
		items.sort(null);
		final List<EmbedField> fields = mapToList(items,
				item -> new EmbedField(item.name, getFormattedMonies(item.price)));

		return new PagedPickerMessageBuilder<>("What will you buy?", 8, fields, items)//
				.imgUrl(jewellerImgUrl)//
				.onPick((in, page, itemAmount) -> onPick(in, page, userData, itemAmount, ""))//
				.build();
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {
		final Server server = interaction.getServer().orElse(null);
		if (server == null) {
			sendEphemeralMessage(interaction, "This command can only be used on server");
			return;
		}

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
		final EmbedBuilder embed = makeEmbed(title, description, shopkeeperImgUrl)//
				.addField("Price", getFormattedMonies(item.price));

		final boolean buyDisabled = userData.monies < item.price;

		final ModularPrompt prompt = prompt(embed, //
				button("Buy", ButtonStyle.PRIMARY, in -> onBuy(in, page, userData, item), buyDisabled), //
				button("Back", ButtonStyle.DANGER, in -> onBack(in, page, userData)));
		fluffer10kFun.modularPromptUtils.addMessage(prompt, interaction);
	}

	private void onBuy(final MessageComponentInteraction in, final int page, final ServerUserData userData,
			final Item item) {
		if (userData.monies < item.price) {
			onPick(in, page, userData, item, "You don't have enough money to buy it");
			return;
		}

		userData.monies -= item.price;
		userData.addItem(item.id, 1);

		onPick(in, page, userData, item, "You bought one");
	}

	private void onBack(final MessageComponentInteraction in, final int page, final ServerUserData userData) {
		fluffer10kFun.pagedMessageUtils.addMessage(makeList(userData).setPage(page), in);
	}
}
