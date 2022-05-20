package bot.userData;

import java.util.HashMap;
import java.util.Map;

import bot.commands.cookies.CookieUpgrade;
import bot.util.CollectionUtils;
import bot.util.Utils;

public class UserCookiesData {

	public Map<String, Long> cookieCounts = new HashMap<>();
	public Map<String, Long> milkCounts = new HashMap<>();
	public Map<CookieUpgrade, Long> upgrades = new HashMap<>();
	public long crumbles = 0;
	public long autoBakeTimeout = 0;

	public UserCookiesData() {
	}

	@SuppressWarnings("unchecked")
	public UserCookiesData(final Map<String, Object> data) {
		cookieCounts = CollectionUtils.mapMapString(
				(Map<String, Number>) data.getOrDefault("cookieCounts", new HashMap<>()),
				lvl -> Utils.longFromNumber(lvl));
		milkCounts = CollectionUtils.mapMapString(
				(Map<String, Number>) data.getOrDefault("milkCounts", new HashMap<>()),
				lvl -> Utils.longFromNumber(lvl));
		upgrades = CollectionUtils.mapMap((Map<String, Number>) data.getOrDefault("upgrades", new HashMap<>()),
				s -> CookieUpgrade.valueOf(s), lvl -> Utils.longFromNumber(lvl));
		crumbles = Utils.longFromNumber(data.getOrDefault("crumbles", 0L));
		autoBakeTimeout = Utils.longFromNumber(data.getOrDefault("autoBakeTimeout", 0L));
	}

	public Map<String, Object> toMap() {
		final Map<String, Object> map = new HashMap<>();
		map.put("cookieCounts", cookieCounts);
		map.put("milkCounts", milkCounts);
		map.put("upgrades", CollectionUtils.mapMapString(upgrades));
		map.put("crumbles", crumbles);
		map.put("autoBakeTimeout", autoBakeTimeout);

		return map;
	}

	public long getUpgradeLevel(final CookieUpgrade upgrade) {
		return upgrades.getOrDefault(upgrade, (long) upgrade.min);
	}
}
