package com.hxzm.kit;

import org.apache.log4j.Logger;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

public class IdEncrypterForjava {
	private static final Logger logger = Logger.getLogger(IdEncrypterForjava.class);
	private static final byte[] idCrypterSaltBytes = "H0w_many_r0ad_a_man_must_been_wa1k_d0wn, before_they_ca11_him_a_man.".getBytes(Charset.forName("ISO-8859-1"));
	private static Blowfish blowfish;

	static {
		blowfish = new Blowfish(Arrays.copyOf(idCrypterSaltBytes, 32), Arrays.copyOfRange(idCrypterSaltBytes, 32, 40));
	}

	public static String encodeVid(int id) {
		try {
			byte[] sourceBytes = new byte[8];
			if (id < 100000000) {
				int2bytes(sourceBytes, id);
			} else {
				sourceBytes[0] = (byte) ((id >> 24) & 0xFF);
				sourceBytes[1] = (byte) ((id >> 16) & 0xFF);
				sourceBytes[2] = (byte) ((id >> 8) & 0xFF);
				sourceBytes[3] = (byte) ((id) & 0xFF);
				sourceBytes[4] = Byte.MAX_VALUE;
				sourceBytes[5] = Byte.MAX_VALUE;
				sourceBytes[6] = Byte.MAX_VALUE;
				sourceBytes[7] = Byte.MAX_VALUE;
			}
			byte[] encryptedBytes = blowfish.encrypt(sourceBytes);

			char[] base64Chars = Base64.encode(encryptedBytes);
			int i;
			for (i = 0; i < base64Chars.length; i++) {
				if (base64Chars[i] == '=') {
					break; // = only appear at end, so i is the ending
				} else if (base64Chars[i] == '+') {
					base64Chars[i] = '-';
				} else if (base64Chars[i] == '/') {
					base64Chars[i] = '_';
				}
			}
			return new String(base64Chars, 0, i);
		} catch (Exception e) {
			logger.error("encode error, id=" + id, e);
		}
		return null;
	}

	public static int decodeVid(String code) {
		try {
			StringBuilder sb = new StringBuilder(code);
			int i = code.length();
			while (i % 4 != 0) {
				sb.append('=');
				i++;
			}
			for (i = 0; i < sb.length(); i++) {
				if (sb.charAt(i) == '-') {
					sb.setCharAt(i, '+');
				} else if (sb.charAt(i) == '_') {
					sb.setCharAt(i, '/');
				}
			}
			byte[] sourceBytes = Base64.decode(sb.toString());
			byte[] decryptedBytes = blowfish.decrypt(sourceBytes);

			if (decryptedBytes.length == 8 && decryptedBytes[7] == Byte.MAX_VALUE) {
				ByteBuffer bf = ByteBuffer.wrap(decryptedBytes);
				return bf.getInt();
			} else {
				for (i = 0; i < decryptedBytes.length; i++) {
					if (decryptedBytes[i] == 0) {
						break;
					}
				}
				return bytes2int(decryptedBytes, 0, i);
			}
		} catch (Exception e) {
			logger.error("decode error, code=" + code, e);
		}
		return 0;
	}

	public static long decodeJuid(String encodeUid) throws NumberFormatException {
		if ((encodeUid != null) && (encodeUid.length() > 10) && (encodeUid.charAt(0) == '0')) {
			long time = Long.parseLong(encodeUid.substring(1, 10), 32);
			long rand = Long.parseLong(encodeUid.substring(10), 32);
			return (time * 100000L + rand);
		}
		return Long.parseLong(encodeUid, 32);
	}

	private static void int2bytes(byte[] bs, int v) {
		int l = 1;
		for (int vv = v; vv >= 10; l++) {
			vv /= 10;
		}
		for (int i = 1; i <= l; i++) { // down to up
			bs[l - i] = (byte) (v - v / 10 * 10 + 48);
			v = v / 10;
		}
	}

	private static int bytes2int(byte[] bs, int start, int l) {
		int r = 0;
		for (int i = 0; i < l; i++) {
			r += (int) (Math.pow(10, l - i - 1) * (bs[start + i] - 48));
			// 48 =
			// 0,
			// 49
			// =
			// 1
		}
		return r;
	}

	public static void main(String[] args) {
//		for (int i = 0; i < 1000000; i++) {
////			if (!IdEncrypterForjava.encodeVid(i).equals(IdEncrypterOld.encrypt(i))) {
////				System.out.println(i);
////			}
//		}
		System.out.println(encodeVid(1));

	}
}