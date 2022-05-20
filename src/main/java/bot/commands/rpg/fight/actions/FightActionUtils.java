package bot.commands.rpg.fight.actions;

import static bot.util.CollectionUtils.toSet;
import static bot.util.RandomUtils.getRandomBoolean;
import static bot.util.RandomUtils.roll;
import static java.lang.Math.max;
import static java.lang.Math.min;

import java.util.Set;

import org.javacord.api.entity.channel.ServerTextChannel;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.FightTempData;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.skills.Skill;
import bot.commands.rpg.spells.ActiveSkill;
import bot.data.fight.FighterClass;
import bot.data.fight.FighterData;
import bot.data.fight.FighterData.FighterType;
import bot.data.fight.FighterStatus;
import bot.data.fight.FighterStatusData;
import bot.data.fight.PlayerFighterData;
import bot.data.quests.QuestType;
import bot.userData.ServerUserData;
import bot.userData.UserBlessingData.Blessing;
import bot.userData.rpg.questData.QuestStep;

public class FightActionUtils {
	private static final Set<ActiveSkill> physicalDmgSpells = toSet(//
			ActiveSkill.BLIZZARD, //
			ActiveSkill.ICE_BOLT, //
			ActiveSkill.METEORITE, //
			ActiveSkill.WHIRLPOOL);

	public static void addFreezingStack(final FightTempData data) {
		if (data.activeFighterStats.classes.contains(FighterClass.FREEZING_AURA)
				&& data.target.statuses.getStacks(FighterStatus.FREEZING) < 3) {
			data.target.statuses.addStatus(new FighterStatusData(FighterStatus.FREEZING).endless().stacks(1));
		}
	}

	public static void wakeUpDormouseOnAttack(final FightTempData data) {
		if (data.target.statuses.isStatus(FighterStatus.DORMOUSE_SLEEP)) {
			data.activeFighter.addExp(10);
			data.target.addExp(20);
			data.activeFighter.statuses.addStatus(new FighterStatusData(FighterStatus.CHARMED).duration(5));
			data.target.statuses.removeStatus(FighterStatus.DORMOUSE_SLEEP);
			data.fight.addTurnDescription(data.activeFighter.name + " falls into " + data.target.name + "'s trap!");
		}
	}

	private final Fluffer10kFun fluffer10kFun;

	public FightActionUtils(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;
	}

	private void checkQuestsOnCast(final ServerTextChannel channel, final PlayerFighterData player) {
		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(player);
		if (userData.rpg.questIsOnStep(QuestType.HERO_ACADEMY_QUEST, QuestStep.MAGE_1)) {
			fluffer10kFun.questUtils.questHeroAcademy().continueMage1Step(channel, userData);
		}
	}

	private boolean spellCastStatSuccess(final int fighterStat, final int requiredStat) {
		return requiredStat <= 0 || fighterStat >= requiredStat || getRandomBoolean(1.0 * fighterStat / requiredStat);
	}

	public boolean castSpell(final FightTempData data, final ActiveSkill spell, final int manaCost) {
		if (data.activeFighter.type == FighterType.PLAYER) {
			final PlayerFighterData player = data.activeFighter.player();
			final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(player);
			if (!userData.blessings.blessingsObtained.contains(Blessing.MANA_MANIAC) || !getRandomBoolean(0.2)) {
				player.mana -= manaCost;
			}

			if (player.mana < 0) {
				player.mana = 0;
				data.fight.addTurnDescription(player.name + " tries to cast " + spell.name + ", but runs out of mana!");
				return false;
			}

			if (!spellCastStatSuccess(data.activeFighterStats.strength, spell.requiredStrength)//
					|| !spellCastStatSuccess(data.activeFighterStats.agility, spell.requiredAgility)//
					|| !spellCastStatSuccess(data.activeFighterStats.intelligence, spell.requiredIntelligence)) {
				data.fight.addTurnDescription(player.name + " tries to cast " + spell.name + ", but the spell fails!");
				return false;
			}

			checkQuestsOnCast(data.channel, player);
		}

		return true;
	}

	private int activateMagicShield(final FightTempData data, final ActiveSkill spell, int dmg) {
		if (data.target.statuses.isStatus(FighterStatus.MAGIC_SHIELD)) {
			final int shieldDefense = roll(data.targetStats.intelligence / 4.0);
			data.activeFighter.addExp(shieldDefense);
			data.target.addExp(shieldDefense * 3);
			dmg -= shieldDefense;

			if (dmg <= 0) {
				data.fight.addTurnDescription(data.activeFighter.name + "'s " + spell.name + " spell is absorbed by "
						+ data.target.name + "'s magic shield!");
				return 0;
			}
		}

		return dmg;
	}

	private boolean dodgeSuccessful(final FightTempData data) {
		if (data.target.statuses.cantDodge()) {
			return false;
		}

		final int targetAgilityAdvantage = data.targetStats.agility - data.activeFighterStats.agility;
		final double dodgeChance = 0.5 * min(1, max(0, targetAgilityAdvantage)
				/ max(1, max(data.activeFighterStats.agility, data.targetStats.agility)));
		return getRandomBoolean(dodgeChance);
	}

	public int spellHitDmgPositiveModifiers(final FightTempData data, int dmg) {
		if (data.target.statuses.isStatus(FighterStatus.WEAKNESS_FOUND)) {
			dmg += 2;
		}

		if (data.target.statuses.isStatus(FighterStatus.MUMMY_CURSE)) {
			dmg *= 2;
		}

		return dmg < 0 ? 0 : dmg;
	}

	private double spellCastPowerModifier(final FightTempData data) {
		double modifier = 1;

		if (data.activeFighter.statuses.isStatus(FighterStatus.CURSED)) {
			modifier *= 0.666;
		}
		if (data.activeFighterStats.classes.contains(FighterClass.SPELL_VOID)) {
			modifier *= 0.5;
		}

		return modifier;
	}

	public double getMagicStrength(final FightTempData data) {
		return spellCastPowerModifier(data) * data.activeFighterStats.getMagicPowerMultiplier();
	}

	public int hitDamagingSpell(final FightTempData data, int baseDamage, final ActiveSkill spell,
			final RPGFightAction action) {
		data.target.attacked = true;

		final PlayerFighterData player = data.activeFighter.player();
		if (player != null) {
			final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(player);
			if (userData.blessings.blessingsObtained.contains(Blessing.CHANNELING) && player.lastSpellUsed == spell) {
				baseDamage += 1;
			}
		}
		int dmg = max(1, roll(baseDamage * getMagicStrength(data)));

		data.activeFighter.addExp(dmg);

		if (dodgeSuccessful(data)) {
			data.activeFighter.addExp(dmg * 3);
			data.target.addExp(dmg * 3);
			data.fight.addTurnDescription(
					data.activeFighter.name + " casts " + spell.name + ", but " + data.target.name + " dodges!");

			return 0;
		}

		dmg = activateMagicShield(data, spell, dmg);
		if (dmg == 0) {
			return 0;
		}

		if (physicalDmgSpells.contains(spell)) {
			dmg -= data.targetStats.getArmorPower();
		}
		dmg -= data.targetStats.magicResistance;

		if (data.targetStats.classes.contains(FighterClass.SPELL_VOID)) {
			final int reduction = dmg / 2;
			data.target.addExp(reduction * 3);
			dmg -= reduction;
		}
		if (dmg <= 0) {
			data.target.addExp(10);
			data.fight.addTurnDescription(data.activeFighter.name + " casts " + spell.name + ", but it gets voided!");
			return 0;
		}

		dmg = spellHitDmgPositiveModifiers(data, dmg);
		if (dmg > data.target.hp) {
			dmg = data.target.hp;
		}

		data.activeFighter.addExp(dmg * 3);
		data.target.addExp(dmg * 3);

		data.target.hp -= dmg;

		data.target.statuses.removeStatus(FighterStatus.SLEEP);
		data.target.statuses.removeStatus(FighterStatus.DORMOUSE_SLEEP);

		data.fight.addTurnDescription(action.description(data.activeFighter.name, data.target.name, dmg));

		return dmg;
	}

	public int getNegativeStatusDurationReduction(final FighterData fighter) {
		if (fighter.type != FighterType.PLAYER) {
			return 0;
		}

		int reduction = 0;
		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(fighter.player());
		if (userData.rpg.skills.contains(Skill.STATUS_RESISTANCE_1)) {
			reduction++;
			if (userData.rpg.skills.contains(Skill.STATUS_RESISTANCE_2)) {
				reduction++;
			}
		}

		return reduction;
	}
}
