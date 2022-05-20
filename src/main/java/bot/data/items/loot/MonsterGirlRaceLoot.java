package bot.data.items.loot;

import static bot.util.RandomUtils.getRandomBoolean;
import static java.util.Arrays.asList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.items.data.MonmusuDropItems;

public class MonsterGirlRaceLoot {
	public static final Map<MonsterGirlRace, List<String>> racesProducts = new HashMap<>();
	static {
		racesProducts.put(MonsterGirlRace.ANT_ARACHNE, asList(MonmusuDropItems.ARACHNE_SILK));
		racesProducts.put(MonsterGirlRace.ARACHNE, asList(MonmusuDropItems.ARACHNE_SILK));
		racesProducts.put(MonsterGirlRace.ATLACH_NACHA, asList(MonmusuDropItems.ARACHNE_SILK));
		racesProducts.put(MonsterGirlRace.BAROMETZ, asList(MonmusuDropItems.ALRAUNE_NECTAR,
				MonmusuDropItems.BAROMETZ_COTTON, MonmusuDropItems.BAROMETZ_FRUIT));
		racesProducts.put(MonsterGirlRace.BUBBLE_SLIME, asList(MonmusuDropItems.SLIME_JELLY_BUBBLE));
		racesProducts.put(MonsterGirlRace.DARK_SLIME, asList(MonmusuDropItems.SLIME_JELLY_DARK));
		racesProducts.put(MonsterGirlRace.GIRTABLILU, asList(MonmusuDropItems.ARACHNE_SILK));
		racesProducts.put(MonsterGirlRace.HOLSTAUR, asList(MonmusuDropItems.HOLSTAUR_MILK));
		racesProducts.put(MonsterGirlRace.HUMPTY_EGG, asList(MonmusuDropItems.SLIME_JELLY_HUMPTY_EGG));
		racesProducts.put(MonsterGirlRace.JOROU_GUMO, asList(MonmusuDropItems.ARACHNE_SILK));
		racesProducts.put(MonsterGirlRace.LILIRAUNE, asList(MonmusuDropItems.ALRAUNE_NECTAR));
		racesProducts.put(MonsterGirlRace.MERMAID, asList(MonmusuDropItems.MERMAID_BLOOD));
		racesProducts.put(MonsterGirlRace.MERROW, asList(MonmusuDropItems.MERROW_BLOOD));
		racesProducts.put(MonsterGirlRace.NUREONAGO, asList(MonmusuDropItems.SLIME_JELLY_NUREONAGO));
		racesProducts.put(MonsterGirlRace.QUEEN_SLIME, asList(MonmusuDropItems.SLIME_JELLY));
		racesProducts.put(MonsterGirlRace.RED_SLIME, asList(MonmusuDropItems.SLIME_JELLY_RED));
		racesProducts.put(MonsterGirlRace.SLIME, asList(MonmusuDropItems.SLIME_JELLY));
		racesProducts.put(MonsterGirlRace.USHI_ONI, asList(MonmusuDropItems.ARACHNE_SILK));
		racesProducts.put(MonsterGirlRace.WERESHEEP, asList(MonmusuDropItems.WERESHEEP_WOOL));
	}

	public static Loot getMGLoot(final MonsterGirlRace race) {
		final LootList loot = new LootList();

		final List<String> raceProducts = racesProducts.get(race);
		if (raceProducts == null) {
			return loot;
		}

		for (final String product : raceProducts) {
			if (getRandomBoolean(0.5)) {
				loot.add(new LootItem(product));
			}
		}

		return loot;
	}
}
