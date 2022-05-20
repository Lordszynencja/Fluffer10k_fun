package bot.commands.rpg;

import static bot.util.CollectionUtils.addToLongOnMap;
import static bot.util.CollectionUtils.mapToList;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionChoice;
import org.javacord.api.interaction.SlashCommandOptionType;

import bot.Fluffer10kFun;
import bot.data.items.Item.ItemRarity;
import bot.data.items.ItemAmount;
import bot.data.items.ItemClass;
import bot.userData.ServerUserData;
import bot.util.EmbedUtils.EmbedField;
import bot.util.pages.builders.PagedPickerMessageBuilder;
import bot.util.pages.messages.PagedMessage;
import bot.util.subcommand.Command;

public class CommandBackpack extends Command {
	private static Predicate<ItemAmount> itemClassFilter(final ItemClass itemClass) {
		return item -> !item.item.classes.contains(itemClass);
	}

	private static Predicate<ItemAmount> itemRarityEqualsFilter(final ItemRarity itemRarity) {
		return item -> item.item.rarity != itemRarity;
	}

	private static Predicate<ItemAmount> itemRarityHigherThanFilter(final ItemRarity itemRarity) {
		return item -> item.item.rarity.value < itemRarity.value;
	}

	private static enum ItemTypeFilter {
		ALL(null, item -> false), //
		ARMOR("Armor", item -> item.item.classes.contains(ItemClass.LIGHT_ARMOR)//
				|| item.item.classes.contains(ItemClass.MEDIUM_ARMOR)//
				|| item.item.classes.contains(ItemClass.HEAVY_ARMOR)), //
		GIFT("Gift", itemClassFilter(ItemClass.GIFT)), //
		GEM("Gem", item -> item.item.gemType == null), //
		MG_DROP("Monster girl drop", itemClassFilter(ItemClass.MONMUSU_DROP)), //
		ORE("Ore", itemClassFilter(ItemClass.ORE)), //
		RING("Ring", itemClassFilter(ItemClass.RING)), //
		SHIELD("Shield", itemClassFilter(ItemClass.SHIELD)), //
		SINGLE_USE("Single use", itemClassFilter(ItemClass.SINGLE_USE)), //
		WEAPON("Weapon", itemClassFilter(ItemClass.WEAPON));

		public final String name;
		public final Predicate<ItemAmount> removeIf;

		private ItemTypeFilter(final String name, final Predicate<ItemAmount> removeIf) {
			this.name = name;
			this.removeIf = removeIf;
		}
	}

	private static enum ItemRarityFilter {
		ALL(null, item -> false), //
		TRASH("Trash", itemRarityEqualsFilter(ItemRarity.TRASH)), //
		COMMON("Common", itemRarityEqualsFilter(ItemRarity.COMMON)), //
		UNCOMMON("Uncommon", itemRarityEqualsFilter(ItemRarity.UNCOMMON)), //
		UNCOMMON_PLUS("Uncommon+", itemRarityHigherThanFilter(ItemRarity.UNCOMMON)), //
		RARE("Rare", itemRarityEqualsFilter(ItemRarity.RARE)), //
		RARE_PLUS("Rare+", itemRarityHigherThanFilter(ItemRarity.RARE));

		public final String name;
		public final Predicate<ItemAmount> removeIf;

		private ItemRarityFilter(final String name, final Predicate<ItemAmount> removeIf) {
			this.name = name;
			this.removeIf = removeIf;
		}
	}

	private final Fluffer10kFun fluffer10kFun;

	private static SlashCommandOption createItemTypeOption() {
		final List<SlashCommandOptionChoice> choices = asList(ItemTypeFilter.values()).stream()//
				.filter(itemType -> itemType != ItemTypeFilter.ALL)//
				.map(filter -> SlashCommandOptionChoice.create(filter.name, filter.name()))//
				.collect(toList());

		return SlashCommandOption.createWithChoices(SlashCommandOptionType.STRING, "item_type", "filter items by type",
				false, choices);
	}

	private static SlashCommandOption createItemRarityOption() {
		final List<SlashCommandOptionChoice> choices = asList(ItemRarityFilter.values()).stream()//
				.filter(itemType -> itemType != ItemRarityFilter.ALL)//
				.map(filter -> SlashCommandOptionChoice.create(filter.name, filter.name()))//
				.collect(toList());

		return SlashCommandOption.createWithChoices(SlashCommandOptionType.STRING, "item_rarity",
				"filter items by rarity", false, choices);
	}

	public CommandBackpack(final Fluffer10kFun fluffer10kFun) {
		super(fluffer10kFun.apiUtils, "backpack", "Check your items", //
				createItemTypeOption(), //
				createItemRarityOption());

		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {
		final Server server = interaction.getServer().orElse(null);
		if (server == null) {
			sendEphemeralMessage(interaction, "This command can only be used on server");
			return;
		}

		final ItemTypeFilter itemType = ItemTypeFilter
				.valueOf(interaction.getOptionStringValueByName("item_type").orElse(ItemTypeFilter.ALL.name()));
		final ItemRarityFilter itemRarity = ItemRarityFilter
				.valueOf(interaction.getOptionStringValueByName("item_rarity").orElse(ItemRarityFilter.ALL.name()));

		final long serverId = server.getId();
		final User user = interaction.getUser();
		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(serverId, user.getId());

		final Map<String, Long> userItems = new HashMap<>(userData.items);
		userData.rpg.eq.values().stream()//
				.filter(item -> item != null)//
				.forEach(itemId -> addToLongOnMap(userItems, itemId, 1));

		final List<ItemAmount> items = new ArrayList<>();
		userItems.forEach((id, amount) -> {
			if (amount != 0) {
				items.add(fluffer10kFun.items.getItemAmount(id, amount));
			}
		});

		items.removeIf(itemType.removeIf);
		items.removeIf(itemRarity.removeIf);
		items.sort(null);

		final List<EmbedField> fields = mapToList(items, itemAmount -> itemAmount.getAsField());

		final PagedMessage msg = new PagedPickerMessageBuilder<>("Backpack", 10, fields, items)//
				.dataToEmbed(itemAmount -> itemAmount.getDetails())//
				.build();
		fluffer10kFun.pagedMessageUtils.addMessage(msg, interaction);
	}
}
