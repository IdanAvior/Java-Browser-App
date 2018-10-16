package il.ac.telhai.cn.browser;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Browser class - a text-based web browser
 * @author Idan Avior
 *
 */
public class Browser {

	private final int PORT_NUMBER = 80;
	private Socket socket;
	private Scanner scanner;
	private String webAddress;
	private DataInputStream in;
	private String host;
	private String path;
	private ArrayList<String> link_list;


	/*
	 * Constructor
	 */
	public Browser(){
		socket = new Socket();
		scanner = new Scanner(System.in);
		webAddress = null;
		link_list = new ArrayList<String>();
	}

	/*
	 * Get the user's input, then send it to the appropriate
	 * method.
	 */
	public void getInput() throws ExitCommandException{
		String line = scanner.next();
		if (!isNumeric(line)){
			if (line.equals("exit"))
				throw new ExitCommandException();
			if (!HTMLParser.isAValidAddress(line))
				line = HTMLParser.getAddressWithProtocol(line);
			webAddress = line;
			goToPage();
		}
		else 
			goToLink(Integer.parseInt(line));
	}

	/*
	 * goToPage() - constructs a GET request and sends it to the server,
	 * then prints the server's input. Links are saved in the ArrayList. 
	 */
	@SuppressWarnings("deprecation")
	public void goToPage(){
		try {
			link_list = new ArrayList<String>();
			URL url = new URL(webAddress);
			host = url.getHost();
			path = url.getPath();
			String request = "GET " + "/" + path + " HTTP/1.1\r\n";
			request += "Host: " + host;
			request += "\r\n\r\n";
			socket = new Socket(host, PORT_NUMBER);
			socket.setSoTimeout(15000);
			PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
			printWriter.print(request);
			printWriter.flush();
			in = new DataInputStream(socket.getInputStream());
			String line = null;
			while ((line = in.readLine()) != null){
				if (containsALink(line))
					addLink(line);
				line = HTMLParser.getParsedLine(line);
				if (!(HTMLParser.containsTABLETag(line) ||
						HTMLParser.containsTRTag(line)))
					System.out.println(line);
			}
			socket.close();
			System.out.println(link_list.size() > 0 ? "Links:" : "No links from this page");
			for (String s : link_list)
				System.out.println("Link " + (link_list.indexOf(s) + 1) + ": " + s);
		} catch(MalformedURLException e){
			System.err.println("Error: invalid URL");
		}
		catch (IOException e) {
			System.err.println("Error: unknown host / connection timed out");
		}
	}

	/*
	 * Checks if a line contains a link 
	 */
	public boolean containsALink(String line){
		return (line.contains("<a") || line.contains("<A"));
	}

	/*
	 * Adds a link to the ArrayList
	 */
	public void addLink(String line){
		String temp = HTMLParser.parseATag(line);
		if (!link_list.contains(temp))
			link_list.add(temp);
	}

	/*
	 * Checks if an address qualifies as a full address
	 */
	public boolean isAFullAddress(String address){
		return address.contains("http://");
	}

	/*
	 * Sets the webAddress variable to the address of the link with
	 * the corresponding number (assuming the number is valid), and then
	 * invokes the goToPage() method.
	 */
	public void goToLink(int number){
		try{
			String temp = link_list.get(number - 1);
			if (isAFullAddress(temp)){
				webAddress = temp;
			}
			else {
				webAddress = "http://" + host + "/" + temp;
			}
			goToPage();
		}
		catch(IndexOutOfBoundsException e){
			System.err.println("Error: Invalid link number\n");
		}

	}

	/*
	 * Returns the number of links in the link list
	 */
	public int getNumberOfLinks(){
		return link_list.size();
	}

	/*
	 * Checks if a String represents an integer
	 */
	public static boolean isNumeric (String str){
		try{
			@SuppressWarnings("unused")
			int i = Integer.parseInt(str);
		}
		catch(NumberFormatException e){
			return false;
		}
		return true;
	}

	/*
	 * ExitCommandException - intended to be thrown when the user
	 * types 'exit' 
	 */
	@SuppressWarnings("serial")
	public class ExitCommandException extends Exception{
		public ExitCommandException(){
			super();
		}

		public ExitCommandException(String str){
			super(str);
		}
	}

	/*
	 * Main method
	 */
	public static void main(String[] args) throws IOException {
		Browser browser = new Browser();
		System.out.println("Enter address: ('exit' to exit)");
		while (true){
			try{
				browser.getInput();
				if (browser.getNumberOfLinks() > 0){
					System.out.println("Enter address or link number: ('exit' to exit)");
				}
				else{
					System.out.println("Enter address: ('exit' to exit)");
				}}
			catch(ExitCommandException e){
				break;
			}
		}
	}
}

