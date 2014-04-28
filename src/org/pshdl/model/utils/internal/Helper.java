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
package org.pshdl.model.utils.internal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Charsets;

public class Helper {
	public static byte[] processFile(Class<?> clazz, String string, Map<String, String> options) throws IOException {
		final InputStream stream = clazz.getResourceAsStream(string);
		final StringBuilder sb = new StringBuilder();
		sb.append("(");
		boolean first = true;
		for (final String key : options.keySet()) {
			if (!first) {
				sb.append('|');
			}
			sb.append(Pattern.quote(key));
			first = false;
		}
		sb.append(")");
		final Pattern p = Pattern.compile(sb.toString());
		try (final BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
			String line = null;
			final StringBuilder res = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				final Matcher matcher = p.matcher(line);
				int offset = 0;
				while (matcher.find()) {
					res.append(line.substring(offset, matcher.start()));
					final String group = matcher.group(1);
					final String replacement = options.get(group);
					res.append(replacement);
					offset = matcher.end();
				}
				res.append(line.substring(offset));
				res.append("\n");
			}
			return res.toString().getBytes(Charsets.UTF_8);
		}
	}
}
