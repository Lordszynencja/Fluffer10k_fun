package bot.data.items.data;

public class DefaultPrices {
	public static final long trashCraftingMaterialPrice = 1;
	public static final long commonCraftingMaterialPrice = 100;
	public static final long uncommonCraftingMaterialPrice = 500;
	public static final long rareCraftingMaterialPrice = 2_500;

	public static final long trashWeaponPrice = trashCraftingMaterialPrice * 20;
	public static final long commonWeaponPrice = commonCraftingMaterialPrice * 20;
	public static final long uncommonWeaponPrice = uncommonCraftingMaterialPrice * 20;
	public static final long rareWeaponPrice = rareCraftingMaterialPrice * 20;

	public static final long commonArmorPrice = commonWeaponPrice * 5 / 2;
	public static final long uncommonArmorPrice = uncommonWeaponPrice * 5 / 2;
	public static final long rareArmorPrice = rareWeaponPrice * 5 / 2;

	public static final long commonMagicScrollPrice = commonWeaponPrice;
	public static final long uncommonMagicScrollPrice = uncommonWeaponPrice;
	public static final long rareMagicScrollPrice = rareWeaponPrice;

	public static final long commonMonmusuDropPrice = commonCraftingMaterialPrice / 2;
	public static final long uncommonMonmusuDropPrice = uncommonCraftingMaterialPrice / 2;
	public static final long rareMonmusuDropPrice = rareCraftingMaterialPrice / 2;

	public static final long commonMonmusuDropMGPrice = commonMonmusuDropPrice / 2;
	public static final long uncommonMonmusuDropMGPrice = uncommonMonmusuDropPrice / 2;
	public static final long rareMonmusuDropMGPrice = rareMonmusuDropPrice / 2;

	public static final long commonPotionPrice = commonCraftingMaterialPrice * 8;
	public static final long uncommonPotionPrice = uncommonCraftingMaterialPrice * 8;
	public static final long rarePotionPrice = rareCraftingMaterialPrice * 8;

	public static final long commonPotionMGPrice = commonPotionPrice / 2;
	public static final long uncommonPotionMGPrice = uncommonPotionPrice / 2;
	public static final long rarePotionMGPrice = rarePotionPrice / 2;

	public static final long trashDecorationPrice = trashCraftingMaterialPrice * 10;
	public static final long commonDecorationPrice = commonCraftingMaterialPrice * 10;
	public static final long uncommonDecorationPrice = uncommonCraftingMaterialPrice * 10;

	public static final long uncommonRingPrice = uncommonWeaponPrice * 5 / 2;
}
