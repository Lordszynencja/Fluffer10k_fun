package bot.commands.rpg.fight.actions;

import static bot.util.CollectionUtils.mapToList;
import static bot.util.CollectionUtils.toMap;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.Utils.Pair.pair;

import java.util.List;
import java.util.Map;

import org.javacord.api.interaction.MessageComponentInteraction;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.FightActionsHandler.FightActionHandler;
import bot.commands.rpg.fight.FightTempData;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.spells.ActiveSkill;
import bot.commands.rpg.spells.CommandSpellbook;
import bot.data.fight.FightData;
import bot.data.fight.FighterData;
import bot.data.fight.FighterData.FighterType;
import bot.data.fight.PlayerFighterData;
import bot.userData.ServerUserData;
import bot.util.EmbedUtils.EmbedField;
import bot.util.pages.builders.PagedPickerMessageBuilder;
import bot.util.pages.messages.PagedMessage;

public class FightSpell implements FightActionHandler {
	private final Fluffer10kFun fluffer10kFun;

	public FightSpell(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final FightTempData data, final RPGFightAction action) {
		if (data.activeFighter.type != FighterType.PLAYER) {
			fluffer10kFun.apiUtils.messageUtils
					.sendMessageToMe("Non player fighter used action " + action + "!!!\n" + data.activeFighter.toMap());
			return;
		}

		final PlayerFighterData player = (PlayerFighterData) data.activeFighter;
		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(player);

		final List<ActiveSkill> spells = userData.rpg.spells();
		final List<EmbedField> fields = mapToList(spells,
				spell -> new EmbedField(spell.getFullName(), "uses " + spell.manaUse + " mana"));

		final PagedMessage msg = new PagedPickerMessageBuilder<>("Pick spell to cast", 5, fields, spells)//
				.imgUrl(CommandSpellbook.spellbookImgUrl)//
				.onPick((interaction, page, spell) -> onPick(interaction, spell, data))//
				.build();
		fluffer10kFun.pagedMessageUtils.addMessage(msg, player.userId, data.channel);
	}

	private static final Map<ActiveSkill, RPGFightAction> spellActions = toMap(//
			pair(ActiveSkill.BASH, RPGFightAction.SPECIAL_ATTACK_BASH), //
			pair(ActiveSkill.BLIZZARD, RPGFightAction.SPELL_BLIZZARD), //
			pair(ActiveSkill.DOUBLE_STRIKE, RPGFightAction.SPECIAL_ATTACK_DOUBLE_STRIKE), //
			pair(ActiveSkill.FIERY_WEAPON, RPGFightAction.SPELL_FIERY_WEAPON), //
			pair(ActiveSkill.FIREBALL, RPGFightAction.SPELL_FIREBALL), //
			pair(ActiveSkill.FORCE_HIT, RPGFightAction.SPELL_FORCE_HIT), //
			pair(ActiveSkill.FREEZE, RPGFightAction.SPELL_FREEZE), //
			pair(ActiveSkill.FROST_ARMOR, RPGFightAction.SPELL_FROST_ARMOR), //
			pair(ActiveSkill.GOUGE, RPGFightAction.SPECIAL_ATTACK_GOUGE), //
			pair(ActiveSkill.HEAL, RPGFightAction.SPELL_HEAL), //
			pair(ActiveSkill.HOLY_AURA, RPGFightAction.SPELL_HOLY_AURA), //
			pair(ActiveSkill.ICE_BOLT, RPGFightAction.SPELL_ICE_BOLT), //
			pair(ActiveSkill.LIFE_DRAIN, RPGFightAction.SPELL_LIFE_DRAIN), //
			pair(ActiveSkill.LIGHTNING, RPGFightAction.SPELL_LIGHTNING), //
			pair(ActiveSkill.MAGIC_SHIELD, RPGFightAction.SPELL_MAGIC_SHIELD), //
			pair(ActiveSkill.METEORITE, RPGFightAction.SPELL_METEORITE), //
			pair(ActiveSkill.PARALYZING_THUNDER, RPGFightAction.SPELL_PARALYZING_THUNDER), //
			pair(ActiveSkill.PETRIFY, RPGFightAction.SPELL_PETRIFY), //
			pair(ActiveSkill.PRECISE_STRIKE, RPGFightAction.SPECIAL_ATTACK_PRECISE_STRIKE), //
			pair(ActiveSkill.QUADRUPLE_FLAME, RPGFightAction.SPELL_QUADRUPLE_FLAME), //
			pair(ActiveSkill.RAGE, RPGFightAction.SPELL_RAGE), //
			pair(ActiveSkill.RAIJU_LIGHTNING, RPGFightAction.SPELL_RAIJU_LIGHTNING), //
			pair(ActiveSkill.SHADOW_CLONE, RPGFightAction.SPECIAL_ACTION_SHADOW_CLONE), //
			pair(ActiveSkill.SLEEP, RPGFightAction.SPELL_SLEEP), //
			pair(ActiveSkill.SPEED_OF_WIND, RPGFightAction.SPELL_SPEED_OF_WIND), //
			pair(ActiveSkill.STONE_SKIN, RPGFightAction.SPELL_STONE_SKIN), //
			pair(ActiveSkill.WHIRLPOOL, RPGFightAction.SPELL_WHIRLPOOL));

	private void onPick(final MessageComponentInteraction interaction, final ActiveSkill spell,
			final FightTempData data) {
		final long userId = interaction.getUser().getId();

		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(interaction.getServer().get(),
				userId);
		if (userData.rpg.fightId == null) {
			interaction.createOriginalMessageUpdater().addEmbed(makeEmbed("You aren't in a fight anymore")).update();
			return;
		}

		final FightData fight = fluffer10kFun.botDataUtils.fights.get(userData.rpg.fightId);
		if (fight == null) {
			interaction.createOriginalMessageUpdater()
					.addEmbed(makeEmbed("There was an error saving your fight, clearing it")).update();
			userData.rpg.fightId = null;
			return;
		}
		final FighterData fighter = fight.getCurrentFighter();
		if (fighter.type != FighterType.PLAYER || ((PlayerFighterData) fighter).userId != userId) {
			interaction.createOriginalMessageUpdater().addEmbed(makeEmbed("It's not your turn!")).update();
			return;
		}

		interaction.acknowledge();
		interaction.getMessage().delete();
		fluffer10kFun.fightActionsHandler.handleAction(data, spellActions.get(spell));
	}
}
