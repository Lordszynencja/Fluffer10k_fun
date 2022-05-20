package bot.userData.rpg.questData;

import java.util.HashMap;
import java.util.Map;

import bot.data.quests.QuestType;
import bot.util.EmbedUtils.EmbedField;

public class UserQuestData implements Comparable<UserQuestData> {
	public QuestType type;
	public QuestStep step;
	public String description;
	public boolean continuable;

	private final Map<String, String> otherVariables;

	public UserQuestData(final QuestType type, final QuestStep step, final String description,
			final boolean continuable) {
		this.type = type;
		this.step = step;
		this.description = description;
		this.continuable = continuable;
		otherVariables = new HashMap<>();
	}

	public UserQuestData(final QuestType type, final QuestStep step, final String description) {
		this(type, step, description, false);
	}

	public UserQuestData(final QuestType type, final QuestStep step) {
		this(type, step, null);
	}

	@SuppressWarnings("unchecked")
	public UserQuestData(final Map<String, Object> data, final QuestType type) {
		this.type = type;
		step = QuestStep.valueOf((String) data.get("step"));
		description = (String) data.get("description");
		continuable = (boolean) data.get("continuable");
		if (step == QuestStep.FINISHED) {
			continuable = false;
		}
		otherVariables = (Map<String, String>) data.get("otherVariables");
	}

	public Map<String, Object> toMap() {
		final Map<String, Object> map = new HashMap<>();

		map.put("step", step);
		map.put("description", description);
		map.put("continuable", continuable);
		map.put("otherVariables", otherVariables);

		return map;
	}

	@Override
	public int compareTo(final UserQuestData o) {
		return type.name.compareTo(o.type.name);
	}

	public EmbedField toField() {
		return new EmbedField(type.name + (step == QuestStep.FINISHED ? " (finished)" : ""), description);
	}

	@SuppressWarnings("unchecked")
	public <T extends UserQuestData> T asSpecific() {
		return (T) this;
	}

	public UserQuestData description(final String description) {
		this.description = description;
		return this;
	}

	public String getS(final String code) {
		return otherVariables.get(code);
	}

	public int getI(final String code) {
		return Integer.valueOf(getS(code));
	}

	public <E extends Enum<E>> E getE(final String code, final Class<E> e) {
		return Enum.valueOf(e, getS(code));
	}

	public UserQuestData set(final String code, final String s) {
		otherVariables.put(code, s);
		return this;
	}

	public UserQuestData set(final String code, final int i) {
		return set(code, Integer.toString(i));
	}

	public UserQuestData set(final String code, final Enum<?> e) {
		return set(code, e.name());
	}
}
