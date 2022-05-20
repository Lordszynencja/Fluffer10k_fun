package bot.commands.rpg.fight.fightRewards;

import static bot.commands.rpg.quests.Quest.description;
import static bot.commands.rpg.quests.Quest.dialogue;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.RandomUtils.getRandomInt;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.FightEnd.RewardCreator;
import bot.commands.rpg.fight.FightTempData;
import bot.data.fight.PlayerFighterData;

public class Blessing0Reward implements RewardCreator {

	private final Fluffer10kFun fluffer10kFun;

	public Blessing0Reward(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void giveRewards(final FightTempData data) {
		final PlayerFighterData player = data.fight.fighters.get("PLAYER").player();
		fluffer10kFun.serverUserDataUtils.getUserData(player).bless();

		final User user = fluffer10kFun.apiUtils.getUser(player.userId);
		final EmbedBuilder loveEmbed = fluffer10kFun.commandMgLove.makeLovedByEmbed(data.channel.getServer(), user,
				getRandomInt(100, 200), "mighty manticore");

		final String description = String.join("\n", //
				description("The manticore uses her tail to drain you, and stings you multiple times in the process."), //
				description(
						"You can barely feel anything but her soft flesh taking away your strength as she mercilessly milks you."), //
				description("Eventually, it stops, with you at the edge of death."), //
				dialogue("Mmmmmmmmm~~ That was nice~"), //
				description("She kisses you on the cheek."), //
				dialogue(
						"You did well surviving this, hehehe~ Maybe when we meet next time you will survive a bit longer~ Goodbye for now though."), //
				description("She gets up, but before leaving she says one more thing."), //
				dialogue("My name is Lilian~"), //
				description(
						"She leaves, her tail dripping with both your and her juices. She seemed happy before your vision turned to black."), //
				description("You woke up much later, in your home, surrounded by your family."), //
				description(
						"Still weak though, it took weeks to recover to the point where you could think of taking on any enemy."), //
				description("You swore to find her and pay her back in the earnest."), //
				description(
						"For your heroic behavior, you were blessed by the goddess, which will surely help you on your way."));

		data.channel.sendMessage(loveEmbed, makeEmbed("You lost the fight with powerful enemy", description));
	}

}
