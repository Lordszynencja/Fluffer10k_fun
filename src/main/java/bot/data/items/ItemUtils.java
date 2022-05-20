package bot.data.items;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import bot.userData.ServerUserData;
import bot.userData.UserBlessingData.Blessing;

public class ItemUtils {
	public static final String playCoinName = "dubloon";
	public static final String playCoinsName = "dubloons";

	private static final DecimalFormat numberFormat;

	static {
		final DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setGroupingSeparator(' ');
		numberFormat = new DecimalFormat("###,###", symbols);
	}

	public static String formatNumber(final long number) {
		return numberFormat.format(number);
	}

	public static String formatNumber(final BigInteger number) {
		return numberFormat.format(number);
	}

	public static String getFormattedMonies(final long amount) {
		return amount == 1 ? "1 gold coin" : formatNumber(amount) + " gold coins";
	}

	public static String getFormattedPlayCoins(final long amount) {
		return amount == 1 ? "1 " + playCoinName : formatNumber(amount) + " " + playCoinsName;
	}

	public static String getFormattedMonsterGold(final long amount) {
		return amount == 1 ? "1 monster gold coin" : formatNumber(amount) + " monster gold coins";
	}

	public static long calculatePrice(final ServerUserData userData, final Item item) {
		double multiplier = 0.8;
		if (userData.blessings.blessingsObtained.contains(Blessing.TRADER)) {
			multiplier *= 1.1;
		}

		return (long) (item.price * multiplier);
	}
}
