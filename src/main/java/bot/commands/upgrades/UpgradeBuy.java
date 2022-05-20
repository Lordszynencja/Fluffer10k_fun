package bot.commands.upgrades;

import static bot.data.items.ItemUtils.formatNumber;
import static bot.data.items.ItemUtils.getFormattedMonies;
import static bot.util.CollectionUtils.mapToList;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;

import java.util.ArrayList;
import java.util.List;

import org.javacord.api.entity.message.component.SelectMenuOption;
import org.javacord.api.interaction.MessageComponentInteraction;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.userData.ServerUserData;
import bot.util.EmbedUtils.EmbedField;
import bot.util.pages.builders.PagedPickerWithConfirmationMessageBuilder;
import bot.util.pages.messages.PagedMessage;
import bot.util.subcommand.Subcommand;

public class UpgradeBuy extends Subcommand {
	private final Fluffer10kFun fluffer10kFun;

	public UpgradeBuy(final Fluffer10kFun fluffer10kFun) {
		super("buy", "Buy an upgrade");

		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {
		final long serverId = interaction.getServer().get().getId();
		final long userId = interaction.getUser().getId();

		final List<SelectMenuOption> options = new ArrayList<>();

		for (final Upgrade u : fluffer10kFun.commandUpgrade.getMissingUpgrades(serverId, userId)) {
			options.add(SelectMenuOption.create(u.name + " (" + formatNumber(u.price) + ")", u.name() + " " + userId));
		}
		if (options.isEmpty()) {
			sendEphemeralMessage(interaction, "You already bought all upgrades!");
			return;
		}

		final List<Upgrade> upgrades = fluffer10kFun.commandUpgrade.getMissingUpgrades(serverId, userId);
		upgrades.sort(null);
		final List<EmbedField> fields = mapToList(upgrades, u -> new EmbedField(u.name, getFormattedMonies(u.price)));

		final PagedMessage msg = new PagedPickerWithConfirmationMessageBuilder<>("Choose upgrade to buy", 5, fields,
				upgrades)//
						.dataToEmbed(
								u -> makeEmbed("Buy " + u.name + " for " + getFormattedMonies(u.price), u.description))//
						.onConfirm(this::onPick)//
						.build();
		fluffer10kFun.pagedMessageUtils.addMessage(msg, interaction);
	}

	public void onPick(final MessageComponentInteraction interaction, final Upgrade upgrade) {
		final long serverId = interaction.getServer().get().getId();
		final long userId = interaction.getUser().getId();

		final ServerUserData serverUserData = fluffer10kFun.serverUserDataUtils.getUserData(serverId, userId);
		if (serverUserData.monies < upgrade.price) {
			interaction.createOriginalMessageUpdater().addEmbed(makeEmbed("You don't have enough money!")).update();
			return;
		}

		serverUserData.monies -= upgrade.price;
		serverUserData.upgrades.add(upgrade);

		interaction.createOriginalMessageUpdater().addEmbed(makeEmbed("You bought " + upgrade.name.toLowerCase() + "!"))
				.update();
	}
}
