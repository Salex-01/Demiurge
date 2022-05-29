import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class SigninHandler implements HttpHandler {
	private final HashMap<String, String> accounts;
	private final HashMap<String, String> tokens;
	private final HashMap<String, String> sessions;

	public SigninHandler(HashMap<String, String> accounts, HashMap<String, String> tokens, HashMap<String, String> sessions) {
		this.accounts = accounts;
		this.tokens = tokens;
		this.sessions = sessions;
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		String response;
		int responseCode;
		if (exchange.getRequestMethod().equals("GET")) {
			String[] query = new String(exchange.getRequestBody().readAllBytes()).split("\n");
			if (query.length != 2 || query[0].equals("") || query[1].equals("")) {
				response = "Bad request";
				responseCode = 400;
			} else {
				String username = query[0];
				String password = query[1];
				try {
					if (accounts.containsKey(username) && accounts.get(username).equals(EncryptionManager.encrypt(password,"SHA3-256"))) {
						String token = EncryptionManager.encrypt(username + System.currentTimeMillis(), "SHA3-256");
						tokens.put(username, token);
						sessions.put(token, username);
						response = "Connected\n" + token;
						responseCode = 200;
					} else {
						response = "Bad credentials";
						responseCode = 401;
					}
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
					response = "Internal server error";
					responseCode = 500;
				}
			}
		} else {
			response = "Bad Request";
			responseCode = 400;
		}
		exchange.sendResponseHeaders(responseCode, response.length());
		exchange.getResponseBody().write(response.getBytes());
		exchange.getResponseBody().close();
	}
}