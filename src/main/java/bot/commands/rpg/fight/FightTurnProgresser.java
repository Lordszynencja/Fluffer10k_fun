package bot.commands.rpg.fight;

import static bot.util.RandomUtils.roll;
import static java.lang.Math.min;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.javacord.api.entity.channel.ServerTextChannel;

import bot.Fluffer10kFun;
import bot.commands.rpg.RPGStatUtils.RPGStatsData;
import bot.data.fight.FightData;
import bot.data.fight.FighterClass;
import bot.data.fight.FighterData;
import bot.data.fight.FighterData.FighterType;
import bot.data.fight.FighterStatus;
import bot.data.fight.FighterStatusData;
import bot.data.fight.PlayerFighterData;
import bot.data.quests.QuestType;
import bot.userData.ServerUserData;
import bot.userData.rpg.questData.QuestStep;

public class FightTurnProgresser {

	private final Fluffer10kFun fluffer10kFun;

	public FightTurnProgresser(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;
	}

	private void tickBleeding(final FightTempData data) {
		if (!data.activeFighter.statuses.isStatus(FighterStatus.BLEEDING)) {
			return;
		}

		final int stacks = data.activeFighter.statuses.getStacks(FighterStatus.BLEEDING);
		if (data.activeFighter.hp >= 1) {
			data.activeFighter.addExp(stacks * 2);
			data.activeFighter.hp -= stacks;
			if (data.activeFighter.hp < 0) {
				data.activeFighter.hp = 0;
			}
			data.fight.addTurnDescription(data.activeFighter.name + " bleeds for " + stacks + " damage.");
		}
	}

	private void tickHolyAura(final FightTempData data) {
		if (!data.activeFighter.statuses.isStatus(FighterStatus.HOLY_AURA)) {
			return;
		}

		for (final FighterData target : data.fight.fighters.values()) {
			final RPGStatsData targetStats = fluffer10kFun.rpgStatUtils.getTotalStatsInFight(target);
			if (targetStats.classes.contains(FighterClass.UNDEAD)) {
				target.attacked = true;
				if (target.hp >= 1) {
					data.activeFighter.addExp(5);
					target.addExp(5);
					target.hp -= 1;
					data.fight.addTurnDescription(
							target.name + " gets struck by " + data.activeFighter.name + "'s holy aura.");
				}
			}
		}
	}

	private void tickKejorouHair(final FighterData fighter) {
		final FighterStatusData kejorouHair = fighter.statuses.statuses.get(FighterStatus.KEJOUROU_HAIR);
		if (kejorouHair != null) {
			kejorouHair.stacks++;
			if (kejorouHair.stacks > 4) {
				kejorouHair.stacks = 4;
			}
		}
	}

	private void tickFieryWeapon(final FighterData fighter) {
		if (fighter.type != FighterType.PLAYER || !fighter.statuses.isStatus(FighterStatus.FIERY_WEAPON)) {
			return;
		}

		final PlayerFighterData playerFighter = (PlayerFighterData) fighter;
		if (playerFighter.mana < 1) {
			fighter.statuses.removeStatus(FighterStatus.FIERY_WEAPON);
			fighter.fight.addTurnDescription(
					fighter.name + "'s weapon is no longer covered in flames because of lacking mana.");
		} else {
			playerFighter.mana -= 1;
		}
	}

	private void tickMagicShield(final FighterData fighter) {
		if (fighter.type != FighterType.PLAYER || !fighter.statuses.isStatus(FighterStatus.MAGIC_SHIELD)) {
			return;
		}

		final PlayerFighterData playerFighter = (PlayerFighterData) fighter;
		if (playerFighter.mana < 1) {
			fighter.statuses.removeStatus(FighterStatus.MAGIC_SHIELD);
			fighter.fight.addTurnDescription(fighter.name + "'s magic shield vanishes because of lacking mana.");
		} else {
			playerFighter.mana -= 1;
		}
	}

	private void tickFighterStatuses(final FightTempData data) {
		tickBleeding(data);
		tickHolyAura(data);
		tickKejorouHair(data.activeFighter);
		tickFieryWeapon(data.activeFighter);
		tickMagicShield(data.activeFighter);

		final boolean statusNegation = data.activeFighterStats.classes.contains(FighterClass.STATUS_NEGATION);

		for (final FighterStatusData status : data.activeFighter.statuses.getSortedStatuses()) {
			if (status.endless) {
				continue;
			}

			int tick = 1;
			if (status.type.negativeExpiringFaster && statusNegation) {
				tick++;
			}

			status.duration -= tick;
			if (status.duration <= 0) {
				data.activeFighter.addExp(10);
				data.activeFighter.statuses.removeStatus(status.type);
				data.fight.addTurnDescription(String.format(status.type.expireMessage, data.activeFighter.name));
			}
		}
	}

	private void markLostFighters(final FightData fight) {
		for (final FighterData fighter : fight.fighters.values()) {
			fighter.updateLost(fluffer10kFun);
		}
	}

	private void checkQuest(final ServerTextChannel channel, final PlayerFighterData fighter) {
		if (fighter.mana == 0) {
			final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(fighter);
			if (userData.rpg.questIsOnStep(QuestType.HERO_ACADEMY_QUEST, QuestStep.MAGE_2)) {
				fluffer10kFun.questUtils.questHeroAcademy().continueMage2Step(channel, userData,
						fluffer10kFun.apiUtils.getUser(fighter.userId));
			}
		}
	}

	private void regenMana(final FightTempData data) {
		if (data.activeFighter.isOut() || data.activeFighter.type != FighterType.PLAYER) {
			return;
		}

		final PlayerFighterData player = (PlayerFighterData) data.activeFighter;

		final double turnPenalty = (1.2 - (player.fight.turn / (data.fight.turn + 10)));
		final double manaRegenBonus = 1 + data.activeFighterStats.manaRegenBonus / 100.0;
		final double rollPower = 0.7 * data.activeFighterStats.intelligence / 4.0 * turnPenalty * manaRegenBonus;
		final int manaRegen = min(roll(rollPower), player.maxMana - player.mana);

		if (manaRegen > 0) {
			player.mana += manaRegen;
			data.fight.addTurnDescription(player.name + " regenerates " + manaRegen + " mana");

			final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(player);
			if (userData.rpg.questIsOnStep(QuestType.HERO_ACADEMY_QUEST, QuestStep.MAGE_3)) {
				fluffer10kFun.questUtils.questHeroAcademy().continueMage3Step(data.channel, userData);
			}
		}
	}

	private void tickHPRegen(final FightTempData data) {
		final int regen = min(data.activeFighterStats.healthRegenerationBonus,
				data.activeFighter.maxHp - data.activeFighter.hp);

		if (regen > 0) {
			data.activeFighter.hp += regen;
			data.fight.addTurnDescription(data.activeFighter.name + " regenerates " + regen + " hp");
		}
	}

	private boolean fightEnded(final FightData fight) {
		final Set<String> teamsLeft = new HashSet<>();

		fight.fighters.values().stream().filter(fighter -> !fighter.isOut())//
				.map(fighter -> fighter.team)//
				.forEach(teamsLeft::add);

		return teamsLeft.size() <= 1;
	}

	private void progressTurn(final FightTempData data) {
		data.fight.turnProgress++;
		if (data.fight.turnProgress >= data.fight.fightersOrder.size()) {
			data.fight.turn++;
			data.fight.turnProgress = 0;
		}

		while (data.fight.turnDescriptions.size() > data.fight.fightersOrder.size()) {
			data.fight.turnDescriptions.remove(0);
		}
		data.fight.turnDescriptions.add(new ArrayList<>());
	}

	public void endTurn(final FightTempData data) {
		tickFighterStatuses(data);
		markLostFighters(data.fight);

		if (data.activeFighter.type == FighterType.PLAYER) {
			checkQuest(data.channel, (PlayerFighterData) data.activeFighter);
		}

		tickHPRegen(data);
		regenMana(data);

		data.fight.ended = fightEnded(data.fight);
		if (data.fight.ended) {
			return;
		}

		progressTurn(data);
	}

	public void startTurn(final FightTempData data) {
		data.resetFighterData(fluffer10kFun);

		int actions = 1;

		if (data.activeFighterStats.classes.contains(FighterClass.DOUBLE_ATTACK)) {
			actions = 2;
		}
		if (data.activeFighterStats.classes.contains(FighterClass.TRIPLE_ATTACK)) {
			actions = 3;
		}

		if (data.activeFighter.statuses.isStatus(FighterStatus.DOPPELGANGER)) {
			actions *= 2;
		}

		data.fight.actionsLeft = actions;
	}
}
