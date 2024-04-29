package bot.commands.jobs;

import static bot.util.CollectionUtils.toMap;
import static bot.util.RandomUtils.getRandom;
import static bot.util.Utils.Pair.pair;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import bot.Fluffer10kFun;
import bot.commands.jobs.Jobs.Job;
import bot.commands.jobs.Jobs.JobData;
import bot.commands.upgrades.Upgrade;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.userData.ServerUserData;

public class JobBaker implements Job {
	private final List<String> foodTasks;

	private static String cookieToTask(final String cookieName) {
		return "bake " + (cookieName.matches("[aeiouyAEIOUY].*") ? "an " : "a ") + cookieName;
	}

	public JobBaker(final Fluffer10kFun fluffer10kFun) {
		foodTasks = new ArrayList<>(
				fluffer10kFun.cookieUtils.cookieTypes.stream().map(JobBaker::cookieToTask).collect(toList()));
		foodTasks.add("bake an apple pie");
		foodTasks.add("bake a baguette");
		foodTasks.add("bake a loaf of bread");
		foodTasks.add("bake a carrot pie");
		foodTasks.add("bake a croissant");
		foodTasks.add("bake a pie flavored pie");
	}

	@Override
	public JobData createJob() {
		final MonsterGirlRace client = getRandom(MonsterGirlRace.values());
		return new JobData(client.race + " wants someone to " + getRandom(foodTasks) + " for her", client.imageLink);
	}

	@Override
	public String getJobId() {
		return "BAKER";
	}

	private static final Map<Upgrade, Long> upgradeBonuses = toMap(//
			pair(Upgrade.HEAVENLY_CHIPS, 25L), //
			pair(Upgrade.NEWSPAPER_ADS, 10L), //
			pair(Upgrade.TV_ADS, 20L), //
			pair(Upgrade.ONLINE_ADS, 30L));

	@Override
	public long calculateReward(final ServerUserData userData) {
		long value = 25;

		for (final Entry<Upgrade, Long> upgradeBonus : upgradeBonuses.entrySet()) {
			if (userData.upgrades.contains(upgradeBonus.getKey())) {
				value += upgradeBonus.getValue();
			}
		}
		value += userData.cookies.crumbles * 5;

		return value;
	}

	@Override
	public String getLabel() {
		return "🍪 baker";
	}
}
