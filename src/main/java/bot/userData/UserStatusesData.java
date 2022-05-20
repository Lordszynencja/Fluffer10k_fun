package bot.userData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserStatusesData {
	public static enum UserStatusType {
		ADORABLENESS, //
		CHARM, //
		COOLNESS, //
		CUTENESS, //
		EXP_MULTIPLIER_2, //
		FLUFFINESS, //
		HOTNESS, //
		MG_LOVE_ATTRACTION, //
		MG_LOVE_PROTECTION, //
		PRETTINESS, //
		RUNNING_ADVANTAGE, //
		SEXINESS, //
		SUPER_EXP_MULTIPLIER, //
		ULTRA_EXP_MULTIPLIER;
	}

	public static class UserStatusData {
		public static final UserStatusData empty = new UserStatusData(null, 0, null);

		public final UserStatusType type;
		public final long timeout;
		public final String description;

		public UserStatusData(final UserStatusType type, final long timeout, final String description) {
			this.type = type;
			this.timeout = timeout;
			this.description = description;
		}

		public UserStatusData(final UserStatusType type, final Map<String, Object> statusData) {
			this.type = type;
			timeout = ((Number) statusData.get("timeout")).longValue();
			description = (String) statusData.get("description");
		}

		public Map<String, Object> toMap() {
			final Map<String, Object> map = new HashMap<>();
			map.put("timeout", timeout);
			map.put("description", description);
			return map;
		}

		public boolean isOn() {
			return timeout >= System.currentTimeMillis();
		}
	}

	private final Map<UserStatusType, UserStatusData> statuses = new HashMap<>();

	public UserStatusesData() {
	}

	public UserStatusesData(final Map<String, Map<String, Object>> data) {
		data.forEach((statusId, statusData) -> {
			try {
				final UserStatusType type = UserStatusType.valueOf(statusId);
				statuses.put(type, new UserStatusData(type, statusData));
			} catch (final Exception e) {
				e.printStackTrace();
			}
		});
	}

	public Map<String, Object> toMap() {
		final Map<String, Object> map = new HashMap<>();
		statuses.forEach((type, statusData) -> map.put(type.name(), statusData.toMap()));
		return map;
	}

	public void addStatus(final UserStatusType type, final long timeout, final String description) {
		final UserStatusData currentStatus = statuses.get(type);
		if (currentStatus != null && currentStatus.timeout > timeout) {
			return;
		}

		statuses.put(type, new UserStatusData(type, timeout, description));
	}

	public long getStatusTimeout(final UserStatusType type) {
		return statuses.getOrDefault(type, UserStatusData.empty).timeout;
	}

	public boolean isStatus(final UserStatusType type) {
		return statuses.getOrDefault(type, UserStatusData.empty).isOn();
	}

	public List<UserStatusData> getSortedStatuses() {
		final List<UserStatusData> statuses = new ArrayList<>();
		for (final UserStatusData status : this.statuses.values()) {
			if (status.isOn()) {
				statuses.add(status);
			}
		}
		statuses.sort((a, b) -> ((Long) a.timeout).compareTo(b.timeout));
		return statuses;
	}
}
