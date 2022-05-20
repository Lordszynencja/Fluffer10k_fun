package bot.commands.rpg.fight.actions.special;

import static bot.data.fight.FighterClass.MECHANICAL;
import static bot.data.fight.FighterClass.SPELL_VOID;
import static bot.data.fight.FighterStatus.CHARMED;
import static bot.data.fight.FighterStatus.CHARM_RESISTANCE;
import static bot.data.fight.FighterStatus.MOTHMAN_POWDER;
import static bot.userData.UserBlessingData.Blessing.NATURAL_CHARM;
import static bot.util.RandomUtils.clash;
import static bot.util.RandomUtils.getRandomBoolean;
import static bot.util.RandomUtils.roll;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.FightActionsHandler.FightActionHandler;
import bot.commands.rpg.fight.FightTempData;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.Targetting;
import bot.commands.rpg.fight.Targetting.TargetCheck;
import bot.commands.rpg.skills.Skill;
import bot.data.fight.FighterData.FighterType;
import bot.data.fight.FighterStatusData;
import bot.data.fight.PlayerFighterData;
import bot.userData.ServerUserData;

public class FightCharm implements FightActionHandler {
	private final Targetting defaultTargetting;

	private final Fluffer10kFun fluffer10kFun;

	public FightCharm(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;

		final TargetCheck validTarget = TargetCheck.ENEMY.alive().without(fluffer10kFun, MECHANICAL);

		defaultTargetting = new Targetting(validTarget.without(CHARMED))//
				.orElse(validTarget);
	}

	@Override
	public void handle(final FightTempData data, final RPGFightAction action) {
		data.setUpTarget(fluffer10kFun, defaultTargetting);
		if (data.target == null) {
			data.fight.addTurnDescription(data.activeFighter.name + " fails to find target for charm!");
			return;
		}

		if (!clash(data.targetStats.intelligence, data.activeFighterStats.intelligence)) {
			data.target.addExp(10);
			data.target.statuses.addStatus(new FighterStatusData(CHARM_RESISTANCE).stacks(1).endless());
			data.fight.addTurnDescription(data.activeFighter.name + " fails to charm " + data.target.name + "!");
			return;
		}

		final int fighterBonus = roll(data.activeFighterStats.intelligence / 4.0);
		final int targetResistance = (int) (data.target.statuses.getStacks(CHARM_RESISTANCE) / 1.5 * data.target.hp
				/ data.target.maxHp);
		int duration = 3 + fighterBonus - targetResistance;
		if (data.targetStats.classes.contains(SPELL_VOID)) {
			data.target.addExp(10);
			duration -= 2;
		}
		if (data.target.statuses.isStatus(MOTHMAN_POWDER)) {
			data.activeFighter.addExp(10);
			duration += 2;
		}
		if (data.targetStats.classes.contains(MECHANICAL)) {
			duration = 0;
		}
		if (duration < 0) {
			duration = 0;
		}
		if (data.target.type == FighterType.PLAYER) {
			final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(data.target.player());
			if (userData.rpg.skills.contains(Skill.STRONG_MIND)) {
				duration--;
			}
		}
		duration -= fluffer10kFun.fightActionUtils.getNegativeStatusDurationReduction(data.target);

		final int exp = 2 * (data.targetStats.intelligence + data.activeFighterStats.intelligence);
		data.activeFighter.addExp(exp);
		data.target.addExp(exp);
		data.target.statuses.addStatus(new FighterStatusData(CHARM_RESISTANCE).stacks(1).endless());

		if (duration <= 0) {
			data.target.addExp(10);
			data.fight.addTurnDescription(data.activeFighter.name + " fails to charm " + data.target.name + "!");
			return;
		}

		data.activeFighter.addExp(10);

		final PlayerFighterData player = data.target.player();
		if (player != null) {
			final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(player);
			if (userData.blessings.blessingsObtained.contains(NATURAL_CHARM) && getRandomBoolean(0.2)) {
				data.activeFighter.statuses.addStatus(new FighterStatusData(CHARMED).duration(3));
				data.fight.addTurnDescription(data.activeFighter.name + " tries to charm " + data.target.name
						+ ", but their natural charm makes her fall in love instead!");
				return;
			}
		}

		data.target.statuses.addStatus(new FighterStatusData(CHARMED).duration(duration));
		data.fight.addTurnDescription(action.description(data.activeFighter.name, data.target.name));
	}
}
