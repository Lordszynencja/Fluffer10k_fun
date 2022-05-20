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

public class FightSalt implements FightActionHandler {
	private final Targetting defaultTargetting;

	private final Fluffer10kFun fluffer10kFun;

	public FightSalt(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;

		final TargetCheck validTarget = TargetCheck.ENEMY.alive().with(fluffer10kFun, FighterClass.SALTABLE);

		defaultTargetting = new Targetting(validTarget.without(FighterStatus.SALTED))//
				.orElse(validTarget);
	}

	@Override
	public void handle(final FightTempData data, final RPGFightAction action) {
		data.setUpTarget(fluffer10kFun, defaultTargetting);
		if (data.target == null) {
			data.fight.addTurnDescription(data.activeFighter.name + " has no one to salt!");
			return;
		}

		if (!data.targetStats.classes.contains(FighterClass.SALTABLE)) {
			data.fight.addTurnDescription(data.activeFighter.name + " has no one to salt!");
			return;
		}

		data.activeFighter.addExp(10);
		data.target.addExp(10);

		data.target.statuses.addStatus(new FighterStatusData(FighterStatus.SALTED).endless());
		data.fight.addTurnDescription(action.description(data.activeFighter.name, data.target.name));
	}
}