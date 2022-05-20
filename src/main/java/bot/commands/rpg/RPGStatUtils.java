package bot.commands.rpg;

import static bot.util.Utils.Pair.pair;
import static java.lang.Math.log;
import static java.lang.Math.max;
import static java.lang.Math.pow;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.javacord.api.entity.server.Server;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.enemies.RPGEnemyData;
import bot.commands.rpg.skills.Skill;
import bot.data.fight.EnemyFighterData;
import bot.data.fight.FighterClass;
import bot.data.fight.FighterData;
import bot.data.fight.FighterData.FighterType;
import bot.data.fight.PlayerFighterData;
import bot.data.items.Item;
import bot.data.items.ItemClass;
import bot.userData.ServerUserData;
import bot.userData.UserBlessingData.Blessing;
import bot.util.CollectionUtils;

public class RPGStatUtils {
	public static class RPGStatsDataBuilder {
		public int strength = 1;
		public int agility = 1;
		public int intelligence = 1;
		public int armor = 0;
		public int magicResistance = 0;

		public int damageRollBonus = 0;
		public double critChanceBonus = 0;
		public int magicPower = 0;

		public int health = 0;
		public int healthRegenerationBonus = 0;

		public int mana = 0;
		public int manaRegenBonus = 0;

		public int level = 0;
		public Set<FighterClass> classes = new HashSet<>();

		public RPGStatsDataBuilder() {
		}

		public RPGStatsDataBuilder strength(final int x) {
			strength = x;
			return this;
		}

		public RPGStatsDataBuilder agility(final int x) {
			agility = x;
			return this;
		}

		public RPGStatsDataBuilder intelligence(final int x) {
			intelligence = x;
			return this;
		}

		public RPGStatsDataBuilder armor(final int x) {
			armor = x;
			return this;
		}

		public RPGStatsDataBuilder magicResistance(final int x) {
			magicResistance = x;
			return this;
		}

		public RPGStatsDataBuilder damageRollBonus(final int x) {
			damageRollBonus = x;
			return this;
		}

		public RPGStatsDataBuilder critChanceBonus(final int x) {
			critChanceBonus = x;
			return this;
		}

		public RPGStatsDataBuilder magicDamageRollBonus(final int x) {
			magicPower = x;
			return this;
		}

		public RPGStatsDataBuilder health(final int x) {
			health = x;
			return this;
		}

		public RPGStatsDataBuilder healthRegenerationBonus(final int x) {
			healthRegenerationBonus = x;
			return this;
		}

		public RPGStatsDataBuilder mana(final int x) {
			mana = x;
			return this;
		}

		public RPGStatsDataBuilder manaRegenBonus(final int x) {
			manaRegenBonus = x;
			return this;
		}

		public RPGStatsDataBuilder level(final int x) {
			level = x;
			return this;
		}

		public RPGStatsDataBuilder classes(final Set<FighterClass> x) {
			classes = x;
			return this;
		}

		public RPGStatsDataBuilder addClass(final FighterClass x) {
			classes.add(x);
			return this;
		}

		public RPGStatsData build() {
			return new RPGStatsData(strength, agility, intelligence, //
					armor, magicResistance, //
					damageRollBonus, critChanceBonus, magicPower, //
					health, healthRegenerationBonus, //
					mana, manaRegenBonus, //
					level, classes);
		}
	}

	public static class RPGStatsData {
		public final int strength;
		public final int agility;
		public final int intelligence;

		public final int armor;
		public final int magicResistance;

		public final int damageRollBonus;
		public final double critChanceBonus;
		public final int magicDamageRollBonus;

		public final int health;
		public final int healthRegenerationBonus;

		public final int mana;
		public final int manaRegenBonus;

		public final int level;
		public final Set<FighterClass> classes;

		public RPGStatsData(final int strength, final int agility, final int intelligence, //
				final int armor, final int magicResistance, //
				final int damageRollBonus, final double critChanceBonus, final int magicDamageRollBonus, //
				final int health, final int healthRegenerationBonus, //
				final int mana, final int manaRegenBonus, //
				final int level, final Set<FighterClass> classes) {
			this.strength = strength;
			this.agility = agility;
			this.intelligence = intelligence;

			this.armor = armor;
			this.magicResistance = magicResistance;

			this.damageRollBonus = damageRollBonus;
			this.critChanceBonus = critChanceBonus;
			this.magicDamageRollBonus = magicDamageRollBonus;

			this.health = health;
			this.healthRegenerationBonus = healthRegenerationBonus;

			this.mana = mana;
			this.manaRegenBonus = manaRegenBonus;

			this.level = level;
			this.classes = classes;
		}

		private static final double magicStrengthExponent = log(7) / log(30);// at 30 it will give 7 power

		public double getMagicPowerMultiplier() {
			return pow(max(0, intelligence + magicDamageRollBonus), magicStrengthExponent);
		}

		public int getArmorPower() {
			return armor;
		}
	}

	private final Fluffer10kFun fluffer10kFun;

	public RPGStatUtils(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;
	}

	private static final Map<ItemClass, FighterClass> itemToFighterClasses = CollectionUtils.toMap(//
			pair(ItemClass.SPELL_VOID, FighterClass.SPELL_VOID), //
			pair(ItemClass.STATUS_NEGATION, FighterClass.STATUS_NEGATION));

	private void addMagicPowerSkillBuffs(final ServerUserData userData, final RPGStatsDataBuilder statsBuilder) {
		if (!userData.rpg.skills.contains(Skill.MAGIC_POWER_1)) {
			return;
		}
		statsBuilder.intelligence += 1;
		statsBuilder.magicPower += 2;

		if (!userData.rpg.skills.contains(Skill.MAGIC_POWER_2)) {
			return;
		}
		statsBuilder.intelligence += 1;
		statsBuilder.magicPower += 3;

		if (!userData.rpg.skills.contains(Skill.MAGIC_POWER_3)) {
			return;
		}
		statsBuilder.intelligence += 1;
		statsBuilder.magicPower += 4;

		if (!userData.rpg.skills.contains(Skill.MAGIC_POWER_4)) {
			return;
		}
		statsBuilder.intelligence += 1;
		statsBuilder.magicPower += 7;

		if (!userData.rpg.skills.contains(Skill.MAGIC_POWER_5)) {
			return;
		}
		statsBuilder.intelligence += 5;
		statsBuilder.magicPower += 15;
	}

	private void addManaMasterSkillBuffs(final ServerUserData userData, final RPGStatsDataBuilder statsBuilder) {
		if (!userData.rpg.skills.contains(Skill.MANA_MASTER_1)) {
			return;
		}
		statsBuilder.mana += 2;

		if (!userData.rpg.skills.contains(Skill.MANA_MASTER_2)) {
			return;
		}
		statsBuilder.mana += 4;

		if (!userData.rpg.skills.contains(Skill.MANA_MASTER_3)) {
			return;
		}
		statsBuilder.mana += 6;

		if (!userData.rpg.skills.contains(Skill.MANA_MASTER_4)) {
			return;
		}
		statsBuilder.mana += 8;
	}

	private void addToughnessSkillBuffs(final ServerUserData userData, final RPGStatsDataBuilder statsBuilder) {
		if (!userData.rpg.skills.contains(Skill.TOUGHNESS_1)) {
			return;
		}
		statsBuilder.health += 3;

		if (!userData.rpg.skills.contains(Skill.TOUGHNESS_2)) {
			return;
		}
		statsBuilder.health += 5;

		if (!userData.rpg.skills.contains(Skill.TOUGHNESS_3)) {
			return;
		}
		statsBuilder.health += 7;

		if (!userData.rpg.skills.contains(Skill.TOUGHNESS_4)) {
			return;
		}
		statsBuilder.health += 9;
	}

	private void addThickSkinSkillBuffs(final ServerUserData userData, final RPGStatsDataBuilder statsBuilder) {
		if (!userData.rpg.skills.contains(Skill.THICK_SKIN_1)) {
			return;
		}
		statsBuilder.armor++;
		statsBuilder.magicResistance++;

		if (!userData.rpg.skills.contains(Skill.THICK_SKIN_2)) {
			return;
		}
		statsBuilder.armor++;
		statsBuilder.magicResistance++;

		if (!userData.rpg.skills.contains(Skill.THICK_SKIN_3)) {
			return;
		}
		statsBuilder.armor++;
		statsBuilder.magicResistance++;

		if (!userData.rpg.skills.contains(Skill.THICK_SKIN_4)) {
			return;
		}
		statsBuilder.armor++;
		statsBuilder.magicResistance++;

		if (!userData.rpg.skills.contains(Skill.THICK_SKIN_5)) {
			return;
		}
		statsBuilder.armor += 2;
		statsBuilder.magicResistance += 2;
	}

	private void addBodybuildingSkillBuffs(final ServerUserData userData, final RPGStatsDataBuilder statsBuilder) {
		if (!userData.rpg.skills.contains(Skill.BODYBUILDING_1)) {
			return;
		}
		statsBuilder.strength++;
		statsBuilder.health += 5;

		if (!userData.rpg.skills.contains(Skill.BODYBUILDING_2)) {
			return;
		}
		statsBuilder.strength++;
		statsBuilder.health += 7;

		if (!userData.rpg.skills.contains(Skill.BODYBUILDING_3)) {
			return;
		}
		statsBuilder.strength++;
		statsBuilder.health += 10;

		if (!userData.rpg.skills.contains(Skill.BODYBUILDING_4)) {
			return;
		}
		statsBuilder.strength++;
		statsBuilder.health += 15;

		if (!userData.rpg.skills.contains(Skill.BODYBUILDING_5)) {
			return;
		}
		statsBuilder.strength += 5;
		statsBuilder.health += 25;
	}

	private void addPreciseHitSkillBuffs(final ServerUserData userData, final RPGStatsDataBuilder statsBuilder) {
		if (!userData.rpg.skills.contains(Skill.PRECISE_HIT_1)) {
			return;
		}
		statsBuilder.agility++;
		statsBuilder.critChanceBonus += 0.01;

		if (!userData.rpg.skills.contains(Skill.PRECISE_HIT_2)) {
			return;
		}
		statsBuilder.agility++;
		statsBuilder.critChanceBonus += 0.02;

		if (!userData.rpg.skills.contains(Skill.PRECISE_HIT_3)) {
			return;
		}
		statsBuilder.agility++;
		statsBuilder.critChanceBonus += 0.03;

		if (!userData.rpg.skills.contains(Skill.PRECISE_HIT_4)) {
			return;
		}
		statsBuilder.agility++;
		statsBuilder.critChanceBonus += 0.04;

		if (!userData.rpg.skills.contains(Skill.PRECISE_HIT_5)) {
			return;
		}
		statsBuilder.agility += 5;
		statsBuilder.critChanceBonus += 0.10;
	}

	private RPGStatsDataBuilder getTotalStatsBuilder(final ServerUserData userData) {
		final RPGStatsDataBuilder statsBuilder = new RPGStatsDataBuilder().strength(userData.rpg.strength)//
				.agility(userData.rpg.agility)//
				.intelligence(userData.rpg.intelligence)//
				.level(userData.rpg.level);

		for (final String itemId : userData.rpg.eq.values()) {
			if (itemId != null) {
				final Item item = fluffer10kFun.items.getItem(itemId);
				statsBuilder.strength += item.strengthBonus;
				statsBuilder.agility += item.agilityBonus;
				statsBuilder.intelligence += item.intelligenceBonus;
				statsBuilder.armor += item.armorBonus;
				statsBuilder.damageRollBonus += item.damageRollBonus;
				statsBuilder.magicPower += item.magicPower;
				statsBuilder.health += item.healthBonus;
				statsBuilder.healthRegenerationBonus += item.healthRegenerationBonus;
				statsBuilder.mana += item.manaBonus;
				statsBuilder.manaRegenBonus += item.manaRegenBonus;

				item.classes.stream().map(itemToFighterClasses::get).filter(c -> c != null)
						.forEach(statsBuilder::addClass);
			}
		}

		addMagicPowerSkillBuffs(userData, statsBuilder);
		addManaMasterSkillBuffs(userData, statsBuilder);
		addToughnessSkillBuffs(userData, statsBuilder);
		addThickSkinSkillBuffs(userData, statsBuilder);
		addBodybuildingSkillBuffs(userData, statsBuilder);
		addPreciseHitSkillBuffs(userData, statsBuilder);

		if (userData.rpg.skills.contains(Skill.FAST_HEALING_1)) {
			statsBuilder.healthRegenerationBonus++;
			if (userData.rpg.skills.contains(Skill.FAST_HEALING_2)) {
				statsBuilder.healthRegenerationBonus++;
			}
		}
		if (userData.rpg.skills.contains(Skill.INNATE_RESISTANCE)) {
			statsBuilder.armor++;
			statsBuilder.magicResistance++;
		}

		if (userData.blessings.blessingsObtained.contains(Blessing.ENTHUSIASTIC_FIGHTER)) {
			statsBuilder.addClass(FighterClass.QUICK);
		}
		if (userData.blessings.blessingsObtained.contains(Blessing.NATURAL_RESISTANCE)) {
			statsBuilder.armor += 3;
		}
		statsBuilder.health += 1 + statsBuilder.level + (int) (statsBuilder.strength * 1.5);
		statsBuilder.mana += 1 + statsBuilder.level / 2 + statsBuilder.intelligence;

		if (userData.rpg.skills.contains(Skill.TOUGHNESS_5)) {
			statsBuilder.health += (int) (statsBuilder.health * 0.2);
		}
		if (userData.rpg.skills.contains(Skill.MANA_MASTER_5)) {
			statsBuilder.mana += (int) (statsBuilder.mana * 0.2);
		}

		return statsBuilder;
	}

	public RPGStatsData getTotalStats(final ServerUserData userData) {
		return getTotalStatsBuilder(userData).build();
	}

	private RPGStatsDataBuilder getTotalStatsBuilder(final RPGEnemyData enemyData) {
		final RPGStatsDataBuilder statsBuilder = new RPGStatsDataBuilder()//
				.strength(enemyData.strength)//
				.agility(enemyData.agility)//
				.intelligence(enemyData.intelligence)//
				.armor(enemyData.armor)//
				.level(enemyData.level)//
				.classes(enemyData.classes);

		return statsBuilder;
	}

	public RPGStatsData getTotalStats(final RPGEnemyData enemyData) {
		return getTotalStatsBuilder(enemyData).build();
	}

	public int getLevel(final FighterData fighter) {
		if (fighter.type == FighterType.PLAYER) {
			return fluffer10kFun.serverUserDataUtils.getUserData(fighter.fight.serverId,
					fighter.player().userId).rpg.level;
		}
		if (fighter.type == FighterType.ENEMY) {
			return fighter.enemy().enemyData(fluffer10kFun).level;
		}
		return 0;
	}

	public RPGStatsData getTotalStatsInFight(final PlayerFighterData fighter, final long serverId) {
		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(serverId, fighter.userId);
		final RPGStatsDataBuilder statsBuilder = getTotalStatsBuilder(userData);
		fighter.statuses.addStats(statsBuilder);
		if (userData.blessings.blessingsObtained.contains(Blessing.LAST_STAND) && fighter.hp <= fighter.maxHp / 5) {
			statsBuilder.strength += 1;
			statsBuilder.agility += 1;
			statsBuilder.intelligence += 1;
		}

		if (userData.rpg.skills.contains(Skill.MANA_ARCHMASTER)) {
			statsBuilder.magicPower += fighter.mana / 10;
		}

		return statsBuilder.build();
	}

	public RPGStatsData getTotalStatsInFight(final EnemyFighterData fighter) {
		final RPGStatsDataBuilder statsBuilder = getTotalStatsBuilder(fighter.enemyData(fluffer10kFun));
		fighter.statuses.addStats(statsBuilder);

		return statsBuilder.build();
	}

	public RPGStatsData getTotalStatsInFight(final FighterData fighter, final long serverId) {
		if (fighter.type == FighterType.PLAYER) {
			return getTotalStatsInFight(fighter.player(), serverId);
		}
		if (fighter.type == FighterType.ENEMY) {
			return getTotalStatsInFight(fighter.enemy());
		}

		return new RPGStatsDataBuilder().build();
	}

	public RPGStatsData getTotalStatsInFight(final FighterData fighter) {
		return getTotalStatsInFight(fighter, fighter.fight.serverId);
	}

	public Set<FighterClass> getClassesBeforeFight(final FighterData fighter, final Server server) {
		final Set<FighterClass> classes = new HashSet<>();

		if (fighter.type == FighterType.PLAYER) {
			final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(server.getId(),
					fighter.player().userId);
			for (final String itemId : userData.rpg.eq.values()) {
				if (itemId != null) {
					final Item item = fluffer10kFun.items.getItem(itemId);

					item.classes.stream().map(itemToFighterClasses::get)//
							.filter(c -> c != null)//
							.forEach(classes::add);
				}
			}
		}
		if (fighter.type == FighterType.ENEMY) {
			classes.addAll(fighter.enemy().enemyData(fluffer10kFun).classes);
		}

		return classes;
	}

	public Set<FighterClass> getClasses(final FighterData fighter) {
		final Set<FighterClass> classes = new HashSet<>();

		if (fighter.type == FighterType.PLAYER) {
			final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(fighter.fight.serverId,
					fighter.player().userId);
			for (final String itemId : userData.rpg.eq.values()) {
				if (itemId != null) {
					final Item item = fluffer10kFun.items.getItem(itemId);

					item.classes.stream().map(itemToFighterClasses::get)//
							.filter(c -> c != null)//
							.forEach(classes::add);
				}
			}
		}
		if (fighter.type == FighterType.ENEMY) {
			classes.addAll(fighter.enemy().enemyData(fluffer10kFun).classes);
		}

		return classes;
	}
}
