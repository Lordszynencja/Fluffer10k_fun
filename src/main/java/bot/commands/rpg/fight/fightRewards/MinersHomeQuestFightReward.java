package bot.commands.rpg.fight.fightRewards;

import static java.util.stream.Collectors.counting;

import org.javacord.api.entity.user.User;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.FightEnd.RewardCreator;
import bot.commands.rpg.fight.FightTempData;
import bot.userData.ServerUserData;

public class MinersHomeQuestFightReward implements RewardCreator {

	private final Fluffer10kFun fluffer10kFun;

	public MinersHomeQuestFightReward(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void giveRewards(final FightTempData data) {
		final int enemiesDefeated = (int) (long) data.fight.fighters.values().stream()//
				.map(fighter -> fighter.enemy())//
				.filter(fighter -> fighter != null)//
				.collect(counting());

		data.fight.fighters.values().stream()//
				.map(fighter -> fighter.player())//
				.filter(player -> player != null)//
				.forEach(player -> {
					final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(player);
					final User user = fluffer10kFun.apiUtils.getUser(player.userId);
					fluffer10kFun.questUtils.questMinersHome().afterFightWon(data.channel, userData, user,
							enemiesDefeated);
				});
	}

}
