package bot.commands.rpg.exploration;

import static bot.data.items.loot.LootTable.weightedI;
import static bot.util.CollectionUtils.toMap;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.RandomUtils.getRandom;
import static bot.util.Utils.Pair.pair;
import static java.util.Arrays.asList;

import java.util.List;
import java.util.Map;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.commands.rpg.exploration.CommandExplore.ExplorationEventHandler;
import bot.data.items.loot.LootTable;
import bot.userData.ServerUserData;

public class ExplorationNothing implements ExplorationEventHandler {
	private enum TerrainType {
		CAVE("You check out the nearby cave."), //
		CRYSTAL_FOREST("You spend some time watching the amazing crystal forest."), //
		FOREST("You slowly roam the forest, and find only a lot of trees."), //
		GRAVEYARD("You walk through the graveyard, surprisingly not meeting any undead."), //
		MARSH("You slowly walk through the marsh."), //
		MOUNTAIN("You climb a mountain and enjoy the view."), //
		ROAD("You walk along the road to the next village, not meeting anyone on the way."), //
		SHORE("You walk the shore and listen to the sea."), //
		SWAMP("You walk through the dark swamp."), //
		VOLCANO("You climb the volcano and look down on the hot lava."), //
		WATERFALL("You pass by a beautiful waterfall.");

		public final String description;

		private TerrainType(final String description) {
			this.description = description;
		}
	}

	private static final LootTable<TerrainType> typeSelector = weightedI(//
			pair(2, TerrainType.CAVE), //
			pair(1, TerrainType.CRYSTAL_FOREST), //
			pair(2, TerrainType.FOREST), //
			pair(1, TerrainType.GRAVEYARD), //
			pair(2, TerrainType.MARSH), //
			pair(2, TerrainType.MOUNTAIN), //
			pair(2, TerrainType.ROAD), //
			pair(2, TerrainType.SHORE), //
			pair(1, TerrainType.SWAMP), //
			pair(1, TerrainType.VOLCANO), //
			pair(1, TerrainType.WATERFALL));

	private static final Map<TerrainType, List<String>> allTerrainPics = toMap(//
			pair(TerrainType.CAVE, asList(//
					"https://cdn.discordapp.com/attachments/831994087330676807/831994496056950844/cave_0.jpg", //
					"https://cdn.discordapp.com/attachments/831994087330676807/831994540897861742/cave_1.jpg")), //
			pair(TerrainType.CRYSTAL_FOREST, asList(//
					"https://cdn.discordapp.com/attachments/831994087330676807/831994578827608084/crystal_forest_0.jpg")), //
			pair(TerrainType.FOREST, asList(//
					"https://cdn.discordapp.com/attachments/831994087330676807/831994617964658729/forest_0.jpg", //
					"https://cdn.discordapp.com/attachments/831994087330676807/831994720552222810/forest_1.jpg", //
					"https://cdn.discordapp.com/attachments/831994087330676807/831994885682233394/forest_2.jpg", //
					"https://cdn.discordapp.com/attachments/831994087330676807/831994916342726676/forest_3.jpg", //
					"https://cdn.discordapp.com/attachments/831994087330676807/831994943216025650/forest_4.jpg", //
					"https://cdn.discordapp.com/attachments/831994087330676807/831994989004849212/forest_5.jpg", //
					"https://cdn.discordapp.com/attachments/831994087330676807/831995018130227261/forest_6.jpg", //
					"https://cdn.discordapp.com/attachments/831994087330676807/831995046109904956/forest_7.jpg", //
					"https://cdn.discordapp.com/attachments/831994087330676807/831995089768284180/forest_8.jpg", //
					"https://cdn.discordapp.com/attachments/831994087330676807/831995117329973268/forest_9.jpg")), //
			pair(TerrainType.GRAVEYARD, asList(//
					"https://cdn.discordapp.com/attachments/831994087330676807/831995231149883392/graveyard_0.jpg")), //
			pair(TerrainType.MOUNTAIN, asList(//
					"https://cdn.discordapp.com/attachments/831994087330676807/831995278290190386/mountain_0.jpg", //
					"https://cdn.discordapp.com/attachments/831994087330676807/831995306068672532/mountain_1.jpg", //
					"https://cdn.discordapp.com/attachments/831994087330676807/831995339698339880/mountain_2.jpg")), //
			pair(TerrainType.ROAD, asList(//
					"https://cdn.discordapp.com/attachments/831994087330676807/831995388407971860/road_0.jpg",
					"https://cdn.discordapp.com/attachments/831994087330676807/831995422242766888/road_1.jpg")), //
			pair(TerrainType.SHORE, asList(//
					"https://cdn.discordapp.com/attachments/831994087330676807/831995519873974383/shore_0.jpg")), //
			pair(TerrainType.SWAMP, asList(//
					"https://cdn.discordapp.com/attachments/831994087330676807/831995612592472124/swamp_0.jpg")), //
			pair(TerrainType.VOLCANO, asList(//
					"https://cdn.discordapp.com/attachments/831994087330676807/831995638034989076/volcano_0.jpg")), //
			pair(TerrainType.WATERFALL, asList(//
					"https://cdn.discordapp.com/attachments/831994087330676807/831995667890044949/waterfall_0.jpg", //
					"https://cdn.discordapp.com/attachments/831994087330676807/831995694297514085/waterfall_1.jpg")));

	private static final String[] explorationHints = {
			"magic gems and demon realm silver can be used to make magic items", //
			"weresheep wool can be used to make someone fall asleep, and is used in production of pillows and bedding", //
			"otohimes are dragons that live underwater, and their tails look like that of a sea horse. They also charm the man they like by dancing", //
			"baphomets are the strongest monster race, which is surprising given their little size. They are also said to have the best vaginas of all the monsters, and belong to Sabbath", //
			"baphomets were generals of Demon Lord\"s army. They are also incredible spellcasters", //
			"current Demon Lord forced every race not to hurt men and seek their mana instead, by giving them all succubi powers, and making them partially succubi. Demon Lord is also a succubus, and succubi is the biggest monster family as of now", //
			"raiju can create electricity in her body, paralyzing her lover and giving him more pleasure", //
			"couple's fruit resembles a couple, growing from two adjacent trees, and connecting later. It's also one of rare plants that have genders", //
			"ghouls use mouth as sex organ, and want to suck on something all the time. That makes their unused vagina overly sensitive", //
			"slimes are edible, some are very tasty or have special properties", //
			"wurms are earth dragons, because they usually dig through the ground or rocks. They are also incredibly strong, and if they find a man they desire, nothing will stop them, including walls, castles or mountains", //
			"ant arachne is arachne that resembles an ant, and hides in ant hives, waiting for ants to bring resources that she then steals", //
			"the orb that lilims sit on is mad of pure mamono mana, since lilims are daughters of Demon Lord and have a lot of it at their disposal", //
			"mana cages can be used to transport mana, if the monster wants to go through a lot of rough terrain without any humans along the way, or just store mana for later", //
			"chimaeras have body parts resembling lion, dragon, goat and snake, and also different personalities. They are very powerful monsters with multiple abilities", //
			"kamaitachis always group in threes, one very strong, one with the very sharp sickles and one with fast and gentle fingers", //
			"queen of hearts is a lilim and also alice, and she is the creator of Wonderland and many of monster species inside it, including cheshire cat, dormouse, jabberwock, mad hatter, march hares and trumparts. She is regarded as the most powerful of the lilim", //
			"phantoms are treated as nobility among ghosts, and also love theater and drama, having the ability to drag a man into their fantasy world, where they live out their dramatic tales", //
			"fruits from demon realm often have special effects on human men, addicting them, increasing their libido or having other sexual side effects", //
			"some men return from underwater palaces of otohimes and bring a tamate-bako, enchanted box that makes someone relive the memories from the palace. Many men return to the palace and live their lives there after opening the box",//
	};

	@Override
	public boolean handle(final SlashCommandInteraction interaction, final ServerUserData userData) {
		final TerrainType terrain = typeSelector.getItem();
		final String description = terrain.description + "\n\n"//
				+ "random person you met on the way tells you that " + getRandom(explorationHints);

		final List<String> terrainPics = allTerrainPics.get(terrain);
		final String imgUrl = terrainPics == null ? null : getRandom(terrainPics);

		final EmbedBuilder embed = makeEmbed("Nothing happens", description, imgUrl);
		final EmbedBuilder expEmbed = userData.addExpAndMakeEmbed(25, interaction.getUser(),
				interaction.getServer().get());

		interaction.createImmediateResponder().addEmbeds(embed, expEmbed).respond();
		return true;
	}
}
