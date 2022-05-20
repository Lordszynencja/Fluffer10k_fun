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

public class CommandCookiesList extends Subcommand {
	private final Fluffer10kFun fluffer10kFun;

	public CommandCookiesList(final Fluffer10kFun fluffer10kFun) {
		super("list", "List your cookies");

		this.fluffer10kFun = fluffer10kFun;
	}

	private static String cookieCountToString(final long count) {
		return count < 20 ? repeat("🍪", (int) count) : (repeat("🍪", 17) + "... (" + count + ")");
	}

	private static List<EmbedField> cookieCountsToFields(final Map<String, Long> counts) {
		return counts.entrySet().stream().sorted((entry0, entry1) -> entry0.getKey().compareTo(entry1.getKey()))//
				.filter(entry -> entry.getValue() > 0)//
				.map(entry -> new EmbedField(entry.getKey(), cookieCountToString(entry.getValue()), false))//
				.collect(toList());
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {
		final long serverId = interaction.getServer().get().getId();
		final long userId = interaction.getUser().getId();

		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(serverId, userId);

		final List<EmbedField> fields = cookieCountsToFields(userData.cookies.cookieCounts);

		final PagedMessage msg = new PagedMessageBuilder<>("Cookie collection", 10, fields).build();
		fluffer10kFun.pagedMessageUtils.addMessage(msg, interaction);
	}
}
