package bot.data.items.loot;

import static bot.data.items.ItemUtils.getFormattedMonies;
import static bot.util.CollectionUtils.addToLongOnMap;
import static java.util.stream.Collectors.summingLong;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bot.data.items.ItemAmount;
import bot.data.items.Items;
import bot.userData.ServerUserData;

public class LootList implements Loot {

	private static final String[][] rarityAccents = { //
			{ "", "" }, //
			{ "- ", " -" }, //
			{ "* ", " *" }, //
			{ "< ", " >" }, //
			{ "<<< ", " >>>" }, //
			{ "! ", " !" }, //
			{ "!!! ", " !!!" } };

	private static String itemAmountToLine(final ItemAmount itemAmount) {
		final String itemName = itemAmount.getDescription();
		final String[] rarityAccent = rarityAccents[itemAmount.item.rarity.value];
		return rarityAccent[0] + itemName + rarityAccent[1];
	}

	public final Map<String, Long> items = new HashMap<>();
	public long goldAmount = 0;

	public LootList() {
	}

	public LootList(final List<Loot> loots) {
		loots.forEach(this::add);
	}

	public LootList add(final Loot loot) {
		switch (loot.getType()) {
		case GOLD:
			return add((LootGold) loot);
		case ITEM:
			return add((LootItem) loot);
		case MIXED:
			return add((LootList) loot);
		default:
			throw new RuntimeException("wrong loot type!");
		}
	}

	public LootList add(final LootItem loot) {
		addToLongOnMap(items, loot.itemId, loot.amount);
		return this;
	}

	public LootList add(final LootGold loot) {
		goldAmount += loot.amount;
		return this;
	}

	public LootList add(final LootList loot) {
		loot.items.forEach((itemId, amount) -> addToLongOnMap(items, itemId, amount));
		goldAmount += loot.goldAmount;
		return this;
	}

	@Override
	public void addToUser(final ServerUserData userData) {
		userData.monies += goldAmount;
		items.forEach(userData::addItem);
	}

	@Override
	public String getDescription(final Items items) {
		final List<String> lines = new ArrayList<>();
		this.items.entrySet().stream().map(entry -> items.getItemAmount(entry.getKey(), entry.getValue()))//
				.sorted((a, b) -> -a.item.rarity.compareTo(b.item.rarity))//
				.map(LootList::itemAmountToLine)//
				.forEach(lines::add);

		if (goldAmount > 0) {
			lines.add(getFormattedMonies(goldAmount));
		}

		return lines.isEmpty() ? "Nothing" : String.join("\n", lines);
	}

	@Override
	public LootType getType() {
		return LootType.MIXED;
	}

	public boolean isEmpty() {
		return items.isEmpty() && goldAmount == 0;
	}

	@Override
	public long totalValue(final Items items) {
		return goldAmount + this.items.entrySet().stream()//
				.map(entry -> items.getItem(entry.getKey()).price * entry.getValue())//
				.collect(summingLong(l -> l));
	}
}
