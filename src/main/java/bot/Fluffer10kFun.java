package bot;

import static bot.util.EmbedUtils.makeEmbed;
import static java.util.Arrays.asList;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.javacord.api.entity.intent.Intent;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandBuilder;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;

import bot.commands.CommandDaily;
import bot.commands.CommandMGE;
import bot.commands.CommandOwoify;
import bot.commands.CommandPp;
import bot.commands.CommandShoot;
import bot.commands.casino.CommandCasino;
import bot.commands.cookies.CommandCookie;
import bot.commands.cookies.CommandCookies;
import bot.commands.cookies.CommandMilk;
import bot.commands.cookies.CommandMilks;
import bot.commands.cookies.CookieUtils;
import bot.commands.debug.CommandDebug;
import bot.commands.fluff.CommandFluff;
import bot.commands.fluff.CommandFluffiness;
import bot.commands.fluff.CommandWag;
import bot.commands.fluff.FluffyTailUtils;
import bot.commands.goldenCookies.GoldenCookies;
import bot.commands.imageManipulation.CommandInvert;
import bot.commands.imageManipulation.CommandMeme;
import bot.commands.imageManipulation.CommandPfp;
import bot.commands.jobs.Jobs;
import bot.commands.mgLove.CommandMgLove;
import bot.commands.mgLove.CommandMgLoveCount;
import bot.commands.mgLove.CommandMgLoveHard;
import bot.commands.mgLove.CommandMgLoveProtectionUse;
import bot.commands.races.CommandRace;
import bot.commands.rpg.CommandAvatar;
import bot.commands.rpg.CommandBackpack;
import bot.commands.rpg.CommandBalance;
import bot.commands.rpg.CommandBlessings;
import bot.commands.rpg.CommandChop;
import bot.commands.rpg.CommandEq;
import bot.commands.rpg.CommandEquip;
import bot.commands.rpg.CommandImprove;
import bot.commands.rpg.CommandName;
import bot.commands.rpg.CommandStatusDescription;
import bot.commands.rpg.CommandStatuses;
import bot.commands.rpg.CommandTrade;
import bot.commands.rpg.CommandTransfer;
import bot.commands.rpg.CommandUnequip;
import bot.commands.rpg.CommandUse;
import bot.commands.rpg.RPGStatUtils;
import bot.commands.rpg.blacksmith.CommandBlacksmith;
import bot.commands.rpg.blacksmith.blueprints.BlacksmithBlueprintsList;
import bot.commands.rpg.blacksmith.tasks.BlacksmithTasksList;
import bot.commands.rpg.danuki.CommandDanuki;
import bot.commands.rpg.exploration.CommandExplore;
import bot.commands.rpg.fight.CommandFight;
import bot.commands.rpg.fight.FightActionsHandler;
import bot.commands.rpg.fight.FightSender;
import bot.commands.rpg.fight.FightStart;
import bot.commands.rpg.fight.FighterCreator;
import bot.commands.rpg.fight.actions.FightActionUtils;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils;
import bot.commands.rpg.harem.CommandHarem;
import bot.commands.rpg.harem.CommandHouse;
import bot.commands.rpg.jeweller.CommandJeweller;
import bot.commands.rpg.questCommand.CommandQuest;
import bot.commands.rpg.quests.QuestUtils;
import bot.commands.rpg.saves.CommandSaves;
import bot.commands.rpg.shop.CommandShop;
import bot.commands.rpg.spells.CommandSkills;
import bot.commands.rpg.spells.CommandSpellbook;
import bot.commands.running.CommandCatch;
import bot.commands.running.CommandCatchLevel;
import bot.commands.running.CommandRun;
import bot.commands.running.CommandRunLevel;
import bot.commands.upgrades.CommandUpgrade;
import bot.commands.utility.CommandAnswer;
import bot.commands.utility.CommandBotChannel;
import bot.commands.utility.CommandHelp;
import bot.commands.utility.CommandMemes;
import bot.commands.utility.CommandModRole;
import bot.commands.utility.CommandReminder;
import bot.commands.utility.CommandRoll;
import bot.commands.utility.CommandVoid;
import bot.data.BotDataUtils;
import bot.data.Emojis;
import bot.data.items.Items;
import bot.userData.ServerUserDataUtils;
import bot.userData.UserDataUtils;
import bot.util.apis.APIUtils;
import bot.util.apis.CommandHandlers;
import bot.util.modularPrompt.ModularPromptUtils;
import bot.util.pages.PagedMessageUtils;

public class Fluffer10kFun {
	public final APIUtils apiUtils;
	public final Emojis emojis;
	public final PagedMessageUtils pagedMessageUtils;
	public final ModularPromptUtils modularPromptUtils;

	public final BotDataUtils botDataUtils;
	public final Items items;
	public final ServerUserDataUtils serverUserDataUtils;
	public final CookieUtils cookieUtils;
	public final GoldenCookies goldenCookies;
	public final Jobs jobs;
	public final FluffyTailUtils fluffyTailUtils;
	public final UserDataUtils userDataUtils;
	public final RPGStatUtils rpgStatUtils;
	public final RPGEnemyActionSelectorUtils rpgEnemyActionSelectorUtils;
	public final RPGEnemies rpgEnemies;
	public final QuestUtils questUtils;
	public final FightActionsHandler fightActionsHandler;
	public final FightActionUtils fightActionUtils;
	public final FighterCreator fighterCreator;
	public final FightStart fightStart;
	public final FightSender fightSender;

	public final CommandAnswer commandAnswer;
	public final CommandAvatar commandAvatar;
	public final CommandBackpack commandBackpack;
	public final CommandBalance commandBalance;
	public final CommandBlacksmith commandBlacksmith;
	public final CommandBlessings commandBlessings;
	public final CommandBotChannel commandBotChannel;
	public final CommandCasino commandCasino;
	public final CommandCatch commandCatch;
	public final CommandCatchLevel commandCatchLevel;
	public final CommandChop commandChop;
	public final CommandCookie commandCookie;
	public final CommandCookies commandCookies;
	public final CommandDaily commandDaily;
	public final CommandDanuki commandDanuki;
	public final CommandDebug commandDebug;
	public final CommandEq commandEq;
	public final CommandEquip commandEquip;
	public final CommandExplore commandExplore;
	public final CommandFight commandFight;
	public final CommandFluff commandFluff;
	public final CommandFluffiness commandFluffiness;
	public final CommandHarem commandHarem;
	public final CommandHelp commandHelp;
	public final CommandHouse commandHouse;
	public final CommandImprove commandImprove;
	public final CommandInvert commandInvert;
	public final CommandJeweller commandJeweller;
	public final CommandMgLove commandMgLove;
	public final CommandMgLoveCount commandMgLoveCount;
	public final CommandMgLoveHard commandMgLoveHard;
	public final CommandMgLoveProtectionUse commandMgLoveProtectionUse;
	public final CommandMeme commandMeme;
	public final CommandMemes commandMemes;
	public final CommandMGE commandMGE;
	public final CommandMilk commandMilk;
	public final CommandMilks commandMilks;
	public final CommandModRole commandModRole;
	public final CommandName commandName;
	public final CommandOwoify commandOwoify;
	public final CommandPfp commandPfp;
	public final CommandPp commandPp;
	public final CommandQuest commandQuest;
	public final CommandRace commandRace;
	public final CommandReminder commandReminder;
	public final CommandRoll commandRoll;
	public final CommandRun commandRun;
	public final CommandRunLevel commandRunLevel;
	public final CommandSaves commandSaves;
	public final CommandShoot commandShoot;
	public final CommandShop commandShop;
	public final CommandSkills commandSkills;
	public final CommandSpellbook commandSpellbook;
	public final CommandStatusDescription commandStatusDescription;
	public final CommandStatuses commandStatuses;
	public final CommandTrade commandTrade;
	public final CommandTransfer commandTransfer;
	public final CommandUnequip commandUnequip;
	public final CommandUpgrade commandUpgrade;
	public final CommandUse commandUse;
	public final CommandVoid commandVoid;
	public final CommandWag commandWag;

	public Fluffer10kFun() throws IOException {
		apiUtils = new APIUtils("Fluffer 10k Fun", "fluffer10kFun_config.txt", asList(Intent.GUILD_MEMBERS));

		try {
			emojis = new Emojis(apiUtils);
			pagedMessageUtils = new PagedMessageUtils(apiUtils.commandHandlers);
			modularPromptUtils = new ModularPromptUtils(apiUtils.commandHandlers);

			botDataUtils = new BotDataUtils(this);
			items = new Items(this);
			BlacksmithTasksList.addTasks();
			BlacksmithBlueprintsList.addBlueprints(items);
			serverUserDataUtils = new ServerUserDataUtils(this);
			cookieUtils = new CookieUtils(this);
			goldenCookies = new GoldenCookies(this);
			jobs = new Jobs(this);
			fluffyTailUtils = new FluffyTailUtils(this);
			userDataUtils = new UserDataUtils(this);
			rpgStatUtils = new RPGStatUtils(this);
			rpgEnemyActionSelectorUtils = new RPGEnemyActionSelectorUtils(this);
			rpgEnemies = new RPGEnemies(this);
			questUtils = new QuestUtils(this);
			fightActionsHandler = new FightActionsHandler(this);
			fightActionUtils = new FightActionUtils(this);
			fighterCreator = new FighterCreator(this);
			fightStart = new FightStart(this);
			fightSender = new FightSender(this);

			commandAnswer = new CommandAnswer(this);
			commandAvatar = new CommandAvatar(this);
			commandBackpack = new CommandBackpack(this);
			commandBalance = new CommandBalance(this);
			commandBlacksmith = new CommandBlacksmith(this);
			commandBlessings = new CommandBlessings(this);
			commandBotChannel = new CommandBotChannel(this);
			commandCasino = new CommandCasino(this);
			commandCatch = new CommandCatch(this);
			commandCatchLevel = new CommandCatchLevel(this);
			commandChop = new CommandChop(this);
			commandCookie = new CommandCookie(this);
			commandCookies = new CommandCookies(this);
			commandDaily = new CommandDaily(this);
			commandDanuki = new CommandDanuki(this);
			commandDebug = new CommandDebug(this);
			commandEq = new CommandEq(this);
			commandEquip = new CommandEquip(this);
			commandExplore = new CommandExplore(this);
			commandFight = new CommandFight(this);
			commandFluff = new CommandFluff(this);
			commandFluffiness = new CommandFluffiness(this);
			commandHarem = new CommandHarem(this);
			commandHelp = new CommandHelp(this);
			commandHouse = new CommandHouse(this);
			commandImprove = new CommandImprove(this);
			commandInvert = new CommandInvert(this);
			commandJeweller = new CommandJeweller(this);
			commandMgLove = new CommandMgLove(this);
			commandMgLoveCount = new CommandMgLoveCount(this);
			commandMgLoveHard = new CommandMgLoveHard(this);
			commandMgLoveProtectionUse = new CommandMgLoveProtectionUse(this);
			commandMeme = new CommandMeme(this);
			commandMemes = new CommandMemes(this);
			commandMGE = new CommandMGE(this);
			commandMilk = new CommandMilk(this);
			commandMilks = new CommandMilks(this);
			commandModRole = new CommandModRole(this);
			commandName = new CommandName(this);
			commandOwoify = new CommandOwoify(this);
			commandPfp = new CommandPfp(this);
			commandPp = new CommandPp(this);
			commandQuest = new CommandQuest(this);
			commandRace = new CommandRace(this);
			commandReminder = new CommandReminder(this);
			commandRoll = new CommandRoll(this);
			commandRun = new CommandRun(this);
			commandRunLevel = new CommandRunLevel(this);
			commandSaves = new CommandSaves(this);
			commandShoot = new CommandShoot(this);
			commandShop = new CommandShop(this);
			commandSkills = new CommandSkills(this);
			commandSpellbook = new CommandSpellbook(this);
			commandStatusDescription = new CommandStatusDescription(this);
			commandStatuses = new CommandStatuses(this);
			commandTrade = new CommandTrade(this);
			commandTransfer = new CommandTransfer(this);
			commandUnequip = new CommandUnequip(this);
			commandUpgrade = new CommandUpgrade(this);
			commandUse = new CommandUse(this);
			commandVoid = new CommandVoid(this);
			commandWag = new CommandWag(this);

			final SlashCommandBuilder scb = SlashCommand.with("print", "prints")
					.addOption(SlashCommandOption.create(SlashCommandOptionType.STRING, "arg", "arg", true));
			apiUtils.commandHandlers.addSlashCommandHandler("print", interaction -> {
				interaction.createImmediateResponder().append("ok").respond();
				System.out.println(interaction.getArgumentStringValueByName("arg").get());
			}, scb);

			apiUtils.api.addServerJoinListener(event -> {
				event.getServer().getOwner().get().sendMessage(makeEmbed("Setup", "To set up the bot, use:\n"//
						+ "/bot_channel in the channel where bot can send game tasks (optional, but without it tasks won't appear and experience will be worse)\n"//
						+ "/mod_role @role to set role for mod commands (optional, some commands will only work for people with that role)"));
			});

			apiUtils.endInit();

			CommandHandlers.addOnExit("restarting bot message on servers", () -> {
				botDataUtils.forEachServer((serverId, serverData) -> {
					final MessageBuilder msg = new MessageBuilder()//
							.addEmbed(makeEmbed(
									"Restarting the bot, please wait.\n\nReason:\n" + CommandHandlers.exitMessage));
					final CompletableFuture<Message> message = serverData.sendMessageOnBotChannel(serverId, apiUtils,
							msg);
					if (message != null) {
						try {
							message.get();
						} catch (InterruptedException | ExecutionException e) {
						}
					}
				}, apiUtils.messageUtils);
			});
		} catch (final Exception e) {
			apiUtils.messageUtils.sendExceptionToMe(e);

			throw e;
		}
	}

	public static void main(final String[] args) {
		try {
			new Fluffer10kFun();
		} catch (final Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
}
