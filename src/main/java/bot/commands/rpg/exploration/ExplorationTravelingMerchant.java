package bot.commands.rpg.exploration;

import static bot.data.items.data.ArmorItems.CHAINMAIL;
import static bot.data.items.data.ArmorItems.RING_MAIL;
import static bot.data.items.data.RingItems.REGENERATION_RING;
import static bot.data.items.data.RingItems.SPELL_VOID_RING;
import static bot.data.items.data.RingItems.STATUS_NEGATION_RING;
import static bot.data.items.data.WeaponItems.BATTLE_AXE;
import static bot.data.items.data.WeaponItems.BLOWGUN;
import static bot.data.items.data.WeaponItems.BROADSWORD;
import static bot.data.items.data.WeaponItems.CHAIN_WHIP;
import static bot.data.items.data.WeaponItems.CLAYMORE;
import static bot.data.items.data.WeaponItems.DAGGER;
import static bot.data.items.data.WeaponItems.ESTOC;
import static bot.data.items.data.WeaponItems.GLAIVE;
import static bot.data.items.data.WeaponItems.HALBERD;
import static bot.data.items.data.WeaponItems.KUKRI;
import static bot.data.items.data.WeaponItems.KUSARIGAMA;
import static bot.data.items.data.WeaponItems.LIGHT_CROSSBOW;
import static bot.data.items.data.WeaponItems.LONG_BOW;
import static bot.data.items.data.WeaponItems.LONG_SWORD;
import static bot.data.items.data.WeaponItems.MACHETE;
import static bot.data.items.data.WeaponItems.METAL_SHIELD;
import static bot.data.items.data.WeaponItems.NUNCHUCK;
import static bot.data.items.data.WeaponItems.RAPIER;
import static bot.data.items.data.WeaponItems.SPEAR;
import static bot.data.items.data.WeaponItems.SPIKED_KNUCKLES;
import static bot.data.items.data.WeaponItems.SPIKED_MACE;
import static bot.data.items.data.WeaponItems.THROWING_KNIVES;
import static bot.data.items.data.WeaponItems.TOMAHAWK;
import static bot.data.items.data.WeaponItems.getMageSpellbookId;
import static bot.data.items.data.WeaponItems.getMageStaffId;
import static bot.util.CollectionUtils.mapToList;
import static bot.util.EmbedUtils.makeEmbed;
import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

import org.javacord.api.interaction.MessageComponentInteraction;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.commands.rpg.exploration.CommandExplore.ExplorationEventHandler;
import bot.data.items.ItemAmount;
import bot.data.items.data.GemItems.GemType;
import bot.data.items.loot.LootTable;
import bot.userData.ServerUserData;
import bot.util.EmbedUtils.EmbedField;
import bot.util.pages.builders.PagedPickerMessageBuilder;
import bot.util.pages.messages.PagedMessage;

public class ExplorationTravelingMerchant implements ExplorationEventHandler {

	private static final List<String> wares = new ArrayList<>(asList(//
			BATTLE_AXE, //
			BLOWGUN, //
			BROADSWORD, //
			CHAIN_WHIP, //
			CLAYMORE, //
			DAGGER, //
			ESTOC, //
			GLAIVE, //
			HALBERD, //
			KUKRI, //
			KUSARIGAMA, //
			LIGHT_CROSSBOW, //
			LONG_BOW, //
			LONG_SWORD, //
			MACHETE, //
			NUNCHUCK, //
			RAPIER, //
			SPEAR, //
			SPIKED_KNUCKLES, //
			SPIKED_MACE, //
			THROWING_KNIVES, //
			TOMAHAWK, //

			METAL_SHIELD, //
			RING_MAIL, //
			CHAINMAIL, //

			SPELL_VOID_RING, //
			STATUS_NEGATION_RING, //
			REGENERATION_RING));

	static {
		for (final GemType gemType : GemType.values()) {
			wares.add(getMageSpellbookId(gemType));
			wares.add(getMageStaffId(gemType));
		}
	}

	private static final LootTable<String> waresTable = LootTable.list(wares);

	private final Fluffer10kFun fluffer10kFun;

	public ExplorationTravelingMerchant(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;
	}

	private void onRemove(final MessageComponentInteraction interaction) {
		interaction.createOriginalMessageUpdater()
				.addEmbed(makeEmbed("Traveling merchant", "Traveling merchant leaves")).update();
	}

	private PagedMessage preparePagedMessage(final ServerUserData userData, final List<EmbedField> fields,
			final List<ItemAmount> items) {
		return this.preparePagedMessage(userData, fields, items, null);
	}

	private PagedMessage preparePagedMessage(final ServerUserData userData, final List<EmbedField> fields,
			final List<ItemAmount> items, final String description) {
		return new PagedPickerMessageBuilder<>("Traveling merchant", 5, fields, items)//
				.description(description)//
				.dataToEmbed(itemAmount -> itemAmount.getDetails())//
				.onConfirm((interaction, page, itemAmount) -> onPurchase(interaction, userData, itemAmount, items))//
				.onRemove(this::onRemove).build();
	}

	@Override
	public boolean handle(final SlashCommandInteraction interaction, final ServerUserData userData) {
		final List<String> itemIds = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			itemIds.add(waresTable.getItem());
		}

		final List<ItemAmount> items = mapToList(itemIds, fluffer10kFun.items::getItemAmount);
		items.sort(null);
		final List<EmbedField> fields = mapToList(items, itemAmount -> itemAmount.getAsFieldWithPrice());

		fluffer10kFun.pagedMessageUtils.addMessage(preparePagedMessage(userData, fields, items), interaction);

		return true;
	}

	private void onPurchase(final MessageComponentInteraction interaction, final ServerUserData userData,
			final ItemAmount itemAmount, final List<ItemAmount> items) {
		String description;
		if (userData.monies >= itemAmount.item.price) {
			userData.monies -= itemAmount.item.price;
			userData.addItem(itemAmount);
			items.remove(itemAmount);
			description = "It's nice to do business with you, gentleman.\n"//
					+ "Anything else you wanna buy?";
		} else {
			description = "I'm sorry, but I can't sell on credit";
		}

		final List<EmbedField> fields = mapToList(items, itemAmount2 -> itemAmount2.getAsFieldWithPrice());

		fluffer10kFun.pagedMessageUtils.addMessage(preparePagedMessage(userData, fields, items, description),
				interaction);
	}
}
