package org.catacombae.hfsexplorer.iphone;

import java.util.Arrays;
import java.util.HashMap;

import org.catacombae.dmgextractor.Util;

public class Keybag {

	public final String[] KEYBAG_TAGS = {"VERS", "TYPE", "UUID", "HMCK", "WRAP", "SALT", "ITER", "PBKY"};
	private HashMap<String, byte[]> attributes;
	
	public Keybag(byte[] data)
	{
		String dataTag = Util.toASCIIString(data, 0, 4);
		assert dataTag == "DATA";
		int dataLen = Util.readIntBE(data, 4);
		assert dataLen < data.length;
		
		attributes = new HashMap<String, byte[]>();
		
		for(int i=8; i < dataLen; )
		{
			String tag = Util.toASCIIString(data, i, 4);
			int len = Util.readIntBE(data, i+4);
			if (Arrays.asList(KEYBAG_TAGS).contains(tag))
			{
				attributes.put(tag, Arrays.copyOfRange(data, i+8, i+8+len));
			}
			i += 8 + len;
		}
	}

	public byte[] getPKBY() {
		return attributes.get("PBKY");
	}
}
