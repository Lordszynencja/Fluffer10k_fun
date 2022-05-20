package bot.userData;

import static bot.commands.rpg.RPGExpUtils.getLevelFromExp;
import static bot.data.items.ItemUtils.formatNumber;
import static bot.util.CollectionUtils.addToLongOnMap;
import static bot.util.CollectionUtils.enumSetToStrings;
import static bot.util.CollectionUtils.mapMap;
import static bot.util.CollectionUtils.mapMapString;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.RandomUtils.getRandom;
import static bot.util.Utils.intFromNumber;
import static bot.util.Utils.longFromNumber;
import static bot.util.Utils.Pair.pair;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

import bot.Fluffer10kFun;
import bot.commands.rpg.danuki.DanukiShopUnlock;
import bot.commands.rpg.skills.Skill;
import bot.commands.rpg.spells.ActiveSkill;
import bot.commands.upgrades.Upgrade;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.items.FurnitureType;
import bot.data.items.Item;
import bot.data.items.ItemAmount;
import bot.data.items.ItemClass;
import bot.data.items.ItemSlot;
import bot.data.items.ItemUtils;
import bot.data.items.SimpleItemAmount;
import bot.data.items.data.MagicScrollItems;
import bot.userData.UserStatusesData.UserStatusType;
import bot.userData.rpg.UserRPGData;
import bot.util.CollectionUtils;
import bot.util.Utils;

public class ServerUserData {
	public static final int maxStamina = 120;

	public final long serverId;
	public final long userId;

	public UserRPGData rpg = new UserRPGData();
	public UserStatusesData statuses = new UserStatusesData();
	public UserCookiesData cookies = new UserCookiesData();
	public UserRaceSponsorData raceSponsor = new UserRaceSponsorData();
	public final UserBlacksmithData blacksmith;
	public final UserBlessingData blessings;
	public Map<String, UserRPGData> saves = new HashMap<>();

	public Set<Upgrade> upgrades = new HashSet<>();
	public Set<DanukiShopUnlock> danukiShopUnlocks = new HashSet<>();

	public House house = House.SLEEPING_BAG;
	public Map<FurnitureType, Integer> houseFurniture = new HashMap<>();
	public Map<String, HaremMemberData> harem = new HashMap<>();
	public Map<String, Long> items = new HashMap<>();

	public long monies = 0;
	public long playCoins = 0;
	public long monsterGold = 0;
	public long cums = 0;
	public long dailyBonus = 0;

	public boolean useMgLoveProtection = true;
	public boolean dailyUsed = false;
	public boolean danukiShopAvailable = false;
	public boolean jewellerAvailable = false;

	public long raceTimeout = 0;
	private int stamina = maxStamina;
	public long nextStaminaRegen = System.currentTimeMillis();

	public ServerUserData(final long serverId, final long userId) {
		this.serverId = serverId;
		this.userId = userId;
		blacksmith = new UserBlacksmithData();
		blessings = new UserBlessingData();
	}

	@SuppressWarnings("unchecked")
	public ServerUserData(final long serverId, final long userId, final Map<String, Object> data) {
		this.serverId = serverId;
		this.userId = userId;

		rpg = new UserRPGData((Map<String, Object>) data.get("rpg"));
		statuses = new UserStatusesData((Map<String, Map<String, Object>>) data.get("statuses"));
		cookies = new UserCookiesData((Map<String, Object>) data.get("cookies"));
		raceSponsor = new UserRaceSponsorData((Map<String, Object>) data.get("raceSponsor"));
		blacksmith = new UserBlacksmithData((Map<String, Object>) data.get("blacksmith"));
		blessings = new UserBlessingData((Map<String, Object>) data.get("blessings"));
		saves = mapMapString((Map<String, Map<String, Object>>) data.get("saves"), UserRPGData::new);

		final Map<ActiveSkill, String> scrolls = CollectionUtils.toMap(//
				pair(ActiveSkill.GOUGE, MagicScrollItems.BOOK_OF_OFFENSE_GOUGE), //
				pair(ActiveSkill.PRECISE_STRIKE, MagicScrollItems.BOOK_OF_OFFENSE_PRECISE_STRIKE), //
				pair(ActiveSkill.FORCE_HIT, MagicScrollItems.MAGIC_SCROLL_FORCE_HIT), //
				pair(ActiveSkill.HEAL, MagicScrollItems.MAGIC_SCROLL_HEAL), //

				pair(ActiveSkill.BASH, MagicScrollItems.BOOK_OF_OFFENSE_BASH), //
				pair(ActiveSkill.DOUBLE_STRIKE, MagicScrollItems.BOOK_OF_OFFENSE_DOUBLE_STRIKE), //
				pair(ActiveSkill.FIERY_WEAPON, MagicScrollItems.MAGIC_SCROLL_FIERY_WEAPON), //
				pair(ActiveSkill.FIREBALL, MagicScrollItems.MAGIC_SCROLL_FIREBALL), //
				pair(ActiveSkill.HOLY_AURA, MagicScrollItems.MAGIC_SCROLL_HOLY_AURA), //
				pair(ActiveSkill.ICE_BOLT, MagicScrollItems.MAGIC_SCROLL_ICE_BOLT), //
				pair(ActiveSkill.LIGHTNING, MagicScrollItems.MAGIC_SCROLL_LIGHTNING), //
				pair(ActiveSkill.MAGIC_SHIELD, MagicScrollItems.MAGIC_SCROLL_MAGIC_SHIELD), //
				pair(ActiveSkill.RAGE, MagicScrollItems.MAGIC_SCROLL_RAGE), //
				pair(ActiveSkill.SLEEP, MagicScrollItems.MAGIC_SCROLL_SLEEP), //
				pair(ActiveSkill.SPEED_OF_WIND, MagicScrollItems.MAGIC_SCROLL_SPEED_OF_WIND), //

				pair(ActiveSkill.BLIZZARD, MagicScrollItems.MAGIC_SCROLL_BLIZZARD), //
				pair(ActiveSkill.FREEZE, MagicScrollItems.MAGIC_SCROLL_FREEZE), //
				pair(ActiveSkill.METEORITE, MagicScrollItems.MAGIC_SCROLL_METEORITE), //
				pair(ActiveSkill.WHIRLPOOL, MagicScrollItems.MAGIC_SCROLL_WHIRLPOOL));

		upgrades = CollectionUtils.<String, Upgrade>mapToSetSafe(data.get("upgrades"), Upgrade::valueOf);
		danukiShopUnlocks = CollectionUtils.<String, DanukiShopUnlock>mapToSetSafe(data.get("danukiShopUnlocks"),
				DanukiShopUnlock::valueOf);

		house = House.valueOf((String) data.getOrDefault("house", House.SLEEPING_BAG.name()));
		houseFurniture = mapMap((Map<String, Number>) data.get("houseFurniture"), FurnitureType::valueOf,
				Utils::intFromNumber);

		harem = new HashMap<>();
		((Map<String, Map<String, Object>>) data.get("harem")).forEach((name, haremMemberData) -> {
			harem.put(name, new HaremMemberData(haremMemberData, name));
		});
		items = mapMapString((Map<String, Object>) data.get("items"), Utils::longFromNumber);

		monies = longFromNumber(data.get("monies"));
		playCoins = longFromNumber(data.get("playCoins"));
		monsterGold = longFromNumber(data.get("monsterGold"));
		cums = longFromNumber(data.get("cums"));
		dailyBonus = longFromNumber(data.get("dailyBonus"));

		useMgLoveProtection = (boolean) data.get("useMgLoveProtection");
		dailyUsed = (boolean) data.get("dailyUsed");
		danukiShopAvailable = (boolean) data.get("danukiShopAvailable");
		jewellerAvailable = (boolean) data.get("jewellerAvailable");

		raceTimeout = longFromNumber(data.get("raceTimeout"));
		stamina = intFromNumber(data.getOrDefault("stamina", maxStamina));
		nextStaminaRegen = longFromNumber(data.getOrDefault("nextStaminaRegen", System.currentTimeMillis()));
	}

	public Map<String, Object> toMap() {
		final Map<String, Object> map = new HashMap<>();
		map.put("rpg", rpg.toMap());
		map.put("statuses", statuses.toMap());
		map.put("cookies", cookies.toMap());
		map.put("raceSponsor", raceSponsor.toMap());
		map.put("blacksmith", blacksmith.toMap());
		map.put("blessings", blessings.toMap());
		map.put("saves", mapMapString(saves, save -> save.toMap()));

		map.put("upgrades", enumSetToStrings(upgrades));
		map.put("danukiShopUnlocks", enumSetToStrings(danukiShopUnlocks));

		map.put("house", house);
		map.put("houseFurniture", houseFurniture);
		map.put("harem", mapMapString(harem, o -> o.toData()));
		map.put("items", items);

		map.put("monies", monies);
		map.put("playCoins", playCoins);
		map.put("monsterGold", monsterGold);
		map.put("cums", cums);
		map.put("dailyBonus", dailyBonus);

		map.put("useMgLoveProtection", useMgLoveProtection);
		map.put("dailyUsed", dailyUsed);
		map.put("danukiShopAvailable", danukiShopAvailable);
		map.put("jewellerAvailable", jewellerAvailable);

		map.put("raceTimeout", raceTimeout);
		map.put("stamina", stamina);
		map.put("nextStaminaRegen", nextStaminaRegen);

		return map;
	}

	public String getFormattedMonies() {
		return ItemUtils.getFormattedMonies(monies);
	}

	public String getFormattedPlayCoins() {
		return ItemUtils.getFormattedPlayCoins(playCoins);
	}

	public String getFormattedMonsterGold() {
		return ItemUtils.getFormattedMonsterGold(monsterGold);
	}

	public void unequip(final ItemSlot slot) {
		final String itemId = rpg.eq.remove(slot);
		if (itemId != null) {
			addToLongOnMap(items, itemId, 1);
		}
	}

	public void equip(final ItemSlot slot, final String itemId) {
		slot.connectedSlots.forEach(this::unequip);
		rpg.eq.put(slot, itemId);
		addToLongOnMap(items, itemId, -1);
	}

	public ItemSlot equip(final Fluffer10kFun fluffer10kFun, final Item item) {
		if (item.classes.contains(ItemClass.RIGHT_HAND)) {
			if (rpg.skills.contains(Skill.DUAL_WIELD)) {
				if (rpg.eq.get(ItemSlot.RIGHT_HAND) == null//
						|| !fluffer10kFun.items.getItem(rpg.eq.get(ItemSlot.RIGHT_HAND)).classes
								.contains(ItemClass.DUAL_WIELD)) {
					equip(ItemSlot.RIGHT_HAND, item.id);
					return ItemSlot.RIGHT_HAND;
				}

				equip(ItemSlot.LEFT_HAND, item.id);
				return ItemSlot.LEFT_HAND;
			}

			if (rpg.eq.get(ItemSlot.LEFT_HAND) != null//
					&& fluffer10kFun.items.getItem(rpg.eq.get(ItemSlot.LEFT_HAND)).classes.contains(ItemClass.WEAPON)) {
				unequip(ItemSlot.LEFT_HAND);
			}
			equip(ItemSlot.RIGHT_HAND, item.id);
			return ItemSlot.RIGHT_HAND;
		}
		if (item.classes.contains(ItemClass.LEFT_HAND)) {
			equip(ItemSlot.LEFT_HAND, item.id);
			return ItemSlot.LEFT_HAND;
		}
		if (item.classes.contains(ItemClass.BOTH_HANDS)) {
			equip(ItemSlot.BOTH_HANDS, item.id);
			return ItemSlot.BOTH_HANDS;
		}
		if (item.classes.contains(ItemClass.PICKAXE)) {
			equip(ItemSlot.PICKAXE, item.id);
			return ItemSlot.PICKAXE;
		}
		if (item.classes.contains(ItemClass.LIGHT_ARMOR)//
				|| item.classes.contains(ItemClass.MEDIUM_ARMOR)//
				|| item.classes.contains(ItemClass.HEAVY_ARMOR)) {
			equip(ItemSlot.ARMOR, item.id);
			return ItemSlot.ARMOR;
		}
		if (item.classes.contains(ItemClass.RING)) {
			if (rpg.eq.get(ItemSlot.RING_1) == null) {
				equip(ItemSlot.RING_1, item.id);
				return ItemSlot.RING_1;
			}
			equip(ItemSlot.RING_2, item.id);
			return ItemSlot.RING_2;
		}

		return null;
	}

	public boolean isProtectedFromMgLove() {
		if (!useMgLoveProtection) {
			return false;
		}

		if (statuses.isStatus(UserStatusType.MG_LOVE_PROTECTION)) {
			return true;
		}

		return false;
	}

	public HaremMemberData addWife(final MonsterGirlRace race) {
		String name = getRandom(HaremMemberData.defaultNames);
		while (harem.containsKey(name)) {
			name = getRandom(HaremMemberData.defaultNames);
		}

		final HaremMemberData haremMember = new HaremMemberData(name, race);
		harem.put(name, haremMember);

		return haremMember;
	}

	public boolean hasItem(final String itemId) {
		return hasItem(itemId, 1);
	}

	public boolean hasItem(final Item item) {
		return hasItem(item.id);
	}

	public boolean hasOneOfItems(final String... itemIds) {
		for (final String itemId : itemIds) {
			if (hasItem(itemId)) {
				return true;
			}
		}

		return false;
	}

	public boolean hasItem(final String itemId, final long amount) {
		return items.getOrDefault(itemId, 0L) >= amount;
	}

	public boolean hasItem(final ItemAmount itemAmount) {
		return hasItem(itemAmount.item.id, itemAmount.amount);
	}

	public void addItem(final String itemId) {
		addItem(itemId, 1);
	}

	public void addItem(final Item item) {
		addItem(item.id);
	}

	public void addItem(final String itemId, final long amount) {
		addToLongOnMap(items, itemId, amount);
	}

	public void addItem(final Item item, final long amount) {
		addItem(item.id, amount);
	}

	public void addItem(final SimpleItemAmount itemAmount) {
		addItem(itemAmount.id, itemAmount.amount);
	}

	public void addItem(final ItemAmount itemAmount) {
		addItem(itemAmount.item.id, itemAmount.amount);
	}

	public int getAdditionalRooms() {
		return houseFurniture.getOrDefault(FurnitureType.ADDDITIONAL_ROOM, 0);
	}

	public int getHouseSize() {
		return house.size + getAdditionalRooms();
	}

	public int getHaremSize() {
		return harem.size();
	}

	public List<HaremMemberData> getHaremMembersSorted() {
		final List<HaremMemberData> haremMembers = new ArrayList<>(harem.values());
		haremMembers.sort((a, b) -> (a.name.compareTo(b.name)));
		return haremMembers;
	}

	public List<FurnitureType> getSortedFurniture() {
		final List<FurnitureType> furniture = new ArrayList<>();
		houseFurniture.forEach((type, amount) -> {
			if (amount > 0) {
				furniture.add(type);
			}
		});
		furniture.sort((a, b) -> (a.singularName.compareTo(b.singularName)));
		return furniture;
	}

	private BigDecimal getExpMultiplier() {
		double multiplier = 1;
		if (statuses.isStatus(UserStatusType.EXP_MULTIPLIER_2)) {
			multiplier *= 2;
		}
		if (statuses.isStatus(UserStatusType.SUPER_EXP_MULTIPLIER)) {
			multiplier *= 7;
		}
		if (statuses.isStatus(UserStatusType.ULTRA_EXP_MULTIPLIER)) {
			multiplier *= 77;
		}

		return new BigDecimal(multiplier);
	}

	public EmbedBuilder addExpAndMakeEmbed(final long exp, final User user, final Server server) {
		return addExpAndMakeEmbed(BigInteger.valueOf(exp), user, server);
	}

	private static final double blessingExpMultiplier = 2;

	public EmbedBuilder addExpAndMakeEmbed(final BigInteger exp, final User user, final Server server) {
		if (exp.compareTo(BigInteger.ZERO) <= 0) {
			return null;
		}

		final BigDecimal statusMultiplier = getExpMultiplier();
		final BigDecimal blessingsMultiplier = new BigDecimal(blessingExpMultiplier).pow(blessings.blessings);
		final BigInteger totalExp = new BigDecimal(exp).multiply(blessingsMultiplier).multiply(statusMultiplier)
				.toBigInteger();

		rpg.exp = rpg.exp.add(totalExp);

		final int userLevelBefore = rpg.level;
		final int userLevelAfter = getLevelFromExp(rpg.exp);
		final int newSkillPoints = userLevelAfter - userLevelBefore;

		final String playerName = rpg.getName(user, server);
		if (newSkillPoints == 0) {
			return makeEmbed("Experience received", playerName + " got " + formatNumber(totalExp) + " experience");
		}

		rpg.level = userLevelAfter;
		rpg.improvementPoints += newSkillPoints;
		return makeEmbed("Level up!", playerName + " got " + formatNumber(totalExp) + " experience and is now level "
				+ rpg.level + ", and got "
				+ (newSkillPoints == 1 ? "an improvement point" : newSkillPoints + " improvement points") + "!");
	}

	public int getStamina() {
		final long t = System.currentTimeMillis();
		while (nextStaminaRegen < t && stamina < maxStamina) {
			stamina++;
			nextStaminaRegen += 60 * 1000;
		}
		if (stamina >= maxStamina) {
			stamina = maxStamina;
			nextStaminaRegen = System.currentTimeMillis();
		}
		return stamina;
	}

	public void reduceStamina(final int work) {
		stamina = getStamina() - work;
		if (stamina < 0) {
			stamina = 0;
		}
	}

	public void addStamina(final int rest) {
		stamina = getStamina() + rest;
		if (stamina > maxStamina) {
			stamina = maxStamina;
		}
	}

	public boolean hasHouseSpace() {
		return harem.size() < house.size;
	}

	public long maxSponsorBonus() {
		if (upgrades.contains(Upgrade.HYPER_CAR)) {
			return 10_000;
		} else if (upgrades.contains(Upgrade.SUPER_CAR)) {
			return 5_000;
		} else if (upgrades.contains(Upgrade.SPORT_CAR)) {
			return 2_000;
		}

		return 1_000;
	}

	public long sponsorBonusIncrease() {
		if (upgrades.contains(Upgrade.HYPER_CAR)) {
			return 100;
		} else if (upgrades.contains(Upgrade.SUPER_CAR)) {
			return 50;
		} else if (upgrades.contains(Upgrade.SPORT_CAR)) {
			return 20;
		}

		return 10;
	}

	public boolean canStartOtherFight() {
		return rpg.fightId == null && getStamina() >= 30;
	}

	public void startOtherFight() {
		reduceStamina(30);
	}

	public int maxStatValue() {
		return 20 + blessings.blessings * 2;
	}

	public void bless() {
		for (final ItemSlot slot : ItemSlot.values()) {
			unequip(slot);
		}

		final UserRPGData newData = new UserRPGData();
		newData.name = rpg.name;
		newData.avatar = rpg.avatar;
		rpg = newData;

		blessings.blessings++;
	}
}
