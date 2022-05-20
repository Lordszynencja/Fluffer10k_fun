package bot.commands.cookies;

import static bot.data.items.ItemUtils.getFormattedPlayCoins;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;

import java.util.ArrayList;
import java.util.List;

import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.SelectMenu;
import org.javacord.api.entity.message.component.SelectMenuOption;
import org.javacord.api.interaction.MessageComponentInteraction;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;
import org.javacord.api.interaction.callback.InteractionCallbackDataFlag;

import bot.Fluffer10kFun;
import bot.userData.ServerUserData;
import bot.util.subcommand.Subcommand;

public class CommandCookiesUpgradeBuy extends Subcommand {

	private final Fluffer10kFun fluffer10kFun;

	public CommandCookiesUpgradeBuy(final Fluffer10kFun fluffer10kFun) {
		super(1, "buy", "Buy cookie upgrade", //
				SlashCommandOption.create(SlashCommandOptionType.LONG, "amount", "how many upgrades to buy"));

		this.fluffer10kFun = fluffer10kFun;

		fluffer10kFun.apiUtils.commandHandlers.addMessageComponentHandler("cookies_upgrade_buy", this::handleBuyAction);
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {
		final long serverId = interaction.getServer().get().getId();
		final long userId = interaction.getUser().getId();
		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(serverId, userId);
		final int amount = getOption(interaction).getOptionLongValueByName("amount").orElse(1L).intValue();
		if (amount < 1) {
			sendEphemeralMessage(interaction, "You can't buy less than 1 upgrade level");
			return;
		}
		if (amount > 100) {
			sendEphemeralMessage(interaction, "You can't buy more than 100 upgrade levels");
			return;
		}

		final List<SelectMenuOption> options = new ArrayList<>();

		for (final CookieUpgrade u : CookieUpgrade.values()) {
			final long upgradeLevel = userData.cookies.getUpgradeLevel(u);
			if (upgradeLevel < u.max) {
				options.add(
						SelectMenuOption.create(u.name + " (" + u.priceCreator.apply(upgradeLevel) + " PC)", u.name()));
			}
		}
		if (options.isEmpty()) {
			sendEphemeralMessage(interaction, "You already have all cookie upgrades");
			return;
		}

		interaction.createImmediateResponder().addEmbed(makeEmbed("Choose cookie upgrade to buy x" + amount))//
				.setFlags(InteractionCallbackDataFlag.EPHEMERAL)//
				.addComponents(ActionRow.of(SelectMenu.create("cookies_upgrade_buy " + amount, options))).respond();
	}

	public void handleBuyAction(final MessageComponentInteraction interaction) {
		final long serverId = interaction.getServer().get().getId();
		final long userId = interaction.getUser().getId();
		final CookieUpgrade upgrade = CookieUpgrade
				.valueOf(interaction.asSelectMenuInteraction().get().getChosenOptions().get(0).getValue());

		int amount = Integer.valueOf(interaction.getCustomId().split(" ")[1]);

		final ServerUserData serverUserData = fluffer10kFun.serverUserDataUtils.getUserData(serverId, userId);

		long level = serverUserData.cookies.getUpgradeLevel(upgrade);
		long totalPrice = 0;
		long price = upgrade.priceCreator.apply(level);
		while (price <= serverUserData.playCoins && level < upgrade.max && amount > 0) {
			totalPrice += price;
			serverUserData.playCoins -= price;
			level++;
			amount--;
			price = upgrade.priceCreator.apply(level);
		}

		serverUserData.cookies.upgrades.put(upgrade, level);

		interaction.createOriginalMessageUpdater().addEmbed(makeEmbed(upgrade.name + " upgraded!",
				"Current level: " + level + "\nPaid: " + getFormattedPlayCoins(totalPrice))).update();
	}
}
