package bot.commands.rpg.blacksmith;

import static bot.data.items.ItemUtils.formatNumber;
import static bot.data.items.data.OreItems.ORE_COAL;
import static bot.data.items.data.OreItems.ORE_COPPER;
import static bot.data.items.data.OreItems.ORE_DEMON_REALM_SILVER;
import static bot.data.items.data.OreItems.ORE_DRAGONIUM;
import static bot.data.items.data.OreItems.ORE_GOLD;
import static bot.data.items.data.OreItems.ORE_IRON;
import static bot.data.items.data.OreItems.ORE_SILVER;
import static bot.data.items.loot.Loot.item;
import static bot.data.items.loot.LootTable.list;
import static bot.data.items.loot.LootTable.single;
import static bot.data.items.loot.LootTable.weightedI;
import static bot.data.items.loot.LootTable.weightedTI;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.Utils.Pair.pair;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.data.items.Item;
import bot.data.items.ItemSlot;
import bot.data.items.data.GemItems.GemSize;
import bot.data.items.loot.Loot;
import bot.data.items.loot.LootList;
import bot.data.items.loot.LootTable;
import bot.userData.ServerUserData;
import bot.userData.UserBlessingData.Blessing;
import bot.userData.rpg.UserRPGData;
import bot.util.subcommand.Subcommand;

public class CommandBlacksmithMine extends Subcommand {
	private static final int staminaUse = 30;

	private final Fluffer10kFun fluffer10kFun;

	private final Map<GemSize, LootTable<Loot>> gemLootTables = new HashMap<>();

	private void prepareGemTables() {
		final Map<GemSize, List<Loot>> gemLoots = new HashMap<>();
		gemLoots.put(GemSize.SMALL, new ArrayList<>());
		gemLoots.put(GemSize.MEDIUM, new ArrayList<>());
		gemLoots.put(GemSize.LARGE, new ArrayList<>());

		for (final Item item : fluffer10kFun.items.items.values()) {
			if (item.gemSize != null) {
				gemLoots.get(item.gemSize).add(item(item.id));
			}
		}

		gemLoots.forEach((size, lootList) -> gemLootTables.put(size, list(lootList)));
	}

	private LootTable<Loot> prepareTier1MiningLootTable() {
		final LootTable<Loot> tier1OresTable = weightedI(//
				pair(1, item(ORE_COAL)), //
				pair(1, item(ORE_COPPER)));
		final LootTable<Loot> tier2OresTable = weightedI(//
				pair(1, item(ORE_IRON)), //
				pair(1, item(ORE_SILVER)));

		final LootTable<Loot> oresTable = weightedTI(//
				pair(3, tier1OresTable), //
				pair(1, tier2OresTable));
		final LootTable<Loot> lootTable = weightedTI(//
				pair(9, oresTable), //
				pair(1, miningGemTiers.get(1)));

		return lootTable;
	}

	private LootTable<Loot> prepareTier2MiningLootTable() {
		final LootTable<Loot> coalTable = weightedI(//
				pair(3, item(ORE_COAL)), //
				pair(1, item(ORE_COAL, 3)));
		final LootTable<Loot> copperTable = weightedI(//
				pair(3, item(ORE_COPPER)), //
				pair(1, item(ORE_COPPER, 2)));
		final LootTable<Loot> tier1OresTable = weightedTI(//
				pair(1, coalTable), //
				pair(1, copperTable));

		final LootTable<Loot> tier2OresTable = weightedI(//
				pair(1, item(ORE_IRON)), //
				pair(1, item(ORE_SILVER)));
		final LootTable<Loot> tier3OresTable = weightedI(//
				pair(1, item(ORE_GOLD)), //
				pair(1, item(ORE_DEMON_REALM_SILVER)));

		final LootTable<Loot> oresTable = weightedTI(//
				pair(9, tier1OresTable), //
				pair(3, tier2OresTable), //
				pair(1, tier3OresTable));
		final LootTable<Loot> lootTable = weightedTI(//
				pair(9, oresTable), //
				pair(1, miningGemTiers.get(2)));

		return lootTable;
	}

	private LootTable<Loot> prepareTier3MiningLootTable() {
		final LootTable<Loot> coalTable = weightedI(//
				pair(5, item(ORE_COAL)), //
				pair(3, item(ORE_COAL, 3)), //
				pair(1, item(ORE_COAL, 10)));
		final LootTable<Loot> copperTable = weightedI(//
				pair(5, item(ORE_COPPER)), //
				pair(3, item(ORE_COPPER, 2)), //
				pair(1, item(ORE_COPPER, 5)));
		final LootTable<Loot> tier1OresTable = weightedTI(//
				pair(1, coalTable), //
				pair(1, copperTable));

		final LootTable<Loot> ironTable = weightedI(//
				pair(3, item(ORE_IRON)), //
				pair(1, item(ORE_IRON, 2)));
		final LootTable<Loot> silverTable = weightedI(//
				pair(3, item(ORE_SILVER)), //
				pair(1, item(ORE_SILVER, 2)));
		final LootTable<Loot> tier2OresTable = weightedTI(//
				pair(1, ironTable), //
				pair(1, silverTable));

		final LootTable<Loot> tier3OresTable = weightedI(//
				pair(1, item(ORE_GOLD)), //
				pair(1, item(ORE_DEMON_REALM_SILVER)));
		final LootTable<Loot> tier4OresTable = weightedI(//
				pair(1, item(ORE_DRAGONIUM)));

		final LootTable<Loot> oresTable = weightedTI(//
				pair(27, tier1OresTable), //
				pair(9, tier2OresTable), //
				pair(3, tier3OresTable), //
				pair(1, tier4OresTable));
		final LootTable<Loot> lootTable = weightedTI(//
				pair(9, oresTable), //
				pair(1, miningGemTiers.get(3)));

		return lootTable;
	}

	private final Map<Integer, LootTable<Loot>> miningGemTiers = new HashMap<>();
	private final Map<Integer, LootTable<Loot>> miningOreTiers = new HashMap<>();
	private final Map<Integer, LootTable<Loot>> miningTiers = new HashMap<>();

	private void prepareMiningTiers() {
		miningGemTiers.put(1, gemLootTables.get(GemSize.SMALL));
		miningGemTiers.put(2, weightedTI(//
				pair(1, gemLootTables.get(GemSize.SMALL)), //
				pair(1, gemLootTables.get(GemSize.MEDIUM))));
		miningGemTiers.put(3, weightedTI(//
				pair(1, gemLootTables.get(GemSize.SMALL)), //
				pair(1, gemLootTables.get(GemSize.MEDIUM)), //
				pair(1, gemLootTables.get(GemSize.LARGE))));

		miningOreTiers.put(1, weightedTI(//
				pair(3, weightedI(//
						pair(1, item(ORE_COAL, 20)), //
						pair(1, item(ORE_COPPER, 10)))), //
				pair(1, weightedI(//
						pair(1, item(ORE_IRON, 10)), //
						pair(1, item(ORE_SILVER, 5))))));
		miningOreTiers.put(2, weightedTI(//
				pair(9, weightedI(//
						pair(1, item(ORE_COAL, 20)), //
						pair(1, item(ORE_COPPER, 10)))), //
				pair(3, weightedI(//
						pair(1, item(ORE_IRON, 10)), //
						pair(1, item(ORE_SILVER, 5)))), //
				pair(1, weightedI(//
						pair(1, item(ORE_GOLD, 5)), //
						pair(1, item(ORE_DEMON_REALM_SILVER, 3))))));
		miningOreTiers.put(3, weightedTI(//
				pair(27, weightedI(//
						pair(1, item(ORE_COAL, 20)), //
						pair(1, item(ORE_COPPER, 10)))), //
				pair(9, weightedI(//
						pair(1, item(ORE_IRON, 10)), //
						pair(1, item(ORE_SILVER, 5)))), //
				pair(3, weightedI(//
						pair(1, item(ORE_GOLD, 5)), //
						pair(1, item(ORE_DEMON_REALM_SILVER, 3)))), //
				pair(1, single(item(ORE_DRAGONIUM, 3)))));

		miningTiers.put(1, prepareTier1MiningLootTable());
		miningTiers.put(2, prepareTier2MiningLootTable());
		miningTiers.put(3, prepareTier3MiningLootTable());
	}

	public void printProficiencyExp(final boolean print) {
		if (!print) {
			return;
		}

		final UserRPGData d = new UserRPGData();
		long exp = 0;
		long levelExp = UserRPGData.lvl2MiningExp;
		for (int i = 0; i < 10; i++) {
			exp += levelExp;
			levelExp = (long) (levelExp * UserRPGData.miningLvlExpMultiplier);
			d.miningExp = exp - 1;
			System.out.println(d.miningExp + ": " + d.getMiningProficiency());
			d.miningExp = exp;
			System.out.println(d.miningExp + ": " + d.getMiningProficiency());
		}
	}

	public CommandBlacksmithMine(final Fluffer10kFun fluffer10kFun) {
		super("mine", "Dig inside your mine");

		this.fluffer10kFun = fluffer10kFun;

		prepareGemTables();
		prepareMiningTiers();
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) throws Exception {
		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(interaction);
		if (!userData.rpg.mineUnlocked) {
			sendEphemeralMessage(interaction, "You didn't unlock this feature");
			return;
		}

		final Item item = fluffer10kFun.items.getItem(userData.rpg.eq.get(ItemSlot.PICKAXE));
		if (item == null) {
			sendEphemeralMessage(interaction, "You don't have pickaxe equipped");
			return;
		}
		if (userData.getStamina() < staminaUse) {
			sendEphemeralMessage(interaction, "You don't have enough stamina");
			return;
		}
		userData.reduceStamina(staminaUse);

		final int proficiency = userData.rpg.getMiningProficiency();
		final LootTable<Loot> table = miningTiers.get(item.pickaxeTier);

		final LootList loot = new LootList();
		for (int i = 0; i < proficiency; i++) {
			loot.add(table.getItem());
		}
		if (userData.blessings.blessingsObtained.contains(Blessing.DWARVEN_ANCESTORS)) {
			loot.add(miningOreTiers.get(item.pickaxeTier).getItem());
		}
		if (userData.blessings.blessingsObtained.contains(Blessing.GEM_SPECIALIST)) {
			loot.add(miningGemTiers.get(item.pickaxeTier).getItem());
		}

		loot.addToUser(userData);
		final long exp = loot.totalValue(fluffer10kFun.items);

		String description = "You find a cave which looks interesting, and decide to dig in it.\n"//
				+ "You find:\n"//
				+ loot.getDescription(fluffer10kFun.items);

		if (exp > 0) {
			userData.rpg.miningExp += exp;
			final int newLevel = userData.rpg.getMiningProficiency();
			if (newLevel > proficiency) {
				description += "\n\n"//
						+ "You get " + formatNumber(exp) + " mining experience and improve your proficiency to "
						+ newLevel + ".";
			} else {
				description += "\n\n"//
						+ "You get " + formatNumber(exp) + " mining experience.";
			}
		}

		interaction.createImmediateResponder().addEmbed(makeEmbed("Mining", description)).respond();
	}
}
