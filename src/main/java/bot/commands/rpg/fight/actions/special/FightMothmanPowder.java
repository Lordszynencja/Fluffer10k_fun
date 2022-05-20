package bot.commands.rpg.fight.actions.special;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.FightActionsHandler.FightActionHandler;
import bot.commands.rpg.fight.FightTempData;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.Targetting;
import bot.commands.rpg.fight.Targetting.TargetCheck;
import bot.data.fight.FighterClass;
import bot.data.fight.FighterStatus;
import bot.data.fight.FighterStatusData;

public class FightMothmanPowder implements FightActionHandler {
	private final Targetting defaultTargetting;

	private final Fluffer10kFun fluffer10kFun;

	public FightMothmanPowder(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;

		defaultTargetting = new Targetting(TargetCheck.ENEMY.alive().without(fluffer10kFun, FighterClass.MECHANICAL)
				.without(FighterStatus.MOTHMAN_POWDER));
	}

	@Override
	public void handle(final FightTempData data, final RPGFightAction action) {
		data.setUpTarget(fluffer10kFun, defaultTargetting);
		if (data.target == null) {
			data.fight.addTurnDescription(data.activeFighter.name + " has no one to throw her powder at!");
			return;
		}
		if (data.targetStats.classes.contains(FighterClass.MECHANICAL)) {
			data.fight.addTurnDescription(
					data.activeFighter.name + " powder doesn't influence " + data.target.name + "!");
			return;
		}

		data.activeFighter.addExp(10);
		data.target.addExp(10);
		data.target.statuses.addStatus(new FighterStatusData(FighterStatus.MOTHMAN_POWDER).endless());
		data.fight.addTurnDescription(action.description(data.activeFighter.name, data.target.name));
	}
}
