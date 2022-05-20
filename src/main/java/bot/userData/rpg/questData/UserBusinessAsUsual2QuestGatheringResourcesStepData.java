package bot.userData.rpg.questData;

import static bot.util.Utils.longFromNumber;
import static java.lang.Math.min;

import java.util.Map;

import bot.commands.rpg.quests.QuestBusinessAsUsual2;
import bot.data.quests.QuestType;

public class UserBusinessAsUsual2QuestGatheringResourcesStepData extends UserQuestData {
	public long resourcesLeft = QuestBusinessAsUsual2.startingValue;

	public UserBusinessAsUsual2QuestGatheringResourcesStepData(final String description) {
		super(QuestType.BUSINESS_AS_USUAL_2_QUEST, QuestStep.GATHERING_RESOURCES, description, true);
	}

	public UserBusinessAsUsual2QuestGatheringResourcesStepData(final Map<String, Object> data, final QuestType type) {
		super(data, type);

		resourcesLeft = min(QuestBusinessAsUsual2.startingValue,
				longFromNumber(data.getOrDefault("resourcesLeft", QuestBusinessAsUsual2.startingValue)));
	}

	@Override
	public Map<String, Object> toMap() {
		final Map<String, Object> map = super.toMap();

		map.put("resourcesLeft", resourcesLeft);

		return map;
	}
}
