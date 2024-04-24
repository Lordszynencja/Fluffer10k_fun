package bot.data.fight;

import static bot.util.CollectionUtils.mapMapString;
import static bot.util.CollectionUtils.mapToList;
import static java.lang.Math.max;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bot.commands.rpg.RPGStatUtils.RPGStatsDataBuilder;

public class FighterStatusesData {
	public final Map<FighterStatus, FighterStatusData> statuses = new HashMap<>();

	public FighterStatusesData() {
	}

	public FighterStatusesData(final Map<String, Map<String, Object>> data) {
		data.forEach((statusId, statusData) -> {
			try {
				final FighterStatus type = FighterStatus.valueOf(statusId);
				statuses.put(type, new FighterStatusData(type, statusData));
			} catch (final Exception e) {
				e.printStackTrace();
			}
		});
	}

	public Map<String, Object> toMap() {
		return mapMapString(statuses, statusData -> statusData.toMap());
	}

	public void setStatus(final FighterStatusData status) {
		statuses.put(status.type, status);
	}

	public void addStatus(final FighterStatusData status) {
		final FighterStatusData currentStatus = statuses.get(status.type);
		if (currentStatus == null) {
			statuses.put(status.type, status);
			return;
		}

		currentStatus.stacks += status.stacks;
		currentStatus.endless |= status.endless;
		currentStatus.duration = max(currentStatus.duration, status.duration);
	}

	public void removeStatus(final FighterStatus... types) {
		for (final FighterStatus type : types) {
			statuses.remove(type);
		}
	}

	public boolean isStatus(final FighterStatus type) {
		return statuses.get(type) != null;
	}

	public List<FighterStatusData> getSortedStatuses() {
		final List<FighterStatusData> statuses = new ArrayList<>(this.statuses.values());
		statuses.sort((a, b) -> a.type.name.compareTo(b.type.name));
		return statuses;
	}

	private int offsetStacks(final int stacks, final int offset) {
		return stacks > 0 ? offset + stacks : 0;
	}

	public void addStats(final RPGStatsDataBuilder stats) {
		statuses.values().forEach(status -> {
			final int multiplier = status.type.statsStack ? status.stacks : 1;
			stats.strength += status.type.strength * multiplier;
			stats.agility += status.type.agility * multiplier;
			stats.intelligence += status.type.intelligence * multiplier;
			stats.armor += status.type.armor * multiplier;
			stats.magicResistance += status.type.magicResistance * multiplier;
			stats.healthRegenerationBonus += status.type.healthRegen * multiplier;
		});

		final int allStatBonus = 0//
				- getStacks(FighterStatus.ALRAUNE_VINES)//
				- getStacks(FighterStatus.ATLACH_NACHA_VENOM)//
				- getStacks(FighterStatus.FREEZING)//
				- (getStacks(FighterStatus.CURSED_SWORD_CUT)//
						+ offsetStacks(getStacks(FighterStatus.ILLUSION_WORLD), 1)//
						+ getStacks(FighterStatus.KEJOUROU_HAIR)//
						+ offsetStacks(getStacks(FighterStatus.POISON), 1)) / 2;

		stats.strength += allStatBonus;
		stats.agility += allStatBonus;
		stats.intelligence += allStatBonus;
	}

	public String getDescription() {
		final List<String> statusDescriptions = mapToList(getSortedStatuses(), data -> data.description());
		return statusDescriptions.isEmpty() ? "None" : String.join(", ", statusDescriptions);
	}

	public boolean cantDoActions() {
		for (final FighterStatusData status : statuses.values()) {
			if (status.type.preventsActions) {
				return true;
			}
		}
		return false;
	}

	public boolean cantDodge() {
		return statuses.values().stream().anyMatch(status -> status.type.preventsDodge);
	}

	public boolean cantUseItems() {
		return statuses.values().stream().anyMatch(status -> status.type.preventsItemUse);
	}

	public boolean canGetFree() {
		return statuses.values().stream().anyMatch(status -> status.type.canBreakFree);
	}

	public int getStacks(final FighterStatus status) {
		return statuses.getOrDefault(status, FighterStatusData.empty).stacks;
	}
}
