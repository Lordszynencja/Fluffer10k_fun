package bot.userData.rpg.questData;

import static bot.util.Utils.longFromNumber;

import java.util.Map;

import bot.data.quests.QuestType;

public class UserInsomniaQuestWaitingStepData extends UserQuestData {
	public long startTime;

	public UserInsomniaQuestWaitingStepData(final String description, final long startTime) {
		super(QuestType.INSOMNIA_QUEST, QuestStep.WAITING, description, true);
		this.startTime = startTime;
	}

	public UserInsomniaQuestWaitingStepData(final Map<String, Object> data, final QuestType type) {
		super(data, type);

		startTime = longFromNumber(data.get("startTime"));
	}

	@Override
	public Map<String, Object> toMap() {
		final Map<String, Object> map = super.toMap();

		map.put("startTime", startTime);

		return map;
	}
}
