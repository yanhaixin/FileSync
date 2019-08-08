package com.sac.file;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.sac.common.Log;

public class XMLReader {
	private Document document = null;

	public XMLReader(String fileName) {
		try {
			document = parse(fileName);
		} catch (DocumentException e) {
			Log.logger.error(fileName, e);
		}
	}

	public Map<String, String> parseXML(String nodeName, String nodeAttributeName) {
		Map<String, String> rvalue = new HashMap<String, String>();
		List<Node> list = document.selectNodes(nodeName);
		for (Node node : list) {
			String name = node.valueOf(nodeAttributeName);
			String value = node.getText();
			rvalue.put(name, value);
		}
		return rvalue;
	}

	public static Document parse(String fileName) throws DocumentException {
		File f = new File(fileName);
		SAXReader reader = new SAXReader();
		return reader.read(f);
	}
}
