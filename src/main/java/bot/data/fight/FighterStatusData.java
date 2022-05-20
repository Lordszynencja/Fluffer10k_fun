package bot.data.fight;

import static bot.util.Utils.intFromNumber;

import java.util.HashMap;
import java.util.Map;

public class FighterStatusData {
	public static final FighterStatusData empty = new FighterStatusData(null);

	public final FighterStatus type;
	public int duration;
	public boolean endless;
	public int stacks;

	public FighterStatusData(final FighterStatus type) {
		this.type = type;
		duration = 0;
		endless = false;
		stacks = 0;
	}

	public FighterStatusData duration(final int duration) {
		this.duration = duration;
		return this;
	}

	public FighterStatusData endless() {
		endless = true;
		return this;
	}

	public FighterStatusData stacks(final int stacks) {
		this.stacks = stacks;
		return this;
	}

	public FighterStatusData(final FighterStatus type, final Map<String, Object> data) {
		this.type = type;
		duration = intFromNumber(data.get("duration"));
		endless = (boolean) data.get("endless");
		stacks = intFromNumber(data.get("stacks"));
	}

	public Map<String, Object> toMap() {
		final Map<String, Object> map = new HashMap<>();

		map.put("duration", duration);
		map.put("endless", endless);
		map.put("stacks", stacks);

		return map;
	}

	public String description() {
		String description = type.name;
		if (stacks > 1) {
			description += " " + stacks;
		}
		if (!endless) {
			description += " (" + duration + ")";
		}

		return description;
	}
}
