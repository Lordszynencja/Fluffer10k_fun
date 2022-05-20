package bot.userData;

import static bot.util.CollectionUtils.mapToSet;
import static bot.util.Utils.capitalize;
import static bot.util.Utils.intFromNumber;
import static java.util.Arrays.asList;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UserBlessingData {
	public enum Blessing {
		CHANNELING, //
		DECISIVE_STRIKE, //
		DWARVEN_ANCESTORS, //
		ENTHUSIASTIC_FIGHTER, //
		FAMILY_MAN, //
		FAST_RUNNER, //
		GEM_SPECIALIST, //
		LAST_STAND, //
		LUCKY_DUCKY, //
		MANA_MANIAC, //
		MARTIAL_ARTIST, //
		MARTYRDOM, //
		NATURAL_CHARM, //
		NATURAL_RESISTANCE, //
		POTION_MASTER, //
		QUICK_RECOVERY, //
		THE_STORM_THAT_IS_APPROACHING, //
		TERRIFYING_PRESENCE, //
		TRADER, //
		UNDYING, //
		UNSATISFIED_EXPLORER;

		public static final List<Blessing> list = asList(//
				CHANNELING, //
				DECISIVE_STRIKE, //
				DWARVEN_ANCESTORS, //
				ENTHUSIASTIC_FIGHTER, //
				FAMILY_MAN, //
				FAST_RUNNER, //
				GEM_SPECIALIST, //
				LAST_STAND, //
				LUCKY_DUCKY, //
				MANA_MANIAC, //
				MARTIAL_ARTIST, //
				MARTYRDOM, //
				NATURAL_CHARM, //
				NATURAL_RESISTANCE, //
				POTION_MASTER, //
				QUICK_RECOVERY, //
				THE_STORM_THAT_IS_APPROACHING, //
				TERRIFYING_PRESENCE, //
				TRADER, //
				UNDYING, //
				UNSATISFIED_EXPLORER);

		public final String name;

		private Blessing() {
			name = capitalize(name().toLowerCase().replace('_', ' '));
		}
	}

	public int blessings;
	public Set<Blessing> blessingsObtained;

	public UserBlessingData() {
		blessings = 0;
		blessingsObtained = new HashSet<>();
	}

	@SuppressWarnings("unchecked")
	public UserBlessingData(final Map<String, Object> data) {
		if (data == null) {
			blessings = 0;
			blessingsObtained = new HashSet<>();
			return;
		}

		blessings = intFromNumber(data.get("blessings"));
		blessingsObtained = mapToSet((Collection<String>) data.get("blessingsObtained"), Blessing::valueOf);
	}

	public Map<String, Object> toMap() {
		final Map<String, Object> map = new HashMap<>();

		map.put("blessings", blessings);
		map.put("blessingsObtained", blessingsObtained);

		return map;
	}
}
