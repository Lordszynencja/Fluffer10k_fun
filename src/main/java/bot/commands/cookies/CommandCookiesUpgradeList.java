package bot.commands.cookies;

import static bot.data.items.ItemUtils.getFormattedPlayCoins;
import static bot.util.EmbedUtils.makeEmbed;

import java.util.ArrayList;
import java.util.List;

import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.userData.ServerUserData;
import bot.util.EmbedUtils.EmbedField;
import bot.util.subcommand.Subcommand;

public class CommandCookiesUpgradeList extends Subcommand {
	private final Fluffer10kFun fluffer10kFun;

	public CommandCookiesUpgradeList(final Fluffer10kFun fluffer10kFun) {
		super(1, "list", "Show your cookie upgrades");

		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {
		final long serverId = interaction.getServer().get().getId();
		final long userId = interaction.getUser().getId();
		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(serverId, userId);

		final List<EmbedField> fields = new ArrayList<>();
		for (final CookieUpgrade upgrade : CookieUpgrade.values()) {
			final long upgradeLevel = userData.cookies.getUpgradeLevel(upgrade);
			String fieldName = upgrade.name + " (" + upgradeLevel + "/" + upgrade.max + ")";
			if (upgradeLevel < upgrade.max) {
				fieldName += " - " + getFormattedPlayCoins(upgrade.priceCreator.apply(upgradeLevel)) + " to upgrade";
			}
			fields.add(new EmbedField(fieldName, upgrade.description));
		}

		interaction.createImmediateResponder().addEmbed(makeEmbed("Cookie upgrades", null, null, fields)//
				.setColor(CookieUtils.cookiesColor))//
				.respond();
	}
}
