package bot.commands.rpg.fight.actions.special;

import static bot.util.RandomUtils.roll;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.FightActionsHandler.FightActionHandler;
import bot.commands.rpg.fight.FightTempData;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.Targetting;
import bot.commands.rpg.fight.Targetting.TargetCheck;
import bot.data.fight.FighterClass;
import bot.data.fight.FighterStatus;
import bot.data.fight.FighterStatusData;

public class FightBansheeWail implements FightActionHandler {
	public static Targetting getDefaultTargetting(final Fluffer10kFun fluffer10kFun) {
		final TargetCheck validTarget = TargetCheck.ENEMY.alive().without(fluffer10kFun, FighterClass.MECHANICAL);

		return new Targetting(validTarget.without(FighterStatus.BANSHEE_WAIL))//
				.orElse(validTarget);
	}

	private final Targetting defaultTargetting;

	private final Fluffer10kFun fluffer10kFun;

	public FightBansheeWail(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;

		defaultTargetting = getDefaultTargetting(fluffer10kFun);
	}

	@Override
	public void handle(final FightTempData data, final RPGFightAction action) {
		data.setUpTarget(fluffer10kFun, defaultTargetting);
		if (data.target == null) {
			data.fight.addTurnDescription(data.activeFighter.name + " doesn't have anyone to use barometz cotton on!");
			return;
		}
		if (data.targetStats.classes.contains(FighterClass.MECHANICAL)) {
			data.target.addExp(10);
			data.fight.addTurnDescription(
					data.activeFighter.name + " fails to influence " + data.target.name + " with her wailing!");
			return;
		}

		final int attackerRoll = roll(data.activeFighterStats.intelligence / 3.0);
		final int targetRoll = roll(data.targetStats.intelligence / 3.0);
		int duration = 3 + attackerRoll - targetRoll;
		if (data.targetStats.classes.contains(FighterClass.SPELL_VOID)) {
			data.target.addExp(10);
			duration -= 2;
		}
		duration -= fluffer10kFun.fightActionUtils.getNegativeStatusDurationReduction(data.target);

		final int exp = 3 * (attackerRoll + targetRoll);
		data.activeFighter.addExp(exp);
		data.target.addExp(exp);

		if (duration <= 0) {
			data.target.addExp(10);
			data.fight.addTurnDescription(
					data.activeFighter.name + " fails to influence " + data.target.name + " with her wailing!");
		}

		data.activeFighter.addExp(10);

		data.target.statuses.addStatus(new FighterStatusData(FighterStatus.BANSHEE_WAIL).duration(duration));
		data.fight.addTurnDescription(action.description(data.activeFighter.name, data.target.name));
	}
}
