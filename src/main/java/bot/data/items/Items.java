package bot.data.items;

import static bot.util.CollectionUtils.addToSetOnMap;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import bot.Fluffer10kFun;
import bot.data.items.data.ArmorItems;
import bot.data.items.data.CraftingItems;
import bot.data.items.data.GemItems;
import bot.data.items.data.MagicScrollItems;
import bot.data.items.data.MonmusuDropItems;
import bot.data.items.data.OreItems;
import bot.data.items.data.PickaxeItems;
import bot.data.items.data.PotionItems;
import bot.data.items.data.QuestItems;
import bot.data.items.data.RingItems;
import bot.data.items.data.SpecialItems;
import bot.data.items.data.WeaponItems;

public class Items {
	public final Map<String, Item> items = new HashMap<>();
	public final Map<ItemClass, Set<Item>> itemsByClass = new HashMap<>();

	public void addItem(final Item item) {
		items.put(item.id, item);
		for (final ItemClass c : item.classes) {
			addToSetOnMap(itemsByClass, c, item);
		}
	}

	private void addItem(final ItemBuilder itemBuilder) {
		addItem(itemBuilder.build());
	}

	public Items(final Fluffer10kFun fluffer10kFun) {
		ArmorItems.addItems(this::addItem);
		CraftingItems.addItems(this::addItem);
		GemItems.addItems(this::addItem);
		MagicScrollItems.addItems(this::addItem);
		MonmusuDropItems.addItems(this::addItem);
		OreItems.addItems(this::addItem);
		PickaxeItems.addItems(this::addItem);
		PotionItems.addItems(this::addItem);
		QuestItems.addItems(this::addItem);
		RingItems.addItems(this::addItem);
		SpecialItems.addItems(this::addItem);
		WeaponItems.addItems(this, this::addItem);

		fluffer10kFun.botDataUtils.specialItems.forEach((itemId, item) -> {
			items.put(itemId, item);
		});
	}

	public Item getItem(final String itemId) {
		return items.get(itemId);
	}

	public static String getName(final Item item, final long amount) {
		return amount == 1 ? item.name : (amount + " " + item.namePlural);
	}

	public static String getName(final ItemAmount itemAmount) {
		return getName(itemAmount.item, itemAmount.amount);
	}

	public String getName(final SimpleItemAmount itemAmount) {
		return getName(items.get(itemAmount.id), itemAmount.amount);
	}

	public String getName(final String itemId, final long amount) {
		return getName(items.get(itemId), amount);
	}

	public String getName(final String itemId) {
		return items.get(itemId).name;
	}

	public ItemAmount getItemAmount(final String itemId, final long amount) {
		return new ItemAmount(getItem(itemId), amount);
	}

	public ItemAmount getItemAmount(final String itemId) {
		return getItemAmount(itemId, 1);
	}
}
