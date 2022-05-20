package bot.commands.rpg.exploration;

import static bot.util.EmbedUtils.makeEmbed;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.commands.rpg.exploration.CommandExplore.ExplorationEventHandler;
import bot.data.items.loot.Loot;
import bot.data.items.loot.RPGLootByLevel;
import bot.userData.ServerUserData;

public class ExplorationStash implements ExplorationEventHandler {
	private static final String stashImgUrl = "https://cdn.discordapp.com/attachments/831994087330676807/831996290937126960/chest.png";

	private final Fluffer10kFun fluffer10kFun;

	public ExplorationStash(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public boolean handle(final SlashCommandInteraction interaction, final ServerUserData userData) {
		final Loot loot = RPGLootByLevel.getLoot(userData.rpg.level);
		loot.addToUser(userData);

		final EmbedBuilder embed = makeEmbed("Exploration: secret stash found", //
				"You search it and find:\n" + loot.getDescription(fluffer10kFun.items), //
				stashImgUrl);

		final long exp = 50 + 25 * userData.rpg.level;
		final EmbedBuilder expEmbed = userData.addExpAndMakeEmbed(exp, interaction.getUser(),
				interaction.getServer().get());

		interaction.createImmediateResponder().addEmbeds(embed, expEmbed).respond();

		return true;
	}
}
