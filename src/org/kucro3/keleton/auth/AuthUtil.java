package org.kucro3.keleton.auth;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.Objects;
import java.util.UUID;

import org.kucro3.keleton.auth.MurmurHash3.LongPair;

public class AuthUtil {
	private AuthUtil()
	{
	}
	
	public static String toMD5(String message)
	{
		Objects.requireNonNull(message);
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
			byte[] byts = message.getBytes("UTF-8");
			byts = md5.digest(byts);
			StringBuilder md5msg = new StringBuilder();
			for(int i = 0; i < byts.length; i++)
			{
				int val = ((int) byts[i]) & 0xFF;
				if(val < 16)
					md5msg.append("0");
				md5msg.append(Integer.toHexString(val));
			}
			return md5msg.toString();
		} catch (Exception e) {
			throw new UnsupportedOperationException(e);
		}
	}
	
	public static UUID toUUID(String name)
	{
		LongPair pair = murmurHash128(name);
		return new UUID(pair.val1, pair.val2);
	}
	
	static LongPair murmurHash128(String str)
	{
		LongPair pair = new LongPair();
		byte[] byts = null;
		try {
			byts = str.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// unused
		}
		MurmurHash3.murmurhash3_x64_128(Objects.requireNonNull(byts, "UTF8 Unsupported"), 0, byts.length, MurmurHash3.DEFAULT_SEED, pair);
		return pair;
	}
}