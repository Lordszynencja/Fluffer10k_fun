package bot.data.items;

import static bot.data.items.ItemUtils.formatNumber;
import static bot.data.items.ItemUtils.getFormattedMonies;
import static bot.data.items.ItemUtils.getFormattedMonsterGold;
import static bot.util.CollectionUtils.toMap;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.Utils.capitalize;
import static bot.util.Utils.Pair.pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.javacord.api.entity.message.embed.EmbedBuilder;

import bot.util.EmbedUtils.EmbedField;

public class ItemAmount implements Comparable<ItemAmount> {
	public Item item;
	public long amount;

	public ItemAmount(final Item item) {
		this(item, 1);
	}

	public ItemAmount(final Item item, final long amount) {
		this.item = item;
		this.amount = amount;
	}

	private void addPrices(final EmbedBuilder embed) {
		final List<String> itemPrices = new ArrayList<>();
		if (item.price > 0) {
			itemPrices.add(getFormattedMonies(item.price));
		}
		if (item.mgPrice > 0) {
			itemPrices.add(getFormattedMonsterGold(item.mgPrice));
		}
		if (!itemPrices.isEmpty()) {
			embed.addField("Price", String.join("\n", itemPrices));
		}
	}

	private void addRequiredStats(final EmbedBuilder embed) {
		final List<String> requiredStats = new ArrayList<>();
		if (item.requiredStrength > 0) {
			requiredStats.add("Strength - " + item.requiredStrength);
		}
		if (item.requiredAgility > 0) {
			requiredStats.add("Agility - " + item.requiredAgility);
		}
		if (item.requiredIntelligence > 0) {
			requiredStats.add("Intelligence - " + item.requiredIntelligence);
		}
		if (!requiredStats.isEmpty()) {
			embed.addField("Required stats", String.join("\n", requiredStats));
		}
	}

	private void addBonuses(final EmbedBuilder embed) {
		final List<String> bonuses = new ArrayList<>();
		if (item.strengthBonus != 0) {
			bonuses.add("Strength bonus: " + item.strengthBonus);
		}
		if (item.agilityBonus != 0) {
			bonuses.add("Agility bonus: " + item.agilityBonus);
		}
		if (item.intelligenceBonus != 0) {
			bonuses.add("Intelligence bonus: " + item.intelligenceBonus);
		}
		if (item.armorBonus != 0) {
			bonuses.add("Armor bonus: " + item.armorBonus);
		}
		if (item.healthBonus != 0) {
			bonuses.add("Health bonus: " + item.healthBonus);
		}
		if (item.healthRegenerationBonus != 0) {
			bonuses.add("Health regeneration bonus: " + item.healthRegenerationBonus);
		}
		if (item.manaBonus != 0) {
			bonuses.add("Mana bonus: " + item.manaBonus);
		}
		if (item.manaRegenBonus != 0) {
			bonuses.add("Mana regen bonus: " + item.manaRegenBonus + "%");
		}
		if (item.damageRollBonus != 0) {
			bonuses.add("Damage roll bonus: " + item.damageRollBonus);
		}
		if (item.magicPower != 0) {
			bonuses.add("Magic damage roll bonus: " + item.magicPower);
		}
		if (item.magicOnHitDamageBonus != 0) {
			bonuses.add("Magic damage on hit bonus: " + item.magicOnHitDamageBonus);
		}
		if (item.criticalStrikeBonus != 0) {
			bonuses.add("Critical strike chance bonus: " + item.criticalStrikeBonus + "%");
		}
		if (!bonuses.isEmpty()) {
			embed.addField("Bonuses", String.join("\n", bonuses));
		}
	}

	private static final Map<ItemClass, String> itemClassDescriptions = toMap(
			pair(ItemClass.AGILITY_BASED, "damage based on agility"), //
			pair(ItemClass.ARMOR_PIERCING, "armor piercing"), //
			pair(ItemClass.BOTH_HANDS, "takes both hands to use"), //
			pair(ItemClass.DUAL_WIELD, "can be wielded in both hands"), //
			pair(ItemClass.GIFT, "can be gifted"), //
			pair(ItemClass.HEAVY_ARMOR, "heavy armor"), //
			pair(ItemClass.LEFT_HAND, "is used in left hand"), //
			pair(ItemClass.LIGHT_ARMOR, "light armor"), //
			pair(ItemClass.LONG, "is long"), //
			pair(ItemClass.MEDIUM_ARMOR, "medium armor"), //
			pair(ItemClass.MONMUSU_DROP, "can drop from monster girl"), //
			pair(ItemClass.ORE, "ore"), //
			pair(ItemClass.PICKAXE, "pickaxe"), //
			pair(ItemClass.QUEST, "quest item"), //
			pair(ItemClass.RANGED, "ranged"), //
			pair(ItemClass.RIGHT_HAND, "is used in right hand"), //
			pair(ItemClass.RING, "ring"), //
			pair(ItemClass.SHIELD, "shield"), //
			pair(ItemClass.SINGLE_USE, "single use"), //
			pair(ItemClass.SPECIAL, "special"), //
			pair(ItemClass.SPELL_VOID, "causes spell void"), //
			pair(ItemClass.STATUS_NEGATION, "negates statuses"), //
			pair(ItemClass.STRENGTH_BASED, "damage based on strength"), //
			pair(ItemClass.USE_IN_COMBAT, "is used in combat"), //
			pair(ItemClass.USE_OUTSIDE_COMBAT, "can be used outside of combat"), //
			pair(ItemClass.WEAPON, "weapon"));

	private void addClasses(final EmbedBuilder embed) {
		final List<String> classes = new ArrayList<>();
		for (final ItemClass itemClass : item.classes) {
			classes.add(itemClassDescriptions.get(itemClass));
		}
		if (!classes.isEmpty()) {
			embed.addField("Item classes", String.join("\n", classes));
		}
	}

	public EmbedField getAsField() {
		return new EmbedField(item.name, formatNumber(amount));
	}

	public EmbedField getAsFieldWithPrice() {
		return new EmbedField(item.name + " (" + getFormattedMonies(item.price) + ")", formatNumber(amount));
	}

	public EmbedField getAsFieldWithPriceAsDescription() {
		return new EmbedField(item.name, getFormattedMonies(item.price));
	}

	public EmbedField getAsFieldWithDescription() {
		return new EmbedField(capitalize(item.name) + " (" + amount + ")", item.description);
	}

	public EmbedBuilder getDetails() {
		final EmbedBuilder embed = makeEmbed(capitalize(item.name) + " (" + amount + ")", item.description, item.image);

		addPrices(embed);
		addRequiredStats(embed);
		addBonuses(embed);
		addClasses(embed);
		embed.addField("Item rarity", item.rarity.name);

		return embed;
	}

	public ItemAmount minus() {
		return new ItemAmount(item, -amount);
	}

	@Override
	public int compareTo(final ItemAmount o) {
		return item.name.compareTo(o.item.name);
	}

	public String getDescription() {
		return Items.getName(item, amount);
	}
}
