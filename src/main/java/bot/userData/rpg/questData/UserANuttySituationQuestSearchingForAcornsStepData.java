package bot.userData.rpg.questData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import bot.data.quests.QuestType;

public class UserANuttySituationQuestSearchingForAcornsStepData extends UserQuestData {
	public List<String> acornsLeft;

	public UserANuttySituationQuestSearchingForAcornsStepData(final String description, final List<String> acornsLeft) {
		super(QuestType.A_NUTTY_SITUATION_QUEST, QuestStep.SEARCHING_FOR_ACORNS, description, true);
		this.acornsLeft = acornsLeft;
	}

	@SuppressWarnings("unchecked")
	public UserANuttySituationQuestSearchingForAcornsStepData(final Map<String, Object> data, final QuestType type) {
		super(data, type);

		acornsLeft = new ArrayList<>((List<String>) data.getOrDefault("acornsLeft", data.get("acornsToFind")));
	}

	@Override
	public Map<String, Object> toMap() {
		final Map<String, Object> map = super.toMap();

		map.put("acornsLeft", acornsLeft);

		return map;
	}
}
