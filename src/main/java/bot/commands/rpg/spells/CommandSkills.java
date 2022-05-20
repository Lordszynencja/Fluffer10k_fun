package bot.commands.rpg.spells;

import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;
import static bot.util.modularPrompt.ModularPrompt.prompt;
import static bot.util.modularPrompt.ModularPromptButton.button;
import static java.util.stream.Collectors.joining;

import org.javacord.api.entity.message.component.ButtonStyle;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.MessageComponentInteraction;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.commands.rpg.skills.Skill;
import bot.userData.ServerUserData;
import bot.util.modularPrompt.ModularPrompt;
import bot.util.subcommand.Command;

public class CommandSkills extends Command {
	private final Fluffer10kFun fluffer10kFun;

	public CommandSkills(final Fluffer10kFun fluffer10kFun) {
		super(fluffer10kFun.apiUtils, "skills", "Shows skill trees");

		this.fluffer10kFun = fluffer10kFun;
	}

	private ModularPrompt makeBaseMessage(final ServerUserData userData) {
		final EmbedBuilder embed = makeEmbed("Skills");
		embed.setFooter("Points left: " + userData.rpg.skillPointsLeft());

		final ModularPrompt prompt = prompt(embed);
		for (final Skill tree : Skill.trees) {
			prompt.addButton(button(tree.name, ButtonStyle.PRIMARY, in -> handleSkillChange(in, userData, tree)));
		}
		prompt.addButton(null)//
				.addButton(button("Close", ButtonStyle.DANGER,
						in -> in.createOriginalMessageUpdater().removeAllEmbeds().update()));

		return prompt;
	}

	private ModularPrompt makeSkillActiveMessage(final ServerUserData userData, final Skill skill) {
		final EmbedBuilder embed = makeEmbed(skill.name, skill.description)//
				.setFooter("Points left: " + userData.rpg.skillPointsLeft());

		final ModularPrompt prompt = prompt(embed);
		for (final Skill sub : skill.sub) {
			prompt.addButton(button(sub.name, ButtonStyle.PRIMARY, in -> handleSkillChange(in, userData, sub)));
		}
		prompt.addButton(null)//
				.addButton(button("Back", ButtonStyle.DANGER,
						in -> handleSkillChange(in, userData, skill.parents.get(0))));

		return prompt;
	}

	private ModularPrompt makeSkillInactiveMessage(final ServerUserData userData, final Skill skill) {
		final EmbedBuilder embed = makeEmbed(skill.name,
				skill.description + "\n\nCost: " + skill.price + " skill point" + (skill.price == 1 ? "" : "s"))//
						.setFooter("Points left: " + userData.rpg.skillPointsLeft())//
						.addField("Cost", skill.price + (skill.price == 1 ? " skill point" : " skill points"))//
						.addField("Requirements", skill.parents.stream().map(s -> s.name).collect(joining(", ")));

		final boolean getDisabled = !userData.rpg.skills.containsAll(skill.parents)
				|| userData.rpg.skillPointsLeft() < skill.price;

		final ModularPrompt prompt = prompt(embed);

		for (final Skill sub : skill.sub) {
			prompt.addButton(button(sub.name, ButtonStyle.PRIMARY, in -> handleSkillChange(in, userData, sub)));
		}

		prompt.addButton(null)//
				.addButton(button("Get", ButtonStyle.PRIMARY, in -> handleSkillGet(in, userData, skill), getDisabled)) //
				.addButton(button("Back", ButtonStyle.DANGER,
						in -> handleSkillChange(in, userData, skill.parents.get(0))));

		return prompt;
	}

	private ModularPrompt makeMessage(final ServerUserData userData, final Skill skill) {
		if (skill == null) {
			return makeBaseMessage(userData);
		}

		if (userData.rpg.skills.contains(skill)) {
			return makeSkillActiveMessage(userData, skill);
		}

		return makeSkillInactiveMessage(userData, skill);
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) throws Exception {
		final Server server = interaction.getServer().orElse(null);
		if (server == null) {
			sendEphemeralMessage(interaction, "This command can only be used on server");
			return;
		}

		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(interaction);
		fluffer10kFun.modularPromptUtils.addMessage(makeMessage(userData, null), interaction);
	}

	private void handleSkillChange(final MessageComponentInteraction in, final ServerUserData userData,
			final Skill skill) {
		fluffer10kFun.modularPromptUtils.addMessage(makeMessage(userData, skill), in);
	}

	private void handleSkillGet(final MessageComponentInteraction in, final ServerUserData userData,
			final Skill skill) {
		if (userData.rpg.skills.containsAll(skill.parents) && userData.rpg.skillPointsLeft() >= skill.price) {
			userData.rpg.skills.add(skill);
		}

		handleSkillChange(in, userData, skill);
	}

}
