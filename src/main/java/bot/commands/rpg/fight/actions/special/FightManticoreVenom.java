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

public class FightManticoreVenom implements FightActionHandler {
	public static Targetting getDefaultTargetting(final Fluffer10kFun fluffer10kFun) {
		return new Targetting(TargetCheck.ENEMY.alive().without(fluffer10kFun, FighterClass.MECHANICAL)
				.with(FighterStatus.CHARM_RESISTANCE));
	}

	private final Targetting defaultTargetting;

	private final Fluffer10kFun fluffer10kFun;

	public FightManticoreVenom(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;

		defaultTargetting = getDefaultTargetting(fluffer10kFun);
	}

	@Override
	public void handle(final FightTempData data, final RPGFightAction action) {
		data.setUpTarget(fluffer10kFun, defaultTargetting);
		if (data.target == null) {
			data.fight.addTurnDescription(data.activeFighter.name + " has no one to sting!");
			return;
		}
		if (data.targetStats.classes.contains(FighterClass.MECHANICAL)) {
			data.fight.addTurnDescription(
					data.activeFighter.name + "'s venom doesn't influence " + data.target.name + "!");
			return;
		}

		data.activeFighter.addExp(10);
		data.target.addExp(10);

		final int fighterPower = data.activeFighterStats.agility;
		final int targetPower = data.targetStats.agility;
		final int exp = 2 * (fighterPower + targetPower);
		data.activeFighter.addExp(exp);
		data.target.addExp(exp);

		if (clash(targetPower, fighterPower)) {
			data.target.addExp(10);
			data.fight.addTurnDescription(
					data.activeFighter.name + " couldn't inject venom into " + data.target.name + "!");
			return;
		}

		data.activeFighter.addExp(10);
		if (data.target.statuses.getStacks(FighterStatus.CHARM_RESISTANCE) > 2) {
			data.target.statuses.addStatus(new FighterStatusData(FighterStatus.CHARM_RESISTANCE).endless().stacks(-2));
		} else {
			data.target.statuses.removeStatus(FighterStatus.CHARM_RESISTANCE);
		}
		data.fight.addTurnDescription(action.description(data.activeFighter.name, data.target.name));
	}
}
