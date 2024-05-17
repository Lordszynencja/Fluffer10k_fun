package bot.commands.rpg.fight.actions.spells;

import static bot.commands.rpg.spells.ActiveSkill.FORCE_HIT;
import static bot.data.fight.FighterClass.MECHANICAL;
import static bot.data.quests.QuestType.HERO_ACADEMY_QUEST;
import static bot.userData.rpg.questData.QuestStep.WARLOCK_2;

import org.javacord.api.entity.channel.TextChannel;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.FightActionsHandler.FightActionHandler;
import bot.commands.rpg.fight.FightTempData;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.Targetting;
import bot.commands.rpg.fight.Targetting.TargetCheck;
import bot.data.fight.PlayerFighterData;
import bot.userData.ServerUserData;

public class FightSpellForceHit implements FightActionHandler {
	private static final Targetting defaultTargetting = //
			new Targetting(TargetCheck.ENEMY.alive());

	private final Fluffer10kFun fluffer10kFun;

	public FightSpellForceHit(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;
	}

	private void checkQuest(final TextChannel channel, final PlayerFighterData player) {
		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(player);
		if (userData.rpg.questIsOnStep(HERO_ACADEMY_QUEST, WARLOCK_2)) {
			fluffer10kFun.questUtils.questHeroAcademy().continueWarlock2Step(channel, userData);
		}
	}

	@Override
	public void handle(final FightTempData data, final RPGFightAction action) {
		data.setUpTarget(fluffer10kFun, defaultTargetting);

		final boolean spellCast = fluffer10kFun.fightActionUtils.castSpell(data, FORCE_HIT, 1);
		if (!spellCast) {
			return;
		}

		final PlayerFighterData player = data.activeFighter.player();
		if (player != null) {
			checkQuest(data.channel, player);
		}

		int baseDamage = 1;
		if (data.targetStats.classes.contains(MECHANICAL)) {
			baseDamage -= 2;
		}

		fluffer10kFun.fightActionUtils.hitDamagingSpell(data, baseDamage, FORCE_HIT, action);

		if (player != null) {
			player.lastSpellUsed = FORCE_HIT;
		}
	}

}
