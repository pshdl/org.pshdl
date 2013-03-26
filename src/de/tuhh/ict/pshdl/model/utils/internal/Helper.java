/*******************************************************************************
 * PSHDL is a library and (trans-)compiler for PSHDL input. It generates
 *     output suitable for implementation or simulation of it.
 *     
 *     Copyright (C) 2013 Karsten Becker (feedback (at) pshdl (dot) org)
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 *     This License does not grant permission to use the trade names, trademarks,
 *     service marks, or product names of the Licensor, except as required for 
 *     reasonable and customary use in describing the origin of the Work.
 * 
 * Contributors:
 *     Karsten Becker - initial API and implementation
 ******************************************************************************/
package de.tuhh.ict.pshdl.model.utils.internal;

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
			if (!first) {
				sb.append('|');
			}
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
