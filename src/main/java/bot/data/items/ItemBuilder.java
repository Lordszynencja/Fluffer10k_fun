package bot.data.items;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import bot.commands.rpg.spells.ActiveSkill;
import bot.data.items.data.GemItems.GemRefinement;
import bot.data.items.data.GemItems.GemSize;
import bot.data.items.data.GemItems.GemType;

public class ItemBuilder {
	public String id;
	protected final Map<String, Object> data = new HashMap<>();
	{
		data.put("classes", new ArrayList<>());
	}

	public ItemBuilder(final String id, final String name, final String namePlural, final String description) {
		this.id = id;
		data.put("id", id);
		data.put("name", name);
		data.put("namePlural", namePlural);
		data.put("typeName", name);
		data.put("description", description);
	}

	public ItemBuilder() {
	}

	private ItemBuilder add(final String key, final Object val) {
		data.put(key, val);
		return this;
	}

	public ItemBuilder id(final String id) {
		this.id = id;
		return add("id", id);
	}

	public ItemBuilder name(final String name) {
		return add("name", name);
	}

	public ItemBuilder namePlural(final String namePlural) {
		return add("namePlural", namePlural);
	}

	public ItemBuilder typeName(final String typeName) {
		return add("typeName", typeName);
	}

	public ItemBuilder description(final String description) {
		return add("description", description);
	}

	public ItemBuilder image(final String image) {
		return add("image", image);
	}

	public long price() {
		return (long) data.getOrDefault("price", 0);
	}

	public ItemBuilder price(final long price) {
		return add("price", price);
	}

	public ItemBuilder mgPrice(final long mgPrice) {
		return add("mgPrice", mgPrice);
	}

	public ItemBuilder rarity(final int rarity) {
		return add("rarity", rarity);
	}

	public ItemBuilder affectionBonus(final double affectionBonus) {
		return add("affectionBonus", affectionBonus);
	}

	public ItemBuilder spellTaught(final ActiveSkill spellTaught) {
		return add("spellTaught", spellTaught == null ? null : spellTaught.name());
	}

	public ItemBuilder gemType(final GemType gemType) {
		return add("gemType", gemType);
	}

	public ItemBuilder gemSize(final GemSize gemSize) {
		return add("gemSize", gemSize);
	}

	public ItemBuilder gemRefinement(final GemRefinement gemRefinement) {
		return add("gemRefinement", gemRefinement);
	}

	public ItemBuilder pickaxeTier(final int pickaxeTier) {
		return add("pickaxeTier", pickaxeTier);
	}

	@SuppressWarnings("unchecked")
	public ItemBuilder addClass(final ItemClass clazz) {
		((List<ItemClass>) data.get("classes")).add(clazz);
		return this;
	}

	@SuppressWarnings("unchecked")
	public ItemBuilder addClasses(final ItemClass... classes) {
		for (final ItemClass c : classes) {
			((List<ItemClass>) data.get("classes")).add(c);
		}
		return this;
	}

	public ItemBuilder classes(final ItemClass... classes) {
		return classes(new ArrayList<>(asList(classes)));
	}

	public ItemBuilder classes(final List<ItemClass> classes) {
		return add("classes", classes);
	}

	public ItemBuilder requiredAgility(final int requiredAgility) {
		return add("requiredAgility", requiredAgility);
	}

	public ItemBuilder requiredIntelligence(final int requiredIntelligence) {
		return add("requiredIntelligence", requiredIntelligence);
	}

	public ItemBuilder requiredStrength(final int requiredStrength) {
		return add("requiredStrength", requiredStrength);
	}

	public ItemBuilder agilityBonus(final int agilityBonus) {
		return add("agilityBonus", agilityBonus);
	}

	public ItemBuilder armorBonus(final int armorBonus) {
		return add("armorBonus", armorBonus);
	}

	public ItemBuilder criticalStrikeBonus(final int criticalStrikeBonus) {
		return add("criticalStrikeBonus", criticalStrikeBonus);
	}

	public ItemBuilder damageRollBonus(final int damageRollBonus) {
		return add("damageRollBonus", damageRollBonus);
	}

	public ItemBuilder healthBonus(final int healthBonus) {
		return add("healthBonus", healthBonus);
	}

	public ItemBuilder healthRegenerationBonus(final int healthRegenerationBonus) {
		return add("healthRegenerationBonus", healthRegenerationBonus);
	}

	public ItemBuilder intelligenceBonus(final int intelligenceBonus) {
		return add("intelligenceBonus", intelligenceBonus);
	}

	public int magicDamageRollBonus() {
		return (int) data.getOrDefault("magicDamageRollBonus", 0);
	}

	public ItemBuilder magicPower(final int magicPower) {
		return add("magicPower", magicPower);
	}

	public ItemBuilder magicOnHitDamageBonus(final int magicOnHitDamageBonus) {
		return add("magicOnHitDamageBonus", magicOnHitDamageBonus);
	}

	public ItemBuilder manaBonus(final int manaBonus) {
		return add("manaBonus", manaBonus);
	}

	public ItemBuilder manaRegenBonus(final int manaRegenBonus) {
		return add("manaRegenBonus", manaRegenBonus);
	}

	public ItemBuilder strengthBonus(final int strengthBonus) {
		return add("strengthBonus", strengthBonus);
	}

	public Item build() {
		return new Item(data);
	}

	public void add(final Consumer<ItemBuilder> itemAdder) {
		itemAdder.accept(this);
	}

}