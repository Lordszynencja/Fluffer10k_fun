package bot.commands.rpg.harem;

import static bot.data.items.ItemUtils.formatNumber;
import static bot.data.items.ItemUtils.getFormattedMonies;
import static bot.util.CollectionUtils.addToIntOnMap;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.Utils.joinNames;
import static bot.util.modularPrompt.ModularPrompt.prompt;

import java.util.ArrayList;
import java.util.List;

import org.javacord.api.entity.message.component.ButtonStyle;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.MessageComponentInteraction;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.data.items.FurnitureType;
import bot.userData.ServerUserData;
import bot.util.modularPrompt.ModularPrompt;
import bot.util.modularPrompt.ModularPromptButton;
import bot.util.subcommand.Subcommand;

public class CommandHouseShow extends Subcommand {
	private final Fluffer10kFun fluffer10kFun;

	public CommandHouseShow(final Fluffer10kFun fluffer10kFun) {
		super("show", "Check your house");

		this.fluffer10kFun = fluffer10kFun;
	}

	private long getRoomPrice(final ServerUserData userData) {
		final int unchangeableHaremMembers = (int) userData.harem.values().stream()
				.filter(haremMember -> !haremMember.changeable).count();
		return 250_000 + 50_000 * (userData.getAdditionalRooms() - unchangeableHaremMembers);
	}

	private ModularPrompt makeMessage(final ServerUserData userData, final String userName) {
		final EmbedBuilder embed = makeEmbed(userName + "'s " + userData.house.name)//
				.addField("Max harem size", formatNumber(userData.getHouseSize()), true)//
				.addField("Value", getFormattedMonies(userData.house.price), true);

		final List<String> residents = new ArrayList<>();
		residents.add(userName);
		userData.getHaremMembersSorted().forEach(haremMember -> residents.add(haremMember.getFullName()));

		embed.addField("Current residents", joinNames(residents));

		final List<String> furniture = new ArrayList<>();
		for (final FurnitureType furnitureType : userData.getSortedFurniture()) {
			furniture.add(furnitureType.getName(userData.houseFurniture.get(furnitureType)));
		}
		if (!furniture.isEmpty()) {
			embed.addField("Special furniture", String.join("\n", furniture));
		}

		final long additionalRoomPrice = getRoomPrice(userData);
		final String expandLabel = "Buy additional room (" + getFormattedMonies(additionalRoomPrice) + ")";
		final boolean expandDisabled = userData.monies < additionalRoomPrice;
		final ModularPrompt prompt = prompt(embed, //
				ModularPromptButton.button(expandLabel, ButtonStyle.PRIMARY, in -> expand(in, userData, userName),
						expandDisabled));

		return prompt;
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {
		final Server server = interaction.getServer().get();
		final User user = interaction.getUser();
		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(interaction);
		final String userName = userData.rpg.getName(user, server);
		fluffer10kFun.modularPromptUtils.addMessage(makeMessage(userData, userName), interaction);
		fluffer10kFun.commandHarem.checkIfGirlsAreAngry(interaction);
	}

	private void expand(final MessageComponentInteraction in, final ServerUserData userData, final String userName) {
		final long roomPrice = getRoomPrice(userData);
		if (userData.monies >= roomPrice) {
			userData.monies -= roomPrice;
			addToIntOnMap(userData.houseFurniture, FurnitureType.ADDDITIONAL_ROOM, 1);
		}

		fluffer10kFun.modularPromptUtils.addMessage(makeMessage(userData, userName), in);
	}
}
