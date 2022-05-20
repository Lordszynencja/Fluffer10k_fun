package bot.commands.rpg.exploration;

import static bot.data.items.loot.Loot.gold;
import static bot.data.items.loot.Loot.item;
import static bot.data.items.loot.LootTable.weightedI;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.Utils.Pair.pair;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.data.items.loot.Loot;
import bot.data.items.loot.LootTable;
import bot.userData.ServerUserData;

public class ExplorationFriendlyPerson {

	private static final LootTable<Loot> lootTable = weightedI(//
			pair(1, gold(500)), //

			pair(1, item("SHORT_SWORD")), //
			pair(1, item("WOODEN_SHIELD")), //

			pair(1, item("MAGIC_SCROLL_HEAL")), //
			pair(1, item("MAGIC_SCROLL_FORCE_HIT")), //

			pair(3, item("HEALTH_POTION")), //
			pair(3, item("MANA_POTION")));

	private final Fluffer10kFun fluffer10kFun;

	public ExplorationFriendlyPerson(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;
	}

	public void meetPerson(final SlashCommandInteraction interaction, final ServerUserData userData) {
		userData.reduceStamina(5);
		final Loot gift = lootTable.getItem();

		gift.addToUser(userData);

		final EmbedBuilder embed = makeEmbed("Friendly person",
				"On the road, you meet a friendly villager, that recognizes you as a hero and gives you "
						+ gift.getDescription(fluffer10kFun.items) + " as a gift.");
		final EmbedBuilder expEmbed = userData.addExpAndMakeEmbed(100, interaction.getUser(),
				interaction.getServer().get());

		interaction.createImmediateResponder().addEmbeds(embed, expEmbed).respond();
	}
}
