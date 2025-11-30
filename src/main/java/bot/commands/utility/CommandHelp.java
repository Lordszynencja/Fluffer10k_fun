package bot.commands.utility;

import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.util.pages.builders.PagedMessageBuilder;
import bot.util.subcommand.Command;

public class CommandHelp extends Command {

	private static final PagedMessageBuilder<?> serverMessageBuilder = new PagedMessageBuilder<>()//
			.addSimplePage("Player commands", //
					String.join("\n", //
							"/avatar - set/reset your character's avatar", //
							"/backpack - list your items", //
							"/balance - to quickly check your wallet", //
							"/blessings - to check your blessings", //
							"/eq - check your equipment and statistics", //
							"/equip - equip an item", //
							"/harem - interact with your wives", //
							"/house - buy or check your house", //
							"/improve - improve your stats after level up", //
							"/name - name your character", //
							"/saves - handle your saves", //
							"/skills - check your skill trees", //
							"/spellbook - check your spells", //
							"/trade - trade items", //
							"/transfer - send someone money", //
							"/unequip - unequip an item", //
							"/use - use an item"))//
			.addSimplePage("Rpg commands", //
					String.join("\n", //
							"/blacksmith - crafting, (mining and shop with weapons/ores will be added here too)", //
							"/chop - chop some wood", //
							"/danuki - (brewing), monster items shop", //
							"/explore - explore the world, you can find epic quests, chests full of loot and scary enemies", //
							"/fight - set up hotbar and refresh fight in case it breaks", //
							"/jeweller - joining and refining gems, (enchanting and shop with gems will be added here too)", //
							"/quest - check or continue your quests", //
							"/shop - buy and sell items", //
							"/status_description - check what a status does in fight"))//
			.addSimplePage("Jobs", //
					String.join("\n", //
							"interact with random events to get money and other rewards", //
							"/race - racing for money", //
							"/statuses - your global statuses", //
							"/upgrade - upgrades for jobs"))//
			.addSimplePage("Play coins commands", //
					String.join("\n", //
							"/casino - gambling", //
							"/cookie - bake a cookie (results may vary)", //
							"/cookies - list your cookies and get upgrades for your bakery", //
							"/daily - get your daily allowance"))//
			.addSimplePage("Other commands", //
					String.join("\n", //
							"/answer - because there's no way to ask for input when updating a message, this command is used when answer is needed (only active when needed)", //
							"/catch - catch someone running", //
							"/catch_level - check your catching proficiency", //
							"/fluff - touch fluffy tail!", //
							"/fluffiness - check how fluffy your tails are", //
							"/invert - invert an image", //
							"/meme - sir, we serve memes here", //
							"/mg_love - force your love onto someone", //
							"/mg_love_count - check how many times you were loved", //
							"/mg_love_protection_use - set whether you want to use protection from love", //
							"/mge - check monster girl encyclopedia", //
							"/milk - got milk? Now you do", //
							"/milks - got milk? Now you know", //
							"/owoify - best cOwOmmand", //
							"/pfp - Check someone's profile picture", //
							"/pp - Check your pp size (changes daily)", //
							"/reminder - set up a message to be sent later", //
							"/roll - roll a dice", //
							"/run - run away!", //
							"/run_level - check your running proficiency", //
							"/shoot - shoot someone", //
							"/wag - wag your fluffy tail"))//
			.addSimplePage("Mod/owner commands", //
					String.join("\n", //
							"/bot_channel - for owner, to set up channel for bot to send jobs and events in (must be marked as NSFW!)", //
							"/mod_role - for owner, to set up mod role for mod commands", //
							"/void - to send an empty message clearing the chat a bit"));

	private static final PagedMessageBuilder<?> dmMessageBuilder = new PagedMessageBuilder<>()//
			.addSimplePage("DM commands", //
					String.join("\n", //
							"/catch - catch someone running", //
							"/catch_level - check your catching proficiency", //
							"/cookie - bake a cookie (results may vary)", //
							"/fluff - touch fluffy tail!", //
							"/fluffiness - check how fluffy your tails are", //
							"/invert - invert an image", //
							"/meme - sir, we serve memes here", //
							"/mge - check monster girl encyclopedia", //
							"/milk - got milk? Now you do", //
							"/owoify - best cOwOmmand", //
							"/pfp - Check someone's profile picture", //
							"/pp - Check your pp size (changes daily)", //
							"/reminder - set up a message to be sent later", //
							"/roll - roll a dice", //
							"/run - run away!", //
							"/run_level - check your running proficiency", //
							"/shoot - shoot someone", //
							"/wag - wag your fluffy tail"));

	private final Fluffer10kFun fluffer10kFun;

	public CommandHelp(final Fluffer10kFun fluffer10kFun) {
		super(fluffer10kFun.apiUtils, "help", "Show basic info about the bot");

		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) throws Exception {
		final PagedMessageBuilder<?> messageBuilder = interaction.getServer().isPresent() ? serverMessageBuilder
				: dmMessageBuilder;

		fluffer10kFun.pagedMessageUtils.addMessage(messageBuilder.build(), interaction);
	}

}
