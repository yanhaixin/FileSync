package com.sac.file;

import java.nio.charset.Charset;

import com.sac.common.Log;

public class GlobalProperties {
	private java.util.Properties config = new java.util.Properties();
	private static GlobalProperties ins = new GlobalProperties();

	public static java.util.Properties getProperties() {
		return ins.config;
	}

	private GlobalProperties() {
		String path = null;
		try {
			path = java.net.URLDecoder.decode(getClass().getClassLoader().getResource("properties.xml").getPath(),
					Charset.defaultCharset().name());
		} catch (Exception e) {
			Log.logger.error("", e);
		}
		XMLReader reader = new XMLReader(path);
		config.putAll(reader.parseXML("//root/property", "@name"));
	}
}
