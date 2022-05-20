package bot.commands.rpg.fight.enemies;

import java.util.Set;

import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.fight.FighterClass;

public abstract class RPGEnemyData {
	public enum EnemyType {
		MONSTER_GIRL, //
		OTHER;
	}

	public final String id;
	public final EnemyType type;
	public final String name;

	public final int strength;
	public final int agility;
	public final int intelligence;
	public final int armor;
	public final int baseHp;
	public final int level;
	public final Set<FighterClass> classes;
	public final ActionSelector actionSelector;

	public RPGEnemyData(final String id, final EnemyType type, final String name, final int strength, final int agility,
			final int intelligence, final int armor, final int baseHp, final int level, final Set<FighterClass> classes,
			final ActionSelector actionSelector) {
		this.id = id;
		this.type = type;
		this.name = name;
		this.strength = strength;
		this.agility = agility;
		this.intelligence = intelligence;
		this.armor = armor;
		this.baseHp = baseHp;
		this.level = level;
		this.classes = classes;
		this.actionSelector = actionSelector;
	}

	public abstract String imgUrl();

	public RPGEnemyMonsterGirlData mg() {
		if (type == EnemyType.MONSTER_GIRL) {
			return (RPGEnemyMonsterGirlData) this;
		}
		return null;
	}
}
