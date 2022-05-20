package bot.commands.rpg.blacksmith;

import static bot.util.CollectionUtils.addToIntOnMap;
import static bot.util.CollectionUtils.mapToList;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.Button;
import org.javacord.api.entity.message.component.ButtonStyle;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.MessageComponentInteraction;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.commands.rpg.blacksmith.tasks.BlacksmithTier;
import bot.userData.BlacksmithTaskData;
import bot.userData.ServerUserData;
import bot.util.EmbedUtils.EmbedField;
import bot.util.pages.builders.PagedPickerMessageBuilder;
import bot.util.pages.messages.PagedMessage;
import bot.util.subcommand.Subcommand;

public class CommandBlacksmithTask extends Subcommand {
	private final Fluffer10kFun fluffer10kFun;

	public CommandBlacksmithTask(final Fluffer10kFun fluffer10kFun) {
		super("task", "Check your current task");

		fluffer10kFun.apiUtils.commandHandlers.addMessageComponentHandler("blacksmith_task_finish",
				this::handleFinishTask);
		fluffer10kFun.apiUtils.commandHandlers.addMessageComponentHandler("blacksmith_task_clear",
				this::handleClearTask);

		this.fluffer10kFun = fluffer10kFun;
	}

	private EmbedBuilder taskDetails(final ServerUserData userData, final BlacksmithTaskData task) {
		final String description = task.task.description + "\n"//
				+ "Reward is " + task.blueprint.name + "\n\n"//
				+ "Requirements:\n"//
				+ task.task.target.progressDescription(userData, fluffer10kFun.items);

		return makeEmbed("Pick this task?", description, CommandBlacksmith.imgUrl);
	}

	private void sendTaskList(final SlashCommandInteraction interaction, final ServerUserData userData) {
		final List<BlacksmithTaskData> tasks = new ArrayList<>();

		final Map<BlacksmithTier, List<BlacksmithTaskData>> userTasks = userData.blacksmith.getTasks();
		for (final BlacksmithTier tier : BlacksmithTier.tiers) {
			final List<BlacksmithTaskData> tierTasks = userTasks.get(tier);
			if (tierTasks != null && !tierTasks.isEmpty()) {
				tasks.addAll(tierTasks);
			}
		}

		final List<EmbedField> fields = mapToList(tasks, task -> task.toField(fluffer10kFun.items));

		final PagedMessage msg = new PagedPickerMessageBuilder<BlacksmithTaskData>("Blacksmith tasks", 5, fields, tasks)//
				.description("pick one to start")//
				.imgUrl(CommandBlacksmith.imgUrl)//
				.dataToEmbed(task -> taskDetails(userData, task))//
				.onConfirm((interaction2, page, task) -> onPickTask(interaction2, userData, task))//
				.build();
		fluffer10kFun.pagedMessageUtils.addMessage(msg, interaction);
	}

	private ActionRow makeButtons(final long userId) {
		return ActionRow.of(Button.create("blacksmith_task_finish " + userId, ButtonStyle.SUCCESS, "Finish"), //
				Button.create("blacksmith_task_clear " + userId, ButtonStyle.DANGER, "Clear"));
	}

	private void onPickTask(final MessageComponentInteraction interaction, final ServerUserData userData,
			final BlacksmithTaskData task) {
		if (userData.blacksmith.currentTask != null) {
			interaction.createOriginalMessageUpdater()
					.addEmbed(makeEmbed("You can't pick two tasks at once!", null, CommandBlacksmith.imgUrl)).update();
			return;
		}

		final boolean removed = userData.blacksmith.currentTasks.get(task.task.tier).remove(task);
		if (!removed) {
			interaction.createOriginalMessageUpdater()
					.addEmbed(makeEmbed("You can't pick the same task twice!", null, CommandBlacksmith.imgUrl))
					.update();
			return;
		}

		userData.blacksmith.currentTask = task;

		final String description = "Reward is " + task.blueprint.name + "\n\n"//
				+ "Progress:\n"//
				+ task.task.target.progressDescription(userData, fluffer10kFun.items);

		final EmbedBuilder embed = makeEmbed("New blacksmith task", description, CommandBlacksmith.imgUrl);
		interaction.createOriginalMessageUpdater().addEmbed(embed).update();
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) throws Exception {
		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(interaction.getServer().get(),
				interaction.getUser());

		if (!userData.blacksmith.available) {
			sendEphemeralMessage(interaction, "This feature is not unlocked yet");
			return;
		}
		if (userData.blacksmith.currentTask == null) {
			sendTaskList(interaction, userData);
			return;
		}

		final BlacksmithTaskData task = userData.blacksmith.currentTask;
		final String description = "Reward is " + task.blueprint.name + "\n\n"//
				+ "Progress:\n"//
				+ task.task.target.progressDescription(userData, fluffer10kFun.items);

		final EmbedBuilder embed = makeEmbed("Current blacksmith task", description, CommandBlacksmith.imgUrl);
		interaction.createImmediateResponder().addEmbed(embed).addComponents(makeButtons(interaction.getUser().getId()))
				.respond();
	}

	private void handleFinishTask(final MessageComponentInteraction interaction) {
		final long userId = Long.parseLong(interaction.getCustomId().split(" ")[1]);
		if (interaction.getUser().getId() != userId) {
			interaction.acknowledge();
			return;
		}

		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(interaction.getServer().get(),
				userId);

		if (userData.blacksmith.currentTask == null) {
			interaction.createOriginalMessageUpdater()
					.addEmbed(makeEmbed("Did you try to tell me you've done the same task twice?", null,
							CommandBlacksmith.imgUrl))
					.update();
			return;
		}

		final BlacksmithTaskData task = userData.blacksmith.currentTask;
		if (!task.task.target.isMet(userData)) {
			interaction.createOriginalMessageUpdater()
					.addEmbed(makeEmbed("You didn't meet the requirements, dummy!", null, CommandBlacksmith.imgUrl))
					.update();
			return;
		}

		task.task.target.apply(userData);
		addToIntOnMap(userData.blacksmith.blueprints, task.blueprint.id, 1);
		userData.blacksmith.currentTask = null;

		final String description = "Good job!\n"//
				+ "Here, have this " + task.blueprint.name + ".";
		interaction.createOriginalMessageUpdater()
				.addEmbed(makeEmbed("Task finished", description, CommandBlacksmith.imgUrl)).update();
	}

	private void handleClearTask(final MessageComponentInteraction interaction) {
		final long userId = Long.parseLong(interaction.getCustomId().split(" ")[1]);
		if (interaction.getUser().getId() != userId) {
			interaction.acknowledge();
			return;
		}

		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(interaction.getServer().get(),
				userId);
		userData.blacksmith.currentTask = null;

		final EmbedBuilder embed = makeEmbed("Task cleared, I hope next one will go better", null,
				CommandBlacksmith.imgUrl);
		interaction.createOriginalMessageUpdater().addEmbed(embed).update();
	}
}
