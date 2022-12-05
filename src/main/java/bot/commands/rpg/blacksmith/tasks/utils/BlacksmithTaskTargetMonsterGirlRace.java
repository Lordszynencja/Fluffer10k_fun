package bot.commands.rpg.blacksmith.tasks.utils;

import org.javacord.api.interaction.MessageComponentInteraction;

import bot.Fluffer10kFun;
import bot.commands.rpg.blacksmith.blueprints.utils.Payer;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.items.Items;
import bot.userData.ServerUserData;
import bot.util.pages.messages.PagedPickerMessage.OnPickHandler;

public class BlacksmithTaskTargetMonsterGirlRace implements BlacksmithTaskTarget {
	private class MonsterGirlRaceDefeatedPayer implements Payer {
		@Override
		public void pay(final ServerUserData userData) {
		}

		@Override
		public boolean canPay(final ServerUserData userData) {
			return userData.blacksmith.currentTask.monsterGirlsDefeated.getOrDefault(race, 0) >= amount;
		}
	}

	private final MonsterGirlRace race;
	private final int amount;

	public BlacksmithTaskTargetMonsterGirlRace(final MonsterGirlRace race, final int amount) {
		this.race = race;
		this.amount = amount;
	}

	@Override
	public String taskDescription(final Items items) {
		return "defeat " + amount + (amount == 1 ? " girl" : " girls") + " of " + race.race + " race";
	}

	@Override
	public String progressDescription(final ServerUserData userData, final Items items) {
		final int defeated = userData.blacksmith.currentTask == null ? 0
				: userData.blacksmith.currentTask.monsterGirlsDefeated.getOrDefault(race, 0);
		return race.race + " defeated: " + defeated + "/" + amount;
	}

	@Override
	public boolean isPickable() {
		return false;
	}

	@Override
	public Payer getPayer() {
		return new MonsterGirlRaceDefeatedPayer();
	}

	@Override
	public void pick(final Fluffer10kFun fluffer10kFun, final MessageComponentInteraction in,
			final ServerUserData userData, final OnPickHandler<Payer> onPick) {
	}
}
