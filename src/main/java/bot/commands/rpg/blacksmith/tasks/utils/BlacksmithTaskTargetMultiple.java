package bot.commands.rpg.blacksmith.tasks.utils;

import static bot.commands.rpg.blacksmith.blueprints.utils.Payer.multiPayer;
import static bot.util.CollectionUtils.mapToList;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;

import java.util.List;

import org.javacord.api.interaction.MessageComponentInteraction;

import bot.Fluffer10kFun;
import bot.commands.rpg.blacksmith.blueprints.utils.Payer;
import bot.data.items.Items;
import bot.userData.ServerUserData;
import bot.util.pages.messages.PagedPickerMessage.OnPickHandler;

public class BlacksmithTaskTargetMultiple implements BlacksmithTaskTarget {

	private final List<BlacksmithTaskTarget> targets;

	public BlacksmithTaskTargetMultiple(final BlacksmithTaskTarget... targets) {
		this(asList(targets));
	}

	public BlacksmithTaskTargetMultiple(final List<BlacksmithTaskTarget> targets) {
		this.targets = targets;
	}

	@Override
	public String taskDescription(final Items items) {
		return targets.stream()//
				.map(target -> target.taskDescription(items))//
				.collect(joining("\n"));
	}

	@Override
	public String progressDescription(final ServerUserData userData, final Items items) {
		return targets.stream()//
				.map(target -> target.progressDescription(userData, items))//
				.collect(joining("\n"));
	}

	@Override
	public boolean isPickable() {
		return false;
	}

	@Override
	public Payer getPayer() {
		return multiPayer(mapToList(targets, t -> t.getPayer()));
	}

	@Override
	public void pick(final Fluffer10kFun fluffer10kFun, final MessageComponentInteraction in,
			final ServerUserData userData, final OnPickHandler<Payer> onPick) {
	}

}
