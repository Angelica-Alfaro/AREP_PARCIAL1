package edu.escuelaing.arem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;


public class WebServer {
	
		/**
		 * Attribute that defines the web server 
		 */ 
		private static final WebServer _instance = new WebServer();
		
		private WebServer() {
		}
		
		/**
	     * This method get the necessary feature to web server
	     * @return web server instance 
	     */
		public static WebServer getInstance() {
			return _instance;
		}
	    
		/**
	     * This method start the client-server connection for communication
	     * @param args - Request to go to another page or file
	     * @param port - The connection port
	     * @throws IOException, URISyntaxException
	     */
		public void start(String[] args, int port) throws IOException, URISyntaxException {
			ServerSocket serverSocket = null;
			try {
				serverSocket = new ServerSocket(port);
			} catch (IOException e) {
				System.err.println("Could not listen on port: " + port);
				System.exit(1);
			}

			boolean running = true;
			while (running) {
				Socket clientSocket = null;

				try {
					System.out.println("Listo para recibir ...");
					clientSocket = serverSocket.accept();
				} catch (IOException e) {
					System.err.println("Accept failed.");
					System.exit(1);
				}
				serverConnection(clientSocket);
			}
			serverSocket.close();
		}
		
		/**
	     * Client connection with Server
	     * @param clientSocket - Client for communication
	     * @throws IOException, URISyntaxException
	     */
		public void serverConnection(Socket clientSocket) throws IOException, URISyntaxException {
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			String inputLine;
			StringBuilder stringBuilderRequest = new StringBuilder();
			while ((inputLine = in.readLine()) != null) {
				// System.out.println("Received: " + inputLine);
				stringBuilderRequest.append(inputLine);
				if (!in.ready()) {
					break;
				}
			}

			String stringRequest = stringBuilderRequest.toString();
			if ((stringRequest != null) && (stringRequest.length() != 0)) {
				String[] request = stringRequest.split(" ");
				String uriStr = request[1];
				//System.out.println("uriStr:" + uriStr);
				URI resourceURI = new URI(uriStr);
				getClima(resourceURI, out, clientSocket.getOutputStream(), uriStr);
			}
			out.close();
			in.close();
			clientSocket.close();
		}
		
		
		/**
	     * This method lets you read a text or image type resource
	     * @param resourceURI - Path of the required resource
	     * @param out - Text output flow
	     * @param outputStream - Write output
	     * @param uriStr - URI as a string
	     * @throws IOException
	     */
	    private void getClima(URI resourceURI, PrintWriter out, OutputStream outputStream, String uriStr) throws IOException {
	    	String outputLine, response;
			String path = resourceURI.getPath();
			if (path != null) {
				if (uriStr.contains("/consulta?lugar=")) {
					String[] query = uriStr.split("\\?lugar=");
					if (query[1] != null) {
						System.out.println(query[1]);
						response = HttpConnection.getClima(query[1]);
						System.out.println(query[1]);
						
					}
					else {
						response = HttpConnection.getClima("London");
					}
					outputLine = pageClima(response);
					out.println(outputLine);
				}
				else if (uriStr.equals("/clima") || uriStr.equals("/")){
					out.println(defaultResponse());
				}
			}
			else {
				outputLine = "NOT FOUND";
				out.println(outputLine);
				}
			}
	    
		/**
		 * Default page when not finding a resource
		 * @return the default html page
		 */
	    private String pageClima(String response) {
	    	String outputLine = "HTTP/1.1 200 OK\r\n" 
					+ "Content-Type: text/html\r\n"
					+ "\r\n" 
					+ "<!DOCTYPE html>\n"
					+ "  <html>\n"
					+ "    <head>\n" 
					+ "      <meta charset=\"UTF-8\">\n"
					+ "      <title>Home page</title>\n" 
					+ "    </head>\n" 
					+ "    <body>\n"
					+       response
					+ "    </body>\n" 
					+ "  </html>\n";
	    	return outputLine;

	    	
		}

		/**
		 * Default page when not finding a resource
		 * @return the default html page
		 */
		public String defaultResponse() {
			String outputLine = "HTTP/1.1 200 OK\r\n" 
								+ "Content-Type: text/html\r\n"
								+ "\r\n" 
								+ "<!DOCTYPE html>\n"
								+ "  <html>\n"
								+ "    <head>\n" 
								+ "      <meta charset=\"UTF-8\">\n"
								+ "      <title>Home page</title>\n" 
								+ "    </head>\n" 
								+ "    <body>\n"
								+ "      <img src=\"https://www.lagabogados.com/wp-content/uploads/2020/01/EN-CONSTRUCCION.jpg\"> "
								+ "    </body>\n" 
								+ "  </html>\n";
			return outputLine;
		}
}