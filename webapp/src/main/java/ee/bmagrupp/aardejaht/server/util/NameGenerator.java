package ee.bmagrupp.aardejaht.server.util;

import java.util.Random;

public class NameGenerator {
	
	private static char[] chars = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','1','2','3','4','5','6','7','8','9','0'};
	
	/**
	 * @author Sander
	 * @param n - the length of the random string to be generated
	 * @return The string that has been randomly generated
	 */
	
	public static String generate(int n){
		String gen = "";
		Random x = new Random();
		for(int i = 0; i < n; i++){
			gen += chars[x.nextInt(chars.length)];
		}
		return gen;
	}
}
