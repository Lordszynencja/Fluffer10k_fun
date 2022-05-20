package bot.data.items;

import static bot.util.CollectionUtils.mapToList;
import static bot.util.Utils.doubleFromNumber;
import static bot.util.Utils.intFromNumber;
import static bot.util.Utils.longFromNumber;
import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bot.commands.rpg.skills.Skill;
import bot.commands.rpg.spells.ActiveSkill;
import bot.data.items.data.GemItems.GemRefinement;
import bot.data.items.data.GemItems.GemSize;
import bot.data.items.data.GemItems.GemType;
import bot.userData.ServerUserData;

public class Item implements Comparable<Item> {
	public enum ItemRarity {
		TRASH(0, "trash"), //
		COMMON(1, "common"), //
		UNCOMMON(2, "uncommon"), //
		RARE(3, "rare"), //
		SUPER_RARE(4, "super rare"), //
		ULTRA_RARE(5, "ultra rare");

		public final int value;
		public final String name;

		private ItemRarity(final int value, final String name) {
			this.value = value;
			this.name = name;
		}

		public static final List<ItemRarity> rarities = asList(TRASH, COMMON, UNCOMMON, RARE, SUPER_RARE, ULTRA_RARE);
	}

	public String id = null;
	public String name = null;
	public String namePlural = null;
	public String typeName = null;
	public String description = null;
	public String image = null;

	public long price = 0;
	public long mgPrice = 0;
	public ItemRarity rarity = ItemRarity.TRASH;
	public double affectionBonus = 0;
	public ActiveSkill spellTaught = null;
	public GemType gemType = null;
	public GemSize gemSize = null;
	public GemRefinement gemRefinement = null;
	public int pickaxeTier;
	public List<ItemClass> classes = new ArrayList<>();

	public int requiredAgility = 0;
	public int requiredIntelligence = 0;
	public int requiredStrength = 0;

	public int agilityBonus = 0;
	public int armorBonus = 0;
	public int criticalStrikeBonus = 0;
	public int damageRollBonus = 0;
	public int healthBonus = 0;
	public int healthRegenerationBonus = 0;
	public int intelligenceBonus = 0;
	public int magicPower = 0;
	public int magicOnHitDamageBonus = 0;
	public int manaBonus = 0;
	public int manaRegenBonus = 0;
	public int strengthBonus = 0;

	@SuppressWarnings("unchecked")
	public Item(final Map<String, Object> data) {
		if (data == null) {
			return;
		}

		id = (String) data.get("id");
		name = (String) data.get("name");
		namePlural = (String) data.get("namePlural");
		typeName = (String) data.get("typeName");
		description = (String) data.get("description");
		image = (String) data.get("image");

		price = longFromNumber(data.getOrDefault("price", 0L));
		mgPrice = longFromNumber(data.getOrDefault("mgPrice", 0L));
		rarity = ItemRarity.rarities.get(intFromNumber(data.getOrDefault("rarity", 0)));
		affectionBonus = doubleFromNumber(data.getOrDefault("affectionBonus", 0));
		spellTaught = data.get("spellTaught") == null ? null : ActiveSkill.valueOf((String) data.get("spellTaught"));
		gemType = data.get("gemType") == null ? null : GemType.valueOf(data.get("gemType").toString());
		gemSize = data.get("gemSize") == null ? null : GemSize.valueOf(data.get("gemSize").toString());
		gemRefinement = data.get("gemRefinement") == null ? null
				: GemRefinement.valueOf(data.get("gemRefinement").toString());
		pickaxeTier = intFromNumber(data.getOrDefault("pickaxeTier", 0));
		classes = mapToList((List<Object>) data.getOrDefault("classes", new ArrayList<>()),
				o -> ItemClass.valueOf(o.toString()));

		requiredAgility = intFromNumber(data.getOrDefault("requiredAgility", 0));
		requiredIntelligence = intFromNumber(data.getOrDefault("requiredIntelligence", 0));
		requiredStrength = intFromNumber(data.getOrDefault("requiredStrength", 0));

		agilityBonus = intFromNumber(data.getOrDefault("agilityBonus", 0));
		armorBonus = intFromNumber(data.getOrDefault("armorBonus", 0));
		criticalStrikeBonus = intFromNumber(data.getOrDefault("criticalStrikeBonus", 0));
		damageRollBonus = intFromNumber(data.getOrDefault("damageRollBonus", 0));
		healthBonus = intFromNumber(data.getOrDefault("healthBonus", 0));
		healthRegenerationBonus = intFromNumber(data.getOrDefault("healthRegenerationBonus", 0));
		magicPower = intFromNumber(data.getOrDefault("magicDamageRollBonus", 0))
				+ intFromNumber(data.getOrDefault("magicPower", 0));
		magicOnHitDamageBonus = intFromNumber(data.getOrDefault("magicOnHitDamageBonus", 0));
		intelligenceBonus = intFromNumber(data.getOrDefault("intelligenceBonus", 0));
		manaBonus = intFromNumber(data.getOrDefault("manaBonus", 0));
		manaRegenBonus = intFromNumber(data.getOrDefault("manaRegenBonus", 0));
		strengthBonus = intFromNumber(data.getOrDefault("strengthBonus", 0));

		if (name == null) {
			throw new RuntimeException("item " + id + " has no name");
		}
		if (namePlural == null) {
			throw new RuntimeException("item " + id + " has no namePlural");
		}
		if (description == null) {
			throw new RuntimeException("item " + id + " has no description");
		}
		if (classes.contains(ItemClass.GIFT) && affectionBonus == 0) {
			throw new RuntimeException("item " + id + " has wrong affectionBonus");
		}
	}

	public Item(final Item item) {
		id = item.id;
		name = item.name;
		namePlural = item.namePlural;
		typeName = item.typeName;
		description = item.description;
		image = item.image;

		price = item.price;
		rarity = item.rarity;
		affectionBonus = item.affectionBonus;
		spellTaught = item.spellTaught;
		gemType = item.gemType;
		gemSize = item.gemSize;
		gemRefinement = item.gemRefinement;
		pickaxeTier = item.pickaxeTier;
		classes = new ArrayList<>(item.classes);

		requiredAgility = item.requiredAgility;
		requiredIntelligence = item.requiredIntelligence;
		requiredStrength = item.requiredStrength;

		agilityBonus = item.agilityBonus;
		armorBonus = item.armorBonus;
		criticalStrikeBonus = item.criticalStrikeBonus;
		damageRollBonus = item.damageRollBonus;
		healthBonus = item.healthBonus;
		healthRegenerationBonus = item.healthRegenerationBonus;
		magicPower = item.magicPower;
		magicOnHitDamageBonus = item.magicOnHitDamageBonus;
		intelligenceBonus = item.intelligenceBonus;
		manaBonus = item.manaBonus;
		manaRegenBonus = item.manaRegenBonus;
		strengthBonus = item.strengthBonus;
	}

	public ItemBuilder builder() {
		final ItemBuilder builder = new ItemBuilder(id, name, namePlural, description)//
				.image(image)//
				.price(price)//
				.rarity(rarity.value)//
				.affectionBonus(affectionBonus)//
				.spellTaught(spellTaught)//
				.gemSize(gemSize)//
				.gemRefinement(gemRefinement)//
				.pickaxeTier(pickaxeTier)//
				.classes(classes)//

				.requiredAgility(requiredAgility)//
				.requiredIntelligence(requiredIntelligence)//
				.requiredStrength(requiredStrength)//

				.agilityBonus(agilityBonus)//
				.armorBonus(armorBonus)//
				.criticalStrikeBonus(criticalStrikeBonus)//
				.damageRollBonus(damageRollBonus)//
				.healthBonus(healthBonus)//
				.healthRegenerationBonus(healthRegenerationBonus)//
				.intelligenceBonus(intelligenceBonus)//
				.magicPower(magicPower)//
				.magicOnHitDamageBonus(magicOnHitDamageBonus)//
				.manaBonus(manaBonus)//
				.manaRegenBonus(manaRegenBonus)//
				.strengthBonus(strengthBonus);

		return builder;
	}

	public Map<String, Object> toMap() {
		final Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("name", name);
		map.put("namePlural", namePlural);
		map.put("typeName", typeName);
		map.put("description", description);
		map.put("image", image);

		map.put("price", price);
		map.put("rarity", rarity.value);
		map.put("affectionBonus", affectionBonus);
		map.put("spellTaught", spellTaught);
		map.put("gemSize", gemSize);
		map.put("gemRefinement", gemRefinement);
		map.put("pickaxeTier", pickaxeTier);
		map.put("classes", classes);

		map.put("requiredAgility", requiredAgility);
		map.put("requiredIntelligence", requiredIntelligence);
		map.put("requiredStrength", requiredStrength);

		map.put("agilityBonus", agilityBonus);
		map.put("armorBonus", armorBonus);
		map.put("criticalStrikeBonus", criticalStrikeBonus);
		map.put("damageRollBonus", damageRollBonus);
		map.put("healthBonus", healthBonus);
		map.put("healthRegenerationBonus", healthRegenerationBonus);
		map.put("magicPower", magicPower);
		map.put("magicOnHitDamageBonus", magicOnHitDamageBonus);
		map.put("intelligenceBonus", intelligenceBonus);
		map.put("manaBonus", manaBonus);
		map.put("manaRegenBonus", manaRegenBonus);
		map.put("strengthBonus", strengthBonus);

		return map;
	}

	@Override
	public boolean equals(final Object o) {
		return o instanceof Item && id.equals(((Item) o).id);
	}

	public boolean isEquippable() {
		return classes.contains(ItemClass.LIGHT_ARMOR)//
				|| classes.contains(ItemClass.MEDIUM_ARMOR) //
				|| classes.contains(ItemClass.HEAVY_ARMOR) //
				|| classes.contains(ItemClass.SHIELD) //
				|| classes.contains(ItemClass.WEAPON) //
				|| classes.contains(ItemClass.PICKAXE) //
				|| classes.contains(ItemClass.RING);
	}

	public boolean isSellable() {
		return !classes.contains(ItemClass.QUEST);
	}

	@Override
	public int compareTo(final Item o) {
		return name.compareTo(o.name);
	}

	public boolean canBeEquippedBy(final ServerUserData userData) {
		if (!isEquippable()) {
			return false;
		}

		if (userData.rpg.strength < requiredStrength//
				|| userData.rpg.agility < requiredAgility//
				|| userData.rpg.intelligence < requiredIntelligence) {
			return false;
		}

		if (classes.contains(ItemClass.MEDIUM_ARMOR) && !userData.rpg.skills.contains(Skill.THICK_SKIN_2)) {
			return false;
		}
		if (classes.contains(ItemClass.HEAVY_ARMOR) && !userData.rpg.skills.contains(Skill.THICK_SKIN_4)) {
			return false;
		}

		return true;
	}
}
