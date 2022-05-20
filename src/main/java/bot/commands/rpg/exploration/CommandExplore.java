package bot.commands.rpg.exploration;

import static bot.util.RandomUtils.getRandomBoolean;
import static bot.util.apis.APIUtils.isTestServer;
import static bot.util.apis.MessageUtils.isNSFWChannel;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;

import java.util.HashMap;
import java.util.Map;

import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;

import bot.Fluffer10kFun;
import bot.data.ExplorationType;
import bot.userData.ServerUserData;
import bot.userData.UserBlessingData.Blessing;
import bot.util.subcommand.Command;

public class CommandExplore extends Command {
	public static interface ExplorationEventHandler {
		boolean handle(SlashCommandInteraction interaction, ServerUserData userData);
	}

	private final Fluffer10kFun fluffer10kFun;

	private final ExplorationFriendlyPerson explorationFriendlyPerson;
	private final ExplorationFight explorationFight;

	private final Map<ExplorationType, ExplorationEventHandler> eventHandlers = new HashMap<>();

	public CommandExplore(final Fluffer10kFun fluffer10kFun) {
		super(fluffer10kFun.apiUtils, "explore", "Explore the world", //
				SlashCommandOption.create(SlashCommandOptionType.STRING, "tracking_target",
						"target for tracking certain race of monster girls"));

		this.fluffer10kFun = fluffer10kFun;

		explorationFriendlyPerson = new ExplorationFriendlyPerson(fluffer10kFun);
		explorationFight = new ExplorationFight(fluffer10kFun);

		eventHandlers.put(ExplorationType.FIGHT, explorationFight);
		eventHandlers.put(ExplorationType.NOTHING, new ExplorationNothing());
		eventHandlers.put(ExplorationType.QUEST, new ExplorationQuest(fluffer10kFun));
		eventHandlers.put(ExplorationType.STASH, new ExplorationStash(fluffer10kFun));
		eventHandlers.put(ExplorationType.TRAVELING_MERCHANT, new ExplorationTravelingMerchant(fluffer10kFun));
	}

	private boolean validationPassed(final SlashCommandInteraction interaction) {
		if (!explorationFight.isValidTrackingTarget(explorationFight.getTrackingTarget(interaction))) {
			sendEphemeralMessage(interaction, "Invalid tracking target, must be one of monster girl races");
			return false;
		}

		return true;
	}

	private void reduceStamina(final ServerUserData userData, final ExplorationType explorationType) {
		if (explorationType == ExplorationType.NOTHING
				&& userData.blessings.blessingsObtained.contains(Blessing.UNSATISFIED_EXPLORER)) {
			return;
		}
		userData.reduceStamina(explorationType.staminaConsumption);
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {
		final Server server = interaction.getServer().orElse(null);
		if (server == null || !isNSFWChannel(interaction)) {
			sendEphemeralMessage(interaction, "This command cannot be used here");
			return;
		}

		if (!validationPassed(interaction)) {
			return;
		}

		final User user = interaction.getUser();
		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(server.getId(), user.getId());
		if (userData.rpg.fightId != null) {
			sendEphemeralMessage(interaction,
					"You are still in a fight (you can use **/fight refresh** to refresh it)");
			return;
		}
		final int stamina = userData.getStamina();
		if (stamina < 30 && !isTestServer(server)) {
			sendEphemeralMessage(interaction, "You are too tired to explore\n"//
					+ "you decide to rest for another " + (30 - stamina) + " minutes");
			return;
		}

		if (userData.blessings.blessingsObtained.contains(Blessing.UNDYING)) {
			userData.rpg.undyingAvailable = true;
		}

		if (getRandomBoolean(0.1 * (3 - userData.rpg.level))) {
			explorationFriendlyPerson.meetPerson(interaction, userData);
			return;
		}

		boolean actionSuccessful = false;
		ExplorationType explorationType;
		do {
			explorationType = userData.rpg.getRandomExploration();
			actionSuccessful = eventHandlers.get(explorationType).handle(interaction, userData);
		} while (!actionSuccessful);

		reduceStamina(userData, explorationType);
	}
}
