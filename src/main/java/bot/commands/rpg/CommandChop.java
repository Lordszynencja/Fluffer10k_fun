package bot.commands.rpg;

import static bot.data.items.data.CraftingItems.WOOD;
import static bot.data.items.data.WeaponItems.LUMBERJACK_AXE;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.RandomUtils.getRandomLong;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;

import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.userData.ServerUserData;
import bot.util.subcommand.Command;

public class CommandChop extends Command {
	private static final int staminaConsumption = 10;

	private final Fluffer10kFun fluffer10kFun;

	public CommandChop(final Fluffer10kFun fluffer10kFun) {
		super(fluffer10kFun.apiUtils, "chop", "Chop some wood");

		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {
		final Server server = interaction.getServer().orElse(null);
		if (server == null) {
			sendEphemeralMessage(interaction, "This command can only be used on server");
			return;
		}

		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(interaction);
		if (!userData.hasItem(LUMBERJACK_AXE)) {
			sendEphemeralMessage(interaction, "You don't have an axe");
			return;
		}
		if (userData.getStamina() < staminaConsumption) {
			sendEphemeralMessage(interaction, "You are too tired");
			return;
		}
		userData.reduceStamina(staminaConsumption);
		final long woodChopped = getRandomLong(1, 5);
		userData.addItem(WOOD, woodChopped);

		interaction.createImmediateResponder().addEmbed(makeEmbed("Wood chopped", "you got " + woodChopped + " wood"))
				.respond();
	}
}
