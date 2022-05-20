package bot.commands.rpg.blacksmith.tasks;

import static bot.util.RandomUtils.getRandom;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bot.commands.rpg.blacksmith.tasks.data.Tier1BlacksmithTasks;
import bot.commands.rpg.blacksmith.tasks.data.Tier2BlacksmithTasks;
import bot.commands.rpg.blacksmith.tasks.data.Tier3BlacksmithTasks;

public class BlacksmithTasksList {
	private static final Map<String, BlacksmithTask> tasks = new HashMap<>();
	private static final Map<BlacksmithTier, List<BlacksmithTask>> tasksByTier = asList(//
			BlacksmithTier.TIER_1, //
			BlacksmithTier.TIER_2, //
			BlacksmithTier.TIER_3)//
					.stream()//
					.collect(toMap(tier -> tier, tier -> new ArrayList<>()));

	public static void addTask(final BlacksmithTask task) {
		if (tasks.get(task.id) != null) {
			throw new RuntimeException("double task id " + task.id);
		}

		tasks.put(task.id, task);
		tasksByTier.get(task.tier).add(task);
	}

	public static void addTasks() {
		Tier1BlacksmithTasks.addTasks();
		Tier2BlacksmithTasks.addTasks();
		Tier3BlacksmithTasks.addTasks();
	}

	public static BlacksmithTask getRandomTask(final BlacksmithTier tier) {
		return getRandom(tasksByTier.get(tier));
	}

	public static BlacksmithTask getTask(final String taskId) {
		return tasks.get(taskId);
	}
}
