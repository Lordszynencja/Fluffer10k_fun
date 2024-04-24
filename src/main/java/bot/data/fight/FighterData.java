package bot.data.fight;

import static bot.util.Utils.intFromNumber;
import static bot.util.Utils.Pair.pair;
import static java.lang.Math.min;

import java.util.HashMap;
import java.util.Map;

import bot.Fluffer10kFun;
import bot.commands.rpg.RPGStatUtils.RPGStatsData;
import bot.util.CollectionUtils;

public abstract class FighterData {
	private interface FighterDataCreator {
		FighterData create(FightData fight, String id, Map<String, Object> data);
	}

	public enum FighterType {
		ENEMY, //
		PLAYER;
	}

	private static final Map<FighterType, FighterDataCreator> creators = CollectionUtils.toMap(//
			pair(FighterType.ENEMY, EnemyFighterData::new), //
			pair(FighterType.PLAYER, PlayerFighterData::new));

	public static FighterData makeFighterData(final FightData fight, final String id, final Map<String, Object> data) {
		final FighterType type = FighterType.valueOf((String) data.get("type"));
		return creators.get(type).create(fight, id, data);
	}

	public FightData fight;
	public final String id;

	public FighterType type;
	public String team;
	public String name;
	public int hp;
	public int maxHp;
	public int level;
	public FighterStatusesData statuses;

	public boolean lost;
	public boolean escaped;
	public boolean attacked;

	public String heldBy;

	public FighterData(final String id, final FighterType type, final String team, final String name, final int hp,
			final int level) {
		this.id = id;

		this.type = type;
		this.team = team;
		this.name = name;
		this.hp = hp;
		maxHp = hp;
		this.level = level;
		statuses = new FighterStatusesData();

		lost = false;
		escaped = false;
		attacked = false;

		heldBy = null;
	}

	@SuppressWarnings("unchecked")
	protected FighterData(final FightData fight, final String id, final Map<String, Object> data) {
		this.fight = fight;
		this.id = id;

		type = FighterType.valueOf((String) data.get("type"));
		team = (String) data.get("team");
		name = (String) data.get("name");
		hp = intFromNumber(data.get("hp"));
		maxHp = intFromNumber(data.get("maxHp"));
		level = intFromNumber(data.getOrDefault("level", -1));
		statuses = new FighterStatusesData((Map<String, Map<String, Object>>) data.get("statuses"));

		lost = (boolean) data.getOrDefault("lost", false);
		escaped = (boolean) data.getOrDefault("escaped", false);
		attacked = (boolean) data.getOrDefault("attacked", false);

		heldBy = (String) data.get("heldBy");
	}

	public Map<String, Object> toMap() {
		final Map<String, Object> map = new HashMap<>();

		map.put("type", type);
		map.put("team", team);
		map.put("name", name);
		map.put("hp", hp);
		map.put("maxHp", maxHp);
		map.put("level", level);
		map.put("statuses", statuses.toMap());

		map.put("lost", lost);
		map.put("escaped", escaped);
		map.put("attacked", attacked);

		map.put("heldBy", heldBy);

		return map;
	}

	public abstract void addExp(long exp);

	private boolean lose() {
		if (statuses.isStatus(FighterStatus.UNDYING)) {
			statuses.removeStatus(FighterStatus.UNDYING);
			if (hp < 1) {
				hp = 1;
			}
			return false;
		}

		lost = true;
		return true;
	}

	public void updateLost(final Fluffer10kFun fluffer10kFun) {
		if (isOut()) {
			return;
		}

		if (hp <= 0) {
			if (lose()) {
				fight.addTurnDescription(name + " is defeated!");
				return;
			}
		}

		final FighterStatusData illusionWorld = statuses.statuses.get(FighterStatus.ILLUSION_WORLD);
		if (illusionWorld != null) {
			final RPGStatsData fighterStats = fluffer10kFun.rpgStatUtils.getTotalStatsInFight(this);
			final int statsTotal = fighterStats.strength + fighterStats.agility + fighterStats.intelligence;
			if (statsTotal < illusionWorld.stacks) {
				if (lose()) {
					fight.addTurnDescription(name + " gets lost in illusion world!");
					return;
				}
			}
		}
	}

	public boolean isOut() {
		return lost || escaped;
	}

	public PlayerFighterData player() {
		if (type == FighterType.PLAYER) {
			return (PlayerFighterData) this;
		}

		return null;
	}

	public EnemyFighterData enemy() {
		if (type == FighterType.ENEMY) {
			return (EnemyFighterData) this;
		}

		return null;
	}

	public int heal(final int healing) {
		final int hpBefore = hp;
		hp = min(maxHp, hp + healing);
		return hp - hpBefore;
	}
}
