package bot.userData.rpg.questData;

import static bot.util.Utils.longFromNumber;
import static java.lang.Math.min;

import java.util.Map;

import bot.commands.rpg.quests.QuestBusinessAsUsual3;
import bot.data.quests.QuestType;

public class UserBusinessAsUsual3QuestGatheringResourcesStepData extends UserQuestData {
	public long resourcesLeft = QuestBusinessAsUsual3.startingValue;

	public UserBusinessAsUsual3QuestGatheringResourcesStepData(final String description) {
		super(QuestType.BUSINESS_AS_USUAL_3_QUEST, QuestStep.GATHERING_RESOURCES, description, true);
	}

	public UserBusinessAsUsual3QuestGatheringResourcesStepData(final Map<String, Object> data, final QuestType type) {
		super(data, type);

		resourcesLeft = min(QuestBusinessAsUsual3.startingValue,
				longFromNumber(data.getOrDefault("resourcesLeft", QuestBusinessAsUsual3.startingValue)));
	}

	@Override
	public Map<String, Object> toMap() {
		final Map<String, Object> map = super.toMap();

		map.put("resourcesLeft", resourcesLeft);

		return map;
	}
}
