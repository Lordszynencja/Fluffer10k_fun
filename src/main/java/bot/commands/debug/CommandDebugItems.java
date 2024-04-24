package bot.commands.debug;

import static bot.util.CollectionUtils.mapArray;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;
import static bot.util.apis.MessageUtils.splitLongMessage;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandInteractionOption;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;

import bot.Fluffer10kFun;
import bot.data.items.Item;
import bot.data.items.ItemClass;
import bot.util.subcommand.Subcommand;

public class CommandDebugItems extends Subcommand {

	private final Fluffer10kFun fluffer10kFun;

	public CommandDebugItems(final Fluffer10kFun fluffer10kFun) {
		super("items", "list all item IDs or item data", //
				SlashCommandOption.create(SlashCommandOptionType.STRING, "item_name",
						"filter by item name (or its part)"), //
				SlashCommandOption.create(SlashCommandOptionType.STRING, "class", "filter by item class"));
		this.fluffer10kFun = fluffer10kFun;
	}

	private static String shortItemToString(final Item i) {
		return i.id + " - " + i.name;
	}

	private static final Map<String, Object> defaultItemValues = new Item((Map<String, Object>) null).toMap();

	private static String longItemToString(final Item i) {
		final StringBuilder b = new StringBuilder(shortItemToString(i));

		final Map<String, Object> map = i.toMap();
		final List<String> keys = new ArrayList<>(map.keySet());
		keys.sort(null);
		for (final String key : keys) {
			final Object value = map.get(key);
			final Object defaultValue = defaultItemValues.get(key);
			if (defaultValue == null ? (value != null) : !defaultValue.equals(value)) {
				b.append("\n" + key + " = " + map.get(key));
			}
		}

		return b.append("\n").toString();
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {
		final SlashCommandInteractionOption option = getOption(interaction);
		String itemName = option.getArgumentStringValueByName("item_name").orElse(null);
		final String className = option.getArgumentStringValueByName("class").orElse(null);

		ItemClass itemClass = null;
		try {
			if (className != null) {
				itemClass = ItemClass.valueOf(className);
			}
		} catch (final Exception ex) {
			final String[] classNames = mapArray(ItemClass.values(), e -> e.name(), String.class);
			sendEphemeralMessage(interaction, "Wrong class, classes are:\n" + String.join("\n", classNames));
			return;
		}
		String text = "";
		if (itemName != null) {
			itemName = itemName.toLowerCase();
			text += "with '" + itemName + "' in name\n";
		}
		if (itemClass != null) {
			text += "with " + itemClass + " in name\n";
		}
		interaction.createImmediateResponder().addEmbed(makeEmbed("List of items", text)).respond();

		final List<Item> items = new ArrayList<>(fluffer10kFun.items.items.values());
		if (itemName != null) {
			final String tmp = itemName;
			items.removeIf(item -> !item.name.toLowerCase().contains(tmp));
		}
		if (itemClass != null) {
			final ItemClass c = itemClass;
			items.removeIf(item -> !item.classes.contains(c));
		}
		items.sort((a, b) -> a.id.compareTo(b.id));

		final Function<Item, String> mapper = items.size() < 10 ? CommandDebugItems::longItemToString
				: CommandDebugItems::shortItemToString;

		final String itemListText = String.join("\n", items.stream().map(mapper).collect(toList()));
		final TextChannel channel = interaction.getChannel().get();
		for (final String s : splitLongMessage(itemListText)) {
			channel.sendMessage(s);
		}
	}
}
