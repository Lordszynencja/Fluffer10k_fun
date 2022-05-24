package bot.commands.rpg.blacksmith.tasks.utils;

import java.util.List;

import org.javacord.api.interaction.MessageComponentInteraction;

import bot.Fluffer10kFun;
import bot.commands.rpg.blacksmith.blueprints.utils.Payer;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.items.Items;
import bot.userData.ServerUserData;
import bot.util.pages.messages.PagedPickerMessage.OnPickHandler;

public interface BlacksmithTaskTarget {
	public static BlacksmithTaskTargetItem itemTarget(final String itemId) {
		return new BlacksmithTaskTargetItem(itemId);
	}

	public static BlacksmithTaskTargetItem itemTarget(final String itemId, final long amount) {
		return new BlacksmithTaskTargetItem(itemId, amount);
	}

	public static BlacksmithTaskTargetMultiple multipleTargets(final BlacksmithTaskTarget... targets) {
		return new BlacksmithTaskTargetMultiple(targets);
	}

	public static BlacksmithTaskTargetMultiple multipleTargets(final List<BlacksmithTaskTarget> targets) {
		return new BlacksmithTaskTargetMultiple(targets);
	}

	public static BlacksmithTaskTargetMonsterGirlRace raceTarget(final MonsterGirlRace race, final int amount) {
		return new BlacksmithTaskTargetMonsterGirlRace(race, amount);
	}

	public boolean isPickable();

	public Payer getPayer();

	public void pick(Fluffer10kFun fluffer10kFun, MessageComponentInteraction in, ServerUserData userData,
			final OnPickHandler<Payer> onPick);

	public String taskDescription(Items items);

	public String progressDescription(ServerUserData userData, Items items);

}
