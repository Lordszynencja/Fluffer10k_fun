package bot.commands.rpg.fight.actions;

import static bot.util.CollectionUtils.mapToList;
import static bot.util.EmbedUtils.makeEmbed;
import static java.util.stream.Collectors.toList;

import java.util.List;

import org.javacord.api.interaction.MessageComponentInteraction;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.FightActionsHandler.FightActionHandler;
import bot.commands.rpg.fight.FightTempData;
import bot.commands.rpg.fight.RPGFightAction;
import bot.data.fight.FightData;
import bot.data.fight.FighterData;
import bot.data.fight.FighterData.FighterType;
import bot.data.fight.PlayerFighterData;
import bot.data.items.Item;
import bot.data.items.ItemAmount;
import bot.data.items.ItemSlot;
import bot.userData.ServerUserData;
import bot.util.EmbedUtils.EmbedField;
import bot.util.pages.builders.PagedPickerMessageBuilder;
import bot.util.pages.messages.PagedMessage;

public class FightEquipItem implements FightActionHandler {

	private final Fluffer10kFun fluffer10kFun;

	public FightEquipItem(final Fluffer10kFun fluffer10kFun) {
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

		final List<ItemAmount> itemAmounts = userData.items.entrySet().stream()//
				.filter(entry -> entry.getValue() > 0)//
				.map(entry -> fluffer10kFun.items.getItemAmount(entry.getKey(), entry.getValue()))//
				.filter(itemAmount -> itemAmount.item.canBeEquippedBy(userData))//
				.sorted()//
				.collect(toList());
		final List<Item> items = mapToList(itemAmounts, itemAmount -> itemAmount.item);
		final List<EmbedField> fields = mapToList(itemAmounts, itemAmount -> itemAmount.getAsFieldWithDescription());

		final PagedMessage msg = new PagedPickerMessageBuilder<>("Pick item to equip", 10, fields, items)//
				.onPick((interaction, page, spell) -> onPick(interaction, spell, data))//
				.build();
		fluffer10kFun.pagedMessageUtils.addMessage(msg, player.userId, data.channel);
	}

	private void onPick(final MessageComponentInteraction interaction, final Item item, final FightTempData data) {
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

		if (!userData.hasItem(item)) {
			interaction.createOriginalMessageUpdater().addEmbed(makeEmbed("You don't have the item anymore!")).update();
			return;
		}
		if (!item.canBeEquippedBy(userData)) {
			interaction.createOriginalMessageUpdater().addEmbed(makeEmbed("Player changed")).update();
			return;
		}

		interaction.acknowledge();
		interaction.getMessage().delete();
		fluffer10kFun.fightActionsHandler.handleAction(data, d -> handleEquip(d, userData, item),
				RPGFightAction.EQUIPPED_ITEM);
	}

	private void handleEquip(final FightTempData data, final ServerUserData userData, final Item item) {
		final ItemSlot slot = userData.equip(fluffer10kFun, item);
		if (slot == ItemSlot.LEFT_HAND) {
			data.fight.addTurnDescription(data.activeFighter.name + " grabs " + item.name + " in the left hand.");
		}
		if (slot == ItemSlot.RIGHT_HAND) {
			data.fight.addTurnDescription(data.activeFighter.name + " grabs " + item.name + " in the right hand.");
		}
		if (slot == ItemSlot.BOTH_HANDS) {
			data.fight.addTurnDescription(data.activeFighter.name + " grabs " + item.name + " in the hands.");
		}
		if (slot == ItemSlot.ARMOR) {
			data.fight.addTurnDescription(data.activeFighter.name + " puts on " + item.name + ".");
		}
		if (slot == ItemSlot.RING_1 || slot == ItemSlot.RING_2) {
			data.fight.addTurnDescription(data.activeFighter.name + " puts " + item.name + " on the finger.");
		}
		if (slot == ItemSlot.PICKAXE) {
			data.fight.addTurnDescription(data.activeFighter.name + " grabs " + item.name + " as pickaxe.");
		}
	}

}
