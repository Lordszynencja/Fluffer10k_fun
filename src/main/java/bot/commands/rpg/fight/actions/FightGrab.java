package bot.commands.rpg.fight.actions;

import static bot.util.RandomUtils.clash;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.FightActionsHandler.FightActionHandler;
import bot.commands.rpg.fight.FightTempData;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.Targetting;
import bot.commands.rpg.fight.Targetting.TargetCheck;
import bot.data.fight.FighterStatus;
import bot.data.fight.FighterStatusData;

public class FightGrab implements FightActionHandler {

	private static Targetting defaultTargetting = new Targetting(TargetCheck.ENEMY.alive());

	private final Fluffer10kFun fluffer10kFun;

	public FightGrab(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final FightTempData data, final RPGFightAction action) {
		data.setUpTarget(fluffer10kFun, defaultTargetting);
		if (data.target.heldBy != null) {
			data.fight.addTurnDescription(data.activeFighter.name + " tries to grab " + data.target.name
					+ ", but they are already held by someone!");
			return;
		}

		data.activeFighter.addExp(10);
		data.target.addExp(10);

		final int fighterPower = data.activeFighterStats.strength + data.activeFighterStats.agility;
		final int targetPower = data.targetStats.strength + data.targetStats.agility;

		final int exp = 2 * (fighterPower + targetPower);
		data.activeFighter.addExp(exp);
		data.target.addExp(exp);

		if (clash(targetPower, fighterPower)) {
			data.fight.addTurnDescription(
					data.activeFighter.name + " tries to grab " + data.target.name + ", but they move out of the way!");
			return;
		}

		data.target.statuses.addStatus(new FighterStatusData(FighterStatus.HELD).endless());
		data.target.heldBy = data.activeFighter.id;
		data.fight.addTurnDescription(action.description(data.activeFighter.name, data.target.name));
	}
}
