package bot.data;

import static bot.util.CollectionUtils.mapMapLong;
import static bot.util.CollectionUtils.mapMapString;
import static bot.util.FileUtils.readJSONFile;
import static bot.util.FileUtils.saveJSONFileWithBackup;
import static bot.util.TimerUtils.startRepeatedTimedEvent;
import static bot.util.Utils.intFromNumber;
import static bot.util.Utils.Pair.pair;
import static bot.util.apis.CommandHandlers.addOnExit;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toMap;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

import bot.Fluffer10kFun;
import bot.data.fight.FightData;
import bot.data.items.Item;
import bot.data.items.ItemBuilder;
import bot.util.Utils.Pair;
import bot.util.apis.APIUtils;
import bot.util.apis.MessageUtils;

public class BotDataUtils {
	private static final String botDataFilePath = "fluffer10kFun/botData.txt";

	private final Fluffer10kFun fluffer10kFun;

	public final BotDataReminders botDataReminders;

	public Map<Long, ServerData> serversData = new HashMap<>();

	public Map<Long, FightData> fights = new HashMap<>();
	private int nextFightId = 0;
	public Map<String, Map<Integer, Pair<Integer, Integer>>> winRates = new HashMap<>();

	public Map<String, Item> specialItems = new HashMap<>();
	private int nextSpecialItemId = 0;

	private void onExit() {
		saveData();
	}

	private void removeUnusedFights() {
		try {
			if (fluffer10kFun.serverUserDataUtils != null) {
				final Set<Long> fightIds = new HashSet<>(fights.keySet());

				fluffer10kFun.serverUserDataUtils.onEveryUser((serverId, userId, userData) -> {
					if (userData.rpg.fightId != null) {
						fightIds.remove(userData.rpg.fightId);
					}
				});
				fightIds.forEach(fights::remove);
			}
		} catch (final Exception e) {
			fluffer10kFun.apiUtils.messageUtils.sendExceptionToMe(e);
		}
	}

	public BotDataUtils(final Fluffer10kFun fluffer10kFun) throws IOException {
		this.fluffer10kFun = fluffer10kFun;

		botDataReminders = new BotDataReminders(fluffer10kFun);

		loadData();

		startRepeatedTimedEvent(this::saveData, 5 * 60, 0, "saving bot data");
		startRepeatedTimedEvent(this::removeUnusedFights, 60 * 60, 0, "removing unused fights");

		addOnExit(this::onExit);

		saveData();
	}

	private Map<Integer, Pair<Integer, Integer>> enemyLevelWinRatesfromData(final Map<String, List<Number>> data) {
		return data.entrySet().stream()//
				.map(entry -> pair(Integer.valueOf(entry.getKey()),
						pair(intFromNumber(entry.getValue().get(0)), intFromNumber(entry.getValue().get(1)))))//
				.collect(toMap(pair -> pair.a, pair -> pair.b));
	}

	private Map<String, Map<Integer, Pair<Integer, Integer>>> enemyWinRatesfromData(
			final Map<String, Map<String, List<Number>>> data) {
		return data.entrySet().stream()//
				.map(entry -> pair(entry.getKey(), enemyLevelWinRatesfromData(entry.getValue())))
				.collect(toMap(entry -> entry.a, entry -> entry.b));
	}

	@SuppressWarnings("unchecked")
	public void loadData() throws IOException {
		if (!new File(botDataFilePath).exists()) {
			return;
		}

		final Map<String, Object> data = readJSONFile(botDataFilePath);

		serversData = mapMapLong((Map<String, Object>) data.get("serversData"),
				(serverData -> new ServerData((Map<String, Object>) serverData)));
		fights = new HashMap<>();
		((Map<String, Map<String, Object>>) data.getOrDefault("fights", new HashMap<>()))
				.forEach((fightId, fightData) -> {
					final Long id = Long.valueOf(fightId);
					fights.put(id, new FightData((Map<String, Object>) fightData, id));
				});
		winRates = enemyWinRatesfromData(
				(Map<String, Map<String, List<Number>>>) data.getOrDefault("winRates", new HashMap<>()));
		nextFightId = intFromNumber(data.getOrDefault("nextFightId", 0));

		((Map<String, Object>) data.getOrDefault("specialItems", new HashMap<>())).forEach((itemId, itemData) -> {
			if (itemData == null) {
				throw new RuntimeException("Unknown special item id with no data" + itemId);
			} else {
				specialItems.put(itemId, new Item((Map<String, Object>) itemData));
			}
		});
		nextSpecialItemId = intFromNumber(data.getOrDefault("nextSpecialItemId", 0));

		botDataReminders.fromData((Map<String, List<List<Object>>>) data.getOrDefault("reminders", new HashMap<>()));
	}

	public void saveData() {
		try {
			final Map<String, Object> data = new HashMap<>();

			data.put("serversData", mapMapString(serversData, serverData -> serverData.toMap()));

			data.put("fights", mapMapString(fights, fightData -> fightData.toMap()));
			data.put("winRates", mapMapString(winRates, //
					enemyWinRates -> mapMapString(enemyWinRates, //
							enemyLevelWinRates -> asList(enemyLevelWinRates.a, enemyLevelWinRates.b))));
			data.put("nextFightId", nextFightId);

			data.put("specialItems", mapMapString(specialItems, item -> item.toMap()));
			data.put("nextSpecialItemId", nextSpecialItemId);

			data.put("reminders", botDataReminders.toData());

			try {
				saveJSONFileWithBackup(botDataFilePath, data);
			} catch (final IOException e) {
				fluffer10kFun.apiUtils.messageUtils.sendExceptionToMe(e);
			}
		} catch (final Exception e) {
			fluffer10kFun.apiUtils.messageUtils.sendExceptionToMe(e);
		}
	}

	public void addFight(final FightData fightData) {
		final long id = nextFightId++;
		fightData.id = id;
		fights.put(id, fightData);
	}

	public void addFightResult(final String id, final int level, final boolean won) {
		Map<Integer, Pair<Integer, Integer>> enemyWinRates = winRates.get(id);
		if (enemyWinRates == null) {
			enemyWinRates = new HashMap<>();
			winRates.put(id, enemyWinRates);
		}

		Pair<Integer, Integer> enemyLevelWinRates = enemyWinRates.get(level);
		if (enemyLevelWinRates == null) {
			enemyLevelWinRates = pair(0, 0);
			enemyWinRates.put(level, enemyLevelWinRates);
		}

		if (won) {
			enemyLevelWinRates.a++;
		} else {
			enemyLevelWinRates.b++;
		}
	}

	public Item addItem(final ItemBuilder itemBuilder) {
		final String id = "SPECIAL_ITEM_" + nextSpecialItemId++;
		final Item item = itemBuilder.id(id).build();
		specialItems.put(id, item);

		return item;
	}

	public boolean isUserMod(final User user, final Server server) {
		final ServerData serverData = getServerData(server.getId());
		if (serverData.modRoleId == null) {
			return false;
		}

		for (final Role r : user.getRoles(server)) {
			if (r.getId() == serverData.modRoleId) {
				return true;
			}
		}

		return false;
	}

	public CompletableFuture<Message> sendMessageOnServerBotChannel(final APIUtils apiUtils, final long serverId,
			final MessageBuilder msg) {
		return getServerData(serverId).sendMessageOnBotChannel(serverId, apiUtils, msg);
	}

	public ServerData getServerData(final long serverId) {
		ServerData serverData = serversData.get(serverId);
		if (serverData == null) {
			serverData = new ServerData();
			serversData.put(serverId, serverData);
		}

		return serverData;
	}

	public ServerData getServerData(final Server server) {
		return getServerData(server.getId());
	}

	public void forEachServer(final BiConsumer<Long, ServerData> action, final MessageUtils messageUtils) {
		serversData.forEach((serverId, serverData) -> {
			try {
				final Optional<Server> serverOption = fluffer10kFun.apiUtils.api.getServerById(serverId);
				if (!serverOption.isPresent()) {
					return;
				}

				if (fluffer10kFun.apiUtils.isServerOk(serverId)) {
					action.accept(serverId, serverData);
				}
			} catch (final Exception e) {
				messageUtils.sendExceptionToMe("Exception thrown for server " + serverId, e);
			}
		});
	}
}
