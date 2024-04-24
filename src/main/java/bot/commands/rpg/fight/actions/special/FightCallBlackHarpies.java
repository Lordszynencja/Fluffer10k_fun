package bot.commands.rpg.fight.actions.special;

import static bot.util.RandomUtils.getRandomInt;

import org.javacord.api.entity.server.Server;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.FightActionsHandler.FightActionHandler;
import bot.commands.rpg.fight.FightTempData;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.races.BlackHarpy;

public class FightCallBlackHarpies implements FightActionHandler {
	private final Fluffer10kFun fluffer10kFun;

	public FightCallBlackHarpies(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final FightTempData data, final RPGFightAction action) {
		final Server server = data.channel.getServer();

		final int numberOfHarpies = getRandomInt(1, 3);
		for (int i = 0; i < numberOfHarpies; i++) {
			final String enemyId = BlackHarpy.getId(getRandomInt(6));
			fluffer10kFun.fighterCreator.addFighter(server, data.fight, enemyId, data.activeFighter.team);
		}

		data.fight.addTurnDescription(action.description(data.activeFighter.name));
	}
}