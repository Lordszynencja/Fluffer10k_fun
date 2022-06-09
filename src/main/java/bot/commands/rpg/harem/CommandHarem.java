package bot.commands.rpg.harem;

import static bot.util.CollectionUtils.addToLongOnMap;
import static bot.util.RandomUtils.getRandom;
import static bot.util.RandomUtils.getRandomBoolean;
import static bot.util.RandomUtils.getRandomInt;
import static bot.util.RandomUtils.getRandomLongWithParts;
import static bot.util.RandomUtils.getSizeWithChance;
import static bot.util.TimerUtils.startRepeatedTimedEvent;
import static bot.util.Utils.joinNames;
import static bot.util.apis.MessageUtils.isNSFWChannel;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.ServerData;
import bot.data.items.FurnitureType;
import bot.userData.HaremMemberData;
import bot.userData.HaremMemberData.HaremMemberInteraction;
import bot.userData.ServerUserData;
import bot.util.subcommand.Command;

public class CommandHarem extends Command {
	private final static long defaultMgPrice = 10_000;

	public final Map<MonsterGirlRace, Long> racePrices = new HashMap<>();

	private final Fluffer10kFun fluffer10kFun;

	private void updateRacePrices() {
		for (final MonsterGirlRace race : MonsterGirlRace.values()) {
			racePrices.put(race, getRandomLongWithParts(defaultMgPrice, 5));
		}
	}

	private static void changeAffectionInUserHarem(final long serverId, final long userId,
			final ServerUserData userData) {
		final boolean hasSleepfulBedding = userData.houseFurniture.getOrDefault(FurnitureType.SLEEPFUL_BEDDING, 0) > 0;
		userData.harem.values().forEach(haremMember -> {
			double affectionChange = 1;
			if (hasSleepfulBedding) {
				affectionChange *= 0.5;
			}
			haremMember.affection -= affectionChange;
			haremMember.desiredInteraction = getRandom(HaremMemberInteraction.values());
		});
	}

	private void changeAffectionInHarems() {
		fluffer10kFun.serverUserDataUtils.onEveryUser(CommandHarem::changeAffectionInUserHarem);
	}

	private static void changeMarketGirlsRandomlyOnServer(final long serverId, final ServerData serverData) {
		for (final MonsterGirlRace race : serverData.monmusuMarket.keySet()) {
			if (getRandomBoolean(0.01 * serverData.monmusuMarket.get(race))) {
				addToLongOnMap(serverData.monmusuMarket, race, -1);
			}
		}

		final int newGirls = getSizeWithChance(1, 0.5);
		for (int i = 0; i < newGirls; i++) {
			addToLongOnMap(serverData.monmusuMarket, getRandom(MonsterGirlRace.values()), 1);
		}
	}

	public static void populateMarketOnNewServer(final ServerData serverData) {
		final int newGirls = getSizeWithChance(10, 0.5);
		for (int i = 0; i < newGirls; i++) {
			addToLongOnMap(serverData.monmusuMarket, getRandom(MonsterGirlRace.values()), 1);
		}
	}

	private void changeMarketGirlsRandomly() {
		fluffer10kFun.botDataUtils.forEachServer(CommandHarem::changeMarketGirlsRandomlyOnServer,
				fluffer10kFun.apiUtils.messageUtils);
	}

	private void runDailyChanges() {
		try {
			updateRacePrices();
			changeAffectionInHarems();
			changeMarketGirlsRandomly();
		} catch (final Exception e) {
			fluffer10kFun.apiUtils.messageUtils.sendExceptionToMe(e);
		}
	}

	public CommandHarem(final Fluffer10kFun fluffer10kFun) {
		super(fluffer10kFun.apiUtils, "harem", "Harem commands", //
				new CommandHaremDo(fluffer10kFun), //
				new CommandHaremList(fluffer10kFun), //
				new CommandHaremMarket(fluffer10kFun));

		this.fluffer10kFun = fluffer10kFun;

		startRepeatedTimedEvent(this::runDailyChanges, 24 * 60 * 60, 24 * 60 * 60,
				"changes prices of monster girls on market and reduces affection of wives");

		updateRacePrices();
	}

	public void checkIfGirlsAreAngry(final SlashCommandInteraction interaction) {
		final Server server = interaction.getServer().get();
		final User user = interaction.getUser();
		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(server.getId(), user.getId());

		int totalCums = 0;
		final List<String> angryWifeNames = new ArrayList<>();
		for (final HaremMemberData haremMember : userData.harem.values()) {
			if (haremMember.affection < 0) {
				int newCums = (int) (-haremMember.affection * 5) + getRandomInt(5);
				if (haremMember.married) {
					newCums = newCums * 2 + getRandomInt(10);
				}
				totalCums += newCums;
				angryWifeNames.add(haremMember.name);
				haremMember.affection = 0;
			}
		}

		if (!angryWifeNames.isEmpty()) {
			final ServerTextChannel channel = interaction.getChannel().get().asServerTextChannel().get();
			if (angryWifeNames.size() == 1) {
				fluffer10kFun.commandMgLove.addCums(server, user, totalCums);
				channel.sendMessage(
						fluffer10kFun.commandMgLove.makeLovedByEmbed(server, user, totalCums, angryWifeNames.get(0)));
			} else {
				final EmbedBuilder embed = fluffer10kFun.commandMgLove.makeLovedByEmbed(server, user, totalCums,
						"multiple wives");
				embed.setDescription("The angry wives were " + joinNames(angryWifeNames) + ".");
				channel.sendMessage(embed);
			}
		}
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) throws Exception {
		final Server server = interaction.getServer().orElse(null);
		if (!isNSFWChannel(interaction) || server == null) {
			sendEphemeralMessage(interaction, "This command cannot be used here");
			return;
		}

		subcommandHandler.handle(interaction);
		checkIfGirlsAreAngry(interaction);
	}
}
