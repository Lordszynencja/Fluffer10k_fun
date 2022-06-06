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

public class Blessing5Reward implements RewardCreator {

	private final Fluffer10kFun fluffer10kFun;

	public Blessing5Reward(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void giveRewards(final FightTempData data) {
		final PlayerFighterData player = data.fight.fighters.get("PLAYER").player();
		fluffer10kFun.serverUserDataUtils.getUserData(player).bless();

		final User user = fluffer10kFun.apiUtils.getUser(player.userId);
		final EmbedBuilder loveEmbed = fluffer10kFun.commandMgLove.makeLovedByEmbed(data.channel.getServer(), user,
				getRandomInt(50, 100), "Helga");

		final String description = String.join("\n", //
				description("Once again the monster officer wins, this time visibly strained."), //
				dialogue("I still got you! haa... You will now pay me... haa... for exhausting me so much!"), //
				description("She once again rides you until you pass out."), //
				description(
						"The next thing you saw was your bedroom, and flowers on the cabinet next to it, with a note saying \"for our brave husband!\"."), //
				dialogue("You feel that next time you will defeat Helga and get a step closer to peace."), //
				description("You get blessed by the goddess to help with that."));

		data.channel.sendMessage(loveEmbed, makeEmbed("You lost the fight with Helga, again", description));
	}

}
