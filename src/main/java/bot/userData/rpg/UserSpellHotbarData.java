package bot.userData.rpg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bot.commands.rpg.spells.ActiveSkill;

public class UserSpellHotbarData {
	public static final int defaultSpellsSize = 5;

	public final List<ActiveSkill> spells = new ArrayList<>();

	{
		setSpell(defaultSpellsSize - 1, null);
	}

	public UserSpellHotbarData() {
	}

	public UserSpellHotbarData(final Map<String, Object> data) {
		if (data != null) {
			data.forEach((positionString, spell) -> {
				setSpell(Integer.valueOf(positionString), ActiveSkill.valueOf((String) spell));
			});
		}
	}

	public Map<String, Object> toMap() {
		final Map<String, Object> map = new HashMap<>();

		for (int i = 0; i < spells.size(); i++) {
			final ActiveSkill spell = spells.get(i);
			if (spell != null) {
				map.put(Integer.toString(i), spell.name());
			}
		}

		return map;
	}

	public void setSpell(final int position, final ActiveSkill spell) {
		while (spells.size() <= position) {
			spells.add(null);
		}
		spells.set(position, spell);

		while (spells.size() > defaultSpellsSize && spells.get(spells.size() - 1) == null) {
			spells.remove(spells.size() - 1);
		}
	}
}
