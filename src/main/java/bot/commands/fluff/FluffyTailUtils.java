package bot.commands.fluff;

import static bot.util.Utils.repeat;

import org.javacord.api.entity.emoji.KnownCustomEmoji;

import bot.Fluffer10kFun;

public class FluffyTailUtils {
	private static final int[] tailsFluffinessRequirements = { //
			0, // 1
			10, // 2
			50, // 3
			150, // 4
			400, // 5
			750, // 6
			1250, // 7
			1750, // 8
			2500 };// 9

	public static enum TailSettingStatus {
		SET, LOWER_LEVEL, ALREADY_UP
	}

	public static int getTailsNumber(final long fluffiness) {
		for (int i = 0; i < tailsFluffinessRequirements.length; i++) {
			if (tailsFluffinessRequirements[i] > fluffiness) {
				return i;
			}
		}

		return 9;
	}

	public final KnownCustomEmoji fluffyTailEmoji;

	public FluffyTailUtils(final Fluffer10kFun fluffer10kFun) {
		fluffyTailEmoji = fluffer10kFun.apiUtils.getEmojiByNameFromMyServer("fluffytail");
	}

	public String getTailsMsg(final int tailsNumber) {
		return repeat(fluffyTailEmoji.getMentionTag(), tailsNumber);
	}
}
