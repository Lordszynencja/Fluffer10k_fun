package bot.commands.rpg.fight.fightRewards;

import org.javacord.api.entity.user.User;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.FightEnd.RewardCreator;
import bot.commands.rpg.fight.FightTempData;
import bot.data.fight.FightData;
import bot.data.fight.FighterData;
import bot.userData.ServerUserData;

public class BusinessAsUsualQuestFightReward implements RewardCreator {

	private final Fluffer10kFun fluffer10kFun;

	public BusinessAsUsualQuestFightReward(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;
	}

	private static boolean playersWon(final FightData fight) {
		for (final FighterData fighter : fight.fighters.values()) {
			if (!fighter.isOut() && fighter.player() != null) {
				return true;
			}
		}

		return false;
	}

	@Override
	public void giveRewards(final FightTempData data) {
		if (!playersWon(data.fight)) {
			return;
		}

		data.fight.fighters.values().stream()//
				.map(fighter -> fighter.player())//
				.filter(player -> player != null)//
				.forEach(player -> {
					final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(player);
					final User user = fluffer10kFun.apiUtils.getUser(player.userId);
					fluffer10kFun.questUtils.questBusinessAsUsual1().afterFightWon(data.channel, userData, user);
				});
	}

}
