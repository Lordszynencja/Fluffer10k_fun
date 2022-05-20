package bot.commands.rpg.harem;

import static bot.data.items.ItemUtils.getFormattedMonies;
import static bot.util.CollectionUtils.addToLongOnMap;
import static bot.util.CollectionUtils.mapToList;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.modularPrompt.ModularPromptButton.button;
import static java.util.stream.Collectors.toList;

import java.util.List;

import org.javacord.api.entity.message.component.ButtonStyle;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.MessageComponentInteraction;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.ServerData;
import bot.userData.HaremMemberData;
import bot.userData.ServerUserData;
import bot.util.EmbedUtils.EmbedField;
import bot.util.modularPrompt.ModularPrompt;
import bot.util.pages.builders.PagedPickerMessageBuilder;
import bot.util.pages.messages.PagedMessage;
import bot.util.subcommand.Subcommand;

public class CommandHaremMarket extends Subcommand {
	private final Fluffer10kFun fluffer10kFun;

	public CommandHaremMarket(final Fluffer10kFun fluffer10kFun) {
		super("market", "Check the market");

		this.fluffer10kFun = fluffer10kFun;
	}

	private PagedMessage makeList(final ServerData serverData, final ServerUserData userData) {
		final List<MonsterGirlRace> data = serverData.monmusuMarket.entrySet().stream()//
				.filter(entry -> entry.getValue() > 0)//
				.map(entry -> entry.getKey())//
				.sorted()//
				.collect(toList());

		final List<EmbedField> fields = mapToList(data, race -> new EmbedField(race.race, //
				serverData.monmusuMarket.get(race) + " available for "
						+ getFormattedMonies(fluffer10kFun.commandHarem.racePrices.get(race))));

		return new PagedPickerMessageBuilder<>("Monster Girl Market", 10, fields, data)//
				.onPick((in, page, race) -> onPick(in, serverData, userData, race))//
				.build();
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {
		final ServerData serverData = fluffer10kFun.botDataUtils.getServerData(interaction.getServer().get());
		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(interaction.getServer().get(),
				interaction.getUser());
		fluffer10kFun.pagedMessageUtils.addMessage(makeList(serverData, userData), interaction);
	}

	private EmbedBuilder toEmbed(final ServerData serverData, final MonsterGirlRace race) {
		final String title = race.race + " (" + serverData.monmusuMarket.get(race) + ")";
		final String description = "Available for "
				+ getFormattedMonies(fluffer10kFun.commandHarem.racePrices.get(race));
		return makeEmbed(title, description).setImage(race.imageLink);
	}

	public void onPick(final MessageComponentInteraction interaction, final ServerData serverData,
			final ServerUserData userData, final MonsterGirlRace race) {
		final long price = fluffer10kFun.commandHarem.racePrices.get(race);
		final boolean buyDisabled = !userData.hasHouseSpace() || price > userData.monies;

		final ModularPrompt prompt = new ModularPrompt(toEmbed(serverData, race), //
				button("Buy", ButtonStyle.PRIMARY, in -> onBuy(in, serverData, userData, race), buyDisabled), //
				button("Back", ButtonStyle.DANGER, in -> onBack(in, serverData, userData)));

		fluffer10kFun.modularPromptUtils.addMessage(prompt, interaction);
	}

	private void onBack(final MessageComponentInteraction interaction, final ServerData serverData,
			final ServerUserData userData) {
		fluffer10kFun.pagedMessageUtils.addMessage(makeList(serverData, userData), interaction);
	}

	private void onBuy(final MessageComponentInteraction interaction, final ServerData serverData,
			final ServerUserData userData, final MonsterGirlRace race) {
		if (!userData.hasHouseSpace()) {
			interaction.createOriginalMessageUpdater().addEmbed(makeEmbed("You don't have space in your house!"))
					.update();
			return;
		}

		if (serverData.monmusuMarket.get(race) < 1) {
			interaction.createOriginalMessageUpdater().addEmbed(makeEmbed("Someone already bought her!")).update();
			return;
		}

		final long price = fluffer10kFun.commandHarem.racePrices.get(race);
		if (price > userData.monies) {
			interaction.createOriginalMessageUpdater().addEmbed(makeEmbed("You don't have enough money!")).update();
			return;
		}

		final HaremMemberData haremMember = userData.addWife(race);
		addToLongOnMap(serverData.monmusuMarket, race, -1);
		userData.monies -= price;

		interaction.createOriginalMessageUpdater().addEmbed(makeEmbed("You have new wife!"))
				.addEmbed(haremMember.toEmbed()).update();
	}
}
