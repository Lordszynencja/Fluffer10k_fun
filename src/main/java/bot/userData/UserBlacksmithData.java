package bot.userData;

import static bot.util.CollectionUtils.mapMap;
import static bot.util.CollectionUtils.mapToList;
import static bot.util.CollectionUtils.mapToSet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import bot.commands.rpg.blacksmith.blueprints.BlacksmithBlueprintsList;
import bot.commands.rpg.blacksmith.tasks.BlacksmithTier;
import bot.util.Utils;

public class UserBlacksmithData {
	public boolean available = false;
	public boolean setToday = false;
	public Set<BlacksmithTier> tiersUnlocked = new HashSet<>();

	public Map<BlacksmithTier, List<BlacksmithTaskData>> currentTasks = new HashMap<>();
	public BlacksmithTaskData currentTask = null;
	public Map<String, Integer> blueprints = new HashMap<>();

	public UserBlacksmithData() {
	}

	@SuppressWarnings("unchecked")
	public UserBlacksmithData(final Map<String, Object> data) {
		available = (boolean) data.get("available");
		setToday = (boolean) data.get("setToday");
		tiersUnlocked = mapToSet((Collection<String>) data.get("tiersUnlocked"), BlacksmithTier::valueOf);
		if (available) {
			tiersUnlocked.add(BlacksmithTier.TIER_1);
		}

		try {
			currentTasks = mapMap((Map<String, List<Map<String, Object>>>) data.get("currentTasks"),
					BlacksmithTier::valueOf, tasks -> mapToList(tasks, BlacksmithTaskData::new));
			currentTask = data.get("currentTask") == null ? null
					: new BlacksmithTaskData((Map<String, Object>) data.get("currentTask"));
		} catch (final Exception e) {
			currentTasks = new HashMap<>();
			currentTask = null;
			setToday = false;
		}
		blueprints = mapMap((Map<String, Number>) data.get("blueprints"), Utils::intFromNumber);
		for (final String blueprintId : new HashSet<>(blueprints.keySet())) {
			if (!BlacksmithBlueprintsList.blueprints.containsKey(blueprintId)) {
				blueprints.remove(blueprintId);
			}
		}
	}

	public Map<String, Object> toMap() {
		final Map<String, Object> map = new HashMap<>();

		map.put("available", available);
		map.put("setToday", setToday);
		map.put("tiersUnlocked", tiersUnlocked);

		try {
			map.put("currentTasks", mapMap(currentTasks, tasks -> mapToList(tasks, task -> task.toMap())));
			map.put("currentTask", currentTask == null ? null : currentTask.toMap());
		} catch (final Exception e) {
			currentTasks = new HashMap<>();
			currentTask = null;
		}
		map.put("blueprints", blueprints);

		return map;
	}

	private void addUserTasksTier(final BlacksmithTier tier) {
		currentTasks.put(tier, new ArrayList<>());
		for (int i = 0; i < 5; i++) {
			currentTasks.get(tier).add(BlacksmithTaskData.randomFromTierId(tier, i + 1));
		}
	}

	public void addTier(final BlacksmithTier tier) {
		if (setToday && currentTasks.get(tier) != null) {
			return;
		}

		addUserTasksTier(tier);
	}

	public Map<BlacksmithTier, List<BlacksmithTaskData>> getTasks() {
		if (!setToday) {
			currentTasks = new HashMap<>();
			for (final BlacksmithTier tier : tiersUnlocked) {
				addTier(tier);
			}

			setToday = true;
		}

		return currentTasks;
	}
}
