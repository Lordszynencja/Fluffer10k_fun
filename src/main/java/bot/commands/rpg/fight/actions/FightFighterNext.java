package bot.commands.rpg.fight.actions;

import bot.commands.rpg.fight.FightActionsHandler.FightActionHandler;
import bot.commands.rpg.fight.FightTempData;
import bot.commands.rpg.fight.RPGFightAction;

public class FightFighterNext implements FightActionHandler {

	@Override
	public void handle(final FightTempData data, final RPGFightAction action) {
		int position = data.fight.fightersOrder.indexOf(data.fight.targetFighter);
		position = (position + 1) % data.fight.fightersOrder.size();
		data.fight.targetFighter = data.fight.fightersOrder.get(position);
	}
}
