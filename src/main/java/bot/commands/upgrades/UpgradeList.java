package bot.commands.upgrades;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.util.EmbedUtils.EmbedField;
import bot.util.pages.builders.PagedMessageBuilder;
import bot.util.pages.messages.PagedMessage;
import bot.util.subcommand.Subcommand;

public class UpgradeList extends Subcommand {
	private final Fluffer10kFun fluffer10kFun;

	public UpgradeList(final Fluffer10kFun fluffer10kFun) {
		super("list", "Show list of bought upgrades");

		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {
		final long serverId = interaction.getServer().get().getId();
		final long userId = interaction.getUser().getId();

		final List<EmbedField> fields = fluffer10kFun.serverUserDataUtils.getUserData(serverId, userId).upgrades
				.stream()//
				.sorted()//
				.map(u -> new EmbedField(u.name + " - " + u.group.name, u.description, false))//
				.collect(toList());

		final PagedMessage msg = new PagedMessageBuilder<>("Bought upgrades", 5, fields).build();
		fluffer10kFun.pagedMessageUtils.addMessage(msg, interaction);
	}
}
