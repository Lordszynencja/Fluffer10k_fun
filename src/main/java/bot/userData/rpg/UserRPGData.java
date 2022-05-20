package bot.userData.rpg;

import static bot.data.items.loot.LootTable.weightedI;
import static bot.util.CollectionUtils.addToIntOnMap;
import static bot.util.CollectionUtils.mapMapString;
import static bot.util.CollectionUtils.mapToSet;
import static bot.util.Utils.bigIntegerFromNumber;
import static bot.util.Utils.intFromNumber;
import static bot.util.Utils.longFromNumber;
import static bot.util.Utils.Pair.pair;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.summingInt;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

import bot.commands.rpg.skills.Skill;
import bot.commands.rpg.spells.ActiveSkill;
import bot.data.ExplorationType;
import bot.data.items.ItemSlot;
import bot.data.quests.QuestType;
import bot.userData.rpg.questData.QuestStep;
import bot.userData.rpg.questData.UserQuestData;
import bot.userData.rpg.questData.UserQuestDataFactory;
import bot.util.CollectionUtils.ValueFrom;
import bot.util.Utils.Pair;

public class UserRPGData {

	public static abstract class StatAccessor {
		UserRPGData data;

		public StatAccessor(final UserRPGData data) {
			this.data = data;
		}

		public abstract int getStat();

		public abstract void setStat(int value);
	}

	private static class StrengthStatAccessor extends StatAccessor {
		public StrengthStatAccessor(final UserRPGData data) {
			super(data);
		}

		@Override
		public int getStat() {
			return data.strength;
		}

		@Override
		public void setStat(final int value) {
			data.strength = value;
		}
	}

	private static class AgilityStatAccessor extends StatAccessor {
		public AgilityStatAccessor(final UserRPGData data) {
			super(data);
		}

		@Override
		public int getStat() {
			return data.agility;
		}

		@Override
		public void setStat(final int value) {
			data.agility = value;
		}
	}

	private static class IntelligenceStatAccessor extends StatAccessor {
		public IntelligenceStatAccessor(final UserRPGData data) {
			super(data);
		}

		@Override
		public int getStat() {
			return data.intelligence;
		}

		@Override
		public void setStat(final int value) {
			data.intelligence = value;
		}
	}

	public String name = null;
	public String avatar = null;

	public BigInteger exp = BigInteger.ZERO;
	public int level = 0;
	public int improvementPoints = 0;
	public int strength = 1;
	public int agility = 1;
	public int intelligence = 1;

	public long miningExp = 0;

	public Map<ItemSlot, String> eq = new HashMap<>();
	public Set<Skill> skills = new HashSet<>();
	public UserSpellHotbarData spellHotbar = new UserSpellHotbarData();
	public Map<QuestType, UserQuestData> quests = new HashMap<>();

	public Long fightId = null;
	public Map<ExplorationType, Integer> explorationWeights = new HashMap<>();

	public boolean mineUnlocked = false;
	public boolean undyingAvailable = false;

	{
		skills.addAll(Skill.trees);

		for (final ItemSlot itemSlot : ItemSlot.values()) {
			eq.put(itemSlot, null);
		}
		for (final ExplorationType explorationType : ExplorationType.activeExplorations) {
			explorationWeights.put(explorationType, explorationType.basicWeight);
		}
	}

	public UserRPGData() {
	}

	@SuppressWarnings("unchecked")
	public UserRPGData(final Map<String, Object> data) {
		name = (String) data.get("name");
		avatar = (String) data.get("avatar");
		exp = bigIntegerFromNumber(data.get("exp"));
		level = intFromNumber(data.get("level"));
		improvementPoints = intFromNumber(data.getOrDefault("skillPoints", 0))
				+ intFromNumber(data.getOrDefault("improvementPoints", 0));
		strength = intFromNumber(data.get("strength"));
		agility = intFromNumber(data.get("agility"));
		intelligence = intFromNumber(data.get("intelligence"));

		miningExp = longFromNumber(data.getOrDefault("miningExp", 0L));

		((Map<String, String>) data.get("eq")).forEach((slotId, item) -> {
			eq.put(ItemSlot.valueOf(slotId), item);
		});
		skills = mapToSet((List<String>) data.getOrDefault("skills", new ArrayList<>()), v -> Skill.valueOf(v));
		skills.addAll(Skill.trees);
		spellHotbar = new UserSpellHotbarData((Map<String, Object>) data.get("spellHotbar"));
		for (int i = 0; i < spellHotbar.spells.size(); i++) {
			if (!spells().contains(spellHotbar.spells.get(i))) {
				spellHotbar.setSpell(i, null);
			}
		}

		((Map<String, Map<String, Object>>) data.get("quests")).forEach((questId, questData) -> {
			try {
				final QuestType type = QuestType.valueOf(questId);
				quests.put(type, UserQuestDataFactory.makeFrom(questData, type));
			} catch (final Exception e) {
				System.err.println(questId + "\n" + questData);
				e.printStackTrace();
				throw e;
			}
		});

		fightId = longFromNumber(data.get("fightId"));

		((Map<String, Integer>) data.get("explorationWeights")).forEach((explorationTypeId, weight) -> {
			explorationWeights.put(ExplorationType.valueOf(explorationTypeId), weight);
		});

		mineUnlocked = (boolean) data.get("mineUnlocked");

		undyingAvailable = (boolean) data.getOrDefault("undyingAvailable", false);
	}

	public Map<String, Object> toMap() {
		final Map<String, Object> map = new HashMap<>();

		map.put("name", name);
		map.put("avatar", avatar);

		map.put("exp", exp);
		map.put("level", level);
		map.put("improvementPoints", improvementPoints);
		map.put("strength", strength);
		map.put("agility", agility);
		map.put("intelligence", intelligence);

		map.put("miningExp", miningExp);

		map.put("eq", eq);
		map.put("skills", skills);
		map.put("spellHotbar", spellHotbar.toMap());
		map.put("quests", mapMapString(quests, o -> o.toMap()));

		map.put("fightId", fightId);
		map.put("explorationWeights", explorationWeights);

		map.put("mineUnlocked", mineUnlocked);

		map.put("undyingAvailable", undyingAvailable);

		return map;
	}

	public String getSaveDescription(final User user, final Server server) {
		return "name: " + getName(user, server) + ", level: " + level + " (" + strength + "/" + agility + "/"
				+ intelligence + ")";
	}

	public String getName(final User user, final Server server) {
		return name == null ? user.getDisplayName(server) : name;
	}

	public StatAccessor getStatAccessor(final String name) {
		if ("strength".equals(name)) {
			return new StrengthStatAccessor(this);
		}
		if ("agility".equals(name)) {
			return new AgilityStatAccessor(this);
		}
		if ("intelligence".equals(name)) {
			return new IntelligenceStatAccessor(this);
		}

		return null;
	}

	public boolean questIsOnStep(final QuestType questType, final QuestStep step) {
		final UserQuestData quest = quests.get(questType);
		return quest != null && quest.step == step;
	}

	public ExplorationType getRandomExploration() {
		final List<Pair<Integer, ExplorationType>> weights = new ArrayList<>();
		for (final ExplorationType type : ExplorationType.activeExplorations) {
			addToIntOnMap(explorationWeights, type, type.basicWeight);
			weights.add(pair(explorationWeights.get(type), type));
		}

		final ExplorationType chosenType = weightedI(weights).getItem();
		addToIntOnMap(explorationWeights, chosenType, -ExplorationType.totalWeights - 1);
		if (explorationWeights.get(chosenType) < 0) {
			explorationWeights.put(chosenType, 0);
		}

		return chosenType;
	}

	public void setQuest(final UserQuestData questData) {
		quests.put(questData.type, questData);
	}

	private static final List<Pair<Skill, ActiveSkill>> activeSkillRequirements = asList(//
			pair(Skill.ANTIMAGIC_SHIELD, ActiveSkill.MAGIC_SHIELD), //
			pair(Skill.BASH, ActiveSkill.BASH), //
			pair(Skill.BLIZZARD, ActiveSkill.BLIZZARD), //
			pair(Skill.DOUBLE_STRIKE, ActiveSkill.DOUBLE_STRIKE), //
			pair(Skill.FIREBALL, ActiveSkill.FIREBALL), //
			pair(Skill.FIERY_WEAPON, ActiveSkill.FIERY_WEAPON), //
			pair(Skill.FORCE_HIT, ActiveSkill.FORCE_HIT), //
			pair(Skill.FREEZE, ActiveSkill.FREEZE), //
			pair(Skill.GOUGE, ActiveSkill.GOUGE), //
			pair(Skill.HEAL, ActiveSkill.HEAL), //
			pair(Skill.HOLY_AURA, ActiveSkill.HOLY_AURA), //
			pair(Skill.ICE_BOLT, ActiveSkill.ICE_BOLT), //
			pair(Skill.LIGHTNING, ActiveSkill.LIGHTNING), //
			pair(Skill.METEORITE, ActiveSkill.METEORITE), //
			pair(Skill.PRECISE_STRIKE, ActiveSkill.PRECISE_STRIKE), //
			pair(Skill.RAGE, ActiveSkill.RAGE), //
			pair(Skill.SHADOW_CLONE, ActiveSkill.SHADOW_CLONE), //
			pair(Skill.SLEEP, ActiveSkill.SLEEP), //
			pair(Skill.STONE_SKIN, ActiveSkill.STONE_SKIN), //
			pair(Skill.WHIRLPOOL, ActiveSkill.WHIRLPOOL));

	public List<ActiveSkill> spells() {
		final List<ActiveSkill> activeSkills = new ArrayList<>();
		for (final Pair<Skill, ActiveSkill> activeSkillRequirement : activeSkillRequirements) {
			if (skills.contains(activeSkillRequirement.a)) {
				activeSkills.add(activeSkillRequirement.b);
			}
		}

		activeSkills.sort(new ValueFrom<>(activeSkill -> activeSkill.name));

		return activeSkills;
	}

	public static final int lvl2MiningExp = 1000;
	public static final double miningLvlExpMultiplier = 1.9;

	public int getMiningProficiency() {
		int proficiency = 1;
		long expNeededForNextLevel = lvl2MiningExp;
		long expLeft = miningExp;
		while (expLeft >= expNeededForNextLevel) {
			proficiency++;
			expLeft -= expNeededForNextLevel;
			expNeededForNextLevel = (long) (expNeededForNextLevel * miningLvlExpMultiplier);
		}

		return proficiency;
	}

	public int skillPointsLeft() {
		return level - skills.stream().collect(summingInt(s -> s.price));
	}
}
