package bot.userData;

import static bot.util.FileUtils.readJSONFile;
import static bot.util.FileUtils.saveJSONFileWithBackup;
import static bot.util.TimerUtils.startRepeatedTimedEvent;
import static bot.util.apis.CommandHandlers.addOnExit;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import bot.Fluffer10kFun;

public class UserDataUtils {
	private final Fluffer10kFun fluffer10kFun;

	private static String usersDataFilePath = "fluffer10kFun/usersData.txt";

	private Map<Long, UserData> usersData = new HashMap<>();

	private void onExit() {
		saveData();
	}

	public UserDataUtils(final Fluffer10kFun fluffer10kFun) throws IOException {
		this.fluffer10kFun = fluffer10kFun;

		loadData();

		startRepeatedTimedEvent(this::saveData, 5 * 60, 0, "saving user data");

		addOnExit("saving user data", this::onExit);
	}

	@SuppressWarnings("unchecked")
	public void loadData() throws IOException {
		if (!new File(usersDataFilePath).exists()) {
			return;
		}

		final Map<String, Object> data = readJSONFile(usersDataFilePath);

		usersData = new HashMap<>();
		for (final Entry<String, Object> entry : data.entrySet()) {
			usersData.put(Long.valueOf(entry.getKey()), new UserData((Map<String, Object>) entry.getValue()));
		}
	}

	public void saveData() {
		final Map<String, Object> data = new HashMap<>();
		for (final Entry<Long, UserData> userData : usersData.entrySet()) {
			data.put(userData.getKey().toString(), userData.getValue().toMap());
		}
		try {
			if (!fluffer10kFun.apiUtils.config.getBoolean("debug")) {
				saveJSONFileWithBackup(usersDataFilePath, data);
			}
		} catch (final IOException e) {
			e.printStackTrace();
			fluffer10kFun.apiUtils.messageUtils.sendExceptionToMe(e);
		}
	}

	public UserData getUserData(final long userId) {
		if (!usersData.containsKey(userId)) {
			usersData.put(userId, new UserData());
		}

		return usersData.get(userId);
	}

}
