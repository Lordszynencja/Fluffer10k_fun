package bot.userData.rpg.questData;

import static bot.util.CollectionUtils.mapToSet;
import static bot.util.RandomUtils.getRandom;
import static bot.util.Utils.bold;
import static bot.util.Utils.longFromNumber;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import bot.commands.rpg.quests.QuestSphinxQuestions.SphinxQuestion;
import bot.data.quests.QuestType;

public class UserSphinxQuestionsQuestAnsweringQuestionsStepData extends UserQuestData {
	public Set<SphinxQuestion> answeredQuestions;
	public SphinxQuestion currentQuestion;
	public long loveEndTime;

	public UserSphinxQuestionsQuestAnsweringQuestionsStepData(final String description,
			final SphinxQuestion currentQuestion) {
		super(QuestType.SPHINX_QUESTIONS_QUEST, QuestStep.ANSWERING_QUESTIONS, description, true);
		answeredQuestions = new HashSet<>();
		this.currentQuestion = currentQuestion;
		loveEndTime = 0;
	}

	@SuppressWarnings("unchecked")
	public UserSphinxQuestionsQuestAnsweringQuestionsStepData(final Map<String, Object> data, final QuestType type) {
		super(data, type);

		final Object tmp = data.get("answeredQuestions");
		if (tmp == null) {
			answeredQuestions = new HashSet<>();
			currentQuestion = getRandom(SphinxQuestion.values());
			loveEndTime = 0;
			description = String.join("\n", //
					"Answer the Sphinx's question!", //
					"\"" + currentQuestion.question + "\"", //
					"Use the " + bold("/quest continue") + " to answer the question.");
		} else {
			answeredQuestions = mapToSet((List<String>) data.get("answeredQuestions"), SphinxQuestion::valueOf);
			currentQuestion = SphinxQuestion.valueOf((String) data.get("currentQuestion"));
			loveEndTime = longFromNumber(data.get("loveEndTime"));
		}
	}

	@Override
	public Map<String, Object> toMap() {
		final Map<String, Object> map = super.toMap();

		map.put("answeredQuestions", answeredQuestions);
		map.put("currentQuestion", currentQuestion);
		map.put("loveEndTime", loveEndTime);

		return map;
	}
}
