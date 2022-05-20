package bot.commands.rpg.fight.actions.special;

import static bot.util.RandomUtils.clash;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.FightActionsHandler.FightActionHandler;
import bot.commands.rpg.fight.FightTempData;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.Targetting;
import bot.commands.rpg.fight.Targetting.TargetCheck;
import bot.data.fight.FighterStatus;
import bot.data.fight.FighterStatusData;

public class FightFindWeakness implements FightActionHandler {
	private static final Targetting defaultTargetting = new Targetting(
			TargetCheck.ENEMY.alive().without(FighterStatus.WEAKNESS_FOUND));

	private final Fluffer10kFun fluffer10kFun;

	public FightFindWeakness(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final FightTempData data, final RPGFightAction action) {
		data.setUpTarget(fluffer10kFun, defaultTargetting);
		if (data.target == null) {
			data.fight.addTurnDescription(data.activeFighter.name + " has no one to find weakness in!");
			return;
		}

		final int exp = 2 * (data.targetStats.intelligence + data.activeFighterStats.intelligence);
		data.activeFighter.addExp(exp);
		data.target.addExp(exp);

		if (clash(data.targetStats.intelligence, data.activeFighterStats.intelligence)) {
			data.target.addExp(10);
			data.fight.addTurnDescription(
					data.activeFighter.name + " couldn't find weakness in " + data.target.name + ".");
			return;
		}

		data.activeFighter.addExp(10);
		data.target.statuses.addStatus(new FighterStatusData(FighterStatus.WEAKNESS_FOUND).endless());
		data.fight.addTurnDescription(action.description(data.activeFighter.name, data.target.name));
	}
}
