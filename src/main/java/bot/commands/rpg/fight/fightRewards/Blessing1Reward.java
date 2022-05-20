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

public class Blessing1Reward implements RewardCreator {

	private final Fluffer10kFun fluffer10kFun;

	public Blessing1Reward(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void giveRewards(final FightTempData data) {
		final PlayerFighterData player = data.fight.fighters.get("PLAYER").player();
		fluffer10kFun.serverUserDataUtils.getUserData(player).bless();

		final User user = fluffer10kFun.apiUtils.getUser(player.userId);
		final EmbedBuilder loveEmbed = fluffer10kFun.commandMgLove.makeLovedByEmbed(data.channel.getServer(), user,
				getRandomInt(75, 150), "Lilian");

		final String description = String.join("\n", //
				description("Lilian once again comes out victorious, panting as she squeezes your body."), //
				dialogue(
						"My, you came prepared this time~ but no use, I am too strong for a mere blessing to stop me!"), //
				description("She nuzzles you."), //
				dialogue(
						"I appreciate the thought though, next time I hope you will pose some kind of a threat so I can finally have some fun."), //
				description("She finally gets up and swipes sweat off her forehead."), //
				dialogue("Who knows, maybe you will become my husband~"), //
				description("She leaves as you lose consciousness."), //
				description("You wake up much later in your home."), //
				description(
						"You again need to recover after being drained, but you also get another blessing from the goddess."));

		data.channel.sendMessage(loveEmbed, makeEmbed("You lost the fight with Lilian", description));
	}

}
