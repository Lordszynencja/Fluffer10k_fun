package bot.commands;

import static bot.util.apis.MessageUtils.getMentions;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;

import java.util.List;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;
import org.javacord.api.interaction.callback.InteractionImmediateResponseBuilder;

import bot.Fluffer10kFun;
import bot.util.RandomUtils;
import bot.util.apis.APIUtils;
import bot.util.subcommand.Command;

public class CommandShoot extends Command {
	private static final String[][] shootTemplates = { //
			{ "%1$s shot %2$s multiple times with an automatic rifle",
					"https://cdn.discordapp.com/attachments/831093717376172032/831130220898943017/automatic_rifle.gif" },
			{ "%1$s blasted %2$s with a really big laser",
					"https://cdn.discordapp.com/attachments/831093717376172032/831130377598664705/big_laser.gif" },
			{ "%1$s shot a picture of %2$s doing lewd stuff",
					"https://cdn.discordapp.com/attachments/831093717376172032/831130444551421982/camera.jpg" },
			{ "%1$s massacred %2$s with grenade launcher",
					"https://cdn.discordapp.com/attachments/831093717376172032/831130499010527242/grenade_launcher_0.gif" },
			{ "%1$s blew up %2$s with grenade launcher",
					"https://cdn.discordapp.com/attachments/831093717376172032/831130575817015367/grenade_launcher_1.gif" },
			{ "%1$s killed %2$s with new spellcard from America",
					"https://cdn.discordapp.com/attachments/831093717376172032/831130645509570570/gun_spellcard.png" },
			{ "%1$s shot %2$s with a handgun",
					"https://cdn.discordapp.com/attachments/831093717376172032/831130691483598928/handgun.gif" },
			{ "%1$s hit %2$s with an Idcinator, and the target lose interest in living",
					"https://cdn.discordapp.com/attachments/831093717376172032/831130745270829176/idcinator.png" },
			{ "%1$s sniped %2$s with a really long sniper rifle",
					"https://cdn.discordapp.com/attachments/831093717376172032/831130806449209344/long_sniper.gif" },
			{ "%1$s teared through %2$s with a minigun",
					"https://cdn.discordapp.com/attachments/831093717376172032/831130940205170688/minigun.gif" },
			{ "%1$s buzzed through %2$s with a minigun",
					"https://cdn.discordapp.com/attachments/831093717376172032/831130887428374528/minigun_2.gif" },
			{ "%1$s exterminated %2$s with a goddamn nuke!",
					"https://cdn.discordapp.com/attachments/831093717376172032/831131009738997770/nuke.gif" },
			{ "%1$s asked Reimu to exterminate %2$s with her hax spellcard",
					"https://cdn.discordapp.com/attachments/831093717376172032/831131070489296906/Reimu_spellcard.png" },
			{ "%1$s asked Reisen to shoot %2$s with her hand cannnon",
					"https://cdn.discordapp.com/attachments/831093717376172032/831131120808361994/Reisen_handcannon.gif" },
			{ "%1$s bombarded %2$s with rocket artillery",
					"https://cdn.discordapp.com/attachments/831093717376172032/831131178638508052/rocket_artillery.gif" },
			{ "%1$s shot ballistic missiles at %2$s",
					"https://cdn.discordapp.com/attachments/831093717376172032/831131230548394014/rocket_hit.gif" },
			{ "%1$s used rocket launcher on %2$s from close range",
					"https://cdn.discordapp.com/attachments/831093717376172032/831131285929984030/rocket_launcher_close.gif" },
			{ "%1$s sprayed %2$s with SMGs",
					"https://cdn.discordapp.com/attachments/831093717376172032/831131330855043082/SMG.gif" },
			{ "%1$s sniped %2$s",
					"https://cdn.discordapp.com/attachments/831093717376172032/831131374059520030/sniper_rifle.gif" },
			{ "%1$s shot %2$s from a tank",
					"https://cdn.discordapp.com/attachments/831093717376172032/831131429458935858/tank.gif" } };

	private final Fluffer10kFun fluffer10kFun;

	public CommandShoot(final Fluffer10kFun fluffer10kFun) {
		super(fluffer10kFun.apiUtils, "shoot", "Pew pew", //
				SlashCommandOption.create(SlashCommandOptionType.STRING, "target", "target to aim at", false));

		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {
		final Server server = interaction.getServer().orElse(null);
		if (server == null) {
			sendEphemeralMessage(interaction, "This command cannot be used here");
			return;
		}

		final String arg = interaction.getArgumentStringValueByName("target").orElse(null);

		final String shooterName = APIUtils.getUserName(interaction.getUser(), server);
		final String target = arg == null ? "themselves"
				: fluffer10kFun.apiUtils.messageUtils.replaceMentionsWithUserNames(arg, server);
		final String[] template = RandomUtils.getRandom(shootTemplates);

		final EmbedBuilder embed = new EmbedBuilder().setTitle(String.format(template[0], shooterName, target))//
				.setImage(template[1]);

		final InteractionImmediateResponseBuilder responder = interaction.createImmediateResponder().addEmbed(embed);
		final List<String> mentions = getMentions(arg);
		if (!mentions.isEmpty()) {
			responder.append(String.join(" ", mentions));
		}
		responder.respond();
	}
}
