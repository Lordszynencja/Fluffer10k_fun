package bot.commands.rpg.fight.actions.special;

import static bot.util.RandomUtils.roll;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.FightActionsHandler.FightActionHandler;
import bot.commands.rpg.fight.FightTempData;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.Targetting;
import bot.commands.rpg.fight.actions.spells.FightSpellSleep;
import bot.data.fight.FighterClass;
import bot.data.fight.FighterStatus;
import bot.data.fight.FighterStatusData;

public class FightWeresheepSleep implements FightActionHandler {

	public static Targetting getDefaultTargetting(final Fluffer10kFun fluffer10kFun) {
		return FightSpellSleep.getDefaultTargetting(fluffer10kFun);
	}

	private final Targetting defaultTargetting;

	private final Fluffer10kFun fluffer10kFun;

	public FightWeresheepSleep(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;

		defaultTargetting = getDefaultTargetting(fluffer10kFun);
	}

	@Override
	public void handle(final FightTempData data, final RPGFightAction action) {
		data.setUpTarget(fluffer10kFun, defaultTargetting);
		if (data.target == null) {
			data.fight.addTurnDescription(data.activeFighter.name + " fails to find target to put to sleep!");
			return;
		}
		if (data.targetStats.classes.contains(FighterClass.MECHANICAL)) {
			data.target.addExp(10);
			data.fight.addTurnDescription(data.activeFighter.name + "tries to rub weresheep wool against "
					+ data.target.name + " but it has no effect!");
			return;
		}

		final int fighterPower = roll(data.activeFighterStats.intelligence / 3.0);
		final int targetPower = roll(data.targetStats.intelligence / 3.0);
		int duration = 6 + fighterPower - targetPower;
		if (data.targetStats.classes.contains(FighterClass.SPELL_VOID)) {
			data.target.addExp(10);
			duration -= 2;
		}
		duration -= fluffer10kFun.fightActionUtils.getNegativeStatusDurationReduction(data.target);

		final int exp = 5 * (fighterPower + targetPower);
		data.activeFighter.addExp(exp);
		data.target.addExp(exp);

		data.target.statuses.addStatus(new FighterStatusData(FighterStatus.SLEEP).duration(duration));
		data.fight.addTurnDescription(action.description(data.activeFighter.name, data.target.name));
	}
}