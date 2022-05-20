package bot.commands.rpg.fight.enemies;

import static bot.data.items.loot.LootTable.weightedI;
import static bot.util.CollectionUtils.toMap;
import static bot.util.CollectionUtils.toSet;
import static bot.util.Utils.Pair.pair;

import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.FightTempData;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.Targetting;
import bot.commands.rpg.fight.Targetting.TargetCheck;
import bot.commands.rpg.fight.actions.special.FightBansheeWail;
import bot.commands.rpg.fight.actions.special.FightBasiliskEyes;
import bot.commands.rpg.fight.actions.special.FightManticoreVenom;
import bot.commands.rpg.fight.actions.special.FightMummyCurse;
import bot.commands.rpg.fight.actions.special.FightWeresheepSleep;
import bot.commands.rpg.fight.actions.spells.FightSpellHeal;
import bot.commands.rpg.fight.actions.spells.FightSpellSleep;
import bot.data.fight.FighterClass;
import bot.data.fight.FighterData;
import bot.data.fight.FighterStatus;
import bot.data.items.loot.LootTable;
import bot.util.Utils.Pair;

public class RPGEnemyActionSelectorUtils {
	public interface ActionSelector {
		RPGFightAction select(FightTempData data);

		default ActionSelector dormouseSleep() {
			final ActionSelector previous = this;
			return data -> {
				if (data.activeFighter.statuses.isStatus(FighterStatus.DORMOUSE_SLEEP)) {
					return RPGFightAction.DORMOUSE_SLEEP_WAKE;
				}
				return previous.select(data);
			};
		}

		default ActionSelector gentle() {
			final ActionSelector previous = this;
			return data -> {
				if (!data.activeFighter.attacked) {
					return data.fight.turn < 3 ? RPGFightAction.WAIT : RPGFightAction.ESCAPE;
				}
				return previous.select(data);
			};
		}
	}

	private final Fluffer10kFun fluffer10kFun;

	private final Map<RPGFightAction, Targetting> defaultTargetting;

	public RPGEnemyActionSelectorUtils(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;

		final TargetCheck aliveEnemy = TargetCheck.ENEMY.alive();
		final TargetCheck nonMechanicalAliveEnemy = aliveEnemy.without(fluffer10kFun, FighterClass.MECHANICAL);
		final TargetCheck atlachNachaVenomValid = nonMechanicalAliveEnemy
				.withStacksLessThan(FighterStatus.ATLACH_NACHA_VENOM, 5);

		final Targetting TARGETTING_ENEMY = new Targetting(aliveEnemy);

		final Targetting TARGETTING_FOR_ATLACH_NACHA_VENOM = new Targetting(
				atlachNachaVenomValid.with(FighterStatus.ATLACH_NACHA_VENOM))//
						.orElse(atlachNachaVenomValid);
		final Targetting TARGETTING_FOR_BAROMETZ_COTTON = new Targetting(
				nonMechanicalAliveEnemy.without(FighterStatus.BAROMETZ_COTTON))//
						.orElse(nonMechanicalAliveEnemy);
		final Targetting TARGETTING_FOR_CHARM = new Targetting(aliveEnemy.without(FighterStatus.CHARMED))
				.orElse(aliveEnemy);
		final Targetting TARGETTING_FOR_CURSE = new Targetting(nonMechanicalAliveEnemy.without(FighterStatus.CURSED))//
				.orElse(nonMechanicalAliveEnemy);
		final Targetting TARGETTING_FOR_FIND_WEAKNESS = new Targetting(
				aliveEnemy.without(FighterStatus.WEAKNESS_FOUND));
		final Targetting TARGETTING_FOR_GRAB = new Targetting(aliveEnemy.notHeld());
		final Targetting TARGETTING_FOR_INTOXICATE = new Targetting(
				nonMechanicalAliveEnemy.without(FighterStatus.INTOXICATED))//
						.orElse(nonMechanicalAliveEnemy);
		final Targetting TARGETTING_FOR_KEJOUROU_HAIR = new Targetting(aliveEnemy.without(FighterStatus.KEJOUROU_HAIR));
		final Targetting TARGETTING_FOR_LICK = new Targetting(nonMechanicalAliveEnemy.without(FighterStatus.LICKED))//
				.orElse(nonMechanicalAliveEnemy);
		final Targetting TARGETTING_FOR_LIFE_DRAIN = new Targetting(nonMechanicalAliveEnemy);
		final Targetting TARGETTING_FOR_MOTHMAN_POWDER = new Targetting(
				nonMechanicalAliveEnemy.without(FighterStatus.MOTHMAN_POWDER));
		final Targetting TARGETTING_FOR_PARALYZE = //
				new Targetting(aliveEnemy.with(fluffer10kFun, FighterClass.MECHANICAL).without(FighterStatus.PARALYZED))//
						.orElse(aliveEnemy.without(FighterStatus.PARALYZED))//
						.orElse(aliveEnemy.with(fluffer10kFun, FighterClass.MECHANICAL))//
						.orElse(aliveEnemy);
		final Targetting TARGETTING_FOR_PETRIFY = new Targetting(aliveEnemy.without(FighterStatus.PETRIFIED))//
				.orElse(aliveEnemy);
		final Targetting TARGETTING_FOR_SHRINK_DOWN = new Targetting(aliveEnemy.without(FighterStatus.SHRINKED))//
				.orElse(aliveEnemy);
		final Targetting TARGETTING_FOR_WRAP = new Targetting(aliveEnemy.without(FighterStatus.WRAPPED_IN_WEB))//
				.orElse(aliveEnemy);

		defaultTargetting = toMap(//
				pair(RPGFightAction.ATLACH_NACHA_VENOM, TARGETTING_FOR_ATLACH_NACHA_VENOM), //
				pair(RPGFightAction.ATTACK_A_1, TARGETTING_ENEMY), //
				pair(RPGFightAction.ATTACK_A_2, TARGETTING_ENEMY), //
				pair(RPGFightAction.ATTACK_A_3, TARGETTING_ENEMY), //
				pair(RPGFightAction.ATTACK_A_4, TARGETTING_ENEMY), //
				pair(RPGFightAction.ATTACK_DOUBLE_PUNCH, TARGETTING_ENEMY), //
				pair(RPGFightAction.ATTACK_HAMMER_SMASH, TARGETTING_ENEMY), //
				pair(RPGFightAction.ATTACK_S_1, TARGETTING_ENEMY), //
				pair(RPGFightAction.ATTACK_S_2, TARGETTING_ENEMY), //
				pair(RPGFightAction.ATTACK_S_3, TARGETTING_ENEMY), //
				pair(RPGFightAction.ATTACK_S_4, TARGETTING_ENEMY), //
				pair(RPGFightAction.ATTACK_S_5, TARGETTING_ENEMY), //
				pair(RPGFightAction.ATTACK_S_6, TARGETTING_ENEMY), //
				pair(RPGFightAction.ATTACK_S_7, TARGETTING_ENEMY), //
				pair(RPGFightAction.ATTACK_S_8, TARGETTING_ENEMY), //
				pair(RPGFightAction.ATTACK_S_9, TARGETTING_ENEMY), //
				pair(RPGFightAction.ATTACK_S_10, TARGETTING_ENEMY), //
				pair(RPGFightAction.ATTACK_S_11, TARGETTING_ENEMY), //
				pair(RPGFightAction.ATTACK_S_12, TARGETTING_ENEMY), //
				pair(RPGFightAction.ATTACK_S_13, TARGETTING_ENEMY), //
				pair(RPGFightAction.ATTACK_S_14, TARGETTING_ENEMY), //
				pair(RPGFightAction.ATTACK_S_15, TARGETTING_ENEMY), //
				pair(RPGFightAction.BANSHEE_WAIL, FightBansheeWail.getDefaultTargetting(fluffer10kFun)), //
				pair(RPGFightAction.BAROMETZ_COTTON, TARGETTING_FOR_BAROMETZ_COTTON), //
				pair(RPGFightAction.BASILISK_EYES, FightBasiliskEyes.getDefaultTargetting()), //
				pair(RPGFightAction.CHARM, TARGETTING_FOR_CHARM), //
				pair(RPGFightAction.CHARM_CUTE, TARGETTING_FOR_CHARM), //
				pair(RPGFightAction.CHARM_FEATHERS, TARGETTING_FOR_CHARM), //
				pair(RPGFightAction.CHARM_FINS, TARGETTING_FOR_CHARM), //
				pair(RPGFightAction.CHARM_KITSUNE, TARGETTING_FOR_CHARM), //
				pair(RPGFightAction.CHARM_MUSIC, TARGETTING_FOR_CHARM), //
				pair(RPGFightAction.CHARM_PURE, TARGETTING_FOR_CHARM), //
				pair(RPGFightAction.CHARM_RATATOSKR, TARGETTING_FOR_CHARM), //
				pair(RPGFightAction.CHARM_ROTTEN_BREATH, TARGETTING_FOR_CHARM), //
				pair(RPGFightAction.CHARM_SPORES, TARGETTING_FOR_CHARM), //
				pair(RPGFightAction.CHARM_SWEET_VOICE, TARGETTING_FOR_CHARM), //
				pair(RPGFightAction.CHARM_WINGS, TARGETTING_FOR_CHARM), //
				pair(RPGFightAction.CURSE, TARGETTING_FOR_CURSE), //
				pair(RPGFightAction.FIND_WEAKNESS, TARGETTING_FOR_FIND_WEAKNESS), //
				pair(RPGFightAction.GRAB, TARGETTING_FOR_GRAB), //
				pair(RPGFightAction.GRAB_CLING, TARGETTING_FOR_GRAB), //
				pair(RPGFightAction.GRAB_COIL, TARGETTING_FOR_GRAB), //
				pair(RPGFightAction.GRAB_JUMP, TARGETTING_FOR_GRAB), //
				pair(RPGFightAction.GRAB_SLIME, TARGETTING_FOR_GRAB), //
				pair(RPGFightAction.GRAB_TOP, TARGETTING_FOR_GRAB), //
				pair(RPGFightAction.INTOXICATE, TARGETTING_FOR_INTOXICATE), //
				pair(RPGFightAction.KEJOUROU_HAIR, TARGETTING_FOR_KEJOUROU_HAIR), //
				pair(RPGFightAction.LICK, TARGETTING_FOR_LICK), //
				pair(RPGFightAction.LEVEL_DRAIN_0, TARGETTING_ENEMY), //
				pair(RPGFightAction.MANTICORE_VENOM, FightManticoreVenom.getDefaultTargetting(fluffer10kFun)), //
				pair(RPGFightAction.MONSTER_LORD_RESTRICTION_0, TARGETTING_ENEMY), //
				pair(RPGFightAction.MOTHMAN_POWDER, TARGETTING_FOR_MOTHMAN_POWDER), //
				pair(RPGFightAction.MUMMY_CURSE, FightMummyCurse.getDefaultTargetting(fluffer10kFun)), //
				pair(RPGFightAction.PARALYZE, TARGETTING_FOR_PARALYZE), //
				pair(RPGFightAction.PARALYZE_CHIMAERA, TARGETTING_FOR_PARALYZE), //
				pair(RPGFightAction.SHRINK_DOWN, TARGETTING_FOR_SHRINK_DOWN), //
				pair(RPGFightAction.SIREN_SONG, TARGETTING_FOR_CHARM), //
				pair(RPGFightAction.SPECIAL_ATTACK_BASH, TARGETTING_ENEMY), //
				pair(RPGFightAction.SPECIAL_ATTACK_DOUBLE_STRIKE, TARGETTING_ENEMY), //
				pair(RPGFightAction.SPECIAL_ATTACK_GOUGE, TARGETTING_ENEMY), //
				pair(RPGFightAction.SPECIAL_ATTACK_PRECISE_STRIKE, TARGETTING_ENEMY), //
				pair(RPGFightAction.SPELL_BLIZZARD, TARGETTING_ENEMY), //
				pair(RPGFightAction.SPELL_FIREBALL, TARGETTING_ENEMY), //
				pair(RPGFightAction.SPELL_FORCE_HIT, TARGETTING_ENEMY), //
				pair(RPGFightAction.SPELL_FREEZE, TARGETTING_ENEMY), //
				pair(RPGFightAction.SPELL_HEAL, FightSpellHeal.getDefaultTargetting(fluffer10kFun)), //
				pair(RPGFightAction.SPELL_HEAL_SISTERS, FightSpellHeal.getDefaultTargetting(fluffer10kFun)), //
				pair(RPGFightAction.SPELL_ICE_BOLT, TARGETTING_ENEMY), //
				pair(RPGFightAction.SPELL_LIFE_DRAIN, TARGETTING_FOR_LIFE_DRAIN), //
				pair(RPGFightAction.SPELL_LIGHTNING, TARGETTING_ENEMY), //
				pair(RPGFightAction.SPELL_METEORITE, TARGETTING_ENEMY), //
				pair(RPGFightAction.SPELL_PARALYZING_THUNDER, TARGETTING_ENEMY), //
				pair(RPGFightAction.SPELL_PETRIFY, TARGETTING_FOR_PETRIFY), //
				pair(RPGFightAction.SPELL_QUADRUPLE_FLAME, TARGETTING_ENEMY), //
				pair(RPGFightAction.SPELL_RAIJU_LIGHTNING, TARGETTING_ENEMY), //
				pair(RPGFightAction.SPELL_SLEEP, FightSpellSleep.getDefaultTargetting(fluffer10kFun)), //
				pair(RPGFightAction.SPELL_WHIRLPOOL, TARGETTING_ENEMY), //
				pair(RPGFightAction.WERESHEEP_WOOL, FightWeresheepSleep.getDefaultTargetting(fluffer10kFun)), //
				pair(RPGFightAction.WRAP_IN_WEB, TARGETTING_FOR_WRAP));

		final Set<RPGFightAction> nonMgActions = toSet(//
				RPGFightAction.ATTACK, //
				RPGFightAction.EQUIP_ITEM, //
				RPGFightAction.EQUIPPED_ITEM, //
				RPGFightAction.FLUFF, //
				RPGFightAction.SALT, //
				RPGFightAction.SPELL, //
				RPGFightAction.USE_ITEM, //
				RPGFightAction.USED_ITEM);
		for (final RPGFightAction action : RPGFightAction.values()) {
			if (!defaultTargetting.containsKey(action) && !nonMgActions.contains(action)
					&& !targetSetters.containsKey(action)) {
				System.err
						.println("RPGEnemyActionSelectorUtils: Action " + action.name() + " is not given targetting!");
			}
		}
	}

	private void targetSelf(final FightTempData data, final RPGFightAction action) {
		data.targetId = data.activeFighter.id;
	}

	private final Map<RPGFightAction, BiConsumer<FightTempData, RPGFightAction>> targetSetters = toMap(//
			pair(RPGFightAction.BUBBLES, this::targetSelf), //
			pair(RPGFightAction.BUNSHIN_NO_JUTSU, this::targetSelf), //
			pair(RPGFightAction.DORMOUSE_SLEEP, this::targetSelf), //
			pair(RPGFightAction.DORMOUSE_SLEEP_WAKE, this::targetSelf), //
			pair(RPGFightAction.EGG_SHELL, this::targetSelf), //
			pair(RPGFightAction.ESCAPE, this::targetSelf), //
			pair(RPGFightAction.FRENZY, this::targetSelf), //
			pair(RPGFightAction.FRENZY_GRIZZLY, this::targetSelf), //
			pair(RPGFightAction.GET_FREE, this::targetSelf), //
			pair(RPGFightAction.GROW_UP, this::targetSelf), //
			pair(RPGFightAction.SAKE, this::targetSelf), //
			pair(RPGFightAction.SPECIAL_ACTION_SHADOW_CLONE, this::targetSelf), //
			pair(RPGFightAction.SPELL_FIERY_WEAPON, this::targetSelf), //
			pair(RPGFightAction.SPELL_FROST_ARMOR, this::targetSelf), //
			pair(RPGFightAction.SPELL_HOLY_AURA, this::targetSelf), //
			pair(RPGFightAction.SPELL_MAGIC_SHIELD, this::targetSelf), //
			pair(RPGFightAction.SPELL_RAGE, this::targetSelf), //
			pair(RPGFightAction.SPELL_SPEED_OF_WIND, this::targetSelf), //
			pair(RPGFightAction.SPELL_STONE_SKIN, this::targetSelf), //
			pair(RPGFightAction.SUCCUBUS_NOSTRUM, this::targetSelf), //
			pair(RPGFightAction.SURRENDER, this::targetSelf), //
			pair(RPGFightAction.WAIT, this::targetSelf));

	private RPGFightAction selectDefault(final FightTempData data, final LootTable<RPGFightAction> actionsTable) {
		RPGFightAction action = null;

		int tries = 0;
		while (data.targetId == null) {
			tries++;
			if (tries > 10) {
				return RPGFightAction.WAIT;
			}

			action = actionsTable.getItem();
			try {
				final Targetting targetting = defaultTargetting.get(action);
				if (targetting != null) {
					final FighterData target = targetting.getFirst(data.fight, data.activeFighter);
					if (target != null) {
						data.targetId = target.id;
					}
				} else {
					targetSetters.get(action).accept(data, action);
				}
			} catch (final Exception e) {
				fluffer10kFun.apiUtils.messageUtils
						.sendExceptionToMe("Error on targetting" + action + " for " + data.activeFighter.toMap(), e);
			}
		}

		return action;
	}

	public static final ActionSelector action(final RPGFightAction action) {
		return data -> action;
	}

	@SafeVarargs
	public final ActionSelector actionsFrom(final Pair<Integer, RPGFightAction>... actionWeights) {
		final LootTable<RPGFightAction> actionsTable = weightedI(actionWeights);
		return data -> selectDefault(data, actionsTable);
	}

	@SafeVarargs
	public final ActionSelector actionsFromTargetted(final ActionSelector defaultAction,
			final Pair<Targetting, ActionSelector>... targettedActions) {
		return data -> {
			for (final Pair<Targetting, ActionSelector> targettedAction : targettedActions) {
				final FighterData target = targettedAction.a.getFirst(data.fight, data.activeFighter);
				if (target != null) {
					data.targetId = target.id;
					return targettedAction.b.select(data);
				}
			}

			return defaultAction.select(data);
		};
	}

//		    if attackType == 'LAVA_GOLEM_TEMP_CHANGE' and fighter.get('lavaGolemTemperatureStacks', 0) >= 3:
//		        return False

}
