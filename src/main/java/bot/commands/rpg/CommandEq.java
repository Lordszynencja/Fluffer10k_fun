package bot.commands.rpg;

import static bot.commands.rpg.RPGExpUtils.getExpForLevel;
import static bot.data.items.ItemUtils.formatNumber;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;

import java.math.BigInteger;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.commands.rpg.RPGStatUtils.RPGStatsData;
import bot.data.items.ItemSlot;
import bot.userData.ServerUserData;
import bot.util.subcommand.Command;

public class CommandEq extends Command {
	private final Fluffer10kFun fluffer10kFun;

	public CommandEq(final Fluffer10kFun fluffer10kFun) {
		super(fluffer10kFun.apiUtils, "eq", "Check your equipment and stats");

		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {
		final Server server = interaction.getServer().orElse(null);
		if (server == null) {
			sendEphemeralMessage(interaction, "This command can only be used on server");
			return;
		}

		final User user = interaction.getUser();
		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(server.getId(), user.getId());

		final RPGStatsData total = fluffer10kFun.rpgStatUtils.getTotalStats(userData);
		final BigInteger nextLevelExp = getExpForLevel(userData.rpg.level + 1);
		String levelInfo = "Level " + userData.rpg.level + " (" + userData.rpg.exp + " xp) - next level in "
				+ (nextLevelExp.subtract(userData.rpg.exp));
		levelInfo += "\nStamina - " + userData.getStamina() + "/" + ServerUserData.maxStamina;

		if (userData.rpg.mineUnlocked) {
			levelInfo += "\nMining proficiency - " + userData.rpg.getMiningProficiency() + " ("
					+ formatNumber(userData.rpg.miningExp) + " exp)";
		}

		final EmbedBuilder embed = makeEmbed("Information about " + userData.rpg.getName(user, server), levelInfo)//
				.setThumbnail(userData.rpg.avatar);

		String currencies = userData.getFormattedMonies() + "\n" + userData.getFormattedPlayCoins();

		if (userData.danukiShopAvailable) {
			currencies += "\n" + userData.getFormattedMonsterGold();
		}
		embed.addField("Currencies", currencies);

		embed.addField("Statistics", "Strength - " + total.strength + " (" + userData.rpg.strength + " base)\n"//
				+ "Agility - " + total.agility + " (" + userData.rpg.agility + " base)\n"//
				+ "Intelligence - " + total.intelligence + " (" + userData.rpg.intelligence + " base)\n"//
				+ "Armor - " + total.armor);

		if (userData.rpg.eq.get(ItemSlot.BOTH_HANDS) == null) {
			for (final ItemSlot slot : new ItemSlot[] { ItemSlot.RIGHT_HAND, ItemSlot.LEFT_HAND }) {
				final String itemId = userData.rpg.eq.get(slot);
				final String itemName = itemId == null ? "Nothing" : fluffer10kFun.items.getItem(itemId).name;
				embed.addField(slot.name, itemName);
			}
		} else {
			final String itemId = userData.rpg.eq.get(ItemSlot.BOTH_HANDS);
			final String itemName = itemId == null ? "Nothing" : fluffer10kFun.items.getItem(itemId).name;
			embed.addField(ItemSlot.BOTH_HANDS.name, itemName);
		}
		for (final ItemSlot slot : new ItemSlot[] { ItemSlot.ARMOR, ItemSlot.RING_1, ItemSlot.RING_2,
				ItemSlot.PICKAXE }) {
			final String itemId = userData.rpg.eq.get(slot);
			final String itemName = itemId == null ? "Nothing" : fluffer10kFun.items.getItem(itemId).name;
			embed.addField(slot.name, itemName);
		}

		interaction.createImmediateResponder().addEmbed(embed).respond();
	}
}
