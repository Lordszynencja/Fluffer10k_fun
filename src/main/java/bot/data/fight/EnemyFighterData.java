package bot.data.fight;

import static java.lang.Math.max;

import java.util.Map;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.enemies.RPGEnemyData;

public class EnemyFighterData extends FighterData {
	public static EnemyFighterData fromStats(final String id, final RPGEnemyData mgData, final String team) {
		final int hp = max(1, 1 + mgData.baseHp + (int) (1.5 * mgData.strength));
		return new EnemyFighterData(id, mgData, team, mgData.name, hp);
	}

	public String enemyId;
	private RPGEnemyData enemyData;

	public EnemyFighterData(final String id, final RPGEnemyData enemyData, final String team, final String name,
			final int hp) {
		super(id, FighterType.ENEMY, team, name, hp, enemyData.level);
		enemyId = enemyData.id;
		this.enemyData = enemyData;
	}

	protected EnemyFighterData(final FightData fight, final String id, final Map<String, Object> data) {
		super(fight, id, data);

		enemyId = (String) data.get("enemyId");
	}

	@Override
	public Map<String, Object> toMap() {
		final Map<String, Object> map = super.toMap();

		map.put("enemyId", enemyId);

		return map;
	}

	@Override
	public void addExp(final long exp) {
	}

	public RPGEnemyData enemyData(final Fluffer10kFun fluffer10kFun) {
		if (enemyData == null) {
			enemyData = fluffer10kFun.rpgEnemies.get(enemyId);
		}

		return enemyData;
	}
}
