package bot.commands.rpg.quests;

import static bot.util.CollectionUtils.toMap;
import static bot.util.Utils.Pair.pair;

import java.util.HashMap;
import java.util.Map;

import bot.Fluffer10kFun;
import bot.data.quests.QuestType;

public class QuestUtils {
	private static final Map<String, String> images = toMap(//
			pair("Chrome",
					"https://cdn.discordapp.com/attachments/831994087330676807/831997292584108072/CHROME_BOOK_QUEST_1.png"));

	public static String getImage(final String name) {
		return images.get(name);
	}

	public final Map<QuestType, Quest> questHandlersMap = new HashMap<>();

	private void addQuest(final Quest quest) {
		questHandlersMap.put(quest.type, quest);
	}

	public QuestUtils(final Fluffer10kFun fluffer10kFun) {
		addQuest(new QuestALittleChemistry());
		addQuest(new QuestANuttySituation(fluffer10kFun));
		addQuest(new QuestBusinessAsUsual1(fluffer10kFun));
		addQuest(new QuestBusinessAsUsual2(fluffer10kFun));
		addQuest(new QuestBusinessAsUsual3(fluffer10kFun));
		addQuest(new QuestChromeBook());
		addQuest(new QuestFluffLover());
		addQuest(new QuestHeroAcademy(fluffer10kFun));
		addQuest(new QuestInsomnia());
		addQuest(new QuestJaggedJeweller(fluffer10kFun));
		addQuest(new QuestLittleHeroes());
		addQuest(new QuestMinersHome(fluffer10kFun));
		addQuest(new QuestSleepyMouseTeddyBear());
		addQuest(new QuestSphinxQuestions(fluffer10kFun));
		addQuest(new QuestUltimateTest(fluffer10kFun));
	}

	public Quest getQuestHandler(final QuestType type) {
		return questHandlersMap.get(type);
	}

	public QuestBusinessAsUsual1 questBusinessAsUsual1() {
		return (QuestBusinessAsUsual1) questHandlersMap.get(QuestType.BUSINESS_AS_USUAL_1_QUEST);
	}

	public QuestHeroAcademy questHeroAcademy() {
		return (QuestHeroAcademy) questHandlersMap.get(QuestType.HERO_ACADEMY_QUEST);
	}

	public QuestMinersHome questMinersHome() {
		return (QuestMinersHome) questHandlersMap.get(QuestType.MINERS_HOME);
	}
}
