package bot.commands.rpg.fight;

import java.util.List;

import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.message.Message;

import bot.Fluffer10kFun;
import bot.commands.rpg.RPGStatUtils.RPGStatsData;
import bot.data.fight.FightData;
import bot.data.fight.FighterData;

public class FightTempData {

	public Message message;
	public ServerTextChannel channel;
	public FightData fight;

	public FighterData activeFighter;
	public RPGStatsData activeFighterStats;

	public String targetId;
	public FighterData target;
	public RPGStatsData targetStats;

	public FightTempData(final Fluffer10kFun fluffer10kFun, final Message message, final FightData fight) {
		setMessage(message);
		this.fight = fight;
	}

	private void setUpActiveFighter(final Fluffer10kFun fluffer10kFun) {
		activeFighter = fight.getCurrentFighter();
		activeFighterStats = fluffer10kFun.rpgStatUtils.getTotalStatsInFight(activeFighter);
	}

	public void setUpTarget(final Fluffer10kFun fluffer10kFun, final Targetting targetting) {
		if (targetId != null) {
			target = fight.fighters.get(targetId);
			if (!targetting.valid(activeFighter, target)) {
				target = null;
			}
		}
		if (target == null) {
			target = targetting.getFirst(fight, activeFighter);
		}

		if (target != null) {
			targetStats = fluffer10kFun.rpgStatUtils.getTotalStatsInFight(target);
		}
	}

	public List<FighterData> getGroup(final Targetting targetting) {
		return targetting.getAll(fight, activeFighter);
	}

	public void setMessage(final Message message) {
		this.message = message;
		channel = message.getServerTextChannel().get();
	}

	public void resetFighterData(final Fluffer10kFun fluffer10kFun) {
		setUpActiveFighter(fluffer10kFun);

		targetId = null;
		target = null;
		targetStats = null;
	}

}
