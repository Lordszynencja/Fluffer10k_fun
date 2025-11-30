package bot.commands.rpg.exploration;

import static bot.data.items.loot.Loot.item;
import static bot.data.items.loot.LootTable.list;
import static bot.data.items.loot.LootTable.weightedI;
import static bot.data.items.loot.LootTable.weightedTI;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.Utils.Pair.pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.commands.rpg.exploration.CommandExplore.ExplorationEventHandler;
import bot.data.items.Item;
import bot.data.items.ItemSlot;
import bot.data.items.data.GemItems.GemSize;
import bot.data.items.loot.Loot;
import bot.data.items.loot.LootList;
import bot.data.items.loot.LootTable;
import bot.userData.ServerUserData;
import bot.util.RandomUtils.RandomValueGenerator;

public class ExplorationMining implements ExplorationEventHandler {
	private static class MiningTierData {
		private final RandomValueGenerator digsGenerator;
		private final LootTable<Loot> lootTable;

		public final long exp;

		public MiningTierData(final RandomValueGenerator digsGenerator, final LootTable<Loot> lootTable,
				final long exp) {
			this.digsGenerator = digsGenerator;
			this.lootTable = lootTable;

			this.exp = exp;
		}

		public Loot getLoot() {
			final LootList loot = new LootList();
			final int digs = digsGenerator.getValue();
			for (int i = 0; i < digs; i++) {
				loot.add(lootTable.getItem());
			}
			return loot;
		}
	}

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

	private final Map<Integer, MiningTierData> miningTiers = new HashMap<>();

	private void prepareMiningTiers() {
		final LootTable<Loot> tier1OreTable = weightedI(//
				pair(5, item("ORE_COAL")), //
				pair(3, item("ORE_COPPER")), //
				pair(1, item("ORE_IRON")));
		final LootTable<Loot> tier1LootTable = weightedTI(//
				pair(9, tier1OreTable), //
				pair(1, gemLootTables.get(GemSize.SMALL)));
		miningTiers.put(1, new MiningTierData(new RandomValueGenerator(1, 5), tier1LootTable, 100));

		final LootTable<Loot> tier2OreTable = weightedI(//
				pair(5, item("ORE_COAL")), //
				pair(3, item("ORE_COAL", 2)), //
				pair(5, item("ORE_COPPER")), //
				pair(1, item("ORE_COPPER", 2)), //
				pair(5, item("ORE_IRON")), //
				pair(3, item("ORE_SILVER")), //
				pair(3, item("ORE_GOLD")), //
				pair(1, item("ORE_DEMON_REALM_SILVER")), //
				pair(1, item("ORE_DRAGONIUM")));
		final LootTable<Loot> tier2LootTable = weightedTI(//
				pair(9, tier2OreTable), //
				pair(1, weightedTI(//
						pair(1, gemLootTables.get(GemSize.SMALL)), //
						pair(1, gemLootTables.get(GemSize.MEDIUM)))));
		miningTiers.put(2, new MiningTierData(new RandomValueGenerator(3, 10), tier2LootTable, 500));

		final LootTable<Loot> tier3OreTable = weightedI(//
				pair(5, item("ORE_COAL")), //
				pair(3, item("ORE_COAL", 2)), //
				pair(1, item("ORE_COAL", 5)), //
				pair(5, item("ORE_COPPER")), //
				pair(3, item("ORE_COPPER", 2)), //
				pair(1, item("ORE_COPPER", 5)), //
				pair(5, item("ORE_IRON")), //
				pair(3, item("ORE_IRON", 2)), //
				pair(1, item("ORE_IRON", 3)), //
				pair(5, item("ORE_SILVER")), //
				pair(1, item("ORE_SILVER", 2)), //
				pair(5, item("ORE_GOLD")), //
				pair(1, item("ORE_GOLD", 2)), //
				pair(5, item("ORE_DEMON_REALM_SILVER")), //
				pair(5, item("ORE_DRAGONIUM")));
		final LootTable<Loot> tier3LootTable = weightedTI(//
				pair(9, tier3OreTable), //
				pair(1, weightedTI(//
						pair(1, gemLootTables.get(GemSize.SMALL)), //
						pair(1, gemLootTables.get(GemSize.MEDIUM)), //
						pair(1, gemLootTables.get(GemSize.LARGE)))));
		miningTiers.put(3, new MiningTierData(new RandomValueGenerator(5, 20), tier3LootTable, 2000));
	}

	private final Fluffer10kFun fluffer10kFun;

	public ExplorationMining(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;
		prepareGemTables();
		prepareMiningTiers();
	}

	@Override
	public boolean handle(final SlashCommandInteraction interaction, final ServerUserData userData) {
		final Item item = fluffer10kFun.items.getItem(userData.rpg.eq.get(ItemSlot.PICKAXE));
		if (item == null) {
			return false;
		}

		final MiningTierData miningTier = miningTiers.get(item.pickaxeTier);
		final Loot loot = miningTier.getLoot();
		loot.addToUser(userData);

		final EmbedBuilder embed = makeEmbed("Mining",
				" You find a cave which looks interesting, and decide to dig in it.\n"//
						+ "You find:\n"//
						+ loot.getDescription(fluffer10kFun.items));

		final EmbedBuilder expEmbed = userData.addExpAndMakeEmbed(fluffer10kFun.apiUtils, miningTier.exp,
				interaction.getUser(), interaction.getServer().get());

		interaction.createImmediateResponder().addEmbeds(embed, expEmbed).respond();

		return true;
	}
}
