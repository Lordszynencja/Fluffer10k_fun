package bot.commands.rpg.fight.actions.spells;

import static bot.commands.rpg.spells.ActiveSkill.HEAL;
import static bot.data.fight.FighterStatus.BLEEDING;
import static bot.userData.UserBlessingData.Blessing.CHANNELING;
import static bot.util.RandomUtils.roll;
import static java.lang.Math.max;
import static java.lang.Math.min;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.FightActionsHandler.FightActionHandler;
import bot.commands.rpg.fight.FightTempData;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.Targetting;
import bot.commands.rpg.fight.Targetting.TargetCheck;
import bot.commands.rpg.skills.Skill;
import bot.data.fight.FighterClass;
import bot.data.fight.PlayerFighterData;
import bot.userData.ServerUserData;

public class FightSpellHeal implements FightActionHandler {
	public static Targetting getDefaultTargetting(final Fluffer10kFun fluffer10kFun) {
		return new Targetting(TargetCheck.ALLY.alive().damaged().without(fluffer10kFun, FighterClass.MECHANICAL));
	}

	private final Targetting defaultTargetting;

	private final Fluffer10kFun fluffer10kFun;

	public FightSpellHeal(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;

		defaultTargetting = getDefaultTargetting(fluffer10kFun);
	}

	@Override
	public void handle(final FightTempData data, final RPGFightAction action) {
		data.setUpTarget(fluffer10kFun, defaultTargetting);
		if (data.target == null) {
			data.fight
					.addTurnDescription(data.activeFighter.name + " wants to heal someone but there's no one to heal.");
			return;
		}

		final boolean spellCast = fluffer10kFun.fightActionUtils.castSpell(data, HEAL, 3);
		if (!spellCast) {
			return;
		}
		if (data.targetStats.classes.contains(FighterClass.MECHANICAL)) {
			data.fight.addTurnDescription(data.activeFighter.name + " tries to heal mechanical companion.");
			return;
		}

		final double healPower = (1 + fluffer10kFun.fightActionUtils.getMagicStrength(data) / 2)
				* (1 + data.target.maxHp / 15.0);
		final int baseHeal = 1 + (int) (healPower / 2);
		int heal = baseHeal + roll(healPower - baseHeal);
		final PlayerFighterData player = data.activeFighter.player();
		if (player != null) {
			final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(player);
			if (userData.blessings.blessingsObtained.contains(CHANNELING) && player.lastSpellUsed == HEAL) {
				heal += 1;
			}
			if (userData.rpg.skills.contains(Skill.FAST_HEALING_3)) {
				heal *= 2;
			}
		}
		heal = min(heal, max(0, data.target.maxHp - data.target.hp));

		data.activeFighter.addExp(heal * 2);
		data.target.addExp(heal * 2);
		data.target.hp += heal;

		final String targetName = data.target == data.activeFighter ? "themselves" : data.target.name;
		data.fight.addTurnDescription(action.description(data.activeFighter.name, targetName, heal));

		if (data.target.statuses.isStatus(BLEEDING)) {
			data.target.statuses.removeStatus(BLEEDING);
			data.fight.addTurnDescription(data.target.name + " is no longer bleeding!");
		}

		if (player != null) {
			player.lastSpellUsed = HEAL;
		}
	}
}
