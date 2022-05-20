package bot.commands.rpg.fight.enemies.special;

import static bot.util.CollectionUtils.toSet;

import java.util.HashSet;
import java.util.Set;

import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.commands.rpg.fight.enemies.RPGEnemyData;
import bot.data.fight.FighterClass;

public class OtherEnemyData extends RPGEnemyData {
	public static class OtherEnemyDataBuilder {
		final String id;
		final EnemyType type;
		final String name;

		int strength;
		int agility;
		int intelligence;
		int armor;
		int baseHp;
		int level;

		Set<FighterClass> classes = new HashSet<>();
		ActionSelector actionSelector;
		String imgUrl;

		public OtherEnemyDataBuilder(final String id, final String name) {
			this.id = id;
			type = EnemyType.OTHER;
			this.name = name;
		}

		public OtherEnemyDataBuilder strength(final int x) {
			strength = x;
			return this;
		}

		public OtherEnemyDataBuilder agility(final int x) {
			agility = x;
			return this;
		}

		public OtherEnemyDataBuilder intelligence(final int x) {
			intelligence = x;
			return this;
		}

		public OtherEnemyDataBuilder armor(final int x) {
			armor = x;
			return this;
		}

		public OtherEnemyDataBuilder baseHp(final int x) {
			baseHp = x;
			return this;
		}

		public OtherEnemyDataBuilder level(final int x) {
			level = x;
			return this;
		}

		public OtherEnemyDataBuilder classes(final FighterClass... x) {
			classes = toSet(x);
			return this;
		}

		public OtherEnemyDataBuilder actionSelector(final ActionSelector x) {
			actionSelector = x;
			return this;
		}

		public OtherEnemyDataBuilder imgUrl(final String x) {
			imgUrl = x;
			return this;
		}

		public void build(final RPGEnemies rpgEnemies) {
			final OtherEnemyData data = new OtherEnemyData(id, type, name, strength, agility, intelligence, armor,
					baseHp, level, classes, actionSelector, imgUrl);
			rpgEnemies.add(data);
		}
	}

	private final String imgUrl;

	public OtherEnemyData(final String id, final EnemyType type, final String name, final int strength,
			final int agility, final int intelligence, final int armor, final int baseHp, final int level,
			final Set<FighterClass> classes, final ActionSelector actionSelector, final String imgUrl) {
		super(id, type, name, strength, agility, intelligence, armor, baseHp, level, classes, actionSelector);
		this.imgUrl = imgUrl;
	}

	@Override
	public String imgUrl() {
		return imgUrl;
	}

}
