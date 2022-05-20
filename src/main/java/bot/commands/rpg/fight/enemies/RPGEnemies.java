package bot.commands.rpg.fight.enemies;

import java.util.HashMap;
import java.util.Map;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.enemies.data.races.EnemiesOfRace;
import bot.commands.rpg.fight.enemies.special.MonsterArmyOfficer;

public class RPGEnemies {
	public static boolean printTiers = false;

	public final Map<String, RPGEnemyData> enemies = new HashMap<>();

	public void add(final RPGEnemyMonsterGirlDataBuilder builder) {
		add(builder.build());
	}

	public void add(final RPGEnemyData data) {
		if (enemies.get(data.id) != null) {
			throw new RuntimeException("doubled enemy id! " + data.id);
		}
		enemies.put(data.id, data);
	}

	public RPGEnemyData get(final String id) {
		return enemies.get(id);
	}

	public RPGEnemies(final Fluffer10kFun fluffer10kFun) {
		EnemiesOfRace.addAllEnemies(fluffer10kFun, this);
		MonsterArmyOfficer.add(fluffer10kFun, this);
	}
}
