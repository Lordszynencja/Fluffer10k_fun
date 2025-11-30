package bot.userData;

import static bot.util.FileUtils.readJSONFile;
import static bot.util.FileUtils.saveJSONFileWithBackup;
import static bot.util.TimerUtils.startRepeatedTimedEvent;
import static bot.util.apis.CommandHandlers.addOnExit;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.data.fight.PlayerFighterData;

public class ServerUserDataUtils {
	public static interface OnEveryUserAction {
		void handle(long serverId, long userId, ServerUserData userData);
	}

	private final Fluffer10kFun fluffer10kFun;

	private static final String serverUsersDataFilePath = "fluffer10kFun/serverUsersData.txt";
	private static final String specialFilePath = "fluffer10kFun/serverUsersDataSpecial.txt";

	private Map<Long, Map<Long, ServerUserData>> serverUsersData = new HashMap<>();

	private void onExit() {
		saveData();
	}

	public ServerUserDataUtils(final Fluffer10kFun fluffer10kFun) throws IOException {
		this.fluffer10kFun = fluffer10kFun;

		loadData();
		loadDataSpecial();
		deleteSpecial();

		startRepeatedTimedEvent(this::saveData, 5 * 60, 0, "saving server user data");

		addOnExit(this::onExit);
	}

	@SuppressWarnings("unchecked")
	public void loadData() throws IOException {
		if (!new File(serverUsersDataFilePath).exists()) {
			return;
		}

		final Map<String, Object> data = readJSONFile(serverUsersDataFilePath);

		serverUsersData = new HashMap<>();
		data.forEach((serverIds, serverData) -> {
			final long serverId = Long.valueOf(serverIds);
			final Map<Long, ServerUserData> serverDataOut = new HashMap<>();

			((Map<String, Object>) serverData).forEach((userIds, serverUserData) -> {
				final long userId = Long.valueOf(userIds);
				serverDataOut.put(userId, new ServerUserData(serverId, userId, (Map<String, Object>) serverUserData));
			});

			serverUsersData.put(serverId, serverDataOut);
		});
	}

	@SuppressWarnings("unchecked")
	private void loadDataSpecial() throws IOException {
		if (!new File(specialFilePath).exists()) {
			return;
		}

		final Map<String, Object> data = readJSONFile(specialFilePath);
		data.forEach((serverIds, serverSpecialData) -> {
			final long serverId = Long.valueOf(serverIds);
			final Map<Long, ServerUserData> serverData = getServerData(serverId);

			((Map<String, Object>) serverSpecialData).forEach((userIds, serverUserData) -> {
				final long userId = Long.valueOf(userIds);
				serverData.put(userId, new ServerUserData(serverId, userId, (Map<String, Object>) serverUserData));
			});
		});
	}

	private void deleteSpecial() {
		final File specialFile = new File(specialFilePath);
		if (!specialFile.exists()) {
			return;
		}

		specialFile.delete();
	}

	private void saveData() {
		try {
			final Map<String, Object> data = new HashMap<>();

			serverUsersData.forEach((serverId, serverData) -> {
				final Map<String, Object> serverDataOut = new HashMap<>();
				data.put(serverId.toString(), serverDataOut);
				serverData.forEach(
						(userId, serverUserData) -> { serverDataOut.put(userId.toString(), serverUserData.toMap()); });
			});

			try {
				if (!fluffer10kFun.apiUtils.config.getBoolean("debug")) {
					saveJSONFileWithBackup(serverUsersDataFilePath, data);
				}
			} catch (final IOException e) {
				e.printStackTrace();
				fluffer10kFun.apiUtils.messageUtils.sendExceptionToMe(e);
			}
		} catch (final Exception e) {
			fluffer10kFun.apiUtils.messageUtils.sendExceptionToMe(e);
		}
	}

	private Map<Long, ServerUserData> getServerData(final long serverId) {
		Map<Long, ServerUserData> serverData = serverUsersData.get(serverId);
		if (serverData == null) {
			serverData = new HashMap<>();
			serverUsersData.put(serverId, serverData);
		}

		return serverData;
	}

	public ServerUserData getUserData(final long serverId, final long userId) {
		final Map<Long, ServerUserData> serverData = getServerData(serverId);

		ServerUserData serverUserData = serverData.get(userId);
		if (serverUserData == null) {
			serverUserData = new ServerUserData(serverId, userId);
			serverData.put(userId, serverUserData);
		}

		return serverUserData;
	}

	public ServerUserData getUserData(final long serverId, final User user) {
		return getUserData(serverId, user.getId());
	}

	public ServerUserData getUserData(final Server server, final long userId) {
		return getUserData(server.getId(), userId);
	}

	public ServerUserData getUserData(final Server server, final User user) {
		return getUserData(server.getId(), user.getId());
	}

	public ServerUserData getUserData(final PlayerFighterData fighter) {
		return getUserData(fighter.fight.serverId, fighter.userId);
	}

	public ServerUserData getUserData(final SlashCommandInteraction interaction) {
		return getUserData(interaction.getServer().get(), interaction.getUser());
	}

	public void onEveryUser(final OnEveryUserAction action) {
		serverUsersData.forEach((serverId, serversData) -> {
			if (fluffer10kFun.apiUtils.isServerOk(serverId)) {
				serversData.forEach((userId, userData) -> action.handle(serverId, userId, userData));
			}
		});
	}
}
