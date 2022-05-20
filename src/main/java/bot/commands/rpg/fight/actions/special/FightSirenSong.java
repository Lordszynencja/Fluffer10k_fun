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

public class FightSirenSong implements FightActionHandler {
	private final Targetting defaultTargetting;

	private final Fluffer10kFun fluffer10kFun;

	public FightSirenSong(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;

		final TargetCheck validTarget = TargetCheck.ENEMY.alive().without(fluffer10kFun, FighterClass.MECHANICAL);

		defaultTargetting = new Targetting(validTarget.without(FighterStatus.CHARMED))//
				.orElse(validTarget);
	}

	@Override
	public void handle(final FightTempData data, final RPGFightAction action) {
		data.setUpTarget(fluffer10kFun, defaultTargetting);
		if (data.target == null) {
			data.fight.addTurnDescription(data.activeFighter.name + " fails to find target for her singing!");
			return;
		}

		final int fighterPower = roll(data.activeFighterStats.intelligence / 3.0);
		final int targetPower = roll(data.targetStats.intelligence / 3.0);
		int duration = 1 + data.target.statuses.getStacks(FighterStatus.SIREN_SONG) + fighterPower - targetPower;

		if (data.targetStats.classes.contains(FighterClass.SPELL_VOID)) {
			duration -= 2;
		}
		if (data.target.statuses.isStatus(FighterStatus.MOTHMAN_POWDER)) {
			duration += 2;
		}
		duration -= fluffer10kFun.fightActionUtils.getNegativeStatusDurationReduction(data.target);

		if (data.targetStats.classes.contains(FighterClass.MECHANICAL)) {
			duration = 0;
		}

		data.target.statuses.addStatus(new FighterStatusData(FighterStatus.SIREN_SONG).stacks(1).endless());

		final int exp = 10 + 5 * (fighterPower + targetPower);
		data.activeFighter.addExp(exp);
		data.target.addExp(exp);

		if (duration <= 0) {
			data.target.addExp(10);
			data.fight.addTurnDescription(
					data.activeFighter.name + " fails to charm " + data.target.name + " with her song!");
			return;
		}

		data.activeFighter.addExp(10);
		data.target.statuses.addStatus(new FighterStatusData(FighterStatus.CHARMED).duration(duration));
		data.fight.addTurnDescription(action.description(data.activeFighter.name, data.target.name));
	}
}