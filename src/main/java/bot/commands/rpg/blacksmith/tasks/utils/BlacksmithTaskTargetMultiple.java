package bot.commands.rpg.blacksmith.tasks.utils;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;

import java.util.List;

import bot.data.items.Items;
import bot.userData.ServerUserData;

public class BlacksmithTaskTargetMultiple implements BlacksmithTaskTarget {

	private final List<BlacksmithTaskTarget> targets;

	public BlacksmithTaskTargetMultiple(final BlacksmithTaskTarget... targets) {
		this.targets = asList(targets);
	}

	@Override
	public boolean isMet(final ServerUserData userData) {
		return targets.stream().allMatch(target -> target.isMet(userData));
	}

	@Override
	public String progressDescription(final ServerUserData userData, final Items items) {
		return targets.stream()//
				.map(target -> target.progressDescription(userData, items))//
				.collect(joining("\n"));
	}

	@Override
	public void apply(final ServerUserData userData) {
		targets.forEach(target -> target.apply(userData));
	}
}
