package bot.commands.cookies;

import static bot.util.Utils.repeat;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Map;

import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.userData.ServerUserData;
import bot.util.EmbedUtils.EmbedField;
import bot.util.pages.builders.PagedMessageBuilder;
import bot.util.pages.messages.PagedMessage;
import bot.util.subcommand.Subcommand;

public class CommandMilksList extends Subcommand {

	private final Fluffer10kFun fluffer10kFun;

	public CommandMilksList(final Fluffer10kFun fluffer10kFun) {
		super("list", "List your cookies");

		this.fluffer10kFun = fluffer10kFun;
	}

	private static String milkCountToString(final long count) {
		return count < 20 ? repeat("🥛", (int) count) : (repeat("🥛", 17) + "... (" + count + ")");
	}

	private static List<EmbedField> milkCountsToFields(final Map<String, Long> counts) {
		return counts.entrySet().stream().sorted((entry0, entry1) -> entry0.getKey().compareTo(entry1.getKey()))//
				.filter(entry -> entry.getValue() > 0)//
				.map(entry -> new EmbedField(entry.getKey(), milkCountToString(entry.getValue()), false))//
				.collect(toList());
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {
		final long serverId = interaction.getServer().get().getId();
		final long userId = interaction.getUser().getId();

		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(serverId, userId);

		final List<EmbedField> fields = milkCountsToFields(userData.cookies.milkCounts);

		final PagedMessage message = new PagedMessageBuilder<>("Milk collection", 10, fields).build();
		fluffer10kFun.pagedMessageUtils.addMessage(message, interaction);
	}
}
