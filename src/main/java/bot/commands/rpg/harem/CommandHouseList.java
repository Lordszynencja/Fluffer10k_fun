package bot.commands.rpg.harem;

import static bot.data.items.ItemUtils.getFormattedMonies;
import static bot.util.CollectionUtils.mapToList;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.modularPrompt.ModularPromptButton.button;

import java.util.List;

import org.javacord.api.entity.message.component.ButtonStyle;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.MessageComponentInteraction;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.userData.House;
import bot.userData.ServerUserData;
import bot.util.EmbedUtils.EmbedField;
import bot.util.modularPrompt.ModularPrompt;
import bot.util.pages.builders.PagedPickerMessageBuilder;
import bot.util.pages.messages.PagedMessage;
import bot.util.subcommand.Subcommand;

public class CommandHouseList extends Subcommand {
	private final Fluffer10kFun fluffer10kFun;

	public CommandHouseList(final Fluffer10kFun fluffer10kFun) {
		super("list", "List the available houses");

		this.fluffer10kFun = fluffer10kFun;
	}

	private PagedMessage makeList(final ServerUserData userData) {
		final List<House> data = House.sorted;
		final List<EmbedField> fields = mapToList(data, house -> new EmbedField(house.name,
				"Size: " + house.size + "\nPrice: " + getFormattedMonies(house.price)));

		return new PagedPickerMessageBuilder<>("Houses", 5, fields, data)//
				.onPick((in, page, house) -> onPick(in, userData, house))//
				.build();
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {
		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(interaction.getServer().get(),
				interaction.getUser());

		fluffer10kFun.pagedMessageUtils.addMessage(makeList(userData), interaction);
	}

	public void onPick(final MessageComponentInteraction interaction, final ServerUserData userData,
			final House house) {
		final boolean buyDisabled = house.size + userData.getAdditionalRooms() < userData.getHaremSize()
				|| house.price > (userData.monies + userData.house.price * 0.8);
		final EmbedBuilder embed = makeEmbed(house.name)//
				.addField("Size", house.size + "")//
				.addField("Price", getFormattedMonies(house.price));

		final ModularPrompt prompt = new ModularPrompt(embed, //
				button("Buy", ButtonStyle.PRIMARY, in -> onBuy(in, userData, house), buyDisabled), //
				button("Back", ButtonStyle.DANGER, in -> onBack(in, userData)));

		fluffer10kFun.modularPromptUtils.addMessage(prompt, interaction);
	}

	private void onBack(final MessageComponentInteraction interaction, final ServerUserData userData) {
		fluffer10kFun.pagedMessageUtils.addMessage(makeList(userData), interaction);
	}

	private void onBuy(final MessageComponentInteraction interaction, final ServerUserData userData,
			final House house) {
		if (house.size + userData.getAdditionalRooms() < userData.getHaremSize()) {
			interaction.createOriginalMessageUpdater().addEmbed(makeEmbed("This house is too small!")).update();
			return;
		}

		final long oldHouseSellPrice = (long) (userData.house.price * 0.8);
		final long newUserMonies = userData.monies + oldHouseSellPrice;
		if (house.price > newUserMonies) {
			interaction.createOriginalMessageUpdater().addEmbed(makeEmbed("You don't have enough money!")).update();
			return;
		}

		userData.monies = newUserMonies - house.price;
		userData.house = house;

		interaction.createOriginalMessageUpdater()//
				.addEmbed(makeEmbed("You bought " + house.name + "!", //
						"You got " + getFormattedMonies(oldHouseSellPrice) + " for selling old house."))//
				.update();
	}
}
