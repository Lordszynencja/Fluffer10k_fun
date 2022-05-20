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

public class FightShrinkDown implements FightActionHandler {
	private final Targetting defaultTargetting;

	private final Fluffer10kFun fluffer10kFun;

	public FightShrinkDown(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;

		final TargetCheck validTarget = TargetCheck.ENEMY.alive();
		defaultTargetting = new Targetting(validTarget.without(FighterStatus.SHRINKED))//
				.orElse(validTarget);
	}

	@Override
	public void handle(final FightTempData data, final RPGFightAction action) {
		data.setUpTarget(fluffer10kFun, defaultTargetting);

		data.activeFighter.addExp(10);
		data.target.addExp(10);

		final int fighterPower = roll(
				fluffer10kFun.fightActionUtils.getMagicStrength(data) + data.activeFighterStats.intelligence / 3.0);
		final int targetPower = roll(data.targetStats.intelligence / 3.0);
		int duration = 2 + fighterPower - targetPower;
		if (data.targetStats.classes.contains(FighterClass.SPELL_VOID)) {
			data.target.addExp(10);
			duration -= 2;
		}
		duration -= fluffer10kFun.fightActionUtils.getNegativeStatusDurationReduction(data.target);

		final int exp = 5 * (fighterPower + targetPower);
		data.activeFighter.addExp(exp);
		data.target.addExp(exp);

		if (duration <= 0) {
			data.target.addExp(10);
			data.fight.addTurnDescription(data.activeFighter.name + " fails to shrink " + data.target.name + "!");
		}

		data.activeFighter.addExp(10);

		data.target.statuses.addStatus(new FighterStatusData(FighterStatus.SHRINKED).duration(duration));
		data.fight.addTurnDescription(action.description(data.activeFighter.name, data.target.name));
	}
}