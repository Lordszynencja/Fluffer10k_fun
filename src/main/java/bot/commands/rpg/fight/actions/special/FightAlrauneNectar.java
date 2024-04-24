package bot.commands.rpg.fight.actions.special;

import static bot.util.RandomUtils.clash;
import static java.lang.Math.max;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.FightActionsHandler.FightActionHandler;
import bot.commands.rpg.fight.FightTempData;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.Targetting;
import bot.commands.rpg.fight.Targetting.TargetCheck;
import bot.data.fight.FighterClass;
import bot.data.fight.FighterStatus;
import bot.data.fight.FighterStatusData;

public class FightAlrauneNectar implements FightActionHandler {
	private final Targetting defaultTargetting;

	private final Fluffer10kFun fluffer10kFun;

	public FightAlrauneNectar(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;

		defaultTargetting = new Targetting(TargetCheck.ENEMY.alive().without(fluffer10kFun, FighterClass.MECHANICAL)
				.with(FighterStatus.CHARM_RESISTANCE));
	}

	@Override
	public void handle(final FightTempData data, final RPGFightAction action) {
		data.setUpTarget(fluffer10kFun, defaultTargetting);
		if (data.target == null) {
			data.fight.addTurnDescription(data.activeFighter.name + " has no one to splash nectar on!");
			return;
		}
		if (data.targetStats.classes.contains(FighterClass.MECHANICAL)) {
			data.fight.addTurnDescription(data.activeFighter.name + " splashed nectar on " + data.target.name
					+ ", but it didn't have an effect!");
			return;
		}

		final int exp = 2 * (data.targetStats.agility + data.activeFighterStats.agility);
		data.activeFighter.addExp(exp);
		data.target.addExp(exp);

		if (clash(data.targetStats.agility, data.activeFighterStats.agility)) {
			data.target.addExp(10);
			data.fight.addTurnDescription(
					data.activeFighter.name + " couldn't splash nectar on " + data.target.name + "!");
			return;
		}

		final int charmResistanceStacks = data.target.statuses.getStacks(FighterStatus.CHARM_RESISTANCE);
		final int removal = max(1, charmResistanceStacks * data.targetStats.intelligence
				/ (data.targetStats.intelligence + data.activeFighterStats.intelligence));

		data.activeFighter.addExp(removal * 5);
		data.fight.addTurnDescription(action.description(data.activeFighter.name, data.target.name));

		if (charmResistanceStacks == removal) {
			data.target.statuses.removeStatus(FighterStatus.CHARM_RESISTANCE);
			data.fight.addTurnDescription(data.target.name + " lost all charm resistance!");
		} else {
			data.target.statuses.addStatus(new FighterStatusData(FighterStatus.CHARM_RESISTANCE).stacks(-removal));
		}
	}
}