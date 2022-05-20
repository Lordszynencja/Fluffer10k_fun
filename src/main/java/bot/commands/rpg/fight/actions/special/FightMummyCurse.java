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

public class FightMummyCurse implements FightActionHandler {
	public static Targetting getDefaultTargetting(final Fluffer10kFun fluffer10kFun) {
		final TargetCheck validTarget = TargetCheck.ENEMY.alive().without(fluffer10kFun, FighterClass.MECHANICAL);

		return new Targetting(validTarget.without(FighterStatus.CURSED))//
				.orElse(validTarget);
	}

	private final Targetting defaultTargetting;

	private final Fluffer10kFun fluffer10kFun;

	public FightMummyCurse(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;

		defaultTargetting = getDefaultTargetting(fluffer10kFun);
	}

	@Override
	public void handle(final FightTempData data, final RPGFightAction action) {
		data.setUpTarget(fluffer10kFun, defaultTargetting);
		if (data.target == null) {
			data.fight.addTurnDescription(data.activeFighter.name + " has no one to curse!");
			return;
		}
		if (data.targetStats.classes.contains(FighterClass.MECHANICAL)) {
			data.fight.addTurnDescription(data.activeFighter.name + " couldn't curse " + data.target.name + "!");
			return;
		}

		final int exp = 2 * (data.targetStats.intelligence + data.activeFighterStats.intelligence);
		data.activeFighter.addExp(exp);
		data.target.addExp(exp);

		if (clash(data.targetStats.intelligence, data.activeFighterStats.intelligence)) {
			data.target.addExp(10);
			data.fight.addTurnDescription(data.activeFighter.name + " couldn't curse " + data.target.name + "!");
			return;
		}

		int duration = 6;
		if (data.targetStats.classes.contains(FighterClass.SPELL_VOID)) {
			duration -= 2;
		}
		duration -= fluffer10kFun.fightActionUtils.getNegativeStatusDurationReduction(data.target);

		data.activeFighter.addExp(10);
		data.target.statuses.addStatus(new FighterStatusData(FighterStatus.MUMMY_CURSE).duration(duration));
		data.fight.addTurnDescription(action.description(data.activeFighter.name, data.target.name));
	}
}
