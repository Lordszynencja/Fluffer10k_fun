package bot.commands.rpg.fight.actions.special;

import static bot.util.RandomUtils.clash;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.FightActionsHandler.FightActionHandler;
import bot.commands.rpg.fight.FightTempData;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.Targetting;
import bot.commands.rpg.fight.Targetting.TargetCheck;
import bot.data.fight.FighterStatus;
import bot.data.fight.FighterStatusData;

public class FightWrapInWeb implements FightActionHandler {
	private static final Targetting defaultTargetting = new Targetting(
			TargetCheck.ENEMY.alive().without(FighterStatus.WRAPPED_IN_WEB))//
					.orElse(TargetCheck.ENEMY.alive());

	private final Fluffer10kFun fluffer10kFun;

	public FightWrapInWeb(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final FightTempData data, final RPGFightAction action) {
		data.setUpTarget(fluffer10kFun, defaultTargetting);
		if (data.target == null) {
			data.fight.addTurnDescription(data.activeFighter.name + " has no one to wrap in web!");
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
			data.fight.addTurnDescription(data.activeFighter.name + " couldn't wrap " + data.target.name + "in silk!");
			return;
		}

		data.activeFighter.addExp(10);
		final int newStacks = data.activeFighterStats.level / 2;
		if (newStacks > data.target.statuses.getStacks(FighterStatus.WRAPPED_IN_WEB)) {
			data.target.statuses
					.setStatus(new FighterStatusData(FighterStatus.WRAPPED_IN_WEB).endless().stacks(newStacks));
		}
		data.fight.addTurnDescription(action.description(data.activeFighter.name, data.target.name));
	}
}