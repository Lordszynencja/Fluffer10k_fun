package bot.commands.rpg.fight.actions.spells;

import static bot.commands.rpg.fight.actions.FightActionUtils.addFreezingStack;
import static bot.commands.rpg.spells.ActiveSkill.FREEZE;
import static bot.commands.rpg.spells.ActiveSkill.SLEEP;
import static bot.data.fight.FighterClass.CANT_BE_FROZEN;
import static bot.data.fight.FighterClass.FIERY;
import static bot.data.fight.FighterClass.ICY;
import static bot.data.fight.FighterClass.MECHANICAL;
import static bot.data.fight.FighterClass.SPELL_VOID;
import static bot.data.fight.FighterClass.WET;
import static bot.data.fight.FighterStatus.FROZEN;
import static bot.userData.UserBlessingData.Blessing.CHANNELING;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.FightActionsHandler.FightActionHandler;
import bot.commands.rpg.fight.FightTempData;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.Targetting;
import bot.commands.rpg.fight.Targetting.TargetCheck;
import bot.data.fight.FighterStatus;
import bot.data.fight.FighterStatusData;
import bot.data.fight.PlayerFighterData;
import bot.userData.ServerUserData;

public class FightSpellFreeze implements FightActionHandler {
	private final Targetting defaultTargetting;

	private final Fluffer10kFun fluffer10kFun;

	public FightSpellFreeze(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;

		defaultTargetting = new Targetting(
				TargetCheck.ENEMY.alive().without(fluffer10kFun, CANT_BE_FROZEN).without(FROZEN))//
						.orElse(TargetCheck.ENEMY.alive().without(fluffer10kFun, CANT_BE_FROZEN));
	}

	private void fieryTarget(final FightTempData data, final RPGFightAction action) {
		final int baseDamage = 4;

		final int dmg = fluffer10kFun.fightActionUtils.hitDamagingSpell(data, baseDamage, FREEZE, action);

		if (dmg > 0) {
			addFreezingStack(data);
		}

		final PlayerFighterData player = data.activeFighter.player();
		if (player != null) {
			player.lastSpellUsed = FREEZE;
		}
	}

	@Override
	public void handle(final FightTempData data, final RPGFightAction action) {
		data.setUpTarget(fluffer10kFun, defaultTargetting);

		final boolean spellCast = fluffer10kFun.fightActionUtils.castSpell(data, FREEZE, 10);
		if (!spellCast) {
			return;
		}

		if (data.targetStats.classes.contains(FIERY)) {
			fieryTarget(data, action);
			return;
		}

		int duration = 4;
		final PlayerFighterData player = data.activeFighter.player();
		if (player != null) {
			final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(player);
			if (userData.blessings.blessingsObtained.contains(CHANNELING) && player.lastSpellUsed == FREEZE) {
				duration += 1;
			}
		}
		if (data.targetStats.classes.contains(SPELL_VOID)) {
			duration -= 2;
		}
		if (data.targetStats.classes.contains(ICY)) {
			duration -= 2;
		}
		if (data.targetStats.classes.contains(WET)) {
			duration += 2;
		}
		if (data.targetStats.classes.contains(MECHANICAL)) {
			duration -= 2;
		}
		if (data.targetStats.classes.contains(CANT_BE_FROZEN)) {
			duration = 0;
		}

		data.activeFighter.addExp(duration * 5);
		data.target.addExp(duration * 5);

		if (duration <= 0) {
			data.fight.addTurnDescription(data.activeFighter.name + " fails to freeze " + data.target.name + "!");
			return;
		}

		data.target.statuses.addStatus(new FighterStatusData(FighterStatus.FROZEN).duration(duration));
		data.fight.addTurnDescription(action.description(data.activeFighter.name, data.target.name));

		addFreezingStack(data);

		if (player != null) {
			player.lastSpellUsed = SLEEP;
		}
	}
}
