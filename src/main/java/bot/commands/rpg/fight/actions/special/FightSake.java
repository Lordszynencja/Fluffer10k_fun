package bot.commands.rpg.fight.actions.special;

import bot.commands.rpg.fight.FightActionsHandler.FightActionHandler;
import bot.commands.rpg.fight.FightTempData;
import bot.commands.rpg.fight.RPGFightAction;
import bot.data.fight.FighterStatus;
import bot.data.fight.FighterStatusData;

public class FightSake implements FightActionHandler {
	@Override
	public void handle(final FightTempData data, final RPGFightAction action) {
		data.activeFighter.statuses.addStatus(new FighterStatusData(FighterStatus.DRUNK).duration(5));
		data.fight.addTurnDescription(action.description(data.activeFighter.name));
	}
}
