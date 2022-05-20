package bot.commands.rpg.questCommand;

import static bot.util.CollectionUtils.mapToList;
import static bot.util.EmbedUtils.makeEmbed;
import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionChoice;
import org.javacord.api.interaction.SlashCommandOptionType;

import bot.Fluffer10kFun;
import bot.userData.ServerUserData;
import bot.userData.rpg.questData.QuestStep;
import bot.userData.rpg.questData.UserQuestData;
import bot.util.EmbedUtils.EmbedField;
import bot.util.pages.builders.PagedMessageBuilder;
import bot.util.pages.messages.PagedMessage;
import bot.util.subcommand.Subcommand;

public class CommandQuestList extends Subcommand {

	private final Fluffer10kFun fluffer10kFun;

	public CommandQuestList(final Fluffer10kFun fluffer10kFun) {
		super("list", "list your quests", //
				SlashCommandOption.createWithChoices(SlashCommandOptionType.STRING, "filter", "filter the quest list",
						false, //
						asList(SlashCommandOptionChoice.create("all", "all"), //
								SlashCommandOptionChoice.create("finished", "finished"), //
								SlashCommandOptionChoice.create("unfinished", "unfinished"))));

		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) throws Exception {
		final Server server = interaction.getServer().get();
		final User user = interaction.getUser();
		final String filter = interaction.getOptionByIndex(0).get().getOptionStringValueByName("filter").orElse("all");

		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(server, user);
		final int questsCount = userData.rpg.quests.size();
		if (questsCount == 0) {
			interaction.createImmediateResponder()
					.addEmbed(makeEmbed("Your quests", "Your quest log is empty for now, go explore!")).respond();
			return;
		}

		final List<UserQuestData> unfinished = new ArrayList<>();
		final List<UserQuestData> finished = new ArrayList<>();
		for (final UserQuestData questData : userData.rpg.quests.values()) {
			(questData.step == QuestStep.FINISHED ? finished : unfinished).add(questData);
		}
		unfinished.sort(null);
		finished.sort(null);

		List<UserQuestData> quests;
		switch (filter) {
		case "all":
			quests = unfinished;
			quests.addAll(finished);
			break;
		case "finished":
			quests = finished;
			break;
		case "unfinished":
			quests = unfinished;
			break;
		default:
			throw new RuntimeException("Wrong filter for quest list " + filter);
		}
		if (quests.isEmpty()) {
			interaction.createImmediateResponder()
					.addEmbed(makeEmbed("Your quests", "You have no quests matching the filters")).respond();
			return;
		}

		final String description = "Your quest log contains " + (questsCount == 1 ? "1 quest" : questsCount + " quests")
				+ ", in this " + finished.size() + " finished";
		final List<EmbedField> fields = mapToList(quests, q -> q.toField());

		final PagedMessage msg = new PagedMessageBuilder<>("Quests", 5, fields)//
				.description(description)//
				.build();
		fluffer10kFun.pagedMessageUtils.addMessage(msg, interaction);
	}

}
