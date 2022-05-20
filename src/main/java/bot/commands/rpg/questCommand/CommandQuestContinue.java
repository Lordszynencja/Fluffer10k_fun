package bot.commands.rpg.questCommand;

import static bot.util.CollectionUtils.mapToList;
import static bot.util.EmbedUtils.makeEmbed;
import static java.util.stream.Collectors.toList;

import java.util.List;

import org.javacord.api.interaction.MessageComponentInteraction;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.userData.ServerUserData;
import bot.userData.rpg.questData.UserQuestData;
import bot.util.EmbedUtils.EmbedField;
import bot.util.pages.builders.PagedPickerMessageBuilder;
import bot.util.pages.messages.PagedMessage;
import bot.util.subcommand.Subcommand;

public class CommandQuestContinue extends Subcommand {

	private final Fluffer10kFun fluffer10kFun;

	public CommandQuestContinue(final Fluffer10kFun fluffer10kFun) {
		super("continue", "continue a quest");

		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) throws Exception {
		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(interaction.getServer().get(),
				interaction.getUser());

		final List<UserQuestData> continuable = userData.rpg.quests.values().stream()//
				.filter(q -> q.continuable)//
				.collect(toList());
		if (continuable.isEmpty()) {
			interaction.createImmediateResponder()
					.addEmbed(makeEmbed("Continue quest", "You don't have quests you can continue this way")).respond();
			return;
		}

		continuable.sort(null);
		final List<EmbedField> fields = mapToList(continuable, q -> q.toField());

		final PagedMessage msg = new PagedPickerMessageBuilder<>("Quests", 5, fields, continuable)//
				.description("Pick quest to continue")//
				.onPick(this::onPick)//
				.build();
		fluffer10kFun.pagedMessageUtils.addMessage(msg, interaction);
	}

	private void onPick(final MessageComponentInteraction interaction, final int page, final UserQuestData questData) {
		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(interaction.getServer().get(),
				interaction.getUser());
		fluffer10kFun.questUtils.getQuestHandler(questData.type).continueQuest(interaction, userData);
	}
}
