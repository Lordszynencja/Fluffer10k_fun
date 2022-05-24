package bot.userData;

import static bot.commands.rpg.blacksmith.blueprints.BlacksmithBlueprintsList.getRandomBlueprint;
import static bot.commands.rpg.blacksmith.tasks.BlacksmithTasksList.getRandomTask;
import static bot.util.CollectionUtils.mapMap;
import static bot.util.Utils.Pair.pair;

import java.util.HashMap;
import java.util.Map;

import bot.commands.rpg.blacksmith.blueprints.BlacksmithBlueprint;
import bot.commands.rpg.blacksmith.blueprints.BlacksmithBlueprintsList;
import bot.commands.rpg.blacksmith.tasks.BlacksmithTask;
import bot.commands.rpg.blacksmith.tasks.BlacksmithTasksList;
import bot.commands.rpg.blacksmith.tasks.BlacksmithTier;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.items.Items;
import bot.util.CollectionUtils;
import bot.util.EmbedUtils.EmbedField;
import bot.util.Utils;

public class BlacksmithTaskData {
	private static final Map<BlacksmithTier, String> tierPrefixes = CollectionUtils.toMap(//
			pair(BlacksmithTier.TIER_1, "1"), //
			pair(BlacksmithTier.TIER_2, "2"), //
			pair(BlacksmithTier.TIER_3, "3"), //
			pair(BlacksmithTier.TIER_ENCHANTMENT, "E"));

	public static BlacksmithTaskData randomFromTierId(final BlacksmithTier tier, final int id) {
		final String number = tierPrefixes.get(tier) + "-" + id;
		final BlacksmithTask task = getRandomTask(tier);
		final BlacksmithBlueprint blueprint = getRandomBlueprint(tier);
		return new BlacksmithTaskData(number, task, blueprint);
	}

	public final String number;
	public final BlacksmithTask task;
	public final BlacksmithBlueprint blueprint;

	public final Map<MonsterGirlRace, Integer> monsterGirlsDefeated;

	public BlacksmithTaskData(final String number, final BlacksmithTask task, final BlacksmithBlueprint blueprint) {
		this.number = number;
		this.task = task;
		this.blueprint = blueprint;
		monsterGirlsDefeated = new HashMap<>();
	}

	@SuppressWarnings("unchecked")
	public BlacksmithTaskData(final Map<String, Object> data) {
		number = (String) data.get("number");
		task = BlacksmithTasksList.getTask((String) data.get("taskId"));
		blueprint = BlacksmithBlueprintsList.getBlueprint((String) data.get("blueprintId"));
		monsterGirlsDefeated = mapMap((Map<String, Number>) data.getOrDefault("monsterGirlsDefeated", new HashMap<>()),
				MonsterGirlRace::valueOf, Utils::intFromNumber);
	}

	public Map<String, Object> toMap() {
		final Map<String, Object> map = new HashMap<>();

		map.put("number", number);
		map.put("taskId", task.id);
		map.put("blueprintId", blueprint.id);
		map.put("monsterGirlsDefeated", monsterGirlsDefeated);

		return map;
	}

	public EmbedField toField(final Items items) {
		return new EmbedField(blueprint.name, task.description//
				+ "\nRequirements:\n"//
				+ task.target.taskDescription(items));
	}
}
