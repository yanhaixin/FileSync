package com.sac.file;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.sac.common.Log;

public class Proprieties {
	private Map<String, String> proprieties_map;
	private static Proprieties ins = new Proprieties();

	public static Proprieties getInstance() {
		return ins;
	}

	private Proprieties() {
		String path = null;
		try {
			path = java.net.URLDecoder.decode(getClass().getClassLoader().getResource("properties.xml").getPath(),
					Charset.defaultCharset().name());
		} catch (Exception e) {
			Log.logger.error("", e);
		}
		XMLReader reader = new XMLReader(path);
		proprieties_map = reader.parseXML("//root/property", "@name");
	}

	public String getValue(String name) {
		return proprieties_map.get(name);
	}

	public Set<Entry<String, String>> getAll() {
		return proprieties_map.entrySet();
	}
}
