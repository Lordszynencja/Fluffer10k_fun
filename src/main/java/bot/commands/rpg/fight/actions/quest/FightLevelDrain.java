package bot.commands.rpg.fight.actions.quest;

import static bot.commands.rpg.RPGExpUtils.getExpForLevel;
import static bot.util.RandomUtils.getRandomBoolean;
import static java.lang.Math.max;
import static java.lang.Math.min;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.FightActionsHandler.FightActionHandler;
import bot.commands.rpg.fight.FightTempData;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.Targetting;
import bot.commands.rpg.fight.Targetting.TargetCheck;
import bot.data.fight.PlayerFighterData;
import bot.userData.ServerUserData;

public class FightLevelDrain implements FightActionHandler {
	public static Targetting getDefaultTargetting() {
		return new Targetting(TargetCheck.ENEMY.alive().player());
	}

	private static final Targetting defaultTargetting = getDefaultTargetting();

	private final Fluffer10kFun fluffer10kFun;

	public FightLevelDrain(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final FightTempData data, final RPGFightAction action) {
		data.setUpTarget(fluffer10kFun, defaultTargetting);

		final PlayerFighterData player = data.target.player();
		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(player);

		final double chance;
		int drain;
		int statDrain;
		if (action == RPGFightAction.LEVEL_DRAIN_0) {
			chance = 1 - userData.blessings.blessings / 3.0;
			drain = 10;
			statDrain = 5;
		} else {
			chance = 1 - 0.75 * (userData.blessings.blessings) / 21;
			drain = 15;
			statDrain = 7;
		}
		if (!getRandomBoolean(chance)) {
			data.fight.addTurnDescription(data.target.name + " is saved from level drain by the blessing!");
			return;
		}

		player.level -= drain;
		if (player.level < 0) {
			player.level = 0;
		}
		userData.rpg.exp = getExpForLevel(player.level);
		userData.rpg.level = player.level;
		userData.rpg.improvementPoints = 0;
		userData.rpg.strength = max(1, userData.rpg.strength - statDrain);
		userData.rpg.agility = max(1, userData.rpg.agility - statDrain);
		userData.rpg.intelligence = max(1, userData.rpg.intelligence - statDrain);

		if (player.level <= 0) {
			player.maxHp = 1;
			player.hp = 0;
		} else {
			player.maxHp = player.level + (int) (userData.rpg.strength * 1.5);
			if (player.maxHp < 1) {
				player.maxHp = 1;
			}
			player.hp = min(player.hp, player.maxHp);
			if (player.hp < 1) {
				player.hp = 1;
			}
		}

		data.fight.addTurnDescription(action.description(data.target.name));
	}
}
