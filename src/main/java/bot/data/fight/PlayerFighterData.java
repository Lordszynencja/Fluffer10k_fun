package bot.data.fight;

import static bot.util.Utils.intFromNumber;
import static bot.util.Utils.longFromNumber;

import java.util.Map;

import bot.commands.rpg.RPGStatUtils.RPGStatsData;
import bot.commands.rpg.spells.ActiveSkill;

public class PlayerFighterData extends FighterData {
	public static PlayerFighterData fromStats(final String id, final long userId, final String team, final String name,
			final RPGStatsData stats) {
		final int hp = stats.health;
		final int mana = stats.mana;
		return new PlayerFighterData(id, team, name, hp, mana, stats.level, userId);
	}

	public long userId;
	public int mana;
	public int maxMana;
	public long exp;

	public ActiveSkill lastSpellUsed;

	public PlayerFighterData(final String id, final String team, final String name, final int hp, final int mana,
			final int level, final long userId) {
		super(id, FighterType.PLAYER, team, name, hp, level);
		this.userId = userId;
		this.mana = mana;
		maxMana = mana;
		exp = 0;

		lastSpellUsed = null;
	}

	protected PlayerFighterData(final FightData fight, final String id, final Map<String, Object> data) {
		super(fight, id, data);

		userId = longFromNumber(data.get("userId"));
		mana = intFromNumber(data.get("mana"));
		maxMana = intFromNumber(data.get("maxMana"));
		exp = longFromNumber(data.get("exp"));

		if (data.get("lastSpellUsed") != null) {
			lastSpellUsed = ActiveSkill.valueOf((String) data.get("lastSpellUsed"));
		}
	}

	@Override
	public Map<String, Object> toMap() {
		final Map<String, Object> map = super.toMap();

		map.put("userId", userId);
		map.put("mana", mana);
		map.put("maxMana", maxMana);
		map.put("exp", exp);

		map.put("lastSpellUsed", lastSpellUsed);

		return map;
	}

	@Override
	public void addExp(final long exp) {
		final double expTurnMultiplier = (1 - fight.turn / (fight.turn + 1));
		this.exp = (long) (7 * exp * expTurnMultiplier);
	}
}
