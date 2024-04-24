package bot.data.fight;

import static bot.util.CollectionUtils.mapMapString;
import static bot.util.Utils.intFromNumber;
import static bot.util.Utils.longFromNumber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bot.commands.rpg.fight.fightRewards.FightEndReward;

public class FightData {
	public enum FightType {
		MULTIPLE_FIGHTERS, //
		NO_NORMAL_REWARDS, //
		PVE, //
		PVP_1V1;
	}

	public long id;
	public long serverId;
	public FightType type;
	public Map<String, FighterData> fighters;
	public int turn;
	public int turnProgress;
	public String targetFighter;
	public List<String> fightersOrder;
	public List<List<String>> turnDescriptions;
	public int actionsLeft;
	public boolean ended = false;
	public FightEndReward fightEndReward;

	public FightData(final long serverId, final FightType type, final Map<String, FighterData> fighters,
			final List<String> fightersOrder, final FightEndReward fightEndReward) {
		this.serverId = serverId;
		this.type = type;
		this.fighters = fighters;
		turn = 0;
		turnProgress = 0;
		targetFighter = fightersOrder.get(0);
		this.fightersOrder = fightersOrder;
		turnDescriptions = new ArrayList<>();
		turnDescriptions.add(new ArrayList<>());
		actionsLeft = 0;
		this.fightEndReward = fightEndReward;

		fighters.values().forEach(fighter -> fighter.fight = this);
	}

	@SuppressWarnings("unchecked")
	public FightData(final Map<String, Object> data, final long id) {
		this.id = id;
		serverId = longFromNumber(data.getOrDefault("serverId", 0));
		type = FightType.valueOf((String) data.get("type"));
		fighters = new HashMap<>();
		((Map<String, Map<String, Object>>) data.get("fighters")).forEach((fighterId, fighterData) -> {
			final FighterData fighter = FighterData.makeFighterData(this, fighterId, fighterData);
			fighters.put(fighterId, fighter);
		});
		turn = intFromNumber(data.get("turn"));
		turnProgress = intFromNumber(data.get("turnProgress"));
		targetFighter = (String) data.get("targetFighter");
		fightersOrder = (List<String>) data.get("fightersOrder");
		turnDescriptions = (List<List<String>>) data.get("turnDescriptions");
		actionsLeft = intFromNumber(data.get("actionsLeft"));
		fightEndReward = FightEndReward.valueOf((String) data.get("fightEndReward"));

		if (targetFighter == null) {
			targetFighter = fightersOrder.get(0);
		}
	}

	public Map<String, Object> toMap() {
		final Map<String, Object> map = new HashMap<>();

		map.put("serverId", serverId);
		map.put("type", type);
		map.put("fighters", mapMapString(fighters, fighterData -> fighterData.toMap()));
		map.put("turn", turn);
		map.put("turnProgress", turnProgress);
		map.put("targetFighter", targetFighter);
		map.put("fightersOrder", fightersOrder);
		map.put("turnDescriptions", turnDescriptions);
		map.put("actionsLeft", actionsLeft);
		map.put("fightEndReward", fightEndReward);

		return map;
	}

	public void addTurnDescription(final String description) {
		turnDescriptions.get(turnDescriptions.size() - 1).add(description);
	}

	public String getTurnDescriptions() {
		final List<String> lines = new ArrayList<>();
		turnDescriptions.forEach(list -> list.forEach(lines::add));
		final String result = lines.isEmpty() ? "None" : String.join("\n", lines);

		return result.length() >= 1024 ? result.substring(result.length() - 1024) : result;
	}

	public FighterData getCurrentFighter() {
		return fighters.get(fightersOrder.get(turnProgress));
	}
}
