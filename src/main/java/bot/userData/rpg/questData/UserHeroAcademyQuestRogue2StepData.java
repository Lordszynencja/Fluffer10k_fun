package bot.userData.rpg.questData;

import static bot.util.Utils.intFromNumber;

import java.util.Map;

import bot.data.quests.QuestType;

public class UserHeroAcademyQuestRogue2StepData extends UserQuestData {
	public int dodgedAttacks;

	public UserHeroAcademyQuestRogue2StepData(final String description) {
		super(QuestType.HERO_ACADEMY_QUEST, QuestStep.ROGUE_2, description);
		dodgedAttacks = 1;
	}

	public UserHeroAcademyQuestRogue2StepData(final Map<String, Object> data, final QuestType type) {
		super(data, type);

		dodgedAttacks = intFromNumber(data.get("dodgedAttacks"));
	}

	@Override
	public Map<String, Object> toMap() {
		final Map<String, Object> map = super.toMap();

		map.put("dodgedAttacks", dodgedAttacks);

		return map;
	}
}
