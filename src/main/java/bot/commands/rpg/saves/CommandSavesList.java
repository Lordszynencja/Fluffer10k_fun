package bot.commands.rpg.saves;

import static bot.util.EmbedUtils.makeEmbed;

import java.util.ArrayList;
import java.util.List;

import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.userData.ServerUserData;
import bot.util.EmbedUtils.EmbedField;
import bot.util.pages.builders.PagedMessageBuilder;
import bot.util.pages.messages.PagedMessage;
import bot.util.subcommand.Subcommand;

public class CommandSavesList extends Subcommand {
	private final Fluffer10kFun fluffer10kFun;

	protected CommandSavesList(final Fluffer10kFun fluffer10kFun) {
		super("list", "list your saves");

		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) throws Exception {
		final Server server = interaction.getServer().get();
		final User user = interaction.getUser();

		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(server.getId(), user.getId());
		if (userData.saves.isEmpty()) {
			interaction.createImmediateResponder().addEmbed(makeEmbed("No saves")).respond();
			return;
		}

		final List<EmbedField> saveDescriptions = new ArrayList<>();
		userData.saves.forEach(
				(name, data) -> saveDescriptions.add(new EmbedField(name, data.getSaveDescription(user, server))));
		saveDescriptions.sort((o0, o1) -> o0.name.compareTo(o1.name));

		final String title = "Your saves (" + saveDescriptions.size() + "/" + CommandSavesSave.maxSaves + ")";
		final PagedMessage msg = new PagedMessageBuilder<>(title, 5, saveDescriptions).build();
		fluffer10kFun.pagedMessageUtils.addMessage(msg, interaction);
	}

	// TODO add delete/load here
}
