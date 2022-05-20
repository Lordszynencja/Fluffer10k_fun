package bot.commands.rpg.fight;

import static bot.util.CollectionUtils.mapToList;

import java.util.ArrayList;
import java.util.List;

import org.javacord.api.interaction.MessageComponentInteraction;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.commands.rpg.spells.ActiveSkill;
import bot.userData.ServerUserData;
import bot.userData.rpg.UserRPGData;
import bot.util.EmbedUtils.EmbedField;
import bot.util.pages.builders.PagedPickerMessageBuilder;
import bot.util.pages.messages.PagedMessage;
import bot.util.subcommand.Subcommand;

public class CommandFightSpellHotbar extends Subcommand {

	private final Fluffer10kFun fluffer10kFun;

	public CommandFightSpellHotbar(final Fluffer10kFun fluffer10kFun) {
		super("spell_hotbar", "check and set up your spell hotbar");

		this.fluffer10kFun = fluffer10kFun;
	}

	private EmbedField slotToField(final int slotId, final ActiveSkill spell) {
		return new EmbedField("Slot " + (slotId + 1), spell == null ? "Nothing" : spell.name);
	}

	private EmbedField spellToField(final ActiveSkill spell) {
		return spell == null ? new EmbedField("Nothing", "Clear the slot")
				: new EmbedField(spell.name, spell.description);
	}

	private PagedMessage makeSlotPicker(final UserRPGData userRPGData) {
		final List<Integer> slotIds = new ArrayList<>();
		for (int i = 0; i < userRPGData.spellHotbar.spells.size(); i++) {
			slotIds.add(i);
		}
		final List<EmbedField> fields = mapToList(slotIds,
				slotId -> slotToField(slotId, userRPGData.spellHotbar.spells.get(slotId)));

		return new PagedPickerMessageBuilder<>("Spell hotbar", 5, fields, slotIds)//
				.onPick((interaction2, page, slotId) -> onPickSlot(interaction2, userRPGData, slotId))//
				.build();
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) throws Exception {
		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(interaction.getServer().get(),
				interaction.getUser());

		final PagedMessage msg = makeSlotPicker(userData.rpg);
		fluffer10kFun.pagedMessageUtils.addMessage(msg, interaction);
	}

	private void onPickSlot(final MessageComponentInteraction interaction, final UserRPGData userRPGData,
			final Integer slotId) {
		final List<ActiveSkill> spells = new ArrayList<>();
		spells.add(null);
		spells.addAll(userRPGData.spells());
		final List<EmbedField> fields = mapToList(spells, this::spellToField);

		final PagedMessage msg = new PagedPickerMessageBuilder<>("Pick a spell to put in slot " + (slotId + 1), 5,
				fields, spells)//
						.onPick((interaction2, page, spell) -> onPickSpell(interaction2, userRPGData, slotId, spell))//
						.onRemove(interaction2 -> sendPickSlotAgain(interaction2, userRPGData))//
						.build();
		fluffer10kFun.pagedMessageUtils.addMessage(msg, interaction);
	}

	private void onPickSpell(final MessageComponentInteraction interaction, final UserRPGData userRPGData,
			final Integer slotId, final ActiveSkill spell) {
		userRPGData.spellHotbar.setSpell(slotId, spell);
		sendPickSlotAgain(interaction, userRPGData);
	}

	private void sendPickSlotAgain(final MessageComponentInteraction interaction, final UserRPGData userRPGData) {
		final PagedMessage msg = makeSlotPicker(userRPGData);
		fluffer10kFun.pagedMessageUtils.addMessage(msg, interaction);
	}
}
