package bot.commands.rpg.fight.actions.special;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.FightActionsHandler.FightActionHandler;
import bot.commands.rpg.fight.FightTempData;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.Targetting;
import bot.commands.rpg.fight.Targetting.TargetCheck;
import bot.data.fight.FighterStatus;
import bot.data.fight.FighterStatusData;

public class FightAlrauneVines implements FightActionHandler {
	private static final Targetting defaultTargetting = new Targetting(TargetCheck.ENEMY.alive());

	private final Fluffer10kFun fluffer10kFun;

	public FightAlrauneVines(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final FightTempData data, final RPGFightAction action) {
		data.setUpTarget(fluffer10kFun, defaultTargetting);
		if (data.target == null) {
			data.fight.addTurnDescription(data.activeFighter.name + " has no one to grab with her vines!");
			return;
		}
		if (data.target.heldBy != null) {
			data.fight.addTurnDescription(data.activeFighter.name + " tries to grab " + data.target.name
					+ " with her vines, but they are already held by someone!");
			return;
		}

		data.activeFighter.addExp(20);
		data.target.addExp(20);
		data.target.statuses.addStatus(new FighterStatusData(FighterStatus.ALRAUNE_VINES).endless().stacks(1));
		data.target.heldBy = data.activeFighter.id;
		data.fight.addTurnDescription(action.description(data.activeFighter.name, data.target.name));
	}
}