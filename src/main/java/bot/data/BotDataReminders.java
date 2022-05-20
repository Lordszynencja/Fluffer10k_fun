package bot.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import bot.Fluffer10kFun;
import bot.util.TimerUtils;
import bot.util.Utils.Pair;

public class BotDataReminders {
	private final Fluffer10kFun fluffer10kFun;

	public final Map<Long, List<Pair<Long, String>>> reminders = new HashMap<>();

	public BotDataReminders(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;

		reminders.forEach((userId, userReminders) -> userReminders
				.forEach(timeMsg -> startReminder(userId, timeMsg.a, timeMsg.b)));
	}

	public void fromData(final Map<String, List<List<Object>>> data) {
		data.forEach((userId, userReminders) -> {
			if (!userReminders.isEmpty()) {
				reminders.put(Long.valueOf(userId), userReminders.stream()//
						.map(timeMsg -> new Pair<>((Long) timeMsg.get(0), (String) timeMsg.get(1)))//
						.collect(Collectors.toList()));
			}
		});
	}

	public Object toData() {
		final Map<String, List<Object[]>> remindersData = new HashMap<>();
		reminders.forEach((userId, userReminders) -> remindersData.put(userId.toString(), userReminders.stream()//
				.map(timeMsg -> new Object[] { timeMsg.a, timeMsg.b })//
				.collect(Collectors.toList())));
		return remindersData;
	}

	private void startReminder(final long userId, final long time, final String msg) {
		TimerUtils.startTimedEvent(() -> {
			try {
				fluffer10kFun.apiUtils.messageUtils.sendMessageToUser(userId, msg);
			} catch (final Exception e) {
				fluffer10kFun.apiUtils.messageUtils.sendExceptionToMe(e);
			}

			reminders.getOrDefault(userId, new ArrayList<>())//
					.removeIf(timeMsg -> timeMsg.a == time && timeMsg.b.equals(msg));
		}, time);
	}

	public void addReminder(final long userId, final long time, final String msg) {
		reminders.put(userId, reminders.getOrDefault(userId, new ArrayList<>()));
		reminders.get(userId).add(new Pair<>(time, msg));
		startReminder(userId, time, msg);
	}
}
