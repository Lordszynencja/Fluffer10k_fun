package bot.commands.rpg.fight.actions.special;

import static bot.util.RandomUtils.clash;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.FightActionsHandler.FightActionHandler;
import bot.commands.rpg.fight.FightTempData;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.Targetting;
import bot.commands.rpg.fight.Targetting.TargetCheck;
import bot.data.fight.FighterClass;
import bot.data.fight.FighterStatus;
import bot.data.fight.FighterStatusData;

public class FightParalyze implements FightActionHandler {
	private final Targetting defaultTargetting;

	private final Fluffer10kFun fluffer10kFun;

	public FightParalyze(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;

		final TargetCheck validTarget = TargetCheck.ENEMY.alive();
		defaultTargetting = new Targetting(
				validTarget.with(fluffer10kFun, FighterClass.MECHANICAL).without(FighterStatus.PARALYZED))//
						.orElse(validTarget.without(FighterStatus.PARALYZED))//
						.orElse(validTarget.with(fluffer10kFun, FighterClass.MECHANICAL))//
						.orElse(validTarget);
	}

	@Override
	public void handle(final FightTempData data, final RPGFightAction action) {
		data.setUpTarget(fluffer10kFun, defaultTargetting);
		if (data.target == null) {
			data.fight.addTurnDescription(data.activeFighter.name + " doesn't have anyone to user barometz cotton on!");
			return;
		}

		data.activeFighter.addExp(10);
		data.target.addExp(10);

		int duration = 3;
		if (data.targetStats.classes.contains(FighterClass.MECHANICAL)) {
			duration += 1;
		}

		final int exp = 2 * (data.targetStats.agility + data.activeFighterStats.agility);
		data.activeFighter.addExp(exp);
		data.target.addExp(exp);

		if (clash(data.targetStats.agility, data.activeFighterStats.agility)) {
			data.target.addExp(10);
			data.fight.addTurnDescription(data.activeFighter.name + " fails to paralyze " + data.target.name + "!");
			return;
		}

		data.activeFighter.addExp(10);

		data.target.statuses.addStatus(new FighterStatusData(FighterStatus.PARALYZED).duration(duration));
		data.fight.addTurnDescription(action.description(data.activeFighter.name, data.target.name));
	}
}