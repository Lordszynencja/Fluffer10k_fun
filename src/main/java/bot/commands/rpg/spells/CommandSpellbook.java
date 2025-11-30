package bot.commands.rpg.spells;

import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;

import java.io.IOException;
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
import bot.util.subcommand.Command;

public class CommandSpellbook extends Command {
	public static final String spellbookImgUrl = null;

	private final Fluffer10kFun fluffer10kFun;

	public CommandSpellbook(final Fluffer10kFun fluffer10kFun) throws IOException {
		super(fluffer10kFun.apiUtils, "spellbook", "Check your book of spells", false);

		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {
		final Server server = interaction.getServer().orElse(null);
		if (server == null) {
			sendEphemeralMessage(interaction, "This command can only be used on server");
			return;
		}

		final long serverId = server.getId();
		final User user = interaction.getUser();

		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(serverId, user.getId());
		final List<ActiveSkill> userSpells = userData.rpg.spells();

		if (userSpells.isEmpty()) {
			interaction.createImmediateResponder().addEmbed(
					makeEmbed(fluffer10kFun.apiUtils.getUserName(user, server) + " has no spells in the spellbook")
							.setImage(spellbookImgUrl))
					.respond();
		} else {
			final List<EmbedField> fields = new ArrayList<>();
			for (final ActiveSkill spell : userSpells) {
				fields.add(new EmbedField(spell.getFullName(), spell.description));
			}

			final PagedMessage msg = new PagedMessageBuilder<>(
					fluffer10kFun.apiUtils.getUserName(user, server) + "'s spellbook", 5, fields)//
					.imgUrl(spellbookImgUrl)//
					.build();
			fluffer10kFun.pagedMessageUtils.addMessage(msg, interaction);
		}
	}
}
