package bot.userData.rpg.questData;

import static bot.util.CollectionUtils.toMap;
import static bot.util.Utils.Pair.pair;

import java.util.Map;

import bot.data.quests.QuestType;
import bot.util.Utils.Pair;

public class UserQuestDataFactory {
	private interface QuestDataStepCreator {
		QuestDataCreator get(QuestStep step);
	}

	@SafeVarargs
	private static QuestDataStepCreator fromMap(final Pair<QuestStep, QuestDataCreator>... pairs) {
		final Map<QuestStep, QuestDataCreator> map = toMap(pairs);
		return step -> map.getOrDefault(step, UserQuestData::new);
	}

	private static QuestDataStepCreator defaultCreator() {
		return simple(UserQuestData::new);
	}

	private static QuestDataStepCreator simple(final QuestDataCreator creator) {
		return step -> creator;
	}

	private interface QuestDataCreator {
		UserQuestData createFrom(Map<String, Object> data, final QuestType type);
	}

	private static final Map<QuestType, QuestDataStepCreator> creators = toMap(
			pair(QuestType.A_LITTLE_CHEMISTRY_QUEST, defaultCreator()), //
			pair(QuestType.A_NUTTY_SITUATION_QUEST, fromMap(//
					pair(QuestStep.SEARCHING_FOR_ACORNS, UserANuttySituationQuestSearchingForAcornsStepData::new))), //
			pair(QuestType.BUSINESS_AS_USUAL_1_QUEST, defaultCreator()), //
			pair(QuestType.BUSINESS_AS_USUAL_2_QUEST, fromMap(//
					pair(QuestStep.GATHERING_RESOURCES, UserBusinessAsUsual2QuestGatheringResourcesStepData::new))), //
			pair(QuestType.BUSINESS_AS_USUAL_3_QUEST, fromMap(//
					pair(QuestStep.GATHERING_RESOURCES, UserBusinessAsUsual3QuestGatheringResourcesStepData::new))), //
			pair(QuestType.CHROME_BOOK_QUEST, defaultCreator()), //
			pair(QuestType.FLUFF_LOVER_QUEST, fromMap(//
					pair(QuestStep.FLUFFING, UserFluffLoverQuestFluffingStepData::new))), //
			pair(QuestType.HERO_ACADEMY_QUEST, fromMap(//
					pair(QuestStep.ROGUE_2, UserHeroAcademyQuestRogue2StepData::new))), //
			pair(QuestType.INSOMNIA_QUEST, fromMap(//
					pair(QuestStep.WAITING, UserInsomniaQuestWaitingStepData::new))), //
			pair(QuestType.JAGGED_JEWELLER_QUEST, defaultCreator()), //
			pair(QuestType.LITTLE_HEROES_QUEST, defaultCreator()), //
			pair(QuestType.MAGE_WITH_ALZHEIMER_QUEST, defaultCreator()), //
			pair(QuestType.MINERS_HOME, defaultCreator()), //
			pair(QuestType.SLEEPY_MOUSE_TEDDY_BEAR_QUEST, defaultCreator()), //
			pair(QuestType.SPHINX_QUESTIONS_QUEST, fromMap(//
					pair(QuestStep.ANSWERING_QUESTIONS, UserSphinxQuestionsQuestAnsweringQuestionsStepData::new))), //
			pair(QuestType.ULTIMATE_TEST, defaultCreator()));

	public static UserQuestData makeFrom(final Map<String, Object> data, final QuestType type) {
		final QuestStep step = QuestStep.valueOf((String) data.get("step"));

		return creators.get(type).get(step).createFrom(data, type);
	}
}
