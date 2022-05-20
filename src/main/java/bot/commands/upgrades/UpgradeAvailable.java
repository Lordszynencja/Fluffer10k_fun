package bot.commands.upgrades;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.util.EmbedUtils.EmbedField;
import bot.util.pages.builders.PagedMessageBuilder;
import bot.util.pages.messages.PagedMessage;
import bot.util.subcommand.Subcommand;

public class UpgradeAvailable extends Subcommand {
	private final Fluffer10kFun fluffer10kFun;

	public UpgradeAvailable(final Fluffer10kFun fluffer10kFun) {
		super("available", "Show list of available upgrades");

		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {
		final long serverId = interaction.getServer().get().getId();
		final long userId = interaction.getUser().getId();

		final List<EmbedField> embedFields = fluffer10kFun.commandUpgrade.getMissingUpgrades(serverId, userId).stream()//
				.sorted()//
				.map(u -> new EmbedField(u.name + " - " + u.group.name + " (" + u.price + ")", u.description, false))//
				.collect(toList());

		final PagedMessage msg = new PagedMessageBuilder<>("Available upgrades", 5, embedFields).build();
		fluffer10kFun.pagedMessageUtils.addMessage(msg, interaction);
	}
}
