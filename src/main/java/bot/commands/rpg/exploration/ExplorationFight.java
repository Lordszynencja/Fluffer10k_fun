package bot.commands.rpg.exploration;

import static bot.commands.rpg.fight.enemies.races.Apsara.APSARA_2;
import static bot.commands.rpg.fight.enemies.races.Mermaid.MERMAID_2;
import static bot.commands.rpg.fight.enemies.races.Minotaur.MINOTAUR_2;
import static bot.commands.rpg.fight.enemies.races.Myconid.MYCONID_2;
import static bot.commands.rpg.fight.enemies.races.Nightmare.NIGHTMARE_2;
import static bot.commands.rpg.fight.enemies.races.Ogre.OGRE_2;
import static bot.commands.rpg.fight.enemies.races.Orc.ORC_2;
import static bot.commands.rpg.fight.enemies.races.Orc.ORC_3;
import static bot.commands.rpg.fight.enemies.races.Papillon.PAPILLON_2;
import static bot.commands.rpg.fight.enemies.races.ParasiteSlimeSlimeCarrier.PARASITE_SLIME_SLIME_CARRIER_2;
import static bot.commands.rpg.fight.enemies.races.QueenSlime.QUEEN_SLIME_2;
import static bot.commands.rpg.fight.enemies.races.QueenSlime.QUEEN_SLIME_3;
import static bot.commands.rpg.fight.enemies.races.Raiju.RAIJU_2;
import static bot.commands.rpg.fight.enemies.races.RedOni.RED_ONI_2;
import static bot.commands.rpg.fight.enemies.races.RedOni.RED_ONI_3;
import static bot.commands.rpg.fight.enemies.races.Redcap.REDCAP_2;
import static bot.commands.rpg.fight.enemies.races.SeaBishop.SEA_BISHOP_2;
import static bot.commands.rpg.fight.enemies.races.Siren.SIREN_2;
import static bot.commands.rpg.fight.enemies.races.Siren.SIREN_3;
import static bot.commands.rpg.fight.enemies.races.Siren.SIREN_4;
import static bot.commands.rpg.fight.enemies.races.Skeleton.SKELETON_2;
import static bot.commands.rpg.fight.enemies.races.Sphinx.SPHINX_2;
import static bot.commands.rpg.fight.enemies.races.Werecat.WERECAT_2;
import static bot.commands.rpg.fight.enemies.races.Werewolf.WEREWOLF_2;
import static bot.commands.rpg.fight.enemies.races.Werewolf.WEREWOLF_3;
import static bot.data.items.loot.LootTable.weightedD;
import static bot.util.CollectionUtils.addToListOnMap;
import static bot.util.CollectionUtils.toSet;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.RandomUtils.getRandom;
import static bot.util.RandomUtils.getRandomBoolean;
import static bot.util.Utils.fixString;
import static bot.util.Utils.Pair.pair;
import static bot.util.apis.MessageUtils.getServerTextChannel;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;
import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.commands.rpg.exploration.CommandExplore.ExplorationEventHandler;
import bot.commands.rpg.fight.enemies.RPGEnemyData;
import bot.commands.rpg.fight.enemies.races.Apsara;
import bot.commands.rpg.fight.enemies.races.Banshee;
import bot.commands.rpg.fight.enemies.races.Barometz;
import bot.commands.rpg.fight.enemies.races.Basilisk;
import bot.commands.rpg.fight.enemies.races.Beelzebub;
import bot.commands.rpg.fight.enemies.races.BubbleSlime;
import bot.commands.rpg.fight.enemies.races.Charybdis;
import bot.commands.rpg.fight.enemies.races.Cockatrice;
import bot.commands.rpg.fight.enemies.races.DarkPriest;
import bot.commands.rpg.fight.enemies.races.DarkSlime;
import bot.commands.rpg.fight.enemies.races.Ghoul;
import bot.commands.rpg.fight.enemies.races.GiantSlug;
import bot.commands.rpg.fight.enemies.races.GyoubuDanuki;
import bot.commands.rpg.fight.enemies.races.Hakutaku;
import bot.commands.rpg.fight.enemies.races.Hinezumi;
import bot.commands.rpg.fight.enemies.races.Hobgoblin;
import bot.commands.rpg.fight.enemies.races.HumptyEgg;
import bot.commands.rpg.fight.enemies.races.Imp;
import bot.commands.rpg.fight.enemies.races.Inari;
import bot.commands.rpg.fight.enemies.races.Kakuen;
import bot.commands.rpg.fight.enemies.races.Kappa;
import bot.commands.rpg.fight.enemies.races.Kejourou;
import bot.commands.rpg.fight.enemies.races.Kikimora;
import bot.commands.rpg.fight.enemies.races.KitsuneTsuki;
import bot.commands.rpg.fight.enemies.races.LesserSuccubus;
import bot.commands.rpg.fight.enemies.races.LivingDoll;
import bot.commands.rpg.fight.enemies.races.MarchHare;
import bot.commands.rpg.fight.enemies.races.Mermaid;
import bot.commands.rpg.fight.enemies.races.Merrow;
import bot.commands.rpg.fight.enemies.races.Minotaur;
import bot.commands.rpg.fight.enemies.races.Mothman;
import bot.commands.rpg.fight.enemies.races.Myconid;
import bot.commands.rpg.fight.enemies.races.Nekomata;
import bot.commands.rpg.fight.enemies.races.Nightmare;
import bot.commands.rpg.fight.enemies.races.Ogre;
import bot.commands.rpg.fight.enemies.races.Orc;
import bot.commands.rpg.fight.enemies.races.Papillon;
import bot.commands.rpg.fight.enemies.races.ParasiteSlimeSlimeCarrier;
import bot.commands.rpg.fight.enemies.races.QueenSlime;
import bot.commands.rpg.fight.enemies.races.Raiju;
import bot.commands.rpg.fight.enemies.races.RedOni;
import bot.commands.rpg.fight.enemies.races.RedSlime;
import bot.commands.rpg.fight.enemies.races.Redcap;
import bot.commands.rpg.fight.enemies.races.Satyros;
import bot.commands.rpg.fight.enemies.races.SeaBishop;
import bot.commands.rpg.fight.enemies.races.SeaSlime;
import bot.commands.rpg.fight.enemies.races.Siren;
import bot.commands.rpg.fight.enemies.races.Skeleton;
import bot.commands.rpg.fight.enemies.races.Sphinx;
import bot.commands.rpg.fight.enemies.races.Tritonia;
import bot.commands.rpg.fight.enemies.races.Werebat;
import bot.commands.rpg.fight.enemies.races.Werecat;
import bot.commands.rpg.fight.enemies.races.Wererabbit;
import bot.commands.rpg.fight.enemies.races.Weresheep;
import bot.commands.rpg.fight.enemies.races.Werewolf;
import bot.commands.rpg.fight.enemies.races.Youko;
import bot.data.items.loot.LootTable;
import bot.userData.ServerUserData;
import bot.userData.UserBlessingData.Blessing;
import bot.util.CollectionUtils.ValueFrom;
import bot.util.Utils.Pair;

public class ExplorationFight implements ExplorationEventHandler {
	private static final Set<String> enemyIds = new HashSet<>(toSet(//
			Apsara.APSARA_1, APSARA_2, //
			Banshee.BANSHEE_1, Banshee.BANSHEE_2, //
			Barometz.BAROMETZ_1, //
			Basilisk.BASILISK_1, //
			Beelzebub.BEELZEBUB_1, //
			BubbleSlime.BUBBLE_SLIME_1, //
			Charybdis.CHARYBDIS_1, //
			Cockatrice.COCKATRICE_1, //
			DarkPriest.DARK_PRIEST_1, DarkPriest.DARK_PRIEST_2, //
			DarkSlime.DARK_SLIME_1, //
			Ghoul.GHOUL_1, Ghoul.GHOUL_2, //
			GiantSlug.GIANT_SLUG_1, //
			GyoubuDanuki.GYOUBU_DANUKI_1, GyoubuDanuki.GYOUBU_DANUKI_2, //
			Hakutaku.HAKUTAKU_1, Hakutaku.HAKUTAKU_2, Hakutaku.HAKUTAKU_3, //
			Hinezumi.HINEZUMI_1, //
			Hobgoblin.HOBGOBLIN_1, Hobgoblin.HOBGOBLIN_2, //
			HumptyEgg.HUMPTY_EGG_1, //
			Imp.IMP_1, Imp.IMP_2, //
			Inari.INARI_1, Inari.INARI_2, //
			Kakuen.KAKUEN_1, Kakuen.KAKUEN_2, Kakuen.KAKUEN_3, //
			Kappa.KAPPA_1, ///
			Kejourou.KEJOUROU_1, Kejourou.KEJOUROU_2, //
			Kikimora.KIKIMORA_1, //
			KitsuneTsuki.KITSUNE_TSUKI_1, KitsuneTsuki.KITSUNE_TSUKI_2, //
			LesserSuccubus.LESSER_SUCCUBUS_1, //
			LivingDoll.LIVING_DOLL_1, //
			MarchHare.MARCH_HARE_1, //
			Mermaid.MERMAID_1, MERMAID_2, //
			Merrow.MERROW_1, //
			Minotaur.MINOTAUR_1, MINOTAUR_2, //
			Mothman.MOTHMAN_1, //
			Myconid.MYCONID_1, MYCONID_2, //
			Nekomata.NEKOMATA_1, //
			Nightmare.NIGHTMARE_1, NIGHTMARE_2, //
			Ogre.OGRE_1, OGRE_2, //
			Orc.ORC_1, ORC_2, ORC_3, //
			Papillon.PAPILLON_1, PAPILLON_2, //
			ParasiteSlimeSlimeCarrier.PARASITE_SLIME_SLIME_CARRIER_1, PARASITE_SLIME_SLIME_CARRIER_2, //
			QueenSlime.QUEEN_SLIME_1, QUEEN_SLIME_2, QUEEN_SLIME_3, //
			Raiju.RAIJU_1, RAIJU_2, //
			Redcap.REDCAP_1, REDCAP_2, //
			RedOni.RED_ONI_1, RED_ONI_2, RED_ONI_3, //
			RedSlime.RED_SLIME_1, //
			Satyros.SATYROS_1, //
			SeaBishop.SEA_BISHOP_1, SEA_BISHOP_2, //
			SeaSlime.SEA_SLIME_1, //
			Siren.SIREN_1, SIREN_2, SIREN_3, SIREN_4, //
			Skeleton.SKELETON_1, SKELETON_2, //
			Sphinx.SPHINX_1, SPHINX_2, //
			Tritonia.TRITONIA_1, //
			Werebat.WEREBAT_1, //
			Werecat.WERECAT_1, WERECAT_2, //
			Wererabbit.WERERABBIT_1, //
			Weresheep.WERESHEEP_1, //
			Werewolf.WEREWOLF_1, WEREWOLF_2, WEREWOLF_3, //
			Youko.YOUKO_1, Youko.YOUKO_2));

	public static void addEnemyId(final String enemyId) {
		enemyIds.add(enemyId);
	}

	private static final double weightOffset = 0.8;

	private static double levelWeight(final int playerLevel, final int enemyLevel) {
		final int distance = abs(playerLevel - enemyLevel);
		return max(0, min(1, (1 + weightOffset) / (weightOffset + distance * distance)));
	}

	private List<Pair<Double, Integer>> getWeightsForAllLevels(final int playerLevel) {
		final List<Pair<Double, Integer>> weights = new ArrayList<>();
		for (final int enemyLevel : enemyLevels.keySet()) {
			weights.add(pair(levelWeight(playerLevel, enemyLevel), enemyLevel));
		}

		return weights;
	}

	private final Map<String, List<String>> targetsByRace = new HashMap<>();
	private final Map<Integer, List<String>> enemyLevels = new HashMap<>();
	private final Map<Integer, LootTable<Integer>> enemyLevelSelectors = new HashMap<>();

	private final Fluffer10kFun fluffer10kFun;

	private void printLevels(final boolean print) {
		if (print) {
			enemyLevels.entrySet().stream()//
					.sorted(new ValueFrom<>(entry -> entry.getKey()))//
					.peek(entry -> entry.getValue().sort(null))//
					.forEach(System.out::println);
		}
	}

	private void printDistributions(final int... levels) {
		for (final int level : levels) {
			final List<Pair<Double, Integer>> weights = getWeightsForAllLevels(level);
			double weightsSum = 0;
			for (int j = 0; j < weights.size(); j++) {
				weightsSum += weights.get(j).a;
			}
			String s = (level) + ":";
			for (int j = 0; j < weights.size(); j++) {
				s += ", " + weights.get(j).b + ": " + String.format("%1.1f%%", weights.get(j).a / weightsSum * 100);
			}
			System.out.println(s);
		}
	}

	public ExplorationFight(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;

		for (final String enemyId : enemyIds) {
			try {
				final RPGEnemyData enemy = fluffer10kFun.rpgEnemies.get(enemyId);
				addToListOnMap(targetsByRace, fixString(enemy.name), enemy.id);
				addToListOnMap(enemyLevels, enemy.level, enemy.id);
			} catch (final Exception e) {
				System.err.println(enemyId);
				e.printStackTrace();
				throw e;
			}
		}

		for (int i = 0; i < 100; i++) {
			enemyLevelSelectors.put(i, weightedD(getWeightsForAllLevels(i)));
		}

		if (fluffer10kFun.apiUtils.config.getBoolean("debug")) {
			printLevels(true);
			printDistributions();
		}
	}

	public String getTrackingTarget(final SlashCommandInteraction interaction) {
		return fixString(interaction.getArgumentStringValueByName("tracking_target").orElse(null));
	}

	public boolean isValidTrackingTarget(final String trackingTarget) {
		return trackingTarget == null || targetsByRace.get(trackingTarget) != null;
	}

	private RPGEnemyData getTarget(final String trackingTarget, final int playerLevel) {
		if (trackingTarget != null) {
			final List<String> targets = targetsByRace.get(trackingTarget);

			for (int i = 0; i < 5; i++) {
				final String target = getRandom(targets);
				final RPGEnemyData enemyData = fluffer10kFun.rpgEnemies.get(target);
				if (getRandomBoolean(0.5 + 0.1 * (playerLevel - enemyData.level))) {
					return enemyData;
				}
			}
		}

		final LootTable<Integer> enemyLevelSelector = enemyLevelSelectors.get(playerLevel);
		final int enemyLevel = enemyLevelSelector.getItem();
		final List<String> targets = enemyLevels.get(enemyLevel);
		return fluffer10kFun.rpgEnemies.get(getRandom(targets));
	}

	@Override
	public boolean handle(final SlashCommandInteraction interaction, final ServerUserData userData) {
		final String trackingTarget = getTrackingTarget(interaction);
		RPGEnemyData enemy = getTarget(trackingTarget, userData.rpg.level);
		if (userData.blessings.blessingsObtained.contains(Blessing.THE_STORM_THAT_IS_APPROACHING)) {
			final RPGEnemyData enemy2 = getTarget(trackingTarget, userData.rpg.level);
			if (enemy2.level > enemy.level) {
				enemy = enemy2;
			}
		}

		interaction.createImmediateResponder()
				.addEmbed(makeEmbed("Fight", "You find " + enemy.name + " roaming the area!")).respond().join();

		sendEphemeralMessage(interaction, "Fight started!");
		fluffer10kFun.fightStart.startFightPvE(getServerTextChannel(interaction), interaction.getUser(), enemy);

		return true;
	}
}
