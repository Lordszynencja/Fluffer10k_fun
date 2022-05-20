package bot.data.items;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

public enum ItemSlot {
	RIGHT_HAND("Right hand"), //
	LEFT_HAND("Left hand"), //
	BOTH_HANDS("Both hands", asList(RIGHT_HAND, LEFT_HAND)), //
	ARMOR("Armor"), //
	RING_1("Ring 1"), //
	RING_2("Ring 2"), //
	PICKAXE("Pickaxe");

	public final String name;
	public final List<ItemSlot> connectedSlots;

	private ItemSlot(final String name) {
		this.name = name;
		connectedSlots = new ArrayList<>();
		connectedSlots.add(this);
	}

	private ItemSlot(final String name, final List<ItemSlot> connectedSlots) {
		this.name = name;
		this.connectedSlots = new ArrayList<>(connectedSlots);
		this.connectedSlots.add(this);
		for (final ItemSlot slot : connectedSlots) {
			slot.connectedSlots.add(this);
		}
	}
}
