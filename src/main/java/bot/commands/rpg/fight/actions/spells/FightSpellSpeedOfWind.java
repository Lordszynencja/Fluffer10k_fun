package bot.commands.rpg.fight.actions.spells;

import static bot.commands.rpg.spells.ActiveSkill.SPEED_OF_WIND;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.FightActionsHandler.FightActionHandler;
import bot.commands.rpg.fight.FightTempData;
import bot.commands.rpg.fight.RPGFightAction;
import bot.data.fight.FighterStatus;
import bot.data.fight.FighterStatusData;
import bot.data.fight.PlayerFighterData;
import bot.userData.ServerUserData;
import bot.userData.UserBlessingData.Blessing;

public class FightSpellSpeedOfWind implements FightActionHandler {

	private final Fluffer10kFun fluffer10kFun;

	public FightSpellSpeedOfWind(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final FightTempData data, final RPGFightAction action) {
		final boolean spellCast = fluffer10kFun.fightActionUtils.castSpell(data, SPEED_OF_WIND, 2);
		if (!spellCast) {
			return;
		}

		int duration = 3 + (int) (fluffer10kFun.fightActionUtils.getMagicStrength(data) * 2
				+ data.activeFighterStats.agility / 4.0);
		final PlayerFighterData player = data.activeFighter.player();
		if (player != null) {
			final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(player);
			if (userData.blessings.blessingsObtained.contains(Blessing.CHANNELING)
					&& player.lastSpellUsed == SPEED_OF_WIND) {
				duration += 1;
			}
		}
		data.activeFighter.addExp(duration * 5);

		data.activeFighter.statuses.addStatus(new FighterStatusData(FighterStatus.SPEED_OF_WIND).duration(duration));
		data.fight.addTurnDescription(action.description(data.activeFighter.name));

		if (player != null) {
			player.lastSpellUsed = SPEED_OF_WIND;
		}
	}
}