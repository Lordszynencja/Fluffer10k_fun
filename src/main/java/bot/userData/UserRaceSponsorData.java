package bot.userData;

import java.util.HashMap;
import java.util.Map;

import bot.commands.races.RaceSponsor;
import bot.util.Utils;

public class UserRaceSponsorData {
	public RaceSponsor sponsor = null;
	public long bonus = 0;

	public UserRaceSponsorData() {
	}

	public UserRaceSponsorData(final Map<String, Object> data) {
		final String name = (String) data.get("name");
		if (name != null) {
			for (final RaceSponsor s : RaceSponsor.values()) {
				if (s.name.toLowerCase().equals(name.toLowerCase())) {
					sponsor = s;
				}
			}
		}
		final String sponsorId = (String) data.get("sponsor");
		if (sponsorId != null) {
			sponsor = RaceSponsor.valueOf(sponsorId);
		}

		bonus = Utils.longFromNumber(data.getOrDefault("bonus", 0L));
	}

	public Map<String, Object> toMap() {
		final Map<String, Object> map = new HashMap<>();
		map.put("sponsor", sponsor == null ? null : sponsor.name());
		map.put("bonus", bonus);

		return map;
	}
}
