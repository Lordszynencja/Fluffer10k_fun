package bot.commands.rpg.danuki;

import static bot.data.items.ItemUtils.getFormattedMonsterGold;
import static bot.util.Utils.capitalize;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import java.util.List;

import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.data.items.Item;
import bot.userData.ServerUserData;
import bot.util.CollectionUtils.ValueFrom;
import bot.util.EmbedUtils.EmbedField;
import bot.util.pages.builders.PagedMessageBuilder;
import bot.util.pages.messages.PagedMessage;
import bot.util.subcommand.Subcommand;

public class CommandDanukiList extends Subcommand {

	private final Fluffer10kFun fluffer10kFun;

	protected CommandDanukiList(final Fluffer10kFun fluffer10kFun) {
		super("list", "List the items which Danuki offers");

		this.fluffer10kFun = fluffer10kFun;
	}

	private EmbedField unlockedToField(final DanukiShopUnlock unlock, final ServerUserData userData) {
		final Item item = fluffer10kFun.items.getItem(unlock.itemId);
		return new EmbedField(capitalize(item.name), getFormattedMonsterGold(item.mgPrice));
	}

	private EmbedField lockedToField(final DanukiShopUnlock unlock, final ServerUserData userData) {
		return new EmbedField(capitalize(fluffer10kFun.items.getName(unlock.itemId)),
				unlock.unlockData.getDescription(fluffer10kFun));
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) throws Exception {
		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(interaction.getServer().get(),
				interaction.getUser());

		final List<EmbedField> fields = userData.danukiShopUnlocks.stream()//
				.sorted(new ValueFrom<>(unlock -> fluffer10kFun.items.getName(unlock.itemId)))//
				.map(unlock -> unlockedToField(unlock, userData))//
				.collect(toList());
		asList(DanukiShopUnlock.values()).stream()//
				.filter(unlock -> !userData.danukiShopUnlocks.contains(unlock) && unlock.unlockData.visible(userData))//
				.sorted(new ValueFrom<>(unlock -> fluffer10kFun.items.getName(unlock.itemId)))//
				.map(unlock -> lockedToField(unlock, userData))//
				.forEach(fields::add);

		final PagedMessage msg = new PagedMessageBuilder<>("Danuki shop items", 5, fields)//
				.imgUrl(CommandDanuki.imgUrl)//
				.build();
		fluffer10kFun.pagedMessageUtils.addMessage(msg, interaction);
	}
}
