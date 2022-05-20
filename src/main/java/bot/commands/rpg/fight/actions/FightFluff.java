package bot.commands.rpg.fight.actions;

import static bot.commands.rpg.fight.actions.FightActionUtils.wakeUpDormouseOnAttack;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.FightActionsHandler.FightActionHandler;
import bot.commands.rpg.fight.FightTempData;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.Targetting;
import bot.commands.rpg.fight.Targetting.TargetCheck;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.FighterData.FighterType;
import bot.data.fight.FighterStatus;
import bot.data.fight.PlayerFighterData;
import bot.data.quests.QuestType;
import bot.userData.ServerUserData;
import bot.userData.rpg.questData.UserFluffLoverQuestFluffingStepData;

public class FightFluff implements FightActionHandler {
	private final Fluffer10kFun fluffer10kFun;

	public FightFluff(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final FightTempData data, final RPGFightAction action) {
		if (data.activeFighter.type != FighterType.PLAYER) {
			data.fight.addTurnDescription(RPGFightAction.WAIT.description(data.activeFighter.name));
			return;
		}

		final ServerUserData userData = fluffer10kFun.serverUserDataUtils
				.getUserData((PlayerFighterData) data.activeFighter);
		final UserFluffLoverQuestFluffingStepData quest = (UserFluffLoverQuestFluffingStepData) userData.rpg.quests
				.get(QuestType.FLUFF_LOVER_QUEST);

		final Targetting targetting = new Targetting(
				TargetCheck.ENEMY.monsterGirlOfRaceFrom(fluffer10kFun, quest.girlsLeft));
		data.setUpTarget(fluffer10kFun, targetting);
		final MonsterGirlRace fluffedRace = data.target.enemy().enemyData(fluffer10kFun).mg().race;
		quest.girlsLeft.remove(fluffedRace);
		quest.updateDescription();

		data.fight.addTurnDescription(action.description(data.activeFighter.name, data.target.name));

		data.target.statuses.removeStatus(FighterStatus.SLEEP);
		wakeUpDormouseOnAttack(data);
	}
}
