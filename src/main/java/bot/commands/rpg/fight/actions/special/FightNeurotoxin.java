package bot.commands.rpg.fight.actions.special;

import static bot.util.RandomUtils.clash;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.FightActionsHandler.FightActionHandler;
import bot.commands.rpg.fight.FightTempData;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.Targetting;
import bot.commands.rpg.fight.Targetting.TargetCheck;
import bot.data.fight.FighterClass;
import bot.data.fight.FighterStatus;
import bot.data.fight.FighterStatusData;

public class FightNeurotoxin implements FightActionHandler {
	private final Targetting defaultTargetting;

	private final Fluffer10kFun fluffer10kFun;

	public FightNeurotoxin(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;

		final TargetCheck validTarget = TargetCheck.ENEMY.alive().without(fluffer10kFun, FighterClass.MECHANICAL);
		final TargetCheck primaryTarget = validTarget.with(FighterStatus.NEUROTOXIN)//
				.withStacksLessThan(FighterStatus.NEUROTOXIN, 4);
		defaultTargetting = new Targetting(primaryTarget)//
				.orElse(validTarget);
	}

	@Override
	public void handle(final FightTempData data, final RPGFightAction action) {
		data.setUpTarget(fluffer10kFun, defaultTargetting);
		if (data.target == null) {
			data.fight.addTurnDescription(data.activeFighter.name + " has no one to bite!");
			return;
		}
		if (data.targetStats.classes.contains(FighterClass.MECHANICAL)) {
			data.fight.addTurnDescription(
					data.activeFighter.name + "'s neurotoxin doesn't influence " + data.target.name + "!");
			return;
		}

		data.activeFighter.addExp(10);
		data.target.addExp(10);

		final int fighterPower = data.activeFighterStats.agility + data.activeFighterStats.strength;
		final int targetPower = data.targetStats.agility + data.targetStats.strength;
		final int exp = 2 * (fighterPower + targetPower);
		data.activeFighter.addExp(exp);
		data.target.addExp(exp);

		if (clash(targetPower, fighterPower)) {
			data.target.addExp(10);
			data.fight.addTurnDescription(
					data.activeFighter.name + " couldn't inject neurotoxin into " + data.target.name + "!");
			return;
		}

		data.activeFighter.addExp(10);
		data.target.statuses.addStatus(new FighterStatusData(FighterStatus.NEUROTOXIN).endless().stacks(1));
		data.fight.addTurnDescription(action.description(data.activeFighter.name, data.target.name));
	}
}
