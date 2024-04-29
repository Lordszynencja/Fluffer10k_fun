package bot.commands.jobs;

import static bot.util.CollectionUtils.toMap;
import static bot.util.RandomUtils.getRandom;
import static bot.util.Utils.Pair.pair;

import java.util.Map;
import java.util.Map.Entry;

import bot.commands.jobs.Jobs.Job;
import bot.commands.jobs.Jobs.JobData;
import bot.commands.upgrades.Upgrade;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.userData.ServerUserData;

public class JobCook implements Job {
	private static final String[] foodTasks = { //
			"cook boar", //
			"make cheeseburger", //
			"cook chicken", //
			"cook chicken tendies", //
			"cook chicken with fries", //
			"prepare chicken with mashed potatoes", //
			"make chicken wrap", //
			"prepare coleslaw", //
			"cook fish and chips", //
			"make fries", //
			"prepare fruit salad", //
			"prepare hamburger", //
			"make kebab", //
			"make mushroom pie", //
			"cook omelette du fromage", //
			"cook Paimon", //
			"cook pizza margharita", //
			"cook pizza quattro formaggi", //
			"make risotto", //
			"make a sandwich", //
			"prepare spaghetti bolognese", //
			"cook spaghetti carbonara", //
			"cook steak", //
			"cook stew", //
			"make sushi", //
			"make takoyaki", //
			"cook turkey", //
			"prepare vegetable salad" };

	@Override
	public JobData createJob() {
		final MonsterGirlRace client = getRandom(MonsterGirlRace.values());
		return new JobData(client.race + " wants someone to " + getRandom(foodTasks) + " for her", client.imageLink);
	}

	@Override
	public String getJobId() {
		return "COOK";
	}

	private static final Map<Upgrade, Long> upgradeBonuses = toMap(//
			pair(Upgrade.CHEF_HAT, 5L), //
			pair(Upgrade.NON_STICK_PAN, 5L), //
			pair(Upgrade.BETTER_SPICES, 10L), //
			pair(Upgrade.BETTER_INGREDIENTS, 10L), //
			pair(Upgrade.CERAMIC_KNIVES, 20L), //
			pair(Upgrade.GOLDEN_SPATULA, 20L), //
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
		return "🍳 chef";
	}
}
