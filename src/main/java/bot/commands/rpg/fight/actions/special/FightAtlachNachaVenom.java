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

public class FightAtlachNachaVenom implements FightActionHandler {
	private final Targetting defaultTargetting;

	private final Fluffer10kFun fluffer10kFun;

	public FightAtlachNachaVenom(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;

		final TargetCheck validTarget = TargetCheck.ENEMY.alive().without(fluffer10kFun, FighterClass.MECHANICAL)
				.withStacksLessThan(FighterStatus.ATLACH_NACHA_VENOM, 5);
		defaultTargetting = new Targetting(validTarget.with(FighterStatus.ATLACH_NACHA_VENOM))//
				.orElse(validTarget);
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

		final int fighterPower = data.activeFighterStats.agility + data.activeFighterStats.strength;
		final int targetPower = data.targetStats.agility + data.targetStats.strength;
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
		if (data.target.statuses.getStacks(FighterStatus.ATLACH_NACHA_VENOM) < 5) {
			data.target.statuses.addStatus(new FighterStatusData(FighterStatus.ATLACH_NACHA_VENOM).endless().stacks(1));
		}
		data.fight.addTurnDescription(action.description(data.activeFighter.name, data.target.name));
	}
}
