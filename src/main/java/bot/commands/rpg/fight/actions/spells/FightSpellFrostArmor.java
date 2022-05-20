package bot.commands.rpg.fight.actions.spells;

import static bot.commands.rpg.spells.ActiveSkill.FROST_ARMOR;
import static bot.commands.rpg.spells.ActiveSkill.SLEEP;
import static bot.userData.UserBlessingData.Blessing.CHANNELING;
import static bot.util.RandomUtils.roll;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.FightActionsHandler.FightActionHandler;
import bot.commands.rpg.fight.FightTempData;
import bot.commands.rpg.fight.RPGFightAction;
import bot.data.fight.FighterStatus;
import bot.data.fight.FighterStatusData;
import bot.data.fight.PlayerFighterData;
import bot.userData.ServerUserData;

public class FightSpellFrostArmor implements FightActionHandler {

	private final Fluffer10kFun fluffer10kFun;

	public FightSpellFrostArmor(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final FightTempData data, final RPGFightAction action) {
		final boolean spellCast = fluffer10kFun.fightActionUtils.castSpell(data, FROST_ARMOR, 3);
		if (!spellCast) {
			return;
		}

		int duration = 5 + roll(fluffer10kFun.fightActionUtils.getMagicStrength(data) * 2);
		final PlayerFighterData player = data.activeFighter.player();
		if (player != null) {
			final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(player);
			if (userData.blessings.blessingsObtained.contains(CHANNELING) && player.lastSpellUsed == FROST_ARMOR) {
				duration += 1;
			}
		}
		data.activeFighter.addExp(duration * 3);

		data.activeFighter.statuses.addStatus(new FighterStatusData(FighterStatus.FROST_ARMOR).duration(duration));
		data.fight.addTurnDescription(action.description(data.activeFighter.name));

		if (player != null) {
			player.lastSpellUsed = SLEEP;
		}
	}

}
