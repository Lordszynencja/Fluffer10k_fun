package bot.commands.rpg.fight.actions.spells;

import static bot.commands.rpg.spells.ActiveSkill.SLEEP;
import static bot.data.fight.FighterClass.MECHANICAL;
import static bot.data.fight.FighterClass.SPELL_VOID;
import static bot.userData.UserBlessingData.Blessing.CHANNELING;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.FightActionsHandler.FightActionHandler;
import bot.commands.rpg.fight.FightTempData;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.Targetting;
import bot.commands.rpg.fight.Targetting.TargetCheck;
import bot.data.fight.FighterClass;
import bot.data.fight.FighterStatus;
import bot.data.fight.FighterStatusData;
import bot.data.fight.PlayerFighterData;
import bot.userData.ServerUserData;

public class FightSpellSleep implements FightActionHandler {
	public static Targetting getDefaultTargetting(final Fluffer10kFun fluffer10kFun) {
		final TargetCheck validTarget = TargetCheck.ENEMY.alive().without(fluffer10kFun, FighterClass.MECHANICAL);

		return new Targetting(validTarget.without(FighterStatus.SLEEP))//
				.orElse(validTarget);
	}

	private static Targetting defaultTargetting;

	private final Fluffer10kFun fluffer10kFun;

	public FightSpellSleep(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;

		defaultTargetting = getDefaultTargetting(fluffer10kFun);
	}

	@Override
	public void handle(final FightTempData data, final RPGFightAction action) {
		data.setUpTarget(fluffer10kFun, defaultTargetting);
		if (data.target == null) {
			data.fight.addTurnDescription(data.activeFighter.name + " has no one to put to sleep!");
			return;
		}
		if (data.targetStats.classes.contains(MECHANICAL)) {
			data.target.addExp(10);
			data.fight.addTurnDescription(data.activeFighter.name + " fails to put " + data.target.name + " to sleep!");
			return;
		}

		final boolean spellCast = fluffer10kFun.fightActionUtils.castSpell(data, SLEEP, 6);
		if (!spellCast) {
			return;
		}

		int duration = 4;
		final PlayerFighterData player = data.activeFighter.player();
		if (player != null) {
			final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(player);
			if (userData.blessings.blessingsObtained.contains(CHANNELING) && player.lastSpellUsed == SLEEP) {
				duration++;
			}
		}

		if (data.targetStats.classes.contains(SPELL_VOID)) {
			data.activeFighter.addExp(3);
			data.target.addExp(5);
			duration--;
		}
		duration -= fluffer10kFun.fightActionUtils.getNegativeStatusDurationReduction(data.target);

		data.activeFighter.addExp(duration * 5);
		data.target.addExp(duration * 3);

		if (duration <= 0) {
			data.fight.addTurnDescription(data.activeFighter.name + " fails to put " + data.target.name + " to sleep!");
			return;
		}
		data.target.statuses.addStatus(new FighterStatusData(FighterStatus.SLEEP).duration(duration));
		data.fight.addTurnDescription(action.description(data.activeFighter.name, data.target.name));

		if (player != null) {
			player.lastSpellUsed = SLEEP;
		}
	}

}
