package bot.commands.rpg.fight.actions.spells;

import static bot.commands.rpg.spells.ActiveSkill.LIFE_DRAIN;
import static bot.data.fight.FighterClass.MECHANICAL;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.FightActionsHandler.FightActionHandler;
import bot.commands.rpg.fight.FightTempData;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.Targetting;
import bot.commands.rpg.fight.Targetting.TargetCheck;
import bot.data.fight.PlayerFighterData;

public class FightSpellLifeDrain implements FightActionHandler {
	private final Targetting defaultTargetting;

	private final Fluffer10kFun fluffer10kFun;

	public FightSpellLifeDrain(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;

		defaultTargetting = new Targetting(TargetCheck.ENEMY.alive().without(fluffer10kFun, MECHANICAL));
	}

	@Override
	public void handle(final FightTempData data, final RPGFightAction action) {
		data.setUpTarget(fluffer10kFun, defaultTargetting);
		if (data.target == null) {
			data.fight.addTurnDescription(data.activeFighter.name + " has no one to drain!");
			return;
		}

		final boolean spellCast = fluffer10kFun.fightActionUtils.castSpell(data, LIFE_DRAIN, 6);
		if (!spellCast) {
			return;
		}

		if (data.targetStats.classes.contains(MECHANICAL)) {
			data.fight.addTurnDescription(
					data.activeFighter.name + " tries to drain " + data.target.name + ", but it can't be done!");
			return;
		}

		final int baseDamage = 1;
		final int dmg = fluffer10kFun.fightActionUtils.hitDamagingSpell(data, baseDamage, LIFE_DRAIN, action);
		final int heal = Math.min(data.activeFighter.maxHp - data.activeFighter.hp, dmg);

		if (heal > 0) {
			data.activeFighter.hp += heal;
			data.fight.addTurnDescription(data.activeFighter.name + " heals for " + heal + " hp.");
		}

		final PlayerFighterData player = data.activeFighter.player();
		if (player != null) {
			player.lastSpellUsed = LIFE_DRAIN;
		}
	}
}
