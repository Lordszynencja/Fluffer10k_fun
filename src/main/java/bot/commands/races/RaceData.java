package bot.commands.races;

import static bot.data.items.ItemUtils.getFormattedMonies;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.RandomUtils.getRandom;
import static bot.util.RandomUtils.getRandomBoolean;
import static bot.util.RandomUtils.getRandomInt;
import static java.lang.Math.max;

import java.awt.Color;

import org.javacord.api.entity.message.embed.EmbedBuilder;

import bot.commands.upgrades.Upgrade;
import bot.userData.ServerUserData;

public class RaceData {
	private static final Color defaultColor = new Color(0x888888);

	private static String positionName(final int position) {
		if (position % 10 == 1 && position % 100 != 11) {
			return position + "st";
		}
		if (position % 10 == 2 && position % 100 != 12) {
			return position + "nd";
		}
		if (position % 10 == 3 && position % 100 != 13) {
			return position + "rd";
		}
		return position + "th";
	}

	private static final String[] gears = { "neutral", "1st", "2nd", "3rd", "4th", "5th", "6th" };
	private static final String[] speedUpMessages = { "speed up to %s gear, there's %s ahead" };
	private static final String[] slowDownMessages = { "slow down to %s gear, there's %s ahead" };
	private static final String[][] checkpointMessages = { {}, //
			{ "very sharp %s turn", "U-turn %s", "180 turn %s" }, // 1
			{ "sharp turn %s", "L-turn %s" }, // 2
			{ "normal turn %s", "short straight, then turn %s" }, // 3
			{ "easy turn %s", "medium straight, then turn %s" }, // 4
			{ "slight turn %s", "long straight" }, // 5
			{ "very long straight" },// 6
	};

	private static int[] generateTrack() {
		final int length = 10 + getRandomInt(10) + getRandomInt(10);
		final int[] track = new int[length];
		for (int i = 0; i < length; i++) {
			track[i] = 1 + getRandomInt(6);
			if (i > 0 && track[i] == track[i - 1]) {
				track[i] = track[i] == 6 ? 5 : track[i] + 1;
			}
		}

		return track;
	}

	private static String prettyTime(final long t) {
		return String.format("%d:%02d.%03d", t / 1000 / 60, (t / 1000) % 60, t % 1000);
	}

	private int stage = 0;
	private int gear = 0;
	private final int[] track = generateTrack();
	private final long startTime = System.currentTimeMillis();
	private String nextCheckpointDescription;
	public boolean finished = false;
	private int position = 1;
	private long finishTime = 0;

	public final Long prize;
	private final RaceSponsor sponsor;

	public RaceData(final ServerUserData userData) {
		if (userData.upgrades.contains(Upgrade.SPORT_TIRES)) {
			position--;
		}
		if (userData.upgrades.contains(Upgrade.CAR_TUNING)) {
			position--;
		}

		prize = 100 + userData.raceSponsor.bonus;
		sponsor = userData.raceSponsor.sponsor;

		setNextCheckpointDescription();
	}

	private void setNextCheckpointDescription() {
		final int nextGear = track[stage];
		final String nextCheckpointTypeMsg = String.format(getRandom(checkpointMessages[nextGear]),
				getRandomBoolean() ? "left" : "right");
		nextCheckpointDescription = String.format(getRandom(nextGear > gear ? speedUpMessages : slowDownMessages),
				gears[nextGear], nextCheckpointTypeMsg);
	}

	public long getPlacePrize() {
		return max(0, prize * (6 - position()) / 5);
	}

	public EmbedBuilder toEmbed(final String racerName) {
		final String title = sponsor == null ? "Race" : ("Race sponsored by " + sponsor.name);
		final Color color = sponsor == null ? defaultColor : sponsor.color;
		final EmbedBuilder embed = makeEmbed(title)//
				.setColor(color)//
				.addField("Racer name", racerName, true)//
				.addField(finished ? "Prize" : "1st prize", getFormattedMonies(finished ? getPlacePrize() : prize),
						true)//
				.addField("Position", positionName(position()));
		if (!finished) {
			embed.addField("Next checkpoint", nextCheckpointDescription)//
					.addField("Current gear", gears[gear], true)//
					.addField("Next checkpoint's gear", gears[track[stage]], true);
		}
		embed.addField("Time", prettyTime((finished ? finishTime : System.currentTimeMillis()) - startTime), true);

		if (finished) {
			String fanFeelings;
			switch (position()) {
			case 1:
				fanFeelings = "very happy";
				break;
			case 2:
			case 3:
				fanFeelings = "happy";
				break;
			default:
				fanFeelings = "satisfied, but expect more next time";
				break;
			}
			String description = "Race finished in " + positionName(position()) + " place, fans are " + fanFeelings;
			if (sponsor != null && position() <= 5) {
				description += ", and sponsor increases your bonus from next race";
			}
			description += "!";

			embed.setDescription(description);
		}

		return embed;
	}

	public void changeGearAndUpdateStatus(final boolean gearUp) {
		if (gearUp && gear < 6) {
			gear++;
		}
		if (!gearUp && gear > 0) {
			gear--;
		}

		final int requiredGear = track[stage];

		if (gearUp ? requiredGear < gear : requiredGear > gear) {
			position++;
		}

		if (gear == track[stage]) {
			stage++;
			if (stage >= track.length) {
				finished = true;
				finishTime = System.currentTimeMillis();
			} else {
				setNextCheckpointDescription();
			}
		}
	}

	public int position() {
		return position < 1 ? 1 : position > 8 ? 8 : position;
	}
}
