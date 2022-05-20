package bot.commands.rpg.exploration;

import static bot.util.RandomUtils.getRandom;
import static java.util.stream.Collectors.toList;

import java.util.List;

import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.commands.rpg.exploration.CommandExplore.ExplorationEventHandler;
import bot.commands.rpg.quests.Quest;
import bot.userData.ServerUserData;

public class ExplorationQuest implements ExplorationEventHandler {

	private final Fluffer10kFun fluffer10kFun;

	public ExplorationQuest(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public boolean handle(final SlashCommandInteraction interaction, final ServerUserData userData) {
		final List<Quest> availableQuests = fluffer10kFun.questUtils.questHandlersMap.values().stream()//
				.filter(q -> q.available(userData))//
				.collect(toList());

		if (availableQuests.isEmpty()) {
			return false;
		}

		getRandom(availableQuests).start(interaction, userData);
		return true;
	}

}
