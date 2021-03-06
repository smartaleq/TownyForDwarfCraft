package com.shade.bukkit.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatTools {
	public static final int lineLength = 54;

	public static List<String> list(Object[] args) {
		return list(Arrays.asList(args));
	}

	public static List<String> list(List<Object> args) {
		if (args.size() > 0) {
			String line = "";
			for (int i = 0; i < args.size() - 1; i++)
				line += args.get(i) + ", ";
			line += args.get(args.size() - 1).toString();

			return color(line);
		}

		return new ArrayList<String>();
	}

	public static List<String> wordWrap(String[] tokens) {
		List<String> out = new ArrayList<String>();
		out.add("");

		for (String s : tokens) {
			if (stripColour(out.get(out.size() - 1)).length()
					+ stripColour(s).length() + 1 > lineLength) {
				out.add("");
			}
			out.set(out.size() - 1, out.get(out.size() - 1) + s + " ");
		}

		return out;
	}

	public static List<String> color(String line) {
		List<String> out = wordWrap(line.split(" "));

		String c = "f";
		for (int i = 0; i < out.size(); i++) {
			if (!out.get(i).startsWith("�"))
				out.set(i, "�" + c + out.get(i));

			for (int index = 0; index < lineLength; index++) {
				try {
					if (out.get(i).substring(index, index + 1)
							.equalsIgnoreCase("\u00A7")) {
						c = out.get(i).substring(index + 1, index + 2);
					}
				} catch (Exception e) {
				}
			}
		}

		return out;
	}

	public static String stripColour(String s) {
		String out = "";
		for (int i = 0; i < s.length() - 1; i++) {
			String c = s.substring(i, i + 1);
			if (c.equals("�")) {
				i += 1;
			} else {
				out += c;
			}
		}
		return out;
	}

	public static String formatTitle(String title) {
		String line = ".oOo.__________________________________________________.oOo.";
		int pivot = line.length() / 2;
		String center = ".[ " + Colors.Yellow + title + Colors.Gold + " ].";
		String out = Colors.Gold
				+ line.substring(0, pivot - (center.length() / 2));
		out += center + line.substring(pivot + (center.length() / 2));
		return out;
	}

	public static String formatCommand(String requirement, String command,
			String subCommand, String help) {
		String out = "  ";
		if (requirement.length() > 0)
			out += Colors.Rose + requirement + ": ";
		out += Colors.Blue + command + " ";
		if (subCommand.length() > 0)
			out += Colors.LightBlue + subCommand + " ";
		if (help.length() > 0)
			out += Colors.LightGray + " : " + help;
		return out;
	}

	public static void main(String[] args) {
		String[] players = { "dude", "bowie", "blarg", "sonbitch", "songoku",
				"pacman", "link", "stacker", "hacker", "newb" };
		for (String line : ChatTools.list(players))
			System.out.println(line);

		String testLine = "Loren Ipsum blarg voila tssssssh, boom wakka wakka �apacman on a boat bitch. From the boat union. Beata lingiushtically �1nootchie lolk erness.";
		for (String line : ChatTools.color(testLine))
			System.out.println(line);
	}
}
