package bot.commands.races;

import java.awt.Color;

public enum RaceSponsor {
	COCA_COLA("Coca Cola", new Color(0xFF0000)), //
	COLGATE("Colgate", new Color(0xFFFFFF)), //
	COOKIE_FACTORY("Cookie Factory", new Color(0xAA4411)), //
	CRYPTON_FUTURE_MEDIA("Crypton Future Media", new Color(0x66FFFF)), //
	DAMNED_SOULS("Damned Souls", new Color(0xFFBBBB)), //
	MOBIL_1("Mobil 1", new Color(0x0000BB)), //
	MONSTER("Monster", new Color(0x88FF11)), //
	MONSTER_GIRLS_R_US("Monster Girls'r'us", new Color(0xFF4444)), //
	MONSTERS_INC("Monsters Inc.", new Color(0x11AAFF)), //
	PEPSI("Pepsi", new Color(0x0000FF)), //
	PIRELLI("Pirelli", new Color(0xFF0000)), //
	ROCKSTAR_ENERGY_DRINK("Rockstar Energy Drink", new Color(0xFFFF00)), //
	UBISOFT("Ubisoft", new Color(0x66FFFF));

	public final String name;
	public final Color color;

	private RaceSponsor(final String name, final Color color) {
		this.name = name;
		this.color = color;
	}
}
