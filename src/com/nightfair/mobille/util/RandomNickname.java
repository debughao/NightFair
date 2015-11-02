package com.nightfair.mobille.util;

import java.util.Random;

public class RandomNickname {

	private final static int delta = 0x9fa5 - 0x4e00 + 1;

	public static char getRandomHan() {
		return (char) (0x4e00 + new Random().nextInt(delta));
	}

	public static String getRandomNick() {
		return "用户" + 2 * getRandomHan();

	}
}
