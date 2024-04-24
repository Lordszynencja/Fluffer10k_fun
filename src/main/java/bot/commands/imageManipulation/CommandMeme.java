package bot.commands.imageManipulation;

import static bot.util.CollectionUtils.addToListOnMap;
import static bot.util.CollectionUtils.mapToList;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.Utils.joinNames;
import static bot.util.apis.MessageUtils.getUserMentionIds;
import static bot.util.apis.MessageUtils.isNSFWChannel;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;
import static java.util.Arrays.asList;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;

import bot.Fluffer10kFun;
import bot.util.RandomUtils;
import bot.util.apis.Config;
import bot.util.subcommand.Command;

public class CommandMeme extends Command {
	private static void putSquare(final SingleImgInsert insert, final BufferedImage from, final BufferedImage to) {
		final int size = 2 * insert.r;
		final int x0 = insert.cpx - insert.r;
		final int y0 = insert.cpy - insert.r;

		final double scale = 1.0 * size / from.getHeight();
		final AffineTransform transform = AffineTransform.getTranslateInstance(x0, y0);
		transform.concatenate(AffineTransform.getScaleInstance(scale, scale));
		if (insert.rotation != 0) {
			transform.concatenate(AffineTransform.getRotateInstance(insert.rotation, insert.r, insert.r));
		}
		final BufferedImageOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BICUBIC);

		final Graphics2D g = to.createGraphics();
		g.drawImage(from, op, 0, 0);
	}

	private static void putCircle(final SingleImgInsert insert, final BufferedImage from, final BufferedImage to) {
		final int size = 2 * insert.r;
		final int x0 = insert.cpx - insert.r;
		final int y0 = insert.cpy - insert.r;

		final double scale = 1.0 * size / from.getHeight();
		final AffineTransform transform = AffineTransform.getTranslateInstance(x0, y0);
		transform.concatenate(AffineTransform.getScaleInstance(scale, scale));
		if (insert.rotation != 0) {
			transform.concatenate(AffineTransform.getRotateInstance(insert.rotation, insert.r, insert.r));
		}
		final BufferedImageOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BICUBIC);

		final Graphics2D g = to.createGraphics();
		g.setClip(new Ellipse2D.Double(x0, y0, size, size));
		g.drawImage(from, op, 0, 0);
	}

	private static enum InsertType {
		CIRCLE(CommandMeme::putCircle), //
		CIRCLE_ROTATION(CommandMeme::putCircle), //
		SQUARE(CommandMeme::putSquare), //
		SQUARE_ROTATION(CommandMeme::putSquare);

		private static interface InsertFunction {
			void apply(SingleImgInsert insert, BufferedImage from, BufferedImage to);
		}

		public final InsertFunction f;

		private InsertType(final InsertFunction f) {
			this.f = f;
		}
	}

	private static interface ImgInsert {
		void apply(final BufferedImage from, final BufferedImage to);
	}

	private static class SingleImgInsert implements ImgInsert {
		public final int cpx;
		public final int cpy;
		public final int r;
		public final double rotation;
		public final InsertType type;

		public SingleImgInsert(final int cpx, final int cpy, final int r, final InsertType type) {
			this.cpx = cpx;
			this.cpy = cpy;
			this.r = r;
			rotation = 0;
			this.type = type;
		}

		public SingleImgInsert(final int cpx, final int cpy, final int r, final double rotation,
				final InsertType type) {
			this.cpx = cpx;
			this.cpy = cpy;
			this.r = r;
			this.rotation = rotation;
			this.type = type;
		}

		@Override
		public void apply(final BufferedImage from, final BufferedImage to) {
			type.f.apply(this, from, to);
		}
	}

	private static SingleImgInsert single(final int cpx, final int cpy, final int r, final double rotation,
			final InsertType type) {
		return new SingleImgInsert(cpx, cpy, r, rotation, type);
	}

	private static SingleImgInsert single(final int cpx, final int cpy, final int r, final InsertType type) {
		return new SingleImgInsert(cpx, cpy, r, type);
	}

	private static class ImgMultiInsert implements ImgInsert {
		private final ImgInsert[] inserts;

		public ImgMultiInsert(final ImgInsert... inserts) {
			this.inserts = inserts;
		}

		@Override
		public void apply(final BufferedImage from, final BufferedImage to) {
			for (final ImgInsert insert : inserts) {
				insert.apply(from, to);
			}
		}
	}

	private static ImgMultiInsert multi(final ImgInsert... inserts) {
		return new ImgMultiInsert(inserts);
	}

	private static class MemeData {
		private final String memeFileName;
		public final boolean nsfw;
		private final ImgInsert[] inserts;

		public MemeData(final String memeFileName, final boolean nsfw, final ImgInsert... inserts) {
			this.memeFileName = memeFileName;
			this.inserts = inserts;
			this.nsfw = nsfw;
		}

		public BufferedImage use(final List<BufferedImage> images, final Config config) throws IOException {
			final String path = config.getString("imageFolderPath") + "imageBackgrounds/" + memeFileName;
			final BufferedImage memeImage = ImageIO.read(new File(path));
			final BufferedImage memeImage2 = new BufferedImage(memeImage.getWidth(), memeImage.getHeight(),
					BufferedImage.TYPE_INT_ARGB);

			memeImage2.createGraphics().drawImage(memeImage, 0, 0, memeImage.getWidth(), memeImage.getHeight(), 0, 0,
					memeImage.getWidth(), memeImage.getHeight(), null);

			for (int i = 0; i < inserts.length; i++) {
				if (i >= images.size()) {
					return memeImage2;
				}
				final BufferedImage img = images.get(i);
				inserts[i].apply(img, memeImage2);
			}

			return memeImage2;
		}

		public int getTargetsNumber() {
			return inserts.length;
		}
	}

	private static final Map<String, MemeData> memes = new HashMap<>();
	private static final Map<Integer, List<String>> SFWMemeNamesByAmount = new HashMap<>();
	private static final Map<Integer, List<String>> NSFWMemeNamesByAmount = new HashMap<>();

	static {
		memes.put("awoo", new MemeData("awoo.jpeg", false, single(550, 300, 140, InsertType.CIRCLE)));
		memes.put("dont_mind_being_hentai",
				new MemeData("dont_mind_being_hentai.jpg", true, single(465, 125, 160, InsertType.CIRCLE)));
		memes.put("father", new MemeData("father.jpg", false, single(610, 635, 55, InsertType.CIRCLE)));
		memes.put("fluff", new MemeData("fluff.jpg", false, single(510, 680, 50, InsertType.CIRCLE),
				single(750, 350, 80, InsertType.CIRCLE)));
		memes.put("from_poland", new MemeData("from_poland.png", false, single(180, 190, 100, InsertType.CIRCLE)));
		memes.put("have_sex_with_me",
				new MemeData("have_sex_with_me.jpg", true, single(160, 280, 150, InsertType.CIRCLE)));
		memes.put("hentai_season", new MemeData("hentai_season.webp", true, single(210, 85, 57, InsertType.SQUARE)));
		memes.put("hora_strength", new MemeData("hora_strength.jpg", true, single(270, 230, 70, InsertType.CIRCLE),
				single(100, 200, 100, InsertType.CIRCLE)));
		memes.put("horny_license",
				new MemeData("horny_license.png", false, single(200, 345, 100, -0.4, InsertType.SQUARE_ROTATION)));
		memes.put("i_believe_in_panties", new MemeData("i_believe_in_panties.jpg", false,
				single(160, 160, 130, InsertType.CIRCLE), single(600, 100, 130, InsertType.CIRCLE)));
		memes.put("i_love_my_boobs",
				new MemeData("i_love_my_boobs.jpg", true, single(410, 140, 150, InsertType.CIRCLE)));
		memes.put("in_the_butt", new MemeData("in_the_butt.jpg", true, single(180, 120, 50, InsertType.CIRCLE),
				single(65, 140, 50, InsertType.CIRCLE)));
		memes.put("innocent_boi", new MemeData("innocent_boi.png", false,
				multi(single(220, 125, 40, InsertType.CIRCLE), single(230, 390, 70, InsertType.CIRCLE))));
		memes.put("its_something",
				new MemeData("its_something.jpg", false, single(190, 72, 34, -0.1, InsertType.CIRCLE_ROTATION)));
		memes.put("jail_is_over_here",
				new MemeData("jail_is_over_here.jpg", false, single(130, 140, 70, InsertType.CIRCLE)));
		memes.put("just_reading", new MemeData("just_reading.jpg", false, single(270, 180, 120, InsertType.CIRCLE)));
		memes.put("kiss_100_girls",
				new MemeData("kiss_100_girls.webp", false, single(180, 470, 70, InsertType.CIRCLE)));
		memes.put("laid_dragon", new MemeData("laid_dragon.jpg", false, single(290, 290, 30, InsertType.CIRCLE),
				single(150, 160, 60, InsertType.CIRCLE), single(160, 360, 30, InsertType.CIRCLE)));
		memes.put("lewd_approval", new MemeData("lewd_approval.jpg", false, single(220, 160, 100, InsertType.CIRCLE)));
		memes.put("lewd_thoughts", new MemeData("lewd_thoughts.png", false, single(340, 170, 170, InsertType.CIRCLE),
				single(410, 550, 160, InsertType.CIRCLE)));
		memes.put("lolis_bad", new MemeData("lolis_bad.png", true, single(110, 150, 40, InsertType.CIRCLE)));
		memes.put("love_and_affection", new MemeData("love_and_affection.jpg", false,
				single(210, 220, 100, InsertType.CIRCLE), single(490, 420, 60, InsertType.CIRCLE)));
		memes.put("meth", new MemeData("meth.jpg", true, single(520, 520, 140, InsertType.CIRCLE)));
		memes.put("mighty_dong", new MemeData("mighty_dong.jpg", true, single(170, 320, 130, InsertType.CIRCLE)));
		memes.put("monday", new MemeData("monday.webp", false, multi(single(220, 125, 100, InsertType.CIRCLE),
				single(320, 350, 100, InsertType.CIRCLE), single(190, 620, 90, InsertType.CIRCLE))));
		memes.put("no_friends", new MemeData("no_friends.jpg", false, multi(single(395, 165, 90, InsertType.CIRCLE),
				single(370, 610, 130, InsertType.CIRCLE), single(310, 1030, 140, InsertType.CIRCLE))));
		memes.put("no_fucks_given", new MemeData("no_fucks_given.png", false, single(145, 150, 55, InsertType.CIRCLE)));
		memes.put("no_sex", new MemeData("no_sex.jpg", true, single(470, 300, 150, InsertType.CIRCLE)));
		memes.put("not_this_shit_again",
				new MemeData("not_this_shit_again.jpg", false, single(370, 210, 100, InsertType.CIRCLE)));
		memes.put("objection", new MemeData("objection.jpg", false, single(205, 230, 50, InsertType.CIRCLE)));
		memes.put("ooga_booga", new MemeData("ooga_booga.jpg", false, single(390, 130, 40, InsertType.CIRCLE)));
		memes.put("out_of_memes", new MemeData("out_of_memes.jpg", false,
				multi(single(435, 180, 40, InsertType.CIRCLE), single(180, 230, 140, InsertType.CIRCLE))));
		memes.put("pamperin", new MemeData("pamperin.jpg", false, single(420, 260, 90, InsertType.CIRCLE)));
		memes.put("relentless_love", new MemeData("relentless_love.png", false, single(350, 80, 30, InsertType.CIRCLE),
				single(110, 370, 80, InsertType.CIRCLE)));
		memes.put("retard", new MemeData("retard.jpg", false, single(100, 93, 53, InsertType.SQUARE)));
		memes.put("rubber", new MemeData("rubber.jpg", true, single(540, 180, 100, InsertType.CIRCLE),
				single(100, 230, 60, InsertType.CIRCLE)));
		memes.put("screams_externally",
				new MemeData("screams_externally.jpg", false, single(115, 130, 50, InsertType.CIRCLE)));
		memes.put("smash", new MemeData("smash.jpg", true, single(590, 350, 120, InsertType.CIRCLE),
				single(350, 330, 120, InsertType.CIRCLE), single(860, 320, 120, InsertType.CIRCLE)));
		memes.put("spiral_into_sadness",
				new MemeData("spiral_into_sadness.png", false, single(90, 120, 80, InsertType.CIRCLE)));
		memes.put("suck_my_futa_cock",
				new MemeData("suck_my_futa_cock.jpg", true, single(510, 415, 220, -0.2, InsertType.CIRCLE_ROTATION)));
		memes.put("this_could_be_us", new MemeData("this_could_be_us.jpg", false,
				single(140, 140, 60, InsertType.CIRCLE), single(540, 140, 50, InsertType.CIRCLE)));
		memes.put("tofu", new MemeData("tofu.jpg", false, single(510, 660, 40, InsertType.CIRCLE),
				single(700, 270, 80, InsertType.CIRCLE)));
		memes.put("totally_feel_that",
				new MemeData("totally_feel_that.jpg", false, single(370, 140, 150, InsertType.CIRCLE)));
		memes.put("where_banana", new MemeData("where_banana.png", false, single(850, 200, 80, InsertType.CIRCLE),
				single(180, 200, 80, InsertType.CIRCLE), single(450, 180, 90, InsertType.CIRCLE)));
		memes.put("wont_do_it_today",
				new MemeData("wont_do_it_today.jpg", false, single(300, 45, 55, InsertType.CIRCLE)));
		memes.put("wtf", new MemeData("wtf.jpg", false, single(200, 100, 70, InsertType.CIRCLE)));

		memes.forEach((final String name, final MemeData data) -> {
			final Integer targetsNumber = data.getTargetsNumber();
			if (!data.nsfw) {
				addToListOnMap(SFWMemeNamesByAmount, targetsNumber, name);
			}

			addToListOnMap(NSFWMemeNamesByAmount, targetsNumber, name);
		});
	}

	private final Fluffer10kFun fluffer10kFun;

	public CommandMeme(final Fluffer10kFun fluffer10kFun) {
		super(fluffer10kFun.apiUtils, "meme", "Creates meme", //
				SlashCommandOption.create(SlashCommandOptionType.STRING, "template", "Template name to use"), //
				SlashCommandOption.create(SlashCommandOptionType.STRING, "targets", "Targets"));

		this.fluffer10kFun = fluffer10kFun;
	}

	private static String getRandomTemplate(final boolean nsfw, final int targetsNumber) {
		final List<String> memeList = (nsfw ? NSFWMemeNamesByAmount : SFWMemeNamesByAmount).get(targetsNumber);
		return memeList.isEmpty() ? null : RandomUtils.getRandom(memeList);
	}

	List<User> getTargets(final SlashCommandInteraction interaction) {
		final String targetsParam = interaction.getArgumentStringValueByName("targets").orElse(null);

		if (targetsParam != null) {
			final List<Long> userMentionIds = getUserMentionIds(targetsParam);
			if (!userMentionIds.isEmpty()) {
				return mapToList(userMentionIds, id -> fluffer10kFun.apiUtils.api.getUserById(id).join());
			}
		}

		return asList(interaction.getUser());
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) throws Exception {
		String template = interaction.getArgumentStringValueByName("template").orElse(null);

		final List<User> targets = getTargets(interaction);

		MemeData meme = memes.get(template);
		if (meme != null && meme.nsfw && !isNSFWChannel(interaction)) {
			sendEphemeralMessage(interaction, "Can't use this template here");
			return;
		}
		if (meme == null) {
			template = getRandomTemplate(isNSFWChannel(interaction), targets.size());
			meme = memes.get(template);
			if (meme == null) {
				sendEphemeralMessage(interaction, "Couldn't find a meme for that many targets");
				return;
			}
		}

		final List<BufferedImage> images = mapToList(targets, user -> user.getAvatar().asBufferedImage().join());
		final BufferedImage result = meme.use(images, fluffer10kFun.apiUtils.config);
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		ImageIO.write(result, "png", out);

		final EmbedBuilder embed = makeEmbed("Meme delivered")//
				.setDescription("People on meme are " + joinNames(mapToList(targets, u -> u.getMentionTag())))//
				.setFooter(template)//
				.setImage(out.toByteArray());

		interaction.createImmediateResponder()//
				.addEmbed(embed)//
				.respond()//
				.join()//
				.update();
	}
}
