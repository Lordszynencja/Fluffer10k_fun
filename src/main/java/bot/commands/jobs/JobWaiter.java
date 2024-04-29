package bot.commands.jobs;

import static bot.util.RandomUtils.getRandom;
import static bot.util.Utils.Pair.pair;

import java.util.Map;
import java.util.Map.Entry;

import bot.commands.jobs.Jobs.Job;
import bot.commands.jobs.Jobs.JobData;
import bot.commands.upgrades.Upgrade;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.userData.ServerUserData;
import bot.util.CollectionUtils;

public class JobWaiter implements Job {

	private static final String[] tasks = { "bring her a bottle of cola", //
			"bring her a can of cola", //
			"bring her some bread", //
			"bring her a bottle of water", //
			"bring her a bottle of wine", //
			"bring her a glass of soda", //
			"bring her a glass of water", //
			"bring her vodka", //
			"clean the table", //
			"dance for her", //
			"kiss you on the cheek", //
			"let her grab your ass", //
			"let her hug you", //
			"let her pat you", //
			"make her a cup of hot chocolate", //
			"make her a cup of cocoa", //
			"make her a cup of milk", //
			"make her a drink", //
			"make her a glass of lemonade", //
			"pour her a glass of beer", //
			"refill her drink", //
			"sing for her", //
			"take her dishes", //
			"welcome her properly", //
			"wiggle your cute butt" };

	@Override
	public JobData createJob() {
		final MonsterGirlRace client = getRandom(MonsterGirlRace.values());
		return new JobData(client.race + " wants someone to " + getRandom(tasks), client.imageLink);
	}

	@Override
	public String getJobId() {
		return "WAITER";
	}

	private static final Map<Upgrade, Long> upgradeBonuses = CollectionUtils.toMap(//
			pair(Upgrade.AGILITY_TRAINING, 5L), //
			pair(Upgrade.CUTE_RIBBON, 5L), //
			pair(Upgrade.CUTE_EARS, 10L), //
			pair(Upgrade.CUTE_TAIL, 10L), //
			pair(Upgrade.FAN_SERVICE, 20L), //
			pair(Upgrade.NEW_OUTFIT, 20L), //
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

		return value;
	}

	@Override
	public String getLabel() {
		return "🛎️ waiter";
	}
}
