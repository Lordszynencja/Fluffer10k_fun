package bot.data.items;

public class SimpleItemAmount {
	public String id;
	public long amount;

	public SimpleItemAmount(final String id) {
		this(id, 1);
	}

	public SimpleItemAmount(final String id, final long amount) {
		this.id = id;
		this.amount = amount;
	}

	public SimpleItemAmount minus() {
		return new SimpleItemAmount(id, -amount);
	}
}
