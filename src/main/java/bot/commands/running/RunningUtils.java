package bot.commands.running;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RunningUtils {
	public static final int runningTimeout = 60;

	public static class RunnerData {
		public final long userId;
		public final String nick;
		public long timeout;

		public RunnerData(final long userId, final String nick) {
			this.userId = userId;
			this.nick = nick;
			refreshTimeout();
		}

		public void refreshTimeout() {
			timeout = System.currentTimeMillis() + runningTimeout * 1000;
		}
	}

	private static final Map<Long, List<RunnerData>> runners = new HashMap<>();

	public static void init() {
	}

	public static RunnerData getCurrentRunner(final long channelId) {
		final List<RunnerData> channelRunners = runners.getOrDefault(channelId, new ArrayList<>());

		channelRunners.removeIf(runnerData -> runnerData.timeout < System.currentTimeMillis());

		return channelRunners.isEmpty() ? null : channelRunners.get(channelRunners.size() - 1);
	}

	public static void removeRunner(final long channelId, final RunnerData runner) {
		runners.getOrDefault(channelId, new ArrayList<>())//
				.removeIf(runnerData -> runnerData.userId == runner.userId);
	}

	public static boolean addRunner(final long channelId, final long userId, final String userName) {
		List<RunnerData> channelRunners = runners.get(channelId);
		if (channelRunners == null) {
			channelRunners = new ArrayList<>();
			runners.put(channelId, channelRunners);
		}

		for (final RunnerData runner : channelRunners) {
			if (runner.userId == userId && runner.timeout >= System.currentTimeMillis()) {
				return false;
			}
		}

		channelRunners.add(new RunnerData(userId, userName));
		return true;
	}
}
