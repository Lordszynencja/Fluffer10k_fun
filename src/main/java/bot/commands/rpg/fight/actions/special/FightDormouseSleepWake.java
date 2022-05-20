package bot.commands.rpg.fight.actions.special;

import bot.commands.rpg.fight.FightActionsHandler.FightActionHandler;
import bot.commands.rpg.fight.FightTempData;
import bot.commands.rpg.fight.RPGFightAction;
import bot.data.fight.FighterStatus;

public class FightDormouseSleepWake implements FightActionHandler {
	@Override
	public void handle(final FightTempData data, final RPGFightAction action) {
		data.activeFighter.statuses.removeStatus(FighterStatus.DORMOUSE_SLEEP);
		data.fight.addTurnDescription(action.description(data.activeFighter.name));
	}
}
