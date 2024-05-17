package bot.commands.rpg.harem;

import static bot.commands.rpg.harem.CommandHaremDo.addBonusItem;
import static bot.commands.rpg.harem.CommandHaremDo.doInteraction;
import static bot.data.items.ItemUtils.getFormattedMonies;
import static bot.util.CollectionUtils.addToLongOnMap;
import static bot.util.CollectionUtils.mapToList;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.apis.MessageUtils.getServerTextChannel;
import static bot.util.modularPrompt.ModularPromptButton.button;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;

import org.javacord.api.entity.message.component.ButtonStyle;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.MessageComponentInteraction;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.ServerData;
import bot.data.items.Item;
import bot.data.items.ItemAmount;
import bot.data.items.ItemClass;
import bot.userData.HaremMemberData;
import bot.userData.ServerUserData;
import bot.util.EmbedUtils.EmbedField;
import bot.util.modularPrompt.ModularPrompt;
import bot.util.pages.builders.PagedPickerMessageBuilder;
import bot.util.pages.messages.PagedMessage;
import bot.util.subcommand.Subcommand;

public class CommandHaremList extends Subcommand {
	private final Fluffer10kFun fluffer10kFun;

	public CommandHaremList(final Fluffer10kFun fluffer10kFun) {
		super("list", "Check your harem");

		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {
		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(interaction.getServer().get(),
				interaction.getUser());

		if (userData.harem.isEmpty()) {
			interaction.createImmediateResponder()
					.addEmbed(makeEmbed("Your harem looks empty, explore the world and find a wife (or a few)!"))
					.respond();
			return;
		}

		fluffer10kFun.pagedMessageUtils.addMessage(makeList(userData), interaction);
	}

	private PagedMessage makeList(final ServerUserData userData) {
		final List<HaremMemberData> data = new ArrayList<>(userData.harem.values());
		data.sort(null);

		final List<EmbedField> fields = mapToList(data,
				haremMember -> new EmbedField(haremMember.getFullName(), haremMember.getDescription()));

		return new PagedPickerMessageBuilder<>("Your harem", 5, fields, data)//
				.onPick((interaction, page, haremMember) -> onPick(interaction, userData, haremMember))//
				.build();
	}

	private void onPick(final MessageComponentInteraction interaction, final ServerUserData userData,
			final HaremMemberData haremMember) {
		final ModularPrompt prompt = new ModularPrompt(haremMember.toEmbed(), //
				button("Gift", ButtonStyle.PRIMARY, in -> onGift(in, userData, haremMember)));

		if (haremMember.changeable) {
			prompt.addButton(button("Rename", ButtonStyle.PRIMARY, in -> onRename(in, userData, haremMember)));
		}

		if (!haremMember.married) {
			if (!haremMember.canBeMarried()) {
				prompt.addButton(button("Marry (not enough affection)", ButtonStyle.PRIMARY, null, true));
			} else if (userData.monies < marryPrice) {
				prompt.addButton(
						button("Marry (need " + getFormattedMonies(marryPrice) + ")", ButtonStyle.PRIMARY, null, true));
			} else {
				prompt.addButton(button("Marry", ButtonStyle.PRIMARY, in -> onMarry(in, userData, haremMember), false));
			}
		} else if (haremMember.desiredInteraction != null) {
			prompt.addButton(button("Interact", ButtonStyle.PRIMARY, in -> onInteraction(in, userData, haremMember)));
		}

		if (haremMember.changeable) {
			prompt.addButton(button("Return to market", ButtonStyle.DANGER,
					in -> onReturnToMarket(in, userData, haremMember), !haremMember.changeable));
		}

		prompt.addButton(null)//
				.addButton(button("Back", ButtonStyle.DANGER, in -> onBack(in, userData)));

		fluffer10kFun.modularPromptUtils.addMessage(prompt, interaction);
	}

	private void onBack(final MessageComponentInteraction interaction, final ServerUserData userData) {
		fluffer10kFun.pagedMessageUtils.addMessage(makeList(userData), interaction);
	}

	private void sendMessageWithBackToDetails(final MessageComponentInteraction interaction,
			final ServerUserData userData, final HaremMemberData haremMember, final String title) {
		sendMessageWithBackToDetails(interaction, userData, haremMember, title, null, null);
	}

	private void sendMessageWithBackToDetails(final MessageComponentInteraction interaction,
			final ServerUserData userData, final HaremMemberData haremMember, final String title,
			final String description, final String imageUrl) {
		final ModularPrompt prompt = new ModularPrompt(makeEmbed(title, description, imageUrl), //
				button("Back", ButtonStyle.DANGER, interaction2 -> onPick(interaction2, userData, haremMember)));
		fluffer10kFun.modularPromptUtils.addMessage(prompt, interaction);
	}

	private void onGift(final MessageComponentInteraction interaction, final ServerUserData userData,
			final HaremMemberData haremMember) {
		final List<ItemAmount> data = userData.items.entrySet().stream()//
				.map(entry -> fluffer10kFun.items.getItemAmount(entry.getKey(), entry.getValue()))//
				.filter(itemAmount -> itemAmount.amount > 0 && itemAmount.item.classes.contains(ItemClass.GIFT))//
				.collect(toList());

		if (data.isEmpty()) {
			sendMessageWithBackToDetails(interaction, userData, haremMember, "You don't have any gifts");
			return;
		}

		data.sort(null);

		final List<EmbedField> fields = mapToList(data, itemAmount -> itemAmount.getAsFieldWithDescription());

		final PagedMessage msg = new PagedPickerMessageBuilder<>("Pick item to gift", 10, fields, data)//
				.onPick((in, page, itemAmount) -> onPickGift(in, userData, haremMember, itemAmount.item))//
				.build();
		fluffer10kFun.pagedMessageUtils.addMessage(msg, interaction);
	}

	private void onPickGift(final MessageComponentInteraction interaction, final ServerUserData userData,
			final HaremMemberData haremMember, final Item item) {
		if (!userData.hasItem(item)) {
			sendMessageWithBackToDetails(interaction, userData, haremMember, "You don't have this item anymore");
			return;
		}

		userData.addItem(item, -1);
		haremMember.addAffection(item.affectionBonus);

		sendMessageWithBackToDetails(interaction, userData, haremMember,
				"Your wife, " + haremMember.name + ", is very happy to receive the gift from you!", //
				"You get a kiss and her smile.", //
				haremMember.race.imageLink);
	}

	private void onRename(final MessageComponentInteraction interaction, final ServerUserData userData,
			final HaremMemberData haremMember) {
		final long channelId = getServerTextChannel(interaction).getId();
		final long userId = interaction.getUser().getId();
		fluffer10kFun.commandAnswer.addAnswerHandler(channelId, userId,
				(in, answer) -> onRenameAnswer(in, userData, haremMember, answer));

		final EmbedBuilder embed = makeEmbed("Pick new name for " + haremMember.name, "Use /answer to do that",
				haremMember.race.imageLink);
		final ModularPrompt prompt = new ModularPrompt(embed, //
				button("Back", ButtonStyle.DANGER, in -> {
					fluffer10kFun.commandAnswer.addAnswerHandler(channelId, userId, null);
					onPick(in, userData, haremMember);
				}));

		fluffer10kFun.modularPromptUtils.addMessage(prompt, interaction);
	}

	public void onRenameAnswer(final SlashCommandInteraction interaction, final ServerUserData userData,
			final HaremMemberData haremMember, final String newName) {
		final String oldName = haremMember.name;
		haremMember.name = newName;
		userData.harem.remove(oldName);
		userData.harem.put(newName, haremMember);

		interaction.createImmediateResponder().addEmbed(
				makeEmbed("You successfuly changed name of your wife", oldName + " will now be called " + newName))
				.respond();
	}

	private static final long marryPrice = 100_000;

	private void onMarry(final MessageComponentInteraction interaction, final ServerUserData userData,
			final HaremMemberData haremMember) {
		final String title = "Do you want to marry " + haremMember.name + "?";
		final String description = "Do you promise to love her and take care of her every day of your life, until death parts you (or not, in some cases)?\n"//
				+ "The fee is " + getFormattedMonies(marryPrice) + ".";

		final ModularPrompt prompt = new ModularPrompt(makeEmbed(title, description, haremMember.race.imageLink), //
				button("Yes", ButtonStyle.SUCCESS, interaction2 -> doMarry(interaction2, userData, haremMember)), //
				button("No", ButtonStyle.DANGER, interaction2 -> onPick(interaction2, userData, haremMember)));

		fluffer10kFun.modularPromptUtils.addMessage(prompt, interaction);
	}

	private void doMarry(final MessageComponentInteraction interaction, final ServerUserData userData,
			final HaremMemberData haremMember) {
		if (!haremMember.canBeMarried()) {
			String msg;
			if (haremMember.married) {
				msg = "You can't marry her twice, just give her a kiss and tell her how much you love her!";
			} else {
				msg = "You can't marry her yet!";
			}
			sendMessageWithBackToDetails(interaction, userData, haremMember, msg);
			return;
		}
		if (userData.monies < marryPrice) {
			sendMessageWithBackToDetails(interaction, userData, haremMember,
					"You don't have enough money, you need " + getFormattedMonies(marryPrice) + "!");
			return;
		}

		userData.monies -= marryPrice;
		haremMember.married = true;

		interaction.createOriginalMessageUpdater().addEmbed(makeEmbed("You married " + haremMember.name + "!",
				"May you be happy together!", haremMember.race.imageLink)).update();
	}

	private void onInteraction(final MessageComponentInteraction interaction, final ServerUserData userData,
			final HaremMemberData haremMember) {
		if (haremMember.desiredInteraction == null) {
			onPick(interaction, userData, haremMember);
			return;
		}

		final String title = "You " + haremMember.getFormattedInteraction() + " to strengthen your bonds";
		final String description = addBonusItem(fluffer10kFun.items, haremMember, userData);

		doInteraction(haremMember);

		sendMessageWithBackToDetails(interaction, userData, haremMember, title, description,
				haremMember.race.imageLink);
	}

	private void onReturnToMarket(final MessageComponentInteraction interaction, final ServerUserData userData,
			final HaremMemberData haremMember) {
		final String title = "Are you sure you want to return " + haremMember.getFullNameWithTypeFamily()
				+ haremMember.getMarriedString() + " to the market?";

		final ModularPrompt prompt = new ModularPrompt(haremMember.toEmbed().setTitle(title), //
				button("No", ButtonStyle.DANGER, interaction2 -> onPick(interaction2, userData, haremMember)), //
				button("Yes", ButtonStyle.SUCCESS,
						interaction2 -> doReturnToMarket(interaction2, userData, haremMember)));

		fluffer10kFun.modularPromptUtils.addMessage(prompt, interaction);
	}

	private long getPrice(final MonsterGirlRace race) {
		return fluffer10kFun.commandHarem.racePrices.get(race) * 4 / 5;
	}

	private void doReturnToMarket(final MessageComponentInteraction interaction, final ServerUserData userData,
			final HaremMemberData haremMember) {
		if (userData.harem.get(haremMember.name) != haremMember) {
			sendMessageWithBackToDetails(interaction, userData, haremMember,
					"This girl's data changed during picking, can't return");
			return;
		}

		final long price = getPrice(haremMember.race);
		final ServerData serverData = fluffer10kFun.botDataUtils.getServerData(interaction.getServer().get());
		addToLongOnMap(serverData.monmusuMarket, haremMember.race, 1);
		userData.harem.remove(haremMember.name);

		final String title = "you sell " + haremMember.getFullName() + " on the market";
		final String description = "She cries when you return her to the market\n"//
				+ "Hopefully she will find a loving husband soon...\n"//
				+ "\n"//
				+ "You get " + getFormattedMonies(price);

		interaction.createOriginalMessageUpdater().addEmbed(makeEmbed(title, description)).update();
	}
}
