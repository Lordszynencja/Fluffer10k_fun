package bot.commands.rpg.fight.actions.spells;

import static bot.commands.rpg.spells.ActiveSkill.MAGIC_SHIELD;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.FightActionsHandler.FightActionHandler;
import bot.commands.rpg.fight.FightTempData;
import bot.commands.rpg.fight.RPGFightAction;
import bot.data.fight.FighterData.FighterType;
import bot.data.fight.FighterStatus;
import bot.data.fight.FighterStatusData;
import bot.data.fight.PlayerFighterData;

public class FightSpellMagicShield implements FightActionHandler {
	private final Fluffer10kFun fluffer10kFun;

	public FightSpellMagicShield(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final FightTempData data, final RPGFightAction action) {
		final boolean spellCast = fluffer10kFun.fightActionUtils.castSpell(data, MAGIC_SHIELD, 1);
		if (!spellCast) {
			return;
		}

		data.activeFighter.addExp(10);
		if (data.activeFighter.statuses.isStatus(FighterStatus.MAGIC_SHIELD)) {
			data.activeFighter.statuses.removeStatus(FighterStatus.MAGIC_SHIELD);
			data.fight.addTurnDescription(data.activeFighter.name + " disables magic shield.");
		} else {
			final FighterStatusData status = new FighterStatusData(FighterStatus.MAGIC_SHIELD);
			if (data.activeFighter.type == FighterType.PLAYER) {
				status.endless();
			} else {
				status.duration(10);
			}
			data.activeFighter.statuses.addStatus(status);
			data.fight.addTurnDescription(action.description(data.activeFighter.name));
		}

		final PlayerFighterData player = data.activeFighter.player();
		if (player != null) {
			player.lastSpellUsed = MAGIC_SHIELD;
		}
	}
}
