package bot.commands.rpg.shop;

import static bot.data.items.data.ArmorItems.LEATHER_ARMOR;
import static bot.data.items.data.CraftingItems.EMPTY_BOOK;
import static bot.data.items.data.MagicScrollItems.MAGIC_SCROLL_FIREBALL;
import static bot.data.items.data.MagicScrollItems.MAGIC_SCROLL_FORCE_HIT;
import static bot.data.items.data.MagicScrollItems.MAGIC_SCROLL_HEAL;
import static bot.data.items.data.MagicScrollItems.MAGIC_SCROLL_ICE_BOLT;
import static bot.data.items.data.MagicScrollItems.MAGIC_SCROLL_LIGHTNING;
import static bot.data.items.data.PickaxeItems.PICKAXE_COPPER;
import static bot.data.items.data.PotionItems.HEALTH_POTION;
import static bot.data.items.data.PotionItems.HEALTH_POTION_MAJOR;
import static bot.data.items.data.PotionItems.HEALTH_POTION_MINOR;
import static bot.data.items.data.PotionItems.MANA_POTION;
import static bot.data.items.data.PotionItems.MANA_POTION_MAJOR;
import static bot.data.items.data.PotionItems.MANA_POTION_MINOR;
import static bot.data.items.data.WeaponItems.BATTLE_AXE;
import static bot.data.items.data.WeaponItems.DAGGER;
import static bot.data.items.data.WeaponItems.DARTS;
import static bot.data.items.data.WeaponItems.JAVELIN;
import static bot.data.items.data.WeaponItems.KAMA;
import static bot.data.items.data.WeaponItems.KNIFE;
import static bot.data.items.data.WeaponItems.KUNAI;
import static bot.data.items.data.WeaponItems.LONG_BOW;
import static bot.data.items.data.WeaponItems.LONG_SWORD;
import static bot.data.items.data.WeaponItems.LUMBERJACK_AXE;
import static bot.data.items.data.WeaponItems.SCYTHE;
import static bot.data.items.data.WeaponItems.SHORT_BOW;
import static bot.data.items.data.WeaponItems.SHORT_SWORD;
import static bot.data.items.data.WeaponItems.SLINGSHOT;
import static bot.data.items.data.WeaponItems.WHIP;
import static bot.data.items.data.WeaponItems.WOODEN_SHIELD;
import static bot.data.items.data.WeaponItems.getApprenticeBookId;
import static bot.data.items.data.WeaponItems.getApprenticeStaffId;
import static bot.data.items.loot.LootTable.weightedI;
import static bot.util.CollectionUtils.addToLongOnMap;
import static bot.util.CollectionUtils.toMap;
import static bot.util.RandomUtils.getRandomBoolean;
import static bot.util.TimerUtils.startRepeatedTimedEvent;
import static bot.util.Utils.Pair.pair;
import static bot.util.apis.MessageUtils.isNSFWChannel;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;

import java.util.Map;

import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.data.ServerData;
import bot.data.items.data.GemItems.GemType;
import bot.data.items.loot.LootTable;
import bot.userData.ServerUserData;
import bot.util.subcommand.Command;

public class CommandShop extends Command {
	public static final String shopkeeperImgUrl = "https://cdn.discordapp.com/attachments/671477247315673128/825150136882692136/seller.jpg";

	private final Fluffer10kFun fluffer10kFun;

	private static void removeRandomItems(final ServerData serverData) {
		for (final String itemId : serverData.shopItems.keySet()) {
			final double amount = 0.1 * serverData.shopItems.get(itemId);
			final int full = (int) amount;
			addToLongOnMap(serverData.shopItems, itemId, -full);
			if (getRandomBoolean(amount % 1.0)) {
				addToLongOnMap(serverData.shopItems, itemId, -1);
			}
		}
	}

	private static final Map<String, Long> basicItemAmounts = toMap(//
			pair(HEALTH_POTION_MINOR, 2L), //
			pair(HEALTH_POTION, 2L), //
			pair(HEALTH_POTION_MAJOR, 2L), //
			pair(MANA_POTION_MINOR, 5L), //
			pair(MANA_POTION, 2L), //
			pair(MANA_POTION_MAJOR, 1L), //
			pair(PICKAXE_COPPER, 1L), //
			pair(EMPTY_BOOK, 1L));

	private static void stockUpBasicItems(final ServerData serverData) {
		basicItemAmounts.forEach((itemId, amount) -> {
			if (serverData.shopItems.getOrDefault(itemId, 0L) < amount) {
				serverData.shopItems.put(itemId, amount);
			}
		});
	}

	private static final LootTable<Long> defaultItemAmountChange = weightedI(pair(2, -1L), pair(3, 0L), pair(1, 1L));
	private static final LootTable<Long> increasedItemAmountChange = weightedI(pair(6, -1L), pair(9, 0L), pair(1, 1L),
			pair(1, 2L));
	private static final LootTable<Long> decreasedItemAmountChange = weightedI(pair(11, -1L), pair(1, 1L));

	private static final Map<String, LootTable<Long>> nonBasicItemChances = toMap(//
			pair(LEATHER_ARMOR, defaultItemAmountChange), //

			pair(DARTS, defaultItemAmountChange), //
			pair(JAVELIN, defaultItemAmountChange), //
			pair(KAMA, defaultItemAmountChange), //
			pair(KNIFE, defaultItemAmountChange), //
			pair(KUNAI, defaultItemAmountChange), //
			pair(LUMBERJACK_AXE, increasedItemAmountChange), //
			pair(SCYTHE, defaultItemAmountChange), //
			pair(SHORT_BOW, increasedItemAmountChange), //
			pair(SHORT_SWORD, defaultItemAmountChange), //
			pair(SLINGSHOT, defaultItemAmountChange), //
			pair(WHIP, defaultItemAmountChange), //
			pair(WOODEN_SHIELD, increasedItemAmountChange), //

			pair(MAGIC_SCROLL_FORCE_HIT, defaultItemAmountChange), //
			pair(MAGIC_SCROLL_HEAL, increasedItemAmountChange), //

			pair(PICKAXE_COPPER, increasedItemAmountChange), //
			pair(EMPTY_BOOK, increasedItemAmountChange), //

			pair(BATTLE_AXE, decreasedItemAmountChange), //
			pair(DAGGER, decreasedItemAmountChange), //
			pair(LONG_BOW, decreasedItemAmountChange), //
			pair(LONG_SWORD, decreasedItemAmountChange), //
			pair(MAGIC_SCROLL_FIREBALL, decreasedItemAmountChange), //
			pair(MAGIC_SCROLL_ICE_BOLT, decreasedItemAmountChange), //
			pair(MAGIC_SCROLL_LIGHTNING, decreasedItemAmountChange));

	static {
		for (final GemType gemType : GemType.values()) {
			nonBasicItemChances.put(getApprenticeBookId(gemType), decreasedItemAmountChange);
			nonBasicItemChances.put(getApprenticeStaffId(gemType), decreasedItemAmountChange);
		}
	}

	private static void setUpNonBasicItems(final ServerData serverData) {
		nonBasicItemChances.forEach((itemId, table) -> {
			long amount = serverData.shopItems.getOrDefault(itemId, 0L) + table.getItem();
			if (amount < 0) {
				amount = 0;
			}
			serverData.shopItems.put(itemId, amount);
		});
	}

	private static void updateItemsOnServer(final long serverId, final ServerData serverData) {
		removeRandomItems(serverData);
		stockUpBasicItems(serverData);
		setUpNonBasicItems(serverData);
	}

	public static void addItemsOnNewServer(final ServerData serverData) {
		stockUpBasicItems(serverData);
		setUpNonBasicItems(serverData);
	}

	private void updateItems() {
		try {
			fluffer10kFun.botDataUtils.forEachServer(CommandShop::updateItemsOnServer);
		} catch (final Exception e) {
			fluffer10kFun.apiUtils.messageUtils.sendExceptionToMe(e);
		}
	}

	public CommandShop(final Fluffer10kFun fluffer10kFun) {
		super(fluffer10kFun.apiUtils, "shop", "RPG shop commands", //
				new CommandShopBuy(fluffer10kFun), //
				new CommandShopSell(fluffer10kFun));

		this.fluffer10kFun = fluffer10kFun;

		startRepeatedTimedEvent(this::updateItems, 60 * 60, 60 * 60, "changes items in shop");
		if (fluffer10kFun.apiUtils.config.getBoolean("debug")) {
			updateItems();
		}
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) throws Exception {
		final Server server = interaction.getServer().orElse(null);
		if (!isNSFWChannel(interaction) || server == null) {
			sendEphemeralMessage(interaction, "This command cannot be used here");
			return;
		}

		final User user = interaction.getUser();
		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(server.getId(), user.getId());
		if (userData.rpg.fightId != null) {
			sendEphemeralMessage(interaction, "Can't use shop during a fight!");
			return;
		}

		subcommandHandler.handle(interaction);
	}
}
