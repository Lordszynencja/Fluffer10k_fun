package bot.userData.rpg.questData;

import static bot.commands.rpg.quests.QuestFluffLover.getStepFluffingDescription;
import static bot.util.CollectionUtils.mapToList;

import java.util.List;
import java.util.Map;

import bot.data.MonsterGirls;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.quests.QuestType;

public class UserFluffLoverQuestFluffingStepData extends UserQuestData {
	public List<MonsterGirlRace> girlsLeft = null;

	public UserFluffLoverQuestFluffingStepData(final String description, final List<MonsterGirlRace> girlsLeft) {
		super(QuestType.FLUFF_LOVER_QUEST, QuestStep.FLUFFING, description, true);
		this.girlsLeft = girlsLeft;
	}

	@SuppressWarnings("unchecked")
	public UserFluffLoverQuestFluffingStepData(final Map<String, Object> data, final QuestType type) {
		super(data, type);

		girlsLeft = mapToList((List<String>) data.get("girlsLeft"), o -> {
			MonsterGirlRace r = MonsterGirls.getByName(o);
			if (r == null) {
				r = MonsterGirlRace.valueOf(o);
			}

			return r;
		});
	}

	@Override
	public Map<String, Object> toMap() {
		final Map<String, Object> map = super.toMap();

		map.put("girlsLeft", girlsLeft);

		return map;
	}

	public void updateDescription() {
		description = getStepFluffingDescription(girlsLeft);
	}
}
