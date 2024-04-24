package bot.commands.rpg.harem;

import static bot.data.items.Items.getName;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.RandomUtils.getRandom;
import static bot.util.RandomUtils.getRandomBoolean;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;
import static java.util.Arrays.asList;

import java.util.List;

import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;

import bot.Fluffer10kFun;
import bot.data.items.Item;
import bot.data.items.Items;
import bot.data.items.data.MonmusuDropItems;
import bot.data.items.loot.MonsterGirlRaceLoot;
import bot.userData.HaremMemberData;
import bot.userData.ServerUserData;
import bot.util.subcommand.Subcommand;

public class CommandHaremDo extends Subcommand {
	private static final List<String> defaultProducts = asList(//
			MonmusuDropItems.COUPLES_FRUIT, //
			MonmusuDropItems.INTOXICATION_FRUIT, //
			MonmusuDropItems.PRISONER_FRUIT, //
			MonmusuDropItems.RAGING_MUSHROOM, //
			MonmusuDropItems.STICKY_MUSHROOM, //
			MonmusuDropItems.SUCCUBUS_NOSTRUM);

	private final Fluffer10kFun fluffer10kFun;

	public CommandHaremDo(final Fluffer10kFun fluffer10kFun) {
		super("do", "Do something with one of your wives",
				SlashCommandOption.create(SlashCommandOptionType.STRING, "action", "Action to do in your harem", true));

		this.fluffer10kFun = fluffer10kFun;
	}

	public static String addBonusItem(final Items items, final HaremMemberData haremMember,
			final ServerUserData userData) {
		List<String> productList = MonsterGirlRaceLoot.racesProducts.get(haremMember.race);
		if (productList == null || getRandomBoolean(0.5)) {
			productList = defaultProducts;
		}

		final Item item = items.getItem(getRandom(productList));
		double chance = haremMember.affection * 2.5 / item.price;
		long amount = 0;
		while (chance > 1) {
			chance--;
			amount++;
		}
		if (getRandomBoolean(chance)) {
			amount++;
		}

		if (amount > 0) {
			userData.addItem(item.id, amount);
			return "You also got " + getName(item, amount) + " from her.";
		}

		return null;
	}

	public static void doInteraction(final HaremMemberData haremMember) {
		haremMember.addAffection(7);
		haremMember.desiredInteraction = null;
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {
		final Server server = interaction.getServer().get();
		final User user = interaction.getUser();
		final String action = getOption(interaction).getArgumentStringValueByName("action").get().trim();
		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(server.getId(), user.getId());

		for (final HaremMemberData haremMember : userData.harem.values()) {
			if (haremMember.checkIfInteractionIsValid(action)) {
				final String title = "You " + haremMember.getFormattedInteraction() + " to strengthen your bonds";
				final String description = addBonusItem(fluffer10kFun.items, haremMember, userData);

				doInteraction(haremMember);

				interaction.createImmediateResponder()
						.addEmbed(makeEmbed(title, description, haremMember.race.imageLink)).respond();
				return;
			}
		}

		sendEphemeralMessage(interaction, "Wrong interaction, remember to check casing and put your wife's name in");
	}
}
