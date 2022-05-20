package bot.commands.rpg.fight.actions.quest;

import static bot.data.fight.FighterStatus.RESTRICTED;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.FightActionsHandler.FightActionHandler;
import bot.commands.rpg.fight.FightTempData;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.Targetting;
import bot.commands.rpg.fight.Targetting.TargetCheck;
import bot.data.fight.FighterStatusData;

public class FightMonsterLordRestriction0 implements FightActionHandler {
	public static Targetting getDefaultTargetting() {
		return new Targetting(TargetCheck.ENEMY.alive());
	}

	private static final Targetting defaultTargetting = getDefaultTargetting();

	private final Fluffer10kFun fluffer10kFun;

	public FightMonsterLordRestriction0(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final FightTempData data, final RPGFightAction action) {
		data.setUpTarget(fluffer10kFun, defaultTargetting);
		data.target.statuses.setStatus(new FighterStatusData(RESTRICTED).endless());
		data.fight.addTurnDescription(action.description(data.target.name));
	}
}
