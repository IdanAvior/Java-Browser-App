package il.ac.telhai.cn.browser;

/**
 * HTMLParser class - used to parse HTML format text
 * into regular text. The class contains only static methods,
 * so creating an object of this class is unnecessary.
 * @author Idan Avior
 *
 */
public class HTMLParser {

	private final static String PROTOCOL = "http://";

	public static String getParsedLine(String line){
		if (containsHTMLTag(line))
			return parseHTMLTag(line);
		if (containsHEADTag(line))
			return parseHEADTag(line);
		if (containsBODYTag(line))
			return parseBODYTag(line);
		if (containsBRTag(line))
			return parseBRTag(line);
		if (containsTABLETag(line))
			parseTABLETag(line);
		if (containsTRTag(line))
			parseTRTag(line);
		if (containsTDTag(line))
			parseTDTag(line);
		if (containsLinkTag(line))
			return "";
		return line;
	}

	public static String parseHTMLTag(String line){
		return "";
	}

	public static String parseHEADTag(String line){
		return "";
	}

	public static String parseBODYTag(String line){
		return "";
	}
	
	public static String parseBRTag(String line){
		return "";
	}
	
	public static String parseTABLETag(String line){
		return "";
	}
	
	public static String parseTRTag(String line){
		return "";
	}
	
	public static String parseTDTag(String line){
		return "";
	}

	public static String parseATag(String line){
		String temp = line;
		if (temp.contains("href") && (temp.indexOf('"') != -1)){
			temp = temp.substring(temp.indexOf("href") + 1);
			temp = temp.substring(temp.indexOf('"') + 1);
		}
		if (temp.indexOf('"') != -1)
			temp = temp.substring(0, temp.indexOf('"'));
		return temp;
	}

	public static boolean isAValidAddress(String address){
		if (!hasProtocol(address))
			return false;
		return true;
	}

	public static boolean hasProtocol(String address){
		return (address.length() >= PROTOCOL.length() &&
				address.substring(0, PROTOCOL.length()).equals(PROTOCOL));
	}

	public static boolean containsHTMLTag(String line){
		return (line.contains("<html") || line.contains("</html>")
				|| line.contains("<HTML") || line.contains("</HTML>"));
	}

	public static boolean containsHEADTag(String line){
		return (line.contains("<head") || line.contains("</head>")
				|| line.contains("<HEAD") || line.contains("</HEAD>"));
	}

	public static boolean containsBODYTag(String line){
		return (line.contains("<body") || line.contains("</body>")
				|| line.contains("<BODY") || line.contains("</BODY>"));
	}
	
	public static boolean containsBRTag(String line){
		return (line.contains("<br") || line.contains("</br>") ||
				line.contains("<BR") || line.contains("</BR>"));
	}
	
	public static boolean containsTABLETag(String line){
		return (line.contains("<table") || line.contains("</table>") ||
				line.contains("<TABLE") || line.contains("</TABLE>"));
	}
	
	public static boolean containsTRTag(String line){
		return (line.contains("<tr") || line.contains("</tr>") ||
				line.contains("<TR") || line.contains("</TR>"));
	}
	
	public static boolean containsTDTag(String line){
		return (line.contains("<td") || line.contains("</td>") ||
				line.contains("<TD") || line.contains("</TD>"));
	}

	public static boolean containsLinkTag(String line){
		return (line.contains("<a") || line.contains("</a>")
				|| line.contains("<A") || line.contains("</A>")
				|| line.contains("href"));
	}

	public static String getAddressWithProtocol(String address){
		return PROTOCOL + address;
	}
}
