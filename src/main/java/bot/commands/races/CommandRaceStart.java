package bot.commands.races;

import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.RandomUtils.getRandom;
import static bot.util.RandomUtils.getRandomLong;
import static bot.util.apis.MessageUtils.getServerTextChannel;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.Button;
import org.javacord.api.entity.message.component.ButtonStyle;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.MessageComponentInteraction;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.callback.ComponentInteractionOriginalMessageUpdater;

import bot.Fluffer10kFun;
import bot.userData.ServerUserData;
import bot.util.apis.APIUtils;
import bot.util.subcommand.Subcommand;

public class CommandRaceStart extends Subcommand {
	private final Map<Long, Map<Long, RaceData>> races = new HashMap<>();

	private RaceData addRace(final long serverId, final long userId, final ServerUserData userData) {
		Map<Long, RaceData> serverRaces = races.get(serverId);
		if (serverRaces == null) {
			serverRaces = new HashMap<>();
			races.put(serverId, serverRaces);
		}

		final RaceData race = new RaceData(userData);
		serverRaces.put(userId, race);
		userData.raceTimeout = System.currentTimeMillis() + 60 * 60 * 1000;
		return race;
	}

	private RaceData getRace(final long serverId, final long userId) {
		return races.getOrDefault(serverId, new HashMap<>()).get(userId);
	}

	private void removeRace(final long serverId, final long userId) {
		races.getOrDefault(serverId, new HashMap<>()).remove(userId);
	}

	private final Fluffer10kFun fluffer10kFun;

	protected CommandRaceStart(final Fluffer10kFun fluffer10kFun) {
		super("start", "Start a new race");

		this.fluffer10kFun = fluffer10kFun;

		fluffer10kFun.apiUtils.commandHandlers.addMessageComponentHandler("race", this::handleActionRace);
		fluffer10kFun.apiUtils.commandHandlers.addMessageComponentHandler("race_sponsor", this::handleActionSponsor);
	}

	private static ActionRow makeRaceButtons(final long userId) {
		return ActionRow.of(Button.create("race " + userId + " UP", ButtonStyle.PRIMARY, "🔼"), //
				Button.create("race " + userId + " DOWN", ButtonStyle.PRIMARY, "🔽"));
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {
		final Server server = interaction.getServer().get();
		final long userId = interaction.getUser().getId();
		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(server, userId);

		if (userData.raceTimeout >= System.currentTimeMillis()) {
			final RaceData race = getRace(server.getId(), userId);
			if (race != null) {
				sendEphemeralMessage(interaction, "You are already in a race!");
			} else {
				sendEphemeralMessage(interaction, "You need a break");
			}
			return;
		}

		final RaceData race = addRace(server.getId(), userId, userData);

		interaction.createImmediateResponder()//
				.addEmbed(race.toEmbed(APIUtils.getUserName(interaction.getUser(), server)))//
				.addComponents(makeRaceButtons(userId))//
				.respond();
	}

	private void endRace(final MessageComponentInteraction interaction, final RaceData race, final Server server,
			final long userId) {
		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(server.getId(), userId);

		if (race.position() <= 5) {
			userData.monies += race.getPlacePrize();
			if (userData.raceSponsor.sponsor != null) {
				final long increase = userData.sponsorBonusIncrease() * (6 - race.position()) / 5;
				userData.raceSponsor.bonus += getRandomLong(increase / 2, increase);
				final long maxBonus = userData.maxSponsorBonus();
				if (userData.raceSponsor.bonus > maxBonus) {
					userData.raceSponsor.bonus = maxBonus;
				}
			}
		}

		removeRace(server.getId(), userId);

		if (race.position() == 1) {
			sendNewSponsorMessage(interaction, userData);
		}
	}

	private void handleActionRace(final MessageComponentInteraction interaction) {
		final Server server = interaction.getServer().get();
		final long userId = interaction.getUser().getId();
		final String[] tokens = interaction.getCustomId().split(" ");
		if (userId != Long.valueOf(tokens[1])) {
			sendEphemeralMessage(interaction, "It's not your race!");
			return;
		}

		final RaceData race = getRace(server.getId(), userId);
		if (race == null) {
			interaction.createOriginalMessageUpdater()
					.append("This race was removed from server, probably due to restart, please start new race")
					.update();
			return;
		}

		race.changeGearAndUpdateStatus(tokens[2].equals("UP"));

		final ComponentInteractionOriginalMessageUpdater msgUpdater = interaction.createOriginalMessageUpdater()//
				.addEmbed(race.toEmbed(APIUtils.getUserName(interaction.getUser(), server)));
		if (!race.finished) {
			msgUpdater.addComponents(makeRaceButtons(userId)).update();
			return;
		}
		msgUpdater.update();

		endRace(interaction, race, server, userId);
	}

	private static ActionRow makeSponsorAcceptButtons(final long userId, final RaceSponsor newSponsor,
			final long newBonus) {
		return ActionRow.of(
				Button.create("race_sponsor " + userId + " ACCEPT " + newSponsor.name() + " " + newBonus,
						ButtonStyle.SUCCESS, "Accept"), //
				Button.create("race_sponsor " + userId + " DECLINE", ButtonStyle.DANGER, "Decline"));
	}

	private static void sendNewSponsorMessage(final MessageComponentInteraction interaction,
			final ServerUserData userData) {
		RaceSponsor newSponsor = getRandom(RaceSponsor.values());
		long newSponsorBonus;
		EmbedBuilder embed;
		if (userData.raceSponsor.sponsor == null) {
			newSponsorBonus = getRandomLong(1, 25);
			embed = makeEmbed("New sponsor appeared!",
					"It's " + newSponsor.name + " and offers you " + newSponsorBonus + " bonus every race.");
		} else {
			final RaceSponsor currentSponsor = userData.raceSponsor.sponsor;
			final long increase = userData.sponsorBonusIncrease() * 3;
			newSponsorBonus = userData.raceSponsor.bonus + getRandomLong(-increase, increase);
			final long maxBonus = userData.maxSponsorBonus();
			if (newSponsorBonus < 1) {
				newSponsorBonus = 1;
			} else if (newSponsorBonus > maxBonus) {
				newSponsorBonus = maxBonus;
			}
			while (newSponsor == currentSponsor) {
				newSponsor = getRandom(RaceSponsor.values());
			}

			final String description = "It's " + newSponsor.name + " and offers you " + newSponsorBonus
					+ " bonus every race.\nYour old sponsor is " + currentSponsor.name + " and gives you "
					+ userData.raceSponsor.bonus + " bonus coins every race.";
			embed = makeEmbed("New sponsor appeared!", description);
		}
		embed.setColor(newSponsor.color);

		new MessageBuilder().append(interaction.getUser())//
				.addEmbed(embed)//
				.addComponents(makeSponsorAcceptButtons(interaction.getUser().getId(), newSponsor, newSponsorBonus))//
				.send(getServerTextChannel(interaction));
	}

	private void handleActionSponsor(final MessageComponentInteraction interaction) {
		final long userId = interaction.getUser().getId();
		final String[] tokens = interaction.getCustomId().split(" ");
		if (Long.valueOf(tokens[1]) != userId) {
			sendEphemeralMessage(interaction, "It's not your choice!");
			return;
		}

		if (tokens[2].equals("DECLINE")) {
			interaction.createOriginalMessageUpdater().addEmbed(makeEmbed("Offer was declined").setColor(Color.RED))
					.update();
			return;
		}

		final ServerUserData userData = fluffer10kFun.serverUserDataUtils
				.getUserData(interaction.getServer().get().getId(), userId);
		userData.raceSponsor.sponsor = RaceSponsor.valueOf(tokens[3]);
		userData.raceSponsor.bonus = Long.valueOf(tokens[4]);

		final String description = "Your new sponsor is " + userData.raceSponsor.sponsor.name + " and will give you "
				+ userData.raceSponsor.bonus + " bonus every race";
		interaction.createOriginalMessageUpdater().addEmbed(makeEmbed("Offer was accepted", description)//
				.setColor(Color.GREEN))//
				.update();
	}
}
