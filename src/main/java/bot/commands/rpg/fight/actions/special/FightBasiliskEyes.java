package bot.commands.rpg.fight.actions.special;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.FightActionsHandler.FightActionHandler;
import bot.commands.rpg.fight.FightTempData;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.Targetting;
import bot.commands.rpg.fight.Targetting.TargetCheck;
import bot.data.fight.FighterStatus;
import bot.data.fight.FighterStatusData;

public class FightBasiliskEyes implements FightActionHandler {
	public static Targetting getDefaultTargetting() {
		final TargetCheck validTarget = TargetCheck.ENEMY.alive();

		return new Targetting(validTarget.without(FighterStatus.BASILISK_EYES))//
				.orElse(validTarget);
	}

	private static final Targetting defaultTargetting = getDefaultTargetting();

	private final Fluffer10kFun fluffer10kFun;

	public FightBasiliskEyes(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final FightTempData data, final RPGFightAction action) {
		data.setUpTarget(fluffer10kFun, defaultTargetting);
		if (data.target == null) {
			data.fight.addTurnDescription(data.activeFighter.name + " has no one to stare at!");
			return;
		}
		int duration = 4;
		duration -= fluffer10kFun.fightActionUtils.getNegativeStatusDurationReduction(data.target);

		data.activeFighter.addExp(20);
		data.target.addExp(20);
		data.target.statuses.addStatus(new FighterStatusData(FighterStatus.BASILISK_EYES).duration(duration));
		data.fight.addTurnDescription(action.description(data.activeFighter.name, data.target.name));
	}
}
