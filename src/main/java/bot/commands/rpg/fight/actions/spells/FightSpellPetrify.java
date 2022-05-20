package bot.commands.rpg.fight.actions.spells;

import static bot.commands.rpg.spells.ActiveSkill.PETRIFY;
import static bot.data.fight.FighterStatus.PETRIFIED;
import static bot.userData.UserBlessingData.Blessing.CHANNELING;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.FightActionsHandler.FightActionHandler;
import bot.commands.rpg.fight.FightTempData;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.Targetting;
import bot.commands.rpg.fight.Targetting.TargetCheck;
import bot.data.fight.FighterStatusData;
import bot.data.fight.PlayerFighterData;
import bot.userData.ServerUserData;

public class FightSpellPetrify implements FightActionHandler {
	private static final Targetting defaultTargetting = //
			new Targetting(TargetCheck.ENEMY.alive().without(PETRIFIED))//
					.orElse(TargetCheck.ENEMY.alive());

	private final Fluffer10kFun fluffer10kFun;

	public FightSpellPetrify(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final FightTempData data, final RPGFightAction action) {
		data.setUpTarget(fluffer10kFun, defaultTargetting);

		final boolean spellCast = fluffer10kFun.fightActionUtils.castSpell(data, PETRIFY, 9);
		if (!spellCast) {
			return;
		}

		int duration = 4;
		final PlayerFighterData player = data.activeFighter.player();
		if (player != null) {
			final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(player);
			if (userData.blessings.blessingsObtained.contains(CHANNELING) && player.lastSpellUsed == PETRIFY) {
				duration += 1;
			}
		}

		data.activeFighter.addExp(10);
		data.target.addExp(10);

		data.target.statuses.addStatus(new FighterStatusData(PETRIFIED).duration(duration));
		data.fight.addTurnDescription(action.description(data.activeFighter.name, data.target.name));

		if (player != null) {
			player.lastSpellUsed = PETRIFY;
		}
	}
}
