package bot.commands.rpg.danuki;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;

import java.util.List;

import bot.Fluffer10kFun;
import bot.data.items.SimpleItemAmount;
import bot.data.items.data.MonmusuDropItems;
import bot.data.items.data.PotionItems;
import bot.userData.ServerUserData;

public enum DanukiShopUnlock {
	STRENGTH_1_POTION(PotionItems.STRENGTH_1_POTION, new QuestUnlock(true)), //
	AGILITY_1_POTION(PotionItems.AGILITY_1_POTION, new QuestUnlock(true)), //
	INTELLIGENCE_1_POTION(PotionItems.INTELLIGENCE_1_POTION, new QuestUnlock(true)), //

	STRENGTH_2_POTION(PotionItems.STRENGTH_2_POTION, //
			new ItemsUnlock(new SimpleItemAmount(MonmusuDropItems.HOLSTAUR_MILK, 25), //
					new SimpleItemAmount(PotionItems.STRENGTH_1_POTION, 5))), //
	AGILITY_2_POTION(PotionItems.AGILITY_2_POTION, //
			new ItemsUnlock(new SimpleItemAmount(MonmusuDropItems.SLIME_JELLY, 5), //
					new SimpleItemAmount(MonmusuDropItems.SLIME_JELLY_BUBBLE, 5), //
					new SimpleItemAmount(MonmusuDropItems.SLIME_JELLY_DARK, 5), //
					new SimpleItemAmount(MonmusuDropItems.SLIME_JELLY_HUMPTY_EGG, 5), //
					new SimpleItemAmount(MonmusuDropItems.SLIME_JELLY_NUREONAGO, 5), //
					new SimpleItemAmount(MonmusuDropItems.SLIME_JELLY_RED, 5), //
					new SimpleItemAmount(PotionItems.AGILITY_1_POTION, 5))), //
	INTELLIGENCE_2_POTION(PotionItems.INTELLIGENCE_2_POTION, //
			new ItemsUnlock(new SimpleItemAmount(MonmusuDropItems.SUCCUBUS_NOSTRUM, 50), //
					new SimpleItemAmount(PotionItems.INTELLIGENCE_1_POTION, 5))), //

	STRENGTH_3_POTION(PotionItems.STRENGTH_3_POTION, new QuestUnlock(false)), //
	AGILITY_3_POTION(PotionItems.AGILITY_3_POTION, new QuestUnlock(false)), //
	INTELLIGENCE_3_POTION(PotionItems.INTELLIGENCE_3_POTION, new QuestUnlock(false)), //

	DOPPELGANGER_POTION(PotionItems.DOPPELGANGER_POTION, new QuestUnlock(false));

	public static interface DanukiShopUnlockData {
		boolean visible(ServerUserData userData);

		boolean canBeUnlocked(ServerUserData userData);

		void pay(ServerUserData userData);

		String getDescription(Fluffer10kFun fluffer10kFun);
	}

	private static class QuestUnlock implements DanukiShopUnlockData {

		private final boolean visible;

		public QuestUnlock(final boolean visible) {
			this.visible = visible;
		}

		@Override
		public boolean visible(final ServerUserData userData) {
			return visible;
		}

		@Override
		public boolean canBeUnlocked(final ServerUserData userData) {
			return false;
		}

		@Override
		public void pay(final ServerUserData userData) {
		}

		@Override
		public String getDescription(final Fluffer10kFun fluffer10kFun) {
			return "Unlocked by quest";
		}
	}

	private static class ItemsUnlock implements DanukiShopUnlockData {
		private final List<SimpleItemAmount> items;

		public ItemsUnlock(final SimpleItemAmount... items) {
			this.items = asList(items);
		}

		@Override
		public boolean visible(final ServerUserData userData) {
			return true;
		}

		@Override
		public boolean canBeUnlocked(final ServerUserData userData) {
			return !items.stream()
					.anyMatch(itemAmount -> userData.items.getOrDefault(itemAmount.id, 0L) < itemAmount.amount);
		}

		@Override
		public void pay(final ServerUserData userData) {
			items.stream()//
					.map(itemAmount -> itemAmount.minus())//
					.forEach(userData::addItem);
		}

		@Override
		public String getDescription(final Fluffer10kFun fluffer10kFun) {
			final String itemNames = items.stream().map(fluffer10kFun.items::getName)//
					.collect(joining("\n"));
			return "Unlocked by paying with items:\n" + itemNames;
		}
	}

	public final String itemId;
	public final DanukiShopUnlockData unlockData;

	private DanukiShopUnlock(final String itemId, final DanukiShopUnlockData unlockData) {
		this.itemId = itemId;
		this.unlockData = unlockData;
	}
}
