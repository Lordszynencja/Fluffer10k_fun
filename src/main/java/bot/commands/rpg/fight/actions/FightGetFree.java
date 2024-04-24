package bot.commands.rpg.fight.actions;

import static bot.util.RandomUtils.clash;
import static bot.util.RandomUtils.roll;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.FightActionsHandler.FightActionHandler;
import bot.commands.rpg.fight.FightTempData;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.Targetting;
import bot.data.fight.FighterStatus;
import bot.data.fight.FighterStatusData;

public class FightGetFree implements FightActionHandler {

	private static Targetting defaultTargetting = new Targetting((fighter, target) -> target.id.equals(fighter.heldBy));

	private final Fluffer10kFun fluffer10kFun;

	public FightGetFree(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;
	}

	private void handleAlrauneVines(final FightTempData data, final RPGFightAction action) {
		data.targetId = data.activeFighter.heldBy;
		data.setUpTarget(fluffer10kFun, defaultTargetting);

		final int exp = 2 * (data.activeFighterStats.strength + data.targetStats.strength);
		data.activeFighter.addExp(exp);
		data.target.addExp(exp);

		if (data.target.hp <= 0 || clash(data.activeFighterStats.strength, data.targetStats.strength, 5, 0.75)) {
			data.activeFighter.addExp(10);
			data.activeFighter.statuses.removeStatus(FighterStatus.ALRAUNE_VINES);
			data.fight
					.addTurnDescription(data.activeFighter.name + " untangles from " + data.target.name + "'s vines!");
		} else {
			data.target.addExp(10);
			data.fight.addTurnDescription(
					data.activeFighter.name + " can't untangle from " + data.target.name + "'s vines!");
		}
	}

	private void handleHeld(final FightTempData data, final RPGFightAction action) {
		data.targetId = data.activeFighter.heldBy;
		data.setUpTarget(fluffer10kFun, defaultTargetting);

		final int exp = 2 * (data.activeFighterStats.strength + data.targetStats.strength);
		data.activeFighter.addExp(exp);
		data.target.addExp(exp);

		if (data.target.hp <= 0 || clash(data.activeFighterStats.strength, data.targetStats.strength, 5, 0.75)) {
			data.activeFighter.addExp(10);
			data.activeFighter.statuses.removeStatus(FighterStatus.HELD);
			data.fight.addTurnDescription(action.description(data.activeFighter.name, data.target.name));
		} else {
			data.target.addExp(10);
			data.fight.addTurnDescription(
					data.activeFighter.name + " can't get free from " + data.target.name + "'s grasp!");
		}
	}

	private void handleKejorouHair(final FightTempData data, final RPGFightAction action) {
		data.targetId = data.activeFighter.heldBy;
		data.setUpTarget(fluffer10kFun, defaultTargetting);

		final int exp = 2 * (data.activeFighterStats.strength + data.targetStats.strength);
		data.activeFighter.addExp(exp);
		data.target.addExp(exp);

		if (data.target.hp <= 0 || clash(data.activeFighterStats.strength, data.targetStats.strength, 5, 0.75)) {
			data.activeFighter.addExp(10);
			data.activeFighter.statuses.removeStatus(FighterStatus.KEJOUROU_HAIR);
			data.fight.addTurnDescription(data.activeFighter.name + " untangles from " + data.target.name + "'s hair!");
		} else {
			data.target.addExp(10);
			data.fight.addTurnDescription(
					data.activeFighter.name + " can't untangle from " + data.target.name + "'s hair!");
		}
	}

	private void handleWrapped(final FightTempData data, final RPGFightAction action) {
		final int netBreakPower = 1 + roll(data.activeFighterStats.strength / 3.0);
		data.activeFighter.addExp(netBreakPower * 5);

		data.activeFighter.statuses
				.addStatus(new FighterStatusData(FighterStatus.WRAPPED_IN_WEB).endless().stacks(-netBreakPower));

		if (data.activeFighter.statuses.getStacks(FighterStatus.WRAPPED_IN_WEB) <= 0) {
			data.activeFighter.addExp(10);
			data.activeFighter.statuses.removeStatus(FighterStatus.WRAPPED_IN_WEB);
			data.fight.addTurnDescription(data.activeFighter.name + " gets free from the web!");
		} else {
			data.fight.addTurnDescription(data.activeFighter.name + " can't get free from the web!");
		}
	}

	@Override
	public void handle(final FightTempData data, final RPGFightAction action) {
		if (data.activeFighter.statuses.isStatus(FighterStatus.HELD)) {
			handleHeld(data, action);
			return;
		}
		if (data.activeFighter.statuses.isStatus(FighterStatus.ALRAUNE_VINES)) {
			handleAlrauneVines(data, action);
			return;
		}
		if (data.activeFighter.statuses.isStatus(FighterStatus.KEJOUROU_HAIR)) {
			handleKejorouHair(data, action);
			return;
		}
		if (data.activeFighter.statuses.isStatus(FighterStatus.WRAPPED_IN_WEB)) {
			handleWrapped(data, action);
			return;
		}
	}
}
