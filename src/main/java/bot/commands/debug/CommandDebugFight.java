package bot.commands.debug;

import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.Utils.Pair.pair;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;
import static java.util.stream.Collectors.joining;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.enemies.RPGEnemyData;
import bot.commands.rpg.fight.fightRewards.FightEndReward;
import bot.util.CollectionUtils.ValueFrom;
import bot.util.Utils.Pair;
import bot.util.subcommand.Subcommand;

public class CommandDebugFight extends Subcommand {

	private final Fluffer10kFun fluffer10kFun;

	public CommandDebugFight(final Fluffer10kFun fluffer10kFun) {
		super("fight", "start a fight with chosen enemy", //
				SlashCommandOption.create(SlashCommandOptionType.STRING, "enemyId", "enemy id", true), //
				SlashCommandOption.create(SlashCommandOptionType.USER, "user", "user"));

		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {
		final Server server = interaction.getServer().get();
		String enemyId = getOption(interaction).getOptionStringValueByName("enemyId").get();
		final User user = getOption(interaction).getOptionUserValueByName("user").orElse(interaction.getUser());

		if (enemyId.startsWith("WINS ")) {
			enemyId = enemyId.substring(5);
			if (enemyId.startsWith("RESET ")) {
				enemyId = enemyId.substring(6);
				fluffer10kFun.botDataUtils.winRates.remove(enemyId);
				sendEphemeralMessage(interaction, "win rates for " + enemyId + " removed");
				return;
			}

			final String enemyIdFinal = enemyId;
			final List<Pair<String, Map<Integer, Pair<Integer, Integer>>>> winRatesList = new ArrayList<>();
			fluffer10kFun.rpgEnemies.enemies.keySet().stream()//
					.filter(id -> id.startsWith(enemyIdFinal))//
					.forEach(id -> winRatesList.add(pair(id, fluffer10kFun.botDataUtils.winRates.get(id))));

			if (winRatesList.isEmpty()) {
				sendEphemeralMessage(interaction, "no win rates for " + enemyId);
				return;
			}

			final StringBuilder b = new StringBuilder("Win rates for " + enemyId);
			for (final Pair<String, Map<Integer, Pair<Integer, Integer>>> winRates : winRatesList) {
				if (winRates.b == null) {
					b.append("\nno win rates for " + winRates.a);
				} else {
					b.append("\n" + winRates.a + ":\n").append(winRates.b.entrySet().stream()//
							.sorted(new ValueFrom<>(entry -> entry.getKey()))//
							.map(entry -> "against level " + entry.getKey() + ": " //
									+ entry.getValue().a + " W " + entry.getValue().b + " L")
							.collect(joining("\n")));
				}
			}

			sendEphemeralMessage(interaction, b.toString());
			return;
		}
		if (enemyId.equals("NONE")) {
			fluffer10kFun.serverUserDataUtils.getUserData(server, user).rpg.fightId = null;

			interaction.createImmediateResponder()
					.addEmbed(makeEmbed("Fight cleared for " + user.getDisplayName(server))).respond();
			return;
		}

		FightEndReward reward = FightEndReward.DEFAULT;
		if (enemyId.startsWith("REWARD ")) {
			final String[] tokens = enemyId.split(" ");
			reward = FightEndReward.valueOf(tokens[1]);
			enemyId = tokens[2];
		}

		final RPGEnemyData enemy = fluffer10kFun.rpgEnemies.get(enemyId);

		interaction.createImmediateResponder()
				.addEmbed(makeEmbed("Fight", "You find " + enemy.name + " roaming the area!")).respond();

		final ServerTextChannel channel = interaction.getChannel().get().asServerTextChannel().get();
		fluffer10kFun.fightStart.startFightPvE(channel, user, enemy, reward);
	}
}
