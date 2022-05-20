package bot.commands.rpg.fight.actions.spells;

import static bot.commands.rpg.spells.ActiveSkill.RAGE;
import static bot.data.fight.FighterStatus.ENRAGED;
import static bot.userData.UserBlessingData.Blessing.CHANNELING;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.FightActionsHandler.FightActionHandler;
import bot.commands.rpg.fight.FightTempData;
import bot.commands.rpg.fight.RPGFightAction;
import bot.data.fight.FighterStatusData;
import bot.data.fight.PlayerFighterData;
import bot.userData.ServerUserData;

public class FightSpellRage implements FightActionHandler {

	private final Fluffer10kFun fluffer10kFun;

	public FightSpellRage(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final FightTempData data, final RPGFightAction action) {
		final boolean spellCast = fluffer10kFun.fightActionUtils.castSpell(data, RAGE, 2);
		if (!spellCast) {
			return;
		}

		int duration = 3 + (int) (fluffer10kFun.fightActionUtils.getMagicStrength(data) * 2
				+ data.activeFighterStats.strength / 4.0);
		final PlayerFighterData player = data.activeFighter.player();
		if (player != null) {
			final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(player);
			if (userData.blessings.blessingsObtained.contains(CHANNELING) && player.lastSpellUsed == RAGE) {
				duration += 1;
			}
		}

		data.activeFighter.addExp(duration * 5);

		data.activeFighter.statuses.addStatus(new FighterStatusData(ENRAGED).duration(duration));
		data.fight.addTurnDescription(action.description(data.activeFighter.name));
	}
}
