package bot.commands.upgrades;

public enum Upgrade {
	HEAVENLY_CHIPS("Heavenly chips", //
			"Unlock the heavenly power of cookies.\n"//
					+ "Adds 25 gold coins for each bake task done.", //
			200, UpgradeGroup.BAKER), //

	CHEF_HAT("Chef hat", //
			"Makes you look like a real chef, so clients feel easier when they see you make food.\n"//
					+ "Adds 5 gold coins for each chef task done.", //
			100, UpgradeGroup.COOK), //
	NON_STICK_PAN("Non-stick pan", //
			"Makes you waste less time and resources on burnt food.\n" //
					+ "Adds 5 gold coins for each chef task done.", //
			100, UpgradeGroup.COOK), //
	BETTER_INGREDIENTS("Better ingredients", //
			"Your food tastes better, because you get ingredients from better suppliers.\n"//
					+ "Adds 10 gold coins for each chef task done.", //
			250, UpgradeGroup.COOK), //
	BETTER_SPICES("Better spices", //
			"Your food tastes better, and clients will pay more for it.\n"//
					+ "Adds 10 gold coins for each chef task done.", //
			250, UpgradeGroup.COOK), //
	CERAMIC_KNIVES("Ceramic knives", //
			"Helps you chop faster, granting more tips from satisfied clients.\n"//
					+ "Adds 20 gold coins for each chef task done.", //
			1_000, UpgradeGroup.COOK), //
	GOLDEN_SPATULA("Golden spatula", //
			"Looks fancy, granting you more clients.\n" //
					+ "Adds 20 gold coins for each chef task done.", //
			1_000, UpgradeGroup.COOK), //

	AGILITY_TRAINING("Agility training", //
			"Makes your more agile, so you finish your tasks easier, and get more clients.\n"//
					+ "Adds 5 gold coins for each waiter task done.", //
			100, UpgradeGroup.WAITER), //
	CUTE_RIBBON("Cute ribbon", //
			"Makes your cuter, so monster girls pay you bigger tips.\n"//
					+ "Adds 5 gold coins for each waiter task done.", //
			100, UpgradeGroup.WAITER), //
	CUTE_EARS("Cute ears", //
			"You put headband with cute ears on your head, much to clients delight.\n"//
					+ "Adds 10 gold coins for each waiter task done.", //
			250, UpgradeGroup.WAITER), //
	CUTE_TAIL("Cute tail", //
			"You attach a tail to your outfit, which looks cute, and gives you more money from tips.\n"//
					+ "Adds 10 gold coins for each waiter task done.", //
			250, UpgradeGroup.WAITER), //
	FAN_SERVICE("Fan service",
			"You practice your cuteness and shorten your outfit to please the monster girls' eyes more.\n"//
					+ "Adds 20 gold coins for each waiter task done.", //
			1_000, UpgradeGroup.WAITER), //
	NEW_OUTFIT("New outfit", //
			"Clients would love your new outfit, and will give you better tips.\n"//
					+ "Adds 20 gold coins for each waiter task done.", //
			1_000, UpgradeGroup.WAITER), //

	NEWSPAPER_ADS("Newspaper ads", //
			"Make you more popular, and people will wait longer just to get your service.\n"//
					+ "Adds 10 gold coins for each restaurant task done.", //
			500, UpgradeGroup.RESTAURANT), //
	TV_ADS("TV ads", "Make you more popular, and people will wait longer just to get your service.\n"//
			+ "Adds 20 gold coins for each restaurant task done.", //
			2_000, UpgradeGroup.RESTAURANT), //
	ONLINE_ADS("Online ads", "Make you more popular, and people will wait longer just to get your service.\n"//
			+ "Adds 30 gold coins for each restaurant task done.", //
			5_000, UpgradeGroup.RESTAURANT), //

	SPORT_TIRES("Sport tires", //
			"Give you slight advantage on the track, letting you make more mistakes while still winning.\n"
					+ "Lets you have one more mistake and still be in first place.", //
			25_000, UpgradeGroup.RACER),
	CAR_TUNING("Car tuning", //
			"Your car is tuned to achieve more speed and better grip on the track, increasing your chances of winning.\n"
					+ "Lets you have one more mistake and still be in first place.", //
			50_000, UpgradeGroup.RACER),

	SPORT_CAR("Sport car", //
			"Makes you more recognizable, increasing bonus from sponsor.\n"//
					+ "Max sponsor bonus becomes 2 000, and sponsor bonus grows faster.", //
			25_000, UpgradeGroup.RACER),
	SUPER_CAR("Super car", //
			"You have car from famous super car manufacturer, catching eye of a lot of people, and making your sponsor pay more for the advertisments.\n"
					+ "Max sponsor bonus becomes 5 000, and sponsor bonus grows much faster.", //
			100_000, UpgradeGroup.RACER),
	HYPER_CAR("Hyper car", //
			"You have known the best car in the city, and everyone wants to see it, so your sponsor pays way more to put something on it.\n"
					+ "Max sponsor bonus becomes 10 000, and sponsor bonus grows very fast.", //
			1_000_000, UpgradeGroup.RACER);

	public static enum UpgradeGroup {
		BAKER("Baker upgrades"), //
		COOK("Chef upgrades"), //
		RACER("Racer upgrades"), //
		RESTAURANT("Restaurant upgrades"), //
		WAITER("Waiter upgrades");

		public final String name;

		private UpgradeGroup(final String name) {
			this.name = name;
		}
	}

	public final String name;
	public final String description;
	public final long price;
	public final UpgradeGroup group;

	private Upgrade(final String name, final String description, final long price, final UpgradeGroup group) {
		this.name = name;
		this.description = description;
		this.price = price;
		this.group = group;
	}
}
