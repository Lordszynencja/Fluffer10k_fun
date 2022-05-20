package bot.commands.rpg;

import java.math.BigDecimal;
import java.math.BigInteger;

public class RPGExpUtils {
	private static final BigInteger firstLevelExp = new BigInteger("300");
	public static final double levelExpMultiplier = 1.252;

	public static int getLevelFromExp(BigInteger exp) {
		BigInteger levelExp = firstLevelExp;
		int level = 0;
		while (exp.compareTo(levelExp) >= 0) {
			exp = exp.subtract(levelExp);
			level++;
			levelExp = new BigDecimal(levelExp).multiply(new BigDecimal(levelExpMultiplier)).toBigInteger();
		}

		return level;
	}

	public static BigInteger getExpForLevel(final int level) {
		BigInteger levelExp = firstLevelExp;
		BigInteger total = BigInteger.ZERO;
		for (int i = 0; i < level; i++) {
			total = total.add(levelExp);
			levelExp = new BigDecimal(levelExp).multiply(new BigDecimal(levelExpMultiplier)).toBigInteger();
		}
		return total;
	}
}
