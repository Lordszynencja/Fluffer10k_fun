package bot.commands.jobs;

import static bot.util.RandomUtils.getRandom;
import static bot.util.TimerUtils.startRepeatedTimedEvent;
import static java.util.Arrays.asList;
import static java.util.Collections.shuffle;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.Button;
import org.javacord.api.entity.message.component.ButtonStyle;
import org.javacord.api.entity.message.component.LowLevelComponent;
import org.javacord.api.entity.message.embed.Embed;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.MessageComponentInteraction;

import bot.Fluffer10kFun;
import bot.data.ServerData;
import bot.userData.ServerUserData;
import bot.util.CollectionUtils;

public class Jobs {

	public static interface Job {
		EmbedBuilder createJob();

		String getJobId();

		long calculateReward(final ServerUserData userData);

		String getLabel();
	}

	private final List<Job> jobs;
	private final Map<String, Job> jobsById = new HashMap<>();
	private final Map<String, String> jobLabels = new HashMap<>();

	private Button makeButton(final String jobId, final String correctJobId) {
		return Button.create("job " + jobId + " " + jobId.equals(correctJobId), ButtonStyle.SECONDARY,
				jobLabels.get(jobId));
	}

	private final Fluffer10kFun fluffer10kFun;

	private void removeLastJobMessage(final long serverId, final ServerData serverData) {
		if (serverData.lastJobMessageId == null) {
			return;
		}

		serverData.removeMessageFromBotChannel(serverId, fluffer10kFun.apiUtils, serverData.lastJobMessageId);
		serverData.lastJobMessageId = null;
	}

	private ActionRow makeActionRow(final String jobId) {
		final List<LowLevelComponent> actions = CollectionUtils.mapToList(jobs,
				job -> makeButton(job.getJobId(), jobId));
		shuffle(actions);
		return ActionRow.of(actions);
	}

	private void createJob(final Long serverId, final ServerData serverData)
			throws InterruptedException, ExecutionException {
		final Job job = getRandom(jobs);

		final EmbedBuilder embed = job.createJob();
		final MessageBuilder msg = new MessageBuilder()//
				.addEmbed(embed)//
				.addComponents(makeActionRow(job.getJobId()));

		final CompletableFuture<Message> newMsg = serverData.sendMessageOnBotChannel(serverId, fluffer10kFun.apiUtils,
				msg);

		if (newMsg == null) {
			return;
		}

		try {
			final Message messagePosted = newMsg.get();
			serverData.lastJobMessageId = messagePosted.getId();
			serverData.lastJobMessageIdWrong = false;
		} catch (InterruptedException | ExecutionException e) {
			throw e;
		} catch (final Exception e) {
			final Server server = fluffer10kFun.apiUtils.api.getServerById(serverId).orElse(null);
			final String serverName = server == null ? "null" : server.getName();
			fluffer10kFun.apiUtils.messageUtils
					.sendExceptionToMe("Couldn't get message id for job on server " + serverId + ": " + serverName, e);
		}
	}

	private void tickServerJobs(final Long serverId, final ServerData serverData) {
		if (!serverData.checkBotChannel(serverId, fluffer10kFun.apiUtils)) {
			return;
		}

		removeLastJobMessage(serverId, serverData);

		try {
			createJob(serverId, serverData);
		} catch (InterruptedException | ExecutionException e) {
			fluffer10kFun.apiUtils.messageUtils.sendExceptionToMe(e);
		}
	}

	private void tickJobs() {
		try {
			fluffer10kFun.botDataUtils.forEachServer(this::tickServerJobs, fluffer10kFun.apiUtils.messageUtils);
		} catch (final Exception e) {
			fluffer10kFun.apiUtils.messageUtils.sendExceptionToMe(e);
		}
	}

	public Jobs(final Fluffer10kFun fluffer10kFun) throws IOException {
		this.fluffer10kFun = fluffer10kFun;

		jobs = asList(new JobBaker(fluffer10kFun), //
				new JobCook(), //
				new JobWaiter());

		for (final Job j : jobs) {
			jobsById.put(j.getJobId(), j);
			jobLabels.put(j.getJobId(), j.getLabel());
		}

		fluffer10kFun.apiUtils.commandHandlers.addMessageComponentHandler("job", this::handleAction);

		startRepeatedTimedEvent(this::tickJobs, 300, 300, "posting jobs");
	}

	private void handleAction(final MessageComponentInteraction interaction) {
		final Server server = interaction.getServer().get();
		final ServerData serverData = fluffer10kFun.botDataUtils.getServerData(server.getId());
		if (!serverData.lastJobMessageIdWrong && serverData.lastJobMessageId == null) {
			interaction.acknowledge();
			return;
		}

		final String[] tokens = interaction.getCustomId().split(" ");
		final String jobId = tokens[1];
		final boolean correct = Boolean.valueOf(tokens[2]);

		final String msg;
		if (!correct) {
			msg = interaction.getUser().getDisplayName(server)
					+ " failed to provide service to the client, and she went away!";
		} else {
			final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(server.getId(),
					interaction.getUser().getId());
			final long pay = jobsById.get(jobId).calculateReward(userData);
			userData.monies += pay;
			msg = interaction.getUser().getDisplayName(server)
					+ " successfully provided service to the client, and got paid " + pay + " gold coins!";
		}
		serverData.lastJobMessageId = null;
		serverData.lastJobMessageIdWrong = false;

		final List<Embed> embeds = interaction.getMessage().getEmbeds();
		if (embeds.isEmpty()) {
			interaction.acknowledge();
			return;
		}
		final EmbedBuilder embed = embeds.get(0).toBuilder();
		embed.setDescription(msg);

		interaction.createOriginalMessageUpdater().addEmbed(embed)//
				.addComponents(ActionRow.of(asList(Button.create("do_nothing",
						correct ? ButtonStyle.SUCCESS : ButtonStyle.DANGER, jobLabels.get(jobId)))))
				.update();
	}
}
