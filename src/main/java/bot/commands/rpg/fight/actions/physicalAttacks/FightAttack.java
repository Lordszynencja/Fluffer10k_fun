package bot.commands.rpg.fight.actions.physicalAttacks;

import static bot.commands.rpg.fight.RPGFightAction.SPECIAL_ATTACK_BASH;
import static bot.commands.rpg.fight.actions.FightActionUtils.wakeUpDormouseOnAttack;
import static bot.data.fight.FighterStatus.DECISIVE_STRIKE;
import static bot.util.RandomUtils.clashChance;
import static bot.util.RandomUtils.getRandomBoolean;
import static bot.util.RandomUtils.roll;
import static java.lang.Math.min;

import java.util.List;
import java.util.Set;

import bot.Fluffer10kFun;
import bot.commands.rpg.RPGStatUtils.RPGStatsData;
import bot.commands.rpg.fight.FightActionsHandler.FightActionHandler;
import bot.commands.rpg.fight.FightTempData;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.Targetting;
import bot.commands.rpg.fight.Targetting.TargetCheck;
import bot.commands.rpg.skills.Skill;
import bot.data.fight.FighterClass;
import bot.data.fight.FighterData.FighterType;
import bot.data.fight.FighterStatus;
import bot.data.fight.FighterStatusData;
import bot.data.fight.PlayerFighterData;
import bot.data.items.ItemClass;
import bot.data.quests.QuestType;
import bot.userData.ServerUserData;
import bot.userData.UserBlessingData.Blessing;
import bot.userData.rpg.questData.QuestStep;
import bot.userData.rpg.questData.UserHeroAcademyQuestRogue2StepData;
import bot.userData.rpg.questData.UserQuestData;

public class FightAttack implements FightActionHandler {
	private static Targetting defaultTarget = new Targetting(TargetCheck.ENEMY.alive());

	private final Fluffer10kFun fluffer10kFun;
	private final WeaponsGetters weaponsGetters;
	private final PhysicalAttackArmor physicalAttackArmor;

	public FightAttack(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;
		weaponsGetters = new WeaponsGetters(fluffer10kFun);
		physicalAttackArmor = new PhysicalAttackArmor(fluffer10kFun);
	}

	private boolean isFar(final RPGStatsData stats) {
		return stats.classes.contains(FighterClass.CLIMBING) || stats.classes.contains(FighterClass.FLYING);
	}

	private int getPowerSkillDmgBonus(final FightTempData data) {
		if (data.activeFighter.type != FighterType.PLAYER) {
			return 0;
		}

		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(data.activeFighter.player());
		if (!userData.rpg.skills.contains(Skill.POWER_1)) {
			return 0;
		}
		if (!userData.rpg.skills.contains(Skill.POWER_2)) {
			return 1;
		}
		if (!userData.rpg.skills.contains(Skill.POWER_3)) {
			return 3;
		}
		if (!userData.rpg.skills.contains(Skill.POWER_4)) {
			return 5;
		}
		if (!userData.rpg.skills.contains(Skill.POWER_5)) {
			return 7;
		}
		return 12;
	}

	private int getBaseDmg(final FightTempData data, final RPGFightAction action, final Weapon weapon) {
		double damageRoll = weapon.dmgRoll + weapon.type.getStatDmgBonus(data.activeFighterStats);
		if (action == SPECIAL_ATTACK_BASH) {
			damageRoll += data.activeFighterStats.strength / 6.0;
		}

		if (data.activeFighter.statuses.isStatus(FighterStatus.ENRAGED)) {
			damageRoll += (int) (damageRoll * 0.25);
		}

		final int rollBaseDmg = (int) (damageRoll * 2 / 3);
		int dmg = 1 + rollBaseDmg + roll(damageRoll - rollBaseDmg) + weapon.magicDamage;
		dmg += getPowerSkillDmgBonus(data);

		if (data.activeFighter.statuses.isStatus(FighterStatus.FIERY_WEAPON)) {
			dmg += 1 + data.activeFighterStats.intelligence / 5 - data.targetStats.magicResistance;
			if (data.targetStats.classes.contains(FighterClass.WEAK_TO_FIRE)) {
				dmg += 2;
			}
		}

		return dmg;
	}

	private boolean isTargetMissed(final FightTempData data, final Weapon weapon, final int dmg) {
		if (dmg <= 0) {
			return true;
		}
		if (isFar(data.targetStats) && !weapon.classes.contains(ItemClass.RANGED)
				&& !weapon.classes.contains(ItemClass.LONG) && getRandomBoolean(0.5)) {
			return true;
		}
		if (data.activeFighter.statuses.isStatus(FighterStatus.CURSED) && getRandomBoolean(0.666)) {
			return true;
		}

		return false;
	}

	private void miss(final FightTempData data) {
		data.target.addExp(10);
		data.fight.addTurnDescription(data.activeFighter.name + " attacks " + data.target.name + ", but misses!");
	}

	private double getAgileFighterDodgeChanceModifier(final Set<Skill> skills) {
		if (!skills.contains(Skill.AGILE_FIGHTER_1)) {
			return 0;
		}
		if (!skills.contains(Skill.AGILE_FIGHTER_2)) {
			return 0.02;
		}
		if (!skills.contains(Skill.AGILE_FIGHTER_3)) {
			return 0.05;
		}
		if (!skills.contains(Skill.AGILE_FIGHTER_4)) {
			return 0.09;
		}
		if (!skills.contains(Skill.AGILE_FIGHTER_5)) {
			return 0.15;
		}
		return 0.25;
	}

	private double getSureHitHitChanceModifier(final Set<Skill> skills) {
		if (!skills.contains(Skill.SURE_HIT_1)) {
			return 0;
		}
		if (!skills.contains(Skill.SURE_HIT_2)) {
			return 0.05;
		}
		if (!skills.contains(Skill.SURE_HIT_3)) {
			return 0.10;
		}
		if (!skills.contains(Skill.SURE_HIT_4)) {
			return 0.15;
		}
		if (!skills.contains(Skill.SURE_HIT_5)) {
			return 0.20;
		}
		return 0.25;
	}

	private boolean didTargetDodge(final FightTempData data, final RPGFightAction action, final int attack) {
		if (data.target.statuses.cantDodge()) {
			return false;
		}

		final double attackerPower = data.activeFighter.level + data.activeFighterStats.agility * 2;
		final double targetPower = data.target.level + data.targetStats.agility * 2;
		double hitChance = clashChance(attackerPower, targetPower, 0, 0.75);
		if (data.target.type == FighterType.PLAYER) {
			final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(data.target.player());
			hitChance -= getAgileFighterDodgeChanceModifier(userData.rpg.skills);
		}

		if (data.activeFighter.type == FighterType.PLAYER) {
			final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(data.activeFighter.player());
			hitChance += getSureHitHitChanceModifier(userData.rpg.skills);
		}

		hitChance /= (attack + 1);

		return !getRandomBoolean(hitChance);
	}

	private void checkDodgeQuests(final FightTempData data) {
		if (data.target.type != FighterType.PLAYER) {
			return;
		}

		final PlayerFighterData playerTarget = (PlayerFighterData) data.target;
		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(data.fight.serverId,
				playerTarget.userId);

		final UserQuestData quest = userData.rpg.quests.get(QuestType.HERO_ACADEMY_QUEST);
		if (quest != null) {
			if (quest.step == QuestStep.ROGUE_2) {
				final UserHeroAcademyQuestRogue2StepData questStepData = (UserHeroAcademyQuestRogue2StepData) quest;
				questStepData.dodgedAttacks++;
				if (questStepData.dodgedAttacks >= 2) {
					fluffer10kFun.questUtils.questHeroAcademy().continueRogue2Step(data.channel, userData);
				}
			}

			if (quest.step == QuestStep.ROGUE_1) {
				fluffer10kFun.questUtils.questHeroAcademy().continueRogue1Step(data.channel, userData);
			}
		}
	}

	private void dodge(final FightTempData data, final int dmg) {
		data.fight.addTurnDescription(
				data.activeFighter.name + " attacks " + data.target.name + ", but " + data.target.name + " dodges!");
		data.activeFighter.addExp(dmg);
		data.target.addExp(dmg * 3);
	}

	private boolean isShadowCloneHit(final FightTempData data) {
		return data.target.statuses.isStatus(FighterStatus.SHADOW_CLONE) && getRandomBoolean(0.5);
	}

	private void hitShadowClone(final FightTempData data) {
		data.activeFighter.addExp(10);
		data.target.addExp(10);

		data.target.statuses.removeStatus(FighterStatus.SHADOW_CLONE);

		data.fight.addTurnDescription(data.activeFighter.name + " attacks " + data.target.name + ", but "
				+ data.target.name + " disappears as it was just a clone!");
	}

	private int applyDmgModifiers(final FightTempData data, final boolean isCrit, int dmg) {
		if (data.target.statuses.isStatus(FighterStatus.WEAKNESS_FOUND)) {
			dmg += 2;
		}
		if (data.target.statuses.isStatus(FighterStatus.MUMMY_CURSE)) {
			dmg *= 2;
		}
		if (data.target.statuses.isStatus(FighterStatus.SPIRIT_FORM)) {
			dmg /= 2;
		}

		return dmg;
	}

	private void checkOnHitQuests(final FightTempData data, final int dmg) {
		if (data.activeFighter.type == FighterType.PLAYER) {
			final PlayerFighterData playerAttacker = (PlayerFighterData) data.activeFighter;
			final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(data.fight.serverId,
					playerAttacker.userId);

			final UserQuestData heroAcademyQuest = userData.rpg.quests.get(QuestType.HERO_ACADEMY_QUEST);
			if (heroAcademyQuest != null) {
				if (dmg >= 2 && heroAcademyQuest.step == QuestStep.BERSERKER_1) {
					fluffer10kFun.questUtils.questHeroAcademy().continueBerserker1Step(data.channel, userData,
							fluffer10kFun.apiUtils.getUser(playerAttacker.userId));
				}
				if (dmg >= 3 && heroAcademyQuest.step == QuestStep.BERSERKER_3) {
					fluffer10kFun.questUtils.questHeroAcademy().continueBerserker3Step(data.channel, userData);
				}
				if (dmg >= 3 && heroAcademyQuest.step == QuestStep.WARLOCK_3) {
					fluffer10kFun.questUtils.questHeroAcademy().continueWarlock3Step(data.channel, userData);
				}
			}
		}

		if (data.target.type == FighterType.PLAYER) {
			final PlayerFighterData playerTarget = (PlayerFighterData) data.target;
			final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(data.fight.serverId,
					playerTarget.userId);
			final UserQuestData heroAcademyQuest = userData.rpg.quests.get(QuestType.HERO_ACADEMY_QUEST);
			if (heroAcademyQuest != null && heroAcademyQuest.step == QuestStep.ROGUE_2) {
				final UserHeroAcademyQuestRogue2StepData stepData = (UserHeroAcademyQuestRogue2StepData) heroAcademyQuest;
				stepData.dodgedAttacks = 0;
			}
		}
	}

	private void handleOnHitStatuses(final FightTempData data) {
		if (data.activeFighterStats.classes.contains(FighterClass.OCELOMEH_SWORD)) {
			data.target.statuses.addStatus(new FighterStatusData(FighterStatus.OCELOMEH_SWORD).duration(2));
		}
		if (data.activeFighterStats.classes.contains(FighterClass.CURSED_SWORD)) {
			data.target.statuses.addStatus(new FighterStatusData(FighterStatus.CURSED_SWORD_CUT).endless().stacks(1));
		}
		if (data.activeFighterStats.classes.contains(FighterClass.POISONOUS_ATTACK)) {
			data.target.statuses.addStatus(new FighterStatusData(FighterStatus.POISON).endless().stacks(1));
		}

		data.target.statuses.removeStatus(FighterStatus.SLEEP);
		wakeUpDormouseOnAttack(data);
	}

	private void addDecisiveStrikeBuff(final FightTempData data, final boolean isCrit) {
		if (!isCrit) {
			data.activeFighter.statuses.removeStatus(DECISIVE_STRIKE);
		}

		final PlayerFighterData player = data.activeFighter.player();
		if (player == null) {
			return;
		}

		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(player);
		if (!userData.blessings.blessingsObtained.contains(Blessing.DECISIVE_STRIKE)) {
			return;
		}

		if (data.activeFighter.statuses.getStacks(DECISIVE_STRIKE) < 2) {
			data.activeFighter.statuses.addStatus(new FighterStatusData(DECISIVE_STRIKE).endless().stacks(1));
		}
	}

	private boolean isCrit(final FightTempData data, final RPGFightAction action, final Weapon weapon) {
		if (data.activeFighter.statuses.isStatus(FighterStatus.ENRAGED)) {
			return false;
		}

		double critChance = (data.activeFighterStats.agility + weapon.criticalStrikeBonus) / 100.0
				+ data.activeFighterStats.critChanceBonus;
		if (action == RPGFightAction.SPECIAL_ATTACK_PRECISE_STRIKE) {
			critChance *= 1.5;
		}

		return getRandomBoolean(critChance);
	}

	private boolean doWeaponAttack(final FightTempData data, final RPGFightAction action, final Weapon weapon,
			final int attack) {
		int dmg = getBaseDmg(data, action, weapon) + data.activeFighter.statuses.getStacks(DECISIVE_STRIKE);
		if (action == SPECIAL_ATTACK_BASH) {
			dmg += 1;
		}
		if (isTargetMissed(data, weapon, dmg)) {
			miss(data);
			data.activeFighter.statuses.removeStatus(DECISIVE_STRIKE);
			return false;
		}
		if (didTargetDodge(data, action, attack)) {
			checkDodgeQuests(data);
			dodge(data, dmg);
			data.activeFighter.statuses.removeStatus(DECISIVE_STRIKE);
			return false;
		}
		if (isShadowCloneHit(data)) {
			hitShadowClone(data);
			data.activeFighter.statuses.removeStatus(DECISIVE_STRIKE);
			return false;
		}

		final boolean isCrit = isCrit(data, action, weapon);
		if (isCrit) {
			dmg += 1 + data.activeFighter.level / 4;

			if (data.activeFighter.type == FighterType.PLAYER) {
				final ServerUserData userData = fluffer10kFun.serverUserDataUtils
						.getUserData(data.activeFighter.player());
				if (userData.rpg.skills.contains(Skill.PRECISE_HIT_5)) {
					dmg += 10;
				}
			}
		}

		dmg -= physicalAttackArmor.armorDmgReduction(data, weapon);
		if (dmg <= 0) {
			data.fight.addTurnDescription(
					data.activeFighter.name + " attacks " + data.target.name + ", but can't get through their armor!");
			data.activeFighter.statuses.removeStatus(DECISIVE_STRIKE);
			return false;
		}

		dmg = applyDmgModifiers(data, isCrit, dmg);

		checkOnHitQuests(data, dmg);

		data.activeFighter.addExp(dmg * 3);
		data.target.addExp(dmg * 2);
		if (isCrit) {
			data.activeFighter.addExp(5);
		}

		data.target.hp -= min(dmg, data.target.hp);

		String attackDescription = action.description(data.activeFighter.name, data.target.name, dmg);
		if (isCrit) {
			if (attackDescription.endsWith(".")) {
				attackDescription = attackDescription.substring(0, attackDescription.length() - 1);
			}
			attackDescription += "!";
		}
		data.fight.addTurnDescription(attackDescription);

		handleOnHitStatuses(data);

		addDecisiveStrikeBuff(data, isCrit);
		return true;
	}

	@Override
	public void handle(final FightTempData data, final RPGFightAction action) {
		data.setUpTarget(fluffer10kFun, defaultTarget);
		data.target.attacked = true;

		final List<Weapon> weapons = weaponsGetters.getWeapons(data, action);

		final int attackRepeats = action == RPGFightAction.SPECIAL_ATTACK_DOUBLE_STRIKE ? 2 : 1;
		int attack = 0;
		boolean attackSucceeded = false;
		for (int i = 0; i < attackRepeats; i++) {
			for (final Weapon weapon : weapons) {
				attackSucceeded |= doWeaponAttack(data, action, weapon, attack++);
			}
		}

		if (attackSucceeded) {
			if (action == RPGFightAction.SPECIAL_ATTACK_BASH) {
				final double chance = 0.1 + 0.15 * min(1.0, data.activeFighterStats.strength / 40.0);
				if (getRandomBoolean(chance)) {
					data.target.statuses.setStatus(new FighterStatusData(FighterStatus.STUNNED).duration(2));
				}
			} else if (action == RPGFightAction.SPECIAL_ATTACK_GOUGE) {
				final int newStacks = data.target.statuses.getStacks(FighterStatus.BLEEDING) >= 3 ? 0 : 1;
				data.target.statuses
						.addStatus(new FighterStatusData(FighterStatus.BLEEDING).duration(3).stacks(newStacks));
			}
		}
	}

}
