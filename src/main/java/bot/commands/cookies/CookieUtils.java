package bot.commands.cookies;

import static bot.util.CollectionUtils.addToLongOnMap;
import static bot.util.RandomUtils.getRandom;
import static bot.util.TimerUtils.startRepeatedTimedEvent;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import bot.Fluffer10kFun;

public class CookieUtils {
	public static final Color cookiesColor = new Color(0xDD6622);
	public static final Color milkColor = new Color(0xFFFFFF);

	private final Fluffer10kFun fluffer10kFun;

	public final List<String> cookieTypes = new ArrayList<>();
	public final List<String> milkTypes = new ArrayList<>();
	private final Set<String> cookieTypesSet;

	private void addAutoCookies() {
		try {
			fluffer10kFun.serverUserDataUtils.onEveryUser((serverId, userId, userData) -> {
				final long level = userData.cookies.upgrades.getOrDefault(CookieUpgrade.AUTO_BAKE,
						(long) CookieUpgrade.AUTO_BAKE.min);
				if (level > 0) {
					userData.cookies.autoBakeTimeout -= 1;
					if (userData.cookies.autoBakeTimeout <= 0) {
						final String cookieName = getRandom(cookieTypes);
						addToLongOnMap(userData.cookies.cookieCounts, cookieName, 1);
						userData.cookies.autoBakeTimeout = 121 - level;
					}
				}
			});
		} catch (final Exception e) {
			fluffer10kFun.apiUtils.messageUtils.sendExceptionToMe(e);
		}
	}

	public CookieUtils(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;

		final File cookiesFolder = new File(fluffer10kFun.apiUtils.config.getString("imageFolderPath") + "cookies/");
		for (final String cookieName : cookiesFolder.list((file, name) -> name.endsWith(".png"))) {
			cookieTypes.add(cookieName.substring(0, cookieName.lastIndexOf('.')));
		}
		cookieTypesSet = new HashSet<>(cookieTypes);

		final File milksFolder = new File(fluffer10kFun.apiUtils.config.getString("imageFolderPath") + "milk/");
		for (final String milkName : milksFolder.list((file, name) -> name.endsWith(".png"))) {
			milkTypes.add(milkName.substring(0, milkName.lastIndexOf('.')));
		}

		startRepeatedTimedEvent(this::addAutoCookies, 60, 0, "adding auto cookies");
	}

	public boolean cookieExists(final String name) {
		return cookieTypesSet.contains(name);
	}

	public File getCookieFile(final String name) {
		return new File(fluffer10kFun.apiUtils.config.getString("imageFolderPath") + "cookies/" + name + ".png");
	}

	public File getMilkFile(final String name) {
		return new File(fluffer10kFun.apiUtils.config.getString("imageFolderPath") + "milk/" + name + ".png");
	}
}
