package bot.commands.upgrades;

import static bot.util.apis.MessageUtils.sendEphemeralMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.util.subcommand.Command;

public class CommandUpgrade extends Command {
	public List<Upgrade> getMissingUpgrades(final long serverId, final long userId) {
		final Set<Upgrade> userUpgrades = fluffer10kFun.serverUserDataUtils.getUserData(serverId, userId).upgrades;
		final List<Upgrade> missingUpgrades = new ArrayList<>();
		for (final Upgrade u : Upgrade.values()) {
			if (!userUpgrades.contains(u)) {
				missingUpgrades.add(u);
			}
		}
		return missingUpgrades;
	}

	private final Fluffer10kFun fluffer10kFun;

	public CommandUpgrade(final Fluffer10kFun fluffer10kFun) throws IOException {
		super(fluffer10kFun.apiUtils, "upgrade", "Upgrade your income", //
				new UpgradeAvailable(fluffer10kFun), //
				new UpgradeBuy(fluffer10kFun), //
				new UpgradeList(fluffer10kFun));

		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) throws Exception {
		final Server server = interaction.getServer().orElse(null);
		if (server == null) {
			sendEphemeralMessage(interaction, "This command is only usable on a server.");
			return;
		}

		subcommandHandler.handle(interaction);
	}
}
