package bot.commands.mgLove;

import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.RandomUtils.getRandom;
import static bot.util.RandomUtils.getRandomBoolean;
import static bot.util.RandomUtils.getRandomInt;
import static bot.util.RandomUtils.sample;
import static bot.util.apis.MessageUtils.isNSFWChannel;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;

import java.io.IOException;
import java.util.List;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;

import bot.Fluffer10kFun;
import bot.userData.ServerUserData;
import bot.userData.UserStatusesData.UserStatusType;
import bot.util.FileUtils;
import bot.util.Utils;
import bot.util.subcommand.Command;

public class CommandMgLove extends Command {
	private static boolean isSavedFromMgLove(final ServerUserData userData) {
		if (!userData.useMgLoveProtection) {
			return false;
		}

		if (userData.statuses.isStatus(UserStatusType.MG_LOVE_PROTECTION)) {
			return true;
		}

		if (getRandomBoolean(userData.rpg.level / 100.0)) {
			return true;
		}

		return false;
	}

	private final Fluffer10kFun fluffer10kFun;
	private final long botId;

	private final String[] mgLoveImages;
	private final String thonkEmote;
	private final String horaEmote;
	private final String[][] mgLoveEmotes;

	public CommandMgLove(final Fluffer10kFun fluffer10kFun) throws IOException {
		super(fluffer10kFun.apiUtils, "mg_love", "Ara ara~", //
				SlashCommandOption.create(SlashCommandOptionType.USER, "target", "victim of ara ara", true));

		this.fluffer10kFun = fluffer10kFun;
		botId = fluffer10kFun.apiUtils.api.getClientId();

		mgLoveImages = FileUtils
				.readFileLines(fluffer10kFun.apiUtils.config.getString("imageFolderPath") + "mg_love/links.txt");
		thonkEmote = fluffer10kFun.apiUtils.getEmojiStringByNameFromMyServer("thonk");
		horaEmote = fluffer10kFun.apiUtils.getEmojiStringByNameFromMyServer("hora");

		final String fufufuEmote = fluffer10kFun.apiUtils.getEmojiStringByNameFromMyServer("fufufu");
		final String aliceStalkEmote = fluffer10kFun.apiUtils.getEmojiStringByNameFromMyServer("aliceStalk");
		final String tamawooEmote = fluffer10kFun.apiUtils.getEmojiStringByNameFromMyServer("tamawoo");
		final String smugCheshEmote = fluffer10kFun.apiUtils.getEmojiStringByNameFromMyServer("smugChesh");
		final String hellwanEmote = fluffer10kFun.apiUtils.getEmojiStringByNameFromMyServer("hellwan");
		mgLoveEmotes = new String[][] { { fufufuEmote, aliceStalkEmote }, //
				{ fufufuEmote, aliceStalkEmote, tamawooEmote, smugCheshEmote }, //
				{ fufufuEmote, aliceStalkEmote, tamawooEmote, smugCheshEmote, hellwanEmote, horaEmote } };
	}

	private String getMgLoveMessage(final int cums) {
		if (cums == 1) {
			return "once.";
		}
		if (cums == 2) {
			return "twice. " + thonkEmote;
		}
		if (cums == 3) {
			return "thrice! " + horaEmote;
		}
		if (cums == 4) {
			return "4 times! " + horaEmote;
		}

		String msg = cums + (cums < 10 ? " times" : " TIMES") + "!!!";
		if (cums >= 10) {
			msg = Utils.bold(msg);
		}
		final String[] emoteList = cums < 7 ? mgLoveEmotes[0] : (cums < 13 ? mgLoveEmotes[1] : mgLoveEmotes[2]);
		final int emoteNumber = Math.min(emoteList.length, Math.min(5, cums / 4));
		final List<String> emotes = sample(emoteList, emoteNumber);
		for (final String emote : emotes) {
			msg += " " + emote;
		}

		return msg;
	}

	public EmbedBuilder makeMgLoveEmbed(final Server server, final User victim, int cums, final String msgIfRaped,
			final String msgIfNotRaped) {
		if (fluffer10kFun.serverUserDataUtils.getUserData(server, victim).statuses
				.isStatus(UserStatusType.MG_LOVE_ATTRACTION)) {
			cums *= 10;
		}

		final String title = cums > 0 ? String.format(msgIfRaped, victim.getDisplayName(server), getMgLoveMessage(cums))
				: String.format(msgIfNotRaped, victim.getDisplayName(server));
		final String imgUrl = cums > 0 ? getRandom(mgLoveImages) : null;

		return makeEmbed(title).setImage(imgUrl);
	}

	public EmbedBuilder makeMgLoveEmbed(final Server server, final User victim, final int cums) {
		return makeMgLoveEmbed(server, victim, cums, "%1$s came %2$s", "%1$s evaded the love!");
	}

	public EmbedBuilder makeLovedByEmbed(final Server server, final User victim, final int cums,
			final String loveGiverNick) {
		return makeMgLoveEmbed(server, victim, cums, loveGiverNick + " made love to %1$s %2$s",
				"%1$s evaded " + loveGiverNick + "'s love!");
	}

	public void addCums(final Server server, final User user, final int cums) {
		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(server.getId(), user.getId());
		userData.cums += cums;
	}

	private void addCums(final Server server, final MgLoveData mgLoveData) {
		addCums(server, mgLoveData.target, mgLoveData.cums);
	}

	public MgLoveData calculateMgLove(final User victim, final User user, final Server server) {
		if (victim.getId() == botId) {
			final MgLoveData rapeData = new MgLoveData(user, getRandomInt(10, 100));
			addCums(server, rapeData);
			return rapeData;
		}

		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(server.getId(), victim.getId());
		if (userData.isProtectedFromMgLove()) {
			return new MgLoveData(victim, true, false);
		}
		if (isSavedFromMgLove(userData)) {
			return new MgLoveData(victim, false, true);
		}

		int rapes = getRandomBoolean(0.4) ? 0 : 1;
		while (getRandomBoolean(0.75)) {
			rapes++;
		}

		final MgLoveData rapeData = new MgLoveData(victim, rapes);
		addCums(server, rapeData);
		return rapeData;
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {
		final Server server = interaction.getServer().orElse(null);
		if (!isNSFWChannel(interaction) || server == null) {
			sendEphemeralMessage(interaction, "This command cannot be used here");
			return;
		}

		final User target = interaction.getOptionUserValueByName("target").get();

		final MgLoveData mgLoveData = calculateMgLove(target, interaction.getUser(), server);

		if (mgLoveData.protectedFromLove) {
			interaction.createImmediateResponder()
					.addEmbed(makeEmbed(mgLoveData.target.getDisplayName(server) + " is protected from lewd love!"))
					.respond();
			return;
		}
		if (mgLoveData.savedFromLove) {
			interaction.createImmediateResponder()
					.addEmbed(makeEmbed(mgLoveData.target.getDisplayName(server) + " is saved from lewd love!"))
					.respond();
			return;
		}

		interaction.createImmediateResponder().append(target.getMentionTag())
				.addEmbed(makeMgLoveEmbed(server, mgLoveData.target, mgLoveData.cums)).respond();
//	    if imgCommands.bonkedUsers.get(msg.author.id, 0) >= time.time():
//	        await msg.channel.send('No horny!')
//	        return
	}
}
