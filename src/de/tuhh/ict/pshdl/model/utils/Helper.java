package de.tuhh.ict.pshdl.model.utils;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import com.google.common.base.*;

public class Helper {
	public static byte[] processFile(Class<?> clazz, String string, Map<String, String> options) throws IOException {
		InputStream stream = clazz.getResourceAsStream(string);
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		boolean first = true;
		for (String key : options.keySet()) {
			if (!first)
				sb.append('|');
			sb.append(Pattern.quote(key));
			first = false;
		}
		sb.append(")");
		Pattern p = Pattern.compile(sb.toString());
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		String line = null;
		StringBuilder res = new StringBuilder();
		while ((line = reader.readLine()) != null) {
			Matcher matcher = p.matcher(line);
			int offset = 0;
			while (matcher.find()) {
				res.append(line.substring(offset, matcher.start()));
				String group = matcher.group(1);
				String replacement = options.get(group);
				res.append(replacement);
				offset = matcher.end();
			}
			res.append(line.substring(offset));
			res.append("\n");
		}
		stream.close();
		return res.toString().getBytes(Charsets.UTF_8);
	}
}
