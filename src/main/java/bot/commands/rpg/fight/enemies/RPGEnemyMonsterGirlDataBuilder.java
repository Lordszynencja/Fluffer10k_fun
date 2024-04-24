package bot.commands.rpg.fight.enemies;

import static bot.util.CollectionUtils.toSet;

import java.util.HashSet;
import java.util.Set;

import bot.commands.rpg.exploration.ExplorationFight;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.FighterClass;

public class RPGEnemyMonsterGirlDataBuilder {
	public String id;
	public MonsterGirlRace race;
	public String name;

	public boolean standard = false;

	public int strength = 1;
	public int agility = 1;
	public int intelligence = 1;
	public int armor = 0;
	public int baseHp = 0;
	public int diff = 0;
	public Set<FighterClass> classes = new HashSet<>();
	public ActionSelector actionSelector;

	public RPGEnemyMonsterGirlDataBuilder(final String id, final MonsterGirlRace race) {
		this.id = id;
		this.race = race;
		name = race.race;
	}

	public RPGEnemyMonsterGirlDataBuilder name(final String name) {
		this.name = name;
		return this;
	}

	public RPGEnemyMonsterGirlDataBuilder standard() {
		standard = true;
		return this;
	}

	public RPGEnemyMonsterGirlDataBuilder strength(final int strength) {
		this.strength = strength;
		return this;
	}

	public RPGEnemyMonsterGirlDataBuilder agility(final int agility) {
		this.agility = agility;
		return this;
	}

	public RPGEnemyMonsterGirlDataBuilder intelligence(final int intelligence) {
		this.intelligence = intelligence;
		return this;
	}

	public RPGEnemyMonsterGirlDataBuilder armor(final int armor) {
		this.armor = armor;
		return this;
	}

	public RPGEnemyMonsterGirlDataBuilder baseHp(final int baseHp) {
		this.baseHp = baseHp;
		return this;
	}

	public RPGEnemyMonsterGirlDataBuilder diff(final int diff) {
		this.diff = diff;
		return this;
	}

	public RPGEnemyMonsterGirlDataBuilder classes(final FighterClass... classes) {
		this.classes = toSet(classes);
		this.classes.remove(null);
		return this;
	}

	public RPGEnemyMonsterGirlDataBuilder actionSelector(final ActionSelector actionSelector) {
		this.actionSelector = actionSelector;
		return this;
	}

	public RPGEnemyMonsterGirlDataBuilder slime() {
		classes.add(FighterClass.SLIME_REGEN);
		classes.add(FighterClass.WET);
		classes.add(FighterClass.WEAK_TO_FIRE);
		return this;
	}

	public RPGEnemyMonsterGirlData build() {
		return RPGEnemyMonsterGirlData.from(id, race, name, strength, agility, intelligence, armor, baseHp, diff,
				classes, actionSelector);
	}

	public void build(final RPGEnemies rpgEnemies) {
		rpgEnemies.add(build());
		if (standard) {
			ExplorationFight.addEnemyId(id);
		}
	}
}
