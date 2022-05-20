package bot.userData;

import static bot.util.Utils.longFromNumber;

import java.util.HashMap;
import java.util.Map;

public class UserData {
	public long runExp = 0;
	public long catchExp = 0;
	public long fluffiness = 0;

	public boolean autoWait = true;

	public UserData() {
	}

	public UserData(final Map<String, Object> data) {
		runExp = longFromNumber(data.getOrDefault("runExp", 0));
		catchExp = longFromNumber(data.getOrDefault("catchExp", 0));
		fluffiness = longFromNumber(data.getOrDefault("fluffs", 0))
				+ longFromNumber(data.getOrDefault("fluffiness", 0));

		autoWait = (boolean) data.getOrDefault("autoWait", true);
	}

	public Map<String, Object> toMap() {
		final Map<String, Object> map = new HashMap<>();
		map.put("runExp", runExp);
		map.put("catchExp", catchExp);
		map.put("fluffiness", fluffiness);

		map.put("autoWait", autoWait);

		return map;
	}

	public void join(final UserData otherData) {
		autoWait &= otherData.autoWait;
	}

	public long getRunningLevel() {
		return runExp / 4 + 1;
	}

	public long getCatchingLevel() {
		return runExp / 2 + 1;
	}
}
