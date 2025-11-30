package bot.commands.rpg.blacksmith;

import static bot.data.items.data.OreItems.ORE_COAL;
import static bot.data.items.data.OreItems.ORE_COPPER;
import static bot.data.items.data.OreItems.ORE_DEMON_REALM_SILVER;
import static bot.data.items.data.OreItems.ORE_DRAGONIUM;
import static bot.data.items.data.OreItems.ORE_GOLD;
import static bot.data.items.data.OreItems.ORE_IRON;
import static bot.data.items.data.OreItems.ORE_SILVER;
import static bot.util.CollectionUtils.toSet;
import static bot.util.TimerUtils.startRepeatedTimedEvent;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;

import java.util.Set;

import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.util.subcommand.Command;

public class CommandBlacksmith extends Command {
	public static final Set<String> oreIds = toSet(//
			ORE_COAL, //
			ORE_COPPER, //
			ORE_IRON, //
			ORE_SILVER, //
			ORE_GOLD, //
			ORE_DEMON_REALM_SILVER, //
			ORE_DRAGONIUM);

	public static final String imgUrl = MonsterGirlRace.DWARF.imageLink;

	private final Fluffer10kFun fluffer10kFun;

	private void resetDailyBlacksmithTasks() {
		fluffer10kFun.serverUserDataUtils
				.onEveryUser((serverId, userId, userData) -> userData.blacksmith.setToday = false);
	}

	public CommandBlacksmith(final Fluffer10kFun fluffer10kFun) {
		super(fluffer10kFun.apiUtils, "blacksmith", "Crafting and enchanting", false, //
				new CommandBlacksmithBlueprints(fluffer10kFun), //
				new CommandBlacksmithBuy(fluffer10kFun), //
				new CommandBlacksmithMine(fluffer10kFun), //
				new CommandBlacksmithSell(fluffer10kFun), //
				new CommandBlacksmithTask(fluffer10kFun));

		this.fluffer10kFun = fluffer10kFun;

		startRepeatedTimedEvent(this::resetDailyBlacksmithTasks, 24 * 60 * 60, 24 * 60 * 60,
				"reset daily blacksmith tasks");
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) throws Exception {
		final Server server = interaction.getServer().orElse(null);
		if (server == null) {
			sendEphemeralMessage(interaction, "This command can only be used on server");
			return;
		}

		subcommandHandler.handle(interaction);
	}

}
