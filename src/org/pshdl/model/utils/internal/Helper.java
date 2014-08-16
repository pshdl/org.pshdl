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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
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

	public static void deleteDirectory(File directory) throws IOException {
		// See
		// http://stackoverflow.com/questions/8666420/how-to-recursively-delete-a-folder-without-following-symlinks
		// Copied from
		// http://grepcode.com/file/repo1.maven.org/maven2/commons-io/commons-io/2.1/org/apache/commons/io/FileUtils.java#FileUtils.deleteDirectory%28java.io.File%29
		if (!directory.exists())
			return;

		if (!Files.isSymbolicLink(directory.toPath())) {
			cleanDirectory(directory);
		}

		if (!directory.delete()) {
			final String message = "Unable to delete directory " + directory + ".";
			throw new IOException(message);
		}
	}

	private static void cleanDirectory(File directory) throws IOException {
		// Copied from
		// http://grepcode.com/file/repo1.maven.org/maven2/commons-io/commons-io/2.1/org/apache/commons/io/FileUtils.java#FileUtils.cleanDirectory%28java.io.File%29
		if (!directory.exists()) {
			final String message = directory + " does not exist";
			throw new IllegalArgumentException(message);
		}

		if (!directory.isDirectory()) {
			final String message = directory + " is not a directory";
			throw new IllegalArgumentException(message);
		}

		final File[] files = directory.listFiles();
		if (files == null)
			throw new IOException("Failed to list contents of " + directory);

		IOException exception = null;
		for (final File file : files) {
			try {
				forceDelete(file);
			} catch (final IOException ioe) {
				exception = ioe;
			}
		}

		if (exception != null)
			throw exception;
	}

	private static void forceDelete(File file) throws IOException {
		if (file.isDirectory()) {
			deleteDirectory(file);
		} else {
			final boolean filePresent = file.exists();
			if (!file.delete()) {
				if (!filePresent)
					throw new FileNotFoundException("File does not exist: " + file);
				final String message = "Unable to delete file: " + file;
				throw new IOException(message);
			}
		}
	}
}
