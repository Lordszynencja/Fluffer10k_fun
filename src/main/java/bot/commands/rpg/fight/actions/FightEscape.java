package bot.commands.rpg.fight.actions;

import static bot.util.RandomUtils.clash;

import bot.Fluffer10kFun;
import bot.commands.rpg.RPGStatUtils.RPGStatsData;
import bot.commands.rpg.fight.FightActionsHandler.FightActionHandler;
import bot.commands.rpg.fight.FightTempData;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.Targetting;
import bot.commands.rpg.fight.Targetting.TargetCheck;
import bot.data.fight.FighterData;
import bot.data.fight.FighterData.FighterType;
import bot.data.fight.PlayerFighterData;
import bot.userData.ServerUserData;
import bot.userData.UserBlessingData.Blessing;

public class FightEscape implements FightActionHandler {

	private static Targetting chasersTargetting = new Targetting(TargetCheck.ENEMY.alive());

	private final Fluffer10kFun fluffer10kFun;

	public FightEscape(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;
	}

	private boolean escapeSuccessful(final FightTempData data) {
		final PlayerFighterData player = data.activeFighter.player();
		if (player != null) {
			final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(player);
			if (userData.blessings.blessingsObtained.contains(Blessing.FAST_RUNNER)) {
				return true;
			}
		}

		final int playerStatTotal = data.activeFighterStats.strength + data.activeFighterStats.agility
				+ data.activeFighterStats.intelligence;
		int enemyStatTotal = 0;
		for (final FighterData enemy : data.getGroup(chasersTargetting)) {
			final RPGStatsData enemyStats = fluffer10kFun.rpgStatUtils.getTotalStatsInFight(enemy);
			enemyStatTotal += enemyStats.strength + enemyStats.agility + enemyStats.intelligence;
		}

		return clash(playerStatTotal, enemyStatTotal);
	}

	private void lost(final FightTempData data) {
		data.activeFighter.lost = true;
		if (data.activeFighter.type == FighterType.PLAYER) {
			final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(data.activeFighter.player());
			userData.reduceStamina(5);

			data.fight.addTurnDescription(data.activeFighter.name + " got caught while trying to escape!");
		}
	}

	private void escaped(final FightTempData data, final RPGFightAction action) {
		data.activeFighter.escaped = true;
		data.fight.addTurnDescription(action.description(data.activeFighter.name));
	}

	@Override
	public void handle(final FightTempData data, final RPGFightAction action) {
		if (escapeSuccessful(data)) {
			escaped(data, action);
		} else {
			lost(data);
		}
	}
}
