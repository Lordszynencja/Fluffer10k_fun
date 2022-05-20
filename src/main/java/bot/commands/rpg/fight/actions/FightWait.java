package bot.commands.rpg.fight.actions;

import bot.commands.rpg.fight.FightActionsHandler.FightActionHandler;
import bot.commands.rpg.fight.FightTempData;
import bot.commands.rpg.fight.RPGFightAction;

public class FightWait implements FightActionHandler {
	@Override
	public void handle(final FightTempData data, final RPGFightAction action) {
		data.fight.addTurnDescription(action.description(data.activeFighter.name));
	}
}
