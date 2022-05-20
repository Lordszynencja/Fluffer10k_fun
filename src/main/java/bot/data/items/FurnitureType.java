package bot.data.items;

public enum FurnitureType {
	SLEEPFUL_BEDDING("set of beautifully crafted bedding that causes sleep",
			"%1$d sets of beautifully crafted bedding that causes sleep"), //
	ADDDITIONAL_ROOM("additional room", "%1$d additional rooms");

	public final String singularName;
	public final String pluralName;

	private FurnitureType(final String singularName, final String pluralName) {
		this.singularName = singularName;
		this.pluralName = pluralName;
	}

	public String getName(final int amount) {
		return amount == 1 ? singularName : String.format(pluralName, amount);
	}
}
