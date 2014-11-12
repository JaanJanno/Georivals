package ee.bmagrupp.georivals.mobile.models.province;

/**
 * Every {@link Province} falls under one of the following categories.
 * 
 * @author TKasekamp
 * 
 */
public enum ProvinceType {
	/**
	 * {@link Province} owned by nobody.
	 */
	BOT,
	/**
	 * {@link Province} owned by the {@link Player} making the request.
	 */
	PLAYER,
	/**
	 * {@link Province} owned by any other {@link Player}, but not the one
	 * making the request.
	 */
	OTHER_PLAYER,
	/**
	 * The home {@link Province} of the {@link Player} making the request.
	 */
	HOME
}