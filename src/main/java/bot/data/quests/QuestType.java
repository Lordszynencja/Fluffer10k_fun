package bot.data.quests;

public enum QuestType {
	A_LITTLE_CHEMISTRY_QUEST("A little chemistry"), //
	A_NUTTY_SITUATION_QUEST("A nutty situation"), //
	BUSINESS_AS_USUAL_1_QUEST("Business as usual"), //
	BUSINESS_AS_USUAL_2_QUEST("Business as usual 2"), //
	BUSINESS_AS_USUAL_3_QUEST("Business as usual 3"), //
	CHROME_BOOK_QUEST("Chrome's book"), //
	FLUFF_LOVER_QUEST("Fluff lover"), //
	HERO_ACADEMY_QUEST("Hero academy"), //
	INSOMNIA_QUEST("Insomnia"), //
	JAGGED_JEWELLER_QUEST("Jagged jeweller"), //
	LITTLE_HEROES_QUEST("Little heroes"), //
	MAGE_WITH_ALZHEIMER_QUEST("Mage with alzheimer"), //
	MINERS_HOME("Miner's home"), //
	SLEEPY_MOUSE_TEDDY_BEAR_QUEST("Sleepy mouse's teddy bear"), //
	SPHINX_QUESTIONS_QUEST("Sphinx's questions"), //
	ULTIMATE_TEST("Ultimate test");

	public final String name;

	private QuestType(final String name) {
		this.name = name;
	}
}
