package bot.commands.rpg.fight;

import static bot.userData.UserBlessingData.Blessing.TERRIFYING_PRESENCE;
import static bot.util.CollectionUtils.toMap;
import static bot.util.CollectionUtils.toSet;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.TimerUtils.startTimedEvent;
import static bot.util.Utils.Pair.pair;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageUpdater;
import org.javacord.api.interaction.MessageComponentInteraction;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.actions.FightEquipItem;
import bot.commands.rpg.fight.actions.FightEscape;
import bot.commands.rpg.fight.actions.FightFluff;
import bot.commands.rpg.fight.actions.FightGetFree;
import bot.commands.rpg.fight.actions.FightGrab;
import bot.commands.rpg.fight.actions.FightSpell;
import bot.commands.rpg.fight.actions.FightSurrender;
import bot.commands.rpg.fight.actions.FightUseItem;
import bot.commands.rpg.fight.actions.FightWait;
import bot.commands.rpg.fight.actions.physicalAttacks.FightAttack;
import bot.commands.rpg.fight.actions.quest.FightLevelDrain;
import bot.commands.rpg.fight.actions.quest.FightMonsterLordRestriction0;
import bot.commands.rpg.fight.actions.special.FightAtlachNachaVenom;
import bot.commands.rpg.fight.actions.special.FightBansheeWail;
import bot.commands.rpg.fight.actions.special.FightBarometzCotton;
import bot.commands.rpg.fight.actions.special.FightBasiliskEyes;
import bot.commands.rpg.fight.actions.special.FightBubbles;
import bot.commands.rpg.fight.actions.special.FightBunshinNoJutsu;
import bot.commands.rpg.fight.actions.special.FightCharm;
import bot.commands.rpg.fight.actions.special.FightCurse;
import bot.commands.rpg.fight.actions.special.FightDormouseSleep;
import bot.commands.rpg.fight.actions.special.FightDormouseSleepWake;
import bot.commands.rpg.fight.actions.special.FightEggShell;
import bot.commands.rpg.fight.actions.special.FightFindWeakness;
import bot.commands.rpg.fight.actions.special.FightFrenzy;
import bot.commands.rpg.fight.actions.special.FightGrowUp;
import bot.commands.rpg.fight.actions.special.FightIntoxicate;
import bot.commands.rpg.fight.actions.special.FightKejourouHair;
import bot.commands.rpg.fight.actions.special.FightLick;
import bot.commands.rpg.fight.actions.special.FightManticoreVenom;
import bot.commands.rpg.fight.actions.special.FightMothmanPowder;
import bot.commands.rpg.fight.actions.special.FightMummyCurse;
import bot.commands.rpg.fight.actions.special.FightParalyze;
import bot.commands.rpg.fight.actions.special.FightSake;
import bot.commands.rpg.fight.actions.special.FightSalt;
import bot.commands.rpg.fight.actions.special.FightShrinkDown;
import bot.commands.rpg.fight.actions.special.FightSuccubusNostrum;
import bot.commands.rpg.fight.actions.special.FightWeresheepSleep;
import bot.commands.rpg.fight.actions.special.FightWrapInWeb;
import bot.commands.rpg.fight.actions.spells.FightSpecialActionShadowClone;
import bot.commands.rpg.fight.actions.spells.FightSpecialAttackBash;
import bot.commands.rpg.fight.actions.spells.FightSpecialAttackDoubleStrike;
import bot.commands.rpg.fight.actions.spells.FightSpecialAttackGouge;
import bot.commands.rpg.fight.actions.spells.FightSpecialAttackPreciseStrike;
import bot.commands.rpg.fight.actions.spells.FightSpellBlizzard;
import bot.commands.rpg.fight.actions.spells.FightSpellFieryWeapon;
import bot.commands.rpg.fight.actions.spells.FightSpellFireball;
import bot.commands.rpg.fight.actions.spells.FightSpellForceHit;
import bot.commands.rpg.fight.actions.spells.FightSpellFreeze;
import bot.commands.rpg.fight.actions.spells.FightSpellFrostArmor;
import bot.commands.rpg.fight.actions.spells.FightSpellHeal;
import bot.commands.rpg.fight.actions.spells.FightSpellHolyAura;
import bot.commands.rpg.fight.actions.spells.FightSpellIceBolt;
import bot.commands.rpg.fight.actions.spells.FightSpellLifeDrain;
import bot.commands.rpg.fight.actions.spells.FightSpellLightning;
import bot.commands.rpg.fight.actions.spells.FightSpellMagicShield;
import bot.commands.rpg.fight.actions.spells.FightSpellMeteorite;
import bot.commands.rpg.fight.actions.spells.FightSpellParalyzingThunder;
import bot.commands.rpg.fight.actions.spells.FightSpellPetrify;
import bot.commands.rpg.fight.actions.spells.FightSpellQuadrupleFlame;
import bot.commands.rpg.fight.actions.spells.FightSpellRage;
import bot.commands.rpg.fight.actions.spells.FightSpellRaijuLightning;
import bot.commands.rpg.fight.actions.spells.FightSpellSleep;
import bot.commands.rpg.fight.actions.spells.FightSpellSpeedOfWind;
import bot.commands.rpg.fight.actions.spells.FightSpellStoneSkin;
import bot.commands.rpg.fight.actions.spells.FightSpellWhirlpool;
import bot.data.fight.EnemyFighterData;
import bot.data.fight.FightData;
import bot.data.fight.FighterData.FighterType;
import bot.data.fight.PlayerFighterData;
import bot.userData.ServerUserData;
import bot.userData.UserData;
import bot.util.RandomUtils;

public class FightActionsHandler {
	public static interface FightActionHandler {
		void handle(FightTempData data, RPGFightAction action);
	}

	public static class ActionData {
		private static int actionIdCount = RandomUtils.getRandomInt(Integer.MAX_VALUE);

		public final RPGFightAction action;
		public final long userId;
		public final long fightId;
		public final String fighterId;
		public final int turn;
		public final int turnProgress;

		public ActionData(final RPGFightAction action, final long userId, final String fighterId,
				final FightData fight) {
			this.action = action;
			this.userId = userId;
			this.fighterId = fighterId;
			fightId = fight.id;
			turn = fight.turn;
			turnProgress = fight.turnProgress;
		}

		@Override
		public int hashCode() {
			return (action.name() + userId + fightId + turn + turnProgress + actionIdCount++).hashCode();
		}
	}

	private final Map<Long, List<String>> fightActions = new HashMap<>();
	private final Map<String, ActionData> activeActions = new HashMap<>();

	private void removeActionData(final long fightId) {
		final List<String> actionIds = fightActions.remove(fightId);
		if (actionIds == null) {
			return;
		}

		actionIds.forEach(activeActions::remove);
	}

	public String addActiveAction(final ActionData data) {
		List<String> actionFightActions = fightActions.get(data.fightId);
		if (actionFightActions == null) {
			actionFightActions = new ArrayList<>();
			fightActions.put(data.fightId, actionFightActions);
		}

		final String id = "" + data.hashCode();
		activeActions.put(id, data);
		actionFightActions.add(id);

		return id;
	}

	private final Fluffer10kFun fluffer10kFun;

	private final FightTurnProgresser fightTurnProgresser;
	private final FightEnd fightEnd;

	private final Map<RPGFightAction, FightActionHandler> actionHandlers;

	private Map<RPGFightAction, FightActionHandler> setUpActionHandlers() {
		final FightAttack fightAttack = new FightAttack(fluffer10kFun);
		final FightCharm fightCharm = new FightCharm(fluffer10kFun);
		final FightFrenzy fightFrenzy = new FightFrenzy();
		final FightGrab fightGrab = new FightGrab(fluffer10kFun);
		final FightParalyze fightParalyze = new FightParalyze(fluffer10kFun);

		return toMap(//
				pair(RPGFightAction.ATLACH_NACHA_VENOM, new FightAtlachNachaVenom(fluffer10kFun)), //
				pair(RPGFightAction.ATTACK, fightAttack), //
				pair(RPGFightAction.ATTACK_A_1, fightAttack), //
				pair(RPGFightAction.ATTACK_A_2, fightAttack), //
				pair(RPGFightAction.ATTACK_A_3, fightAttack), //
				pair(RPGFightAction.ATTACK_A_4, fightAttack), //
				pair(RPGFightAction.ATTACK_DOUBLE_PUNCH, fightAttack), //
				pair(RPGFightAction.ATTACK_HAMMER_SMASH, fightAttack), //
				pair(RPGFightAction.ATTACK_S_1, fightAttack), //
				pair(RPGFightAction.ATTACK_S_2, fightAttack), //
				pair(RPGFightAction.ATTACK_S_3, fightAttack), //
				pair(RPGFightAction.ATTACK_S_4, fightAttack), //
				pair(RPGFightAction.ATTACK_S_5, fightAttack), //
				pair(RPGFightAction.ATTACK_S_6, fightAttack), //
				pair(RPGFightAction.ATTACK_S_7, fightAttack), //
				pair(RPGFightAction.ATTACK_S_8, fightAttack), //
				pair(RPGFightAction.ATTACK_S_9, fightAttack), //
				pair(RPGFightAction.ATTACK_S_10, fightAttack), //
				pair(RPGFightAction.ATTACK_S_11, fightAttack), //
				pair(RPGFightAction.ATTACK_S_12, fightAttack), //
				pair(RPGFightAction.ATTACK_S_13, fightAttack), //
				pair(RPGFightAction.ATTACK_S_14, fightAttack), //
				pair(RPGFightAction.ATTACK_S_15, fightAttack), //
				pair(RPGFightAction.BANSHEE_WAIL, new FightBansheeWail(fluffer10kFun)), //
				pair(RPGFightAction.BAROMETZ_COTTON, new FightBarometzCotton(fluffer10kFun)), //
				pair(RPGFightAction.BASILISK_EYES, new FightBasiliskEyes(fluffer10kFun)), //
				pair(RPGFightAction.BUBBLES, new FightBubbles()), //
				pair(RPGFightAction.BUNSHIN_NO_JUTSU, new FightBunshinNoJutsu()), //
				pair(RPGFightAction.CHARM, fightCharm), //
				pair(RPGFightAction.CHARM_CUTE, fightCharm), //
				pair(RPGFightAction.CHARM_FEATHERS, fightCharm), //
				pair(RPGFightAction.CHARM_FINS, fightCharm), //
				pair(RPGFightAction.CHARM_KITSUNE, fightCharm), //
				pair(RPGFightAction.CHARM_MUSIC, fightCharm), //
				pair(RPGFightAction.CHARM_PURE, fightCharm), //
				pair(RPGFightAction.CHARM_RATATOSKR, fightCharm), //
				pair(RPGFightAction.CHARM_ROTTEN_BREATH, fightCharm), //
				pair(RPGFightAction.CHARM_SPORES, fightCharm), //
				pair(RPGFightAction.CHARM_SWEET_VOICE, fightCharm), //
				pair(RPGFightAction.CHARM_WINGS, fightCharm), //
				pair(RPGFightAction.CURSE, new FightCurse(fluffer10kFun)), //
				pair(RPGFightAction.DORMOUSE_SLEEP, new FightDormouseSleep()), //
				pair(RPGFightAction.DORMOUSE_SLEEP_WAKE, new FightDormouseSleepWake()), //
				pair(RPGFightAction.EGG_SHELL, new FightEggShell()), //
				pair(RPGFightAction.ESCAPE, new FightEscape(fluffer10kFun)), //
				pair(RPGFightAction.EQUIP_ITEM, new FightEquipItem(fluffer10kFun)), //
				pair(RPGFightAction.EQUIPPED_ITEM, null), //
				pair(RPGFightAction.FIND_WEAKNESS, new FightFindWeakness(fluffer10kFun)), //
				pair(RPGFightAction.FLUFF, new FightFluff(fluffer10kFun)), //
				pair(RPGFightAction.FRENZY, fightFrenzy), //
				pair(RPGFightAction.FRENZY_GRIZZLY, fightFrenzy), //
				pair(RPGFightAction.GET_FREE, new FightGetFree(fluffer10kFun)), //
				pair(RPGFightAction.GRAB, fightGrab), //
				pair(RPGFightAction.GRAB_CLING, fightGrab), //
				pair(RPGFightAction.GRAB_COIL, fightGrab), //
				pair(RPGFightAction.GRAB_JUMP, fightGrab), //
				pair(RPGFightAction.GRAB_SLIME, fightGrab), //
				pair(RPGFightAction.GRAB_TOP, fightGrab), //
				pair(RPGFightAction.GROW_UP, new FightGrowUp()), //
				pair(RPGFightAction.INTOXICATE, new FightIntoxicate(fluffer10kFun)), //
				pair(RPGFightAction.KEJOUROU_HAIR, new FightKejourouHair(fluffer10kFun)), //
				pair(RPGFightAction.LEVEL_DRAIN_0, new FightLevelDrain(fluffer10kFun)), //
				pair(RPGFightAction.LEVEL_DRAIN_1, new FightLevelDrain(fluffer10kFun)), //
				pair(RPGFightAction.LICK, new FightLick(fluffer10kFun)), //
				pair(RPGFightAction.MANTICORE_VENOM, new FightManticoreVenom(fluffer10kFun)), //
				pair(RPGFightAction.MONSTER_LORD_RESTRICTION_0, new FightMonsterLordRestriction0(fluffer10kFun)), //
				pair(RPGFightAction.MOTHMAN_POWDER, new FightMothmanPowder(fluffer10kFun)), //
				pair(RPGFightAction.MUMMY_CURSE, new FightMummyCurse(fluffer10kFun)), //
				pair(RPGFightAction.PARALYZE, fightParalyze), //
				pair(RPGFightAction.PARALYZE_CHIMAERA, fightParalyze), //
				pair(RPGFightAction.SAKE, new FightSake()), //
				pair(RPGFightAction.SALT, new FightSalt(fluffer10kFun)), //
				pair(RPGFightAction.SHRINK_DOWN, new FightShrinkDown(fluffer10kFun)), //
				pair(RPGFightAction.SIREN_SONG, fightCharm), //
				pair(RPGFightAction.SPECIAL_ACTION_SHADOW_CLONE, new FightSpecialActionShadowClone(fluffer10kFun)), //
				pair(RPGFightAction.SPECIAL_ATTACK_BASH, new FightSpecialAttackBash(fluffer10kFun, fightAttack)), //
				pair(RPGFightAction.SPECIAL_ATTACK_DOUBLE_STRIKE,
						new FightSpecialAttackDoubleStrike(fluffer10kFun, fightAttack)), //
				pair(RPGFightAction.SPECIAL_ATTACK_GOUGE, new FightSpecialAttackGouge(fluffer10kFun, fightAttack)), //
				pair(RPGFightAction.SPECIAL_ATTACK_PRECISE_STRIKE,
						new FightSpecialAttackPreciseStrike(fluffer10kFun, fightAttack)), //
				pair(RPGFightAction.SPELL, new FightSpell(fluffer10kFun)), //
				pair(RPGFightAction.SPELL_BLIZZARD, new FightSpellBlizzard(fluffer10kFun)), //
				pair(RPGFightAction.SPELL_FIERY_WEAPON, new FightSpellFieryWeapon(fluffer10kFun)), //
				pair(RPGFightAction.SPELL_FIREBALL, new FightSpellFireball(fluffer10kFun)), //
				pair(RPGFightAction.SPELL_FORCE_HIT, new FightSpellForceHit(fluffer10kFun)), //
				pair(RPGFightAction.SPELL_FREEZE, new FightSpellFreeze(fluffer10kFun)), //
				pair(RPGFightAction.SPELL_FROST_ARMOR, new FightSpellFrostArmor(fluffer10kFun)), //
				pair(RPGFightAction.SPELL_HEAL, new FightSpellHeal(fluffer10kFun)), //
				pair(RPGFightAction.SPELL_HEAL_SISTERS, new FightSpellHeal(fluffer10kFun)), //
				pair(RPGFightAction.SPELL_HOLY_AURA, new FightSpellHolyAura(fluffer10kFun)), //
				pair(RPGFightAction.SPELL_ICE_BOLT, new FightSpellIceBolt(fluffer10kFun)), //
				pair(RPGFightAction.SPELL_LIFE_DRAIN, new FightSpellLifeDrain(fluffer10kFun)), //
				pair(RPGFightAction.SPELL_LIGHTNING, new FightSpellLightning(fluffer10kFun)), //
				pair(RPGFightAction.SPELL_LIGHTNING, new FightSpellLightning(fluffer10kFun)), //
				pair(RPGFightAction.SPELL_MAGIC_SHIELD, new FightSpellMagicShield(fluffer10kFun)), //
				pair(RPGFightAction.SPELL_METEORITE, new FightSpellMeteorite(fluffer10kFun)), //
				pair(RPGFightAction.SPELL_PARALYZING_THUNDER, new FightSpellParalyzingThunder(fluffer10kFun)), //
				pair(RPGFightAction.SPELL_PETRIFY, new FightSpellPetrify(fluffer10kFun)), //
				pair(RPGFightAction.SPELL_QUADRUPLE_FLAME, new FightSpellQuadrupleFlame(fluffer10kFun)), //
				pair(RPGFightAction.SPELL_RAGE, new FightSpellRage(fluffer10kFun)), //
				pair(RPGFightAction.SPELL_RAIJU_LIGHTNING, new FightSpellRaijuLightning(fluffer10kFun)), //
				pair(RPGFightAction.SPELL_SLEEP, new FightSpellSleep(fluffer10kFun)), //
				pair(RPGFightAction.SPELL_SPEED_OF_WIND, new FightSpellSpeedOfWind(fluffer10kFun)), //
				pair(RPGFightAction.SPELL_STONE_SKIN, new FightSpellStoneSkin(fluffer10kFun)), //
				pair(RPGFightAction.SPELL_WHIRLPOOL, new FightSpellWhirlpool(fluffer10kFun)), //
				pair(RPGFightAction.SUCCUBUS_NOSTRUM, new FightSuccubusNostrum()), //
				pair(RPGFightAction.SURRENDER, new FightSurrender()), //
				pair(RPGFightAction.USE_ITEM, new FightUseItem(fluffer10kFun)), //
				pair(RPGFightAction.USED_ITEM, null), //
				pair(RPGFightAction.WAIT, new FightWait()), //
				pair(RPGFightAction.WERESHEEP_WOOL, new FightWeresheepSleep(fluffer10kFun)), //
				pair(RPGFightAction.WRAP_IN_WEB, new FightWrapInWeb(fluffer10kFun)));
	}

	public FightActionsHandler(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;

		fightTurnProgresser = new FightTurnProgresser(fluffer10kFun);
		fightEnd = new FightEnd(fluffer10kFun);

		actionHandlers = setUpActionHandlers();
		for (final RPGFightAction action : RPGFightAction.values()) {
			if (!actionHandlers.containsKey(action)) {
				System.err.println("Action " + action.name() + " is not mapped!");
			}
		}

		fluffer10kFun.apiUtils.commandHandlers.addMessageComponentHandler("fight_action", this::handleFightAction);
	}

	private void handleFightAction(final MessageComponentInteraction interaction) {
		final ActionData actionData = activeActions.get(interaction.getCustomId().split(" ")[1]);
		if (actionData == null) {
			interaction.createOriginalMessageUpdater().addEmbed(makeEmbed("Please use /fight refresh")).update();
			return;
		}
		if (actionData.userId != interaction.getUser().getId()) {
			sendEphemeralMessage(interaction, "Not your turn/fight!");
			return;
		}

		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(interaction.getServer().get(),
				actionData.userId);
		if (userData.rpg.fightId == null || userData.rpg.fightId != actionData.fightId) {
			interaction.createOriginalMessageUpdater().addEmbed(makeEmbed("You weren't in this fight anymore"))
					.update();
			return;
		}

		final FightData fight = fluffer10kFun.botDataUtils.fights.get(actionData.fightId);
		if (fight == null) {
			interaction.createOriginalMessageUpdater()
					.addEmbed(makeEmbed("There was an error saving your fight, clearing it")).update();
			userData.rpg.fightId = null;
			return;
		}
		if (fight.turn != actionData.turn || fight.turnProgress != actionData.turnProgress) {
			interaction.createOriginalMessageUpdater()
					.addEmbed(makeEmbed("This data was for old news, don't use old embeds to continue fights"))
					.update();
			return;
		}

		removeActionData(fight.id);
		interaction.createOriginalMessageUpdater().removeAllComponents().update().join();

		final FightTempData data = new FightTempData(fluffer10kFun, interaction.getMessage(), fight);
		handleAction(data, actionData.action);
	}

	private FightActionHandler getHandler(final RPGFightAction action) {
		final FightActionHandler handler = actionHandlers.get(action);
		if (handler == null) {
			fluffer10kFun.apiUtils.messageUtils.sendExceptionToMe("Action " + action + " has no handler!",
					new Exception());
		}
		return handler;
	}

	public void handleAction(final FightTempData data, final RPGFightAction action) {
		handleAction(data, d -> getHandler(action).handle(d, action), action);
	}

	public void handleAction(final FightTempData data, final Consumer<FightTempData> actionHandler,
			final RPGFightAction action) {
		startTimedEvent(() -> {
			handleActionInternal(data, actionHandler, action);
		}, 0);
	}

	public void handleActionInternal(final FightTempData data, final RPGFightAction action) {
		handleActionInternal(data, d -> getHandler(action).handle(d, action), action);
	}

	private void handleActionInternal(final FightTempData data, final Consumer<FightTempData> actionHandler,
			final RPGFightAction action) {
		try {
			data.resetFighterData(fluffer10kFun);
			actionHandler.accept(data);
			finishAction(data, action);
		} catch (final Exception e) {
			fluffer10kFun.apiUtils.messageUtils.sendExceptionToMe(
					"Exception on action " + action.name() + ", fighter " + data.activeFighter.name, e);
			if (data.activeFighter.type == FighterType.ENEMY) {
				handleActionInternal(data, RPGFightAction.WAIT);
			}
		}
	}

	private void sendCurrentFight(final FightTempData data) {
		final MessageUpdater messageUpdater = new MessageUpdater(data.message);
		fluffer10kFun.fightSender.addFight(messageUpdater, data.fight);
		data.setMessage(messageUpdater.replaceMessage().join());
	}

	public void startFight(final TextChannel channel, final FightData fight) {
		final Message message = fluffer10kFun.fightSender.addFight(channel, fight);
		final FightTempData data = new FightTempData(fluffer10kFun, message, fight);

		fightTurnProgresser.startTurn(data);

		handleNextAction(data);
	}

	private static final Set<RPGFightAction> actionsNotChangingAnything = toSet(//
			RPGFightAction.EQUIP_ITEM, //
			RPGFightAction.SPELL, //
			RPGFightAction.USE_ITEM);

	private void finishAction(final FightTempData data, final RPGFightAction lastAction) {
		if (actionsNotChangingAnything.contains(lastAction)) {
			sendCurrentFight(data);
			return;
		}

		data.fight.actionsLeft--;
		if (data.fight.actionsLeft <= 0) {
			fightTurnProgresser.endTurn(data);
			if (data.fight.ended) {
				fightEnd.endFight(data);
				return;
			}
			fightTurnProgresser.startTurn(data);

			if (data.fight.getCurrentFighter().isOut()) {
				data.fight.actionsLeft = 0;
				finishAction(data, RPGFightAction.WAIT);
				return;
			}
		}

		handleNextAction(data);
	}

	private void waitBeforeAction() {
		try {
			Thread.sleep(1000);
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void handleNextPlayerAction(final FightTempData data, final PlayerFighterData player) {
		final UserData userData = fluffer10kFun.userDataUtils.getUserData(player.userId);
		final boolean canOnlyWait = player.statuses.cantDoActions();
		if (canOnlyWait && userData.autoWait) {
			waitBeforeAction();
			handleActionInternal(data, RPGFightAction.WAIT);
		}
	}

	private void handleNextEnemyAction(final FightTempData data, final EnemyFighterData enemy) {
		final int myLevel = data.activeFighter.level;
		final boolean shouldSurrender = data.fight.fighters.values().stream()//
				.filter(fighter -> fighter.type == FighterType.PLAYER && fighter.level >= myLevel * 2)//
				.map(fighter -> fluffer10kFun.serverUserDataUtils.getUserData(fighter.player()))//
				.anyMatch(userData -> userData.blessings.blessingsObtained.contains(TERRIFYING_PRESENCE));

		final RPGFightAction action;
		if (shouldSurrender) {
			action = RPGFightAction.SURRENDER;
		} else if (enemy.statuses.cantDoActions()) {
			action = RPGFightAction.WAIT;
		} else {
			action = enemy.enemyData(fluffer10kFun).actionSelector.select(data);
		}

		waitBeforeAction();
		handleActionInternal(data, action);
	}

	private void handleNextAction(final FightTempData data) {
		sendCurrentFight(data);

		if (data.activeFighter.type == FighterType.PLAYER) {
			handleNextPlayerAction(data, data.activeFighter.player());
			return;
		}
		if (data.activeFighter.type == FighterType.ENEMY) {
			handleNextEnemyAction(data, data.activeFighter.enemy());
		}
	}
}
