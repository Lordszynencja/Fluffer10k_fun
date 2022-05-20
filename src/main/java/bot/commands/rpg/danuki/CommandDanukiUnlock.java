package bot.commands.rpg.danuki;

import static bot.util.CollectionUtils.mapToList;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import java.util.List;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.MessageComponentInteraction;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.userData.ServerUserData;
import bot.util.CollectionUtils.ValueFrom;
import bot.util.EmbedUtils.EmbedField;
import bot.util.pages.builders.PagedPickerMessageBuilder;
import bot.util.pages.messages.PagedMessage;
import bot.util.subcommand.Subcommand;

public class CommandDanukiUnlock extends Subcommand {

	private final Fluffer10kFun fluffer10kFun;

	protected CommandDanukiUnlock(final Fluffer10kFun fluffer10kFun) {
		super("unlock", "Unlock item in the Danuki shop");

		this.fluffer10kFun = fluffer10kFun;
	}

	private EmbedField lockedToField(final DanukiShopUnlock unlock, final ServerUserData userData) {
		return new EmbedField(fluffer10kFun.items.getName(unlock.itemId),
				unlock.unlockData.getDescription(fluffer10kFun));
	}

	private EmbedBuilder unlockToEmbed(final DanukiShopUnlock unlock) {
		return makeEmbed("Unlock " + fluffer10kFun.items.getName(unlock.itemId) + "?", //
				unlock.unlockData.getDescription(fluffer10kFun), //
				CommandDanuki.imgUrl);
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) throws Exception {
		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(interaction.getServer().get(),
				interaction.getUser());

		final List<DanukiShopUnlock> locked = asList(DanukiShopUnlock.values()).stream()//
				.filter(unlock -> !userData.danukiShopUnlocks.contains(unlock) && unlock.unlockData.visible(userData)
						&& unlock.unlockData.canBeUnlocked(userData))//
				.sorted(new ValueFrom<>(unlock -> fluffer10kFun.items.getName(unlock.itemId)))//
				.collect(toList());
		if (locked.isEmpty()) {
			sendEphemeralMessage(interaction, "There are no items to unlock");
			return;
		}

		final List<EmbedField> fields = mapToList(locked, unlock -> lockedToField(unlock, userData));

		final PagedMessage msg = new PagedPickerMessageBuilder<>("Unlock Danuki shop item", 5, fields, locked)//
				.imgUrl(CommandDanuki.imgUrl)//
				.dataToEmbed(this::unlockToEmbed)//
				.onConfirm((interaction2, page, unlock) -> onConfirm(interaction2, userData, unlock))//
				.build();
		fluffer10kFun.pagedMessageUtils.addMessage(msg, interaction);
	}

	private void onConfirm(final MessageComponentInteraction interaction, final ServerUserData userData,
			final DanukiShopUnlock unlock) {
		if (!unlock.unlockData.canBeUnlocked(userData)) {
			interaction.createOriginalMessageUpdater()//
					.addEmbed(makeEmbed("Can't unlock", //
							"You don't have necessary materials", //
							CommandDanuki.imgUrl))//
					.update();
			return;
		}

		unlock.unlockData.pay(userData);
		userData.danukiShopUnlocks.add(unlock);

		interaction.createOriginalMessageUpdater()//
				.addEmbed(makeEmbed("Item unlocked!", //
						"You unlocked " + fluffer10kFun.items.getName(unlock.itemId), //
						CommandDanuki.imgUrl))//
				.update();
	}
}
