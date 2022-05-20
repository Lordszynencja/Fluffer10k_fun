package bot.commands.casino;

import static bot.data.items.ItemUtils.getFormattedPlayCoins;
import static bot.data.items.ItemUtils.playCoinsName;
import static bot.util.RandomUtils.getRandom;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.Button;
import org.javacord.api.entity.message.component.ButtonStyle;
import org.javacord.api.entity.message.component.LowLevelComponent;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.MessageComponentInteraction;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandInteractionOption;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;
import org.javacord.api.interaction.callback.ComponentInteractionOriginalMessageUpdater;
import org.javacord.api.interaction.callback.InteractionImmediateResponseBuilder;

import bot.Fluffer10kFun;
import bot.userData.ServerUserData;
import bot.util.EmbedUtils;

public class CasinoBlackjack {
	private static final String[] cardRanks = { "A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K" };
	private static final String[] cardSuits = { "♥", "♦", "♣", "♠" };

	private static final Map<String, Integer> blackjackRankPointValues = new HashMap<>();
	static {
		blackjackRankPointValues.put("A", 1);
		blackjackRankPointValues.put("2", 2);
		blackjackRankPointValues.put("3", 3);
		blackjackRankPointValues.put("4", 4);
		blackjackRankPointValues.put("5", 5);
		blackjackRankPointValues.put("6", 6);
		blackjackRankPointValues.put("7", 7);
		blackjackRankPointValues.put("8", 8);
		blackjackRankPointValues.put("9", 9);
		blackjackRankPointValues.put("10", 10);
		blackjackRankPointValues.put("J", 10);
		blackjackRankPointValues.put("Q", 10);
		blackjackRankPointValues.put("K", 10);
	}

	private static class BlackjackCard {
		public static BlackjackCard random() {
			return new BlackjackCard(getRandom(cardRanks), getRandom(cardSuits));
		}

		public final String name;
		public final int value;

		public BlackjackCard(final String rank, final String suit) {
			name = rank + suit;
			value = blackjackRankPointValues.get(rank);
		}
	}

	private static class BlackjackCards {
		private final List<BlackjackCard> cards = new ArrayList<>();

		public void addCard() {
			cards.add(BlackjackCard.random());
		}

		public int points() {
			int sum = 0;
			for (final BlackjackCard card : cards) {
				sum += card.value;
			}
			return sum;
		}

		@Override
		public String toString() {
			int sum = 0;
			for (final BlackjackCard card : cards) {
				sum += card.value;
			}
			final String cardsString = cards.stream().map(card -> card.name).collect(joining(", "));
			return cardsString + " (" + sum + ")";
		}
	}

	public static enum GameStage {
		PLAYER_TURN("Player's turn"), //
		CASINO_TURN("Casino's turn"), //
		LOST("Game lost!"), //
		TIED("Game tied!"), //
		WON("Game won!");

		public final String description;

		private GameStage(final String description) {
			this.description = description;
		}
	}

	public static enum Action {
		HIT("Hit"), //
		DOUBLE("Double"), //
		STAND("Stand");

		public final String moveName;

		private Action(final String moveName) {
			this.moveName = moveName;
		}
	}

	public static SlashCommandOption getCommandOption() {
		return SlashCommandOption.createWithOptions(SlashCommandOptionType.SUB_COMMAND, "blackjack", "Play Blackjack",
				asList(SlashCommandOption.create(SlashCommandOptionType.LONG, "bet",
						"Amount of " + playCoinsName + " to bet", true)));
	}

	private class BlackjackGame {
		private final BlackjackCards playerCards = new BlackjackCards();
		private final BlackjackCards casinoCards = new BlackjackCards();
		private GameStage stage = GameStage.PLAYER_TURN;
		private long bet;
		private final long serverId;
		private final long userId;

		public BlackjackGame(final long bet, final long serverId, final long userId) {
			playerCards.addCard();
			playerCards.addCard();
			this.bet = bet;
			this.serverId = serverId;
			this.userId = userId;
		}

		private void clearGame() {
			games.get(serverId).put(userId, null);
		}

		private void loseGame(final MessageComponentInteraction interaction) {
			stage = GameStage.LOST;
			fluffer10kFun.botDataUtils.getServerData(serverId).addJackpotStake(bet / 2);
			clearGame();
			sendInfo(interaction);
		}

		private void tieGame(final MessageComponentInteraction interaction) {
			stage = GameStage.TIED;
			final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(serverId, userId);
			userData.playCoins += bet;
			clearGame();
			sendInfo(interaction);
		}

		private void winGame(final MessageComponentInteraction interaction) {
			stage = GameStage.WON;
			final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(serverId, userId);
			userData.playCoins += 2 * bet;
			clearGame();
			sendInfo(interaction);
		}

		public void action(final MessageComponentInteraction interaction, final Action action) {
			if (stage != GameStage.PLAYER_TURN) {
				return;
			}

			if (action == Action.DOUBLE) {
				final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(serverId, userId);
				if (userData.playCoins < bet) {
					return;
				}
				userData.playCoins -= bet;
				bet *= 2;
			}

			if (action == Action.HIT || action == Action.DOUBLE) {
				playerCards.addCard();
			}

			if (playerCards.points() > 21) {
				loseGame(interaction);
				return;
			}

			if (action == Action.HIT && playerCards.points() < 21) {
				sendInfo(interaction);
				return;
			}
			stage = GameStage.CASINO_TURN;

			final int playerPoints = playerCards.points();
			while (stage == GameStage.CASINO_TURN) {
				casinoCards.addCard();

				final int casinoPoints = casinoCards.points();
				if (casinoPoints > 21) {
					winGame(interaction);
					return;
				}

				if (casinoPoints > playerPoints) {
					loseGame(interaction);
					return;
				}

				if (casinoPoints >= 17) {
					if (casinoPoints == playerPoints) {
						tieGame(interaction);
						return;
					}
					winGame(interaction);
					return;
				}
			}
		}

		public EmbedBuilder makeEmbed() {
			final String description = "bet: " + getFormattedPlayCoins(bet) + "\n" + stage.description;
			final EmbedBuilder embed = EmbedUtils.makeEmbed("Blackjack", description)//
					.addField("Player's cards", playerCards.toString(), false);
			if (stage != GameStage.PLAYER_TURN) {
				embed.addField("Casino's cards", casinoCards.toString(), false);
			}

			return embed;
		}

		public ActionRow makeActionRow(final long userId) {
			final List<LowLevelComponent> buttons = new ArrayList<>();
			for (final Action action : Action.values()) {
				buttons.add(Button.create("casino_blackjack " + action.name() + " " + userId, ButtonStyle.PRIMARY,
						action.moveName));
			}
			return ActionRow.of(buttons);
		}

		private void sendInfo(final MessageComponentInteraction interaction) {
			final ComponentInteractionOriginalMessageUpdater updater = interaction.createOriginalMessageUpdater();

			updater.addEmbed(makeEmbed());

			if (stage == GameStage.PLAYER_TURN) {
				updater.addComponents(makeActionRow(interaction.getUser().getId()));
			}

			updater.update();
		}
	}

	private final Fluffer10kFun fluffer10kFun;
	private final Map<Long, Map<Long, BlackjackGame>> games = new HashMap<>();

	public CasinoBlackjack(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;
	}

	public void handleAction(final MessageComponentInteraction interaction) {
		final String[] tokens = interaction.getCustomId().split(" ");
		final long userId = interaction.getUser().getId();
		final long userIdCorrect = Long.valueOf(tokens[2]);
		if (userId != userIdCorrect) {
			sendEphemeralMessage(interaction, "It's not your game!");
			return;
		}

		final Action action = Action.valueOf(tokens[1]);
		final BlackjackGame game = games.get(interaction.getServer().get().getId()).get(interaction.getUser().getId());
		game.action(interaction, action);
	}

	public void handle(final SlashCommandInteraction interaction) {
		final long serverId = interaction.getServer().get().getId();
		final long userId = interaction.getUser().getId();

		final SlashCommandInteractionOption firstOption = interaction.getOptionByName("blackjack").get();
		final long bet = firstOption.getOptionLongValueByName("bet").get();
		if (bet < 1) {
			sendEphemeralMessage(interaction, "Can't place negative or no bet");
			return;
		}
		if (bet > CommandCasino.maxBet) {
			sendEphemeralMessage(interaction, "Max bet is " + CommandCasino.maxBet);
			return;
		}

		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(serverId, userId);
		if (userData.playCoins < bet) {
			sendEphemeralMessage(interaction, "You dont have enough " + playCoinsName);
			return;
		}

		Map<Long, BlackjackGame> serverGames = games.get(serverId);
		if (serverGames == null) {
			serverGames = new HashMap<>();
			games.put(serverId, serverGames);
		}
		if (serverGames.get(userId) != null) {
			sendEphemeralMessage(interaction, "You already play Blackjack");
			return;
		}

		userData.playCoins -= bet;
		final BlackjackGame game = new BlackjackGame(bet, serverId, userId);
		serverGames.put(userId, game);
		final InteractionImmediateResponseBuilder responder = interaction.createImmediateResponder()
				.addEmbed(game.makeEmbed());
		responder.addComponents(game.makeActionRow(userId)).respond();
	}
}
