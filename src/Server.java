import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.util.HashMap;

public class Server {

	private static HashMap<String, Class<?>> routes = new HashMap<>() {{
		put("signin", SigninHandler.class);
		put("signout", SignoutHandler.class);
		put("create", CreateHandler.class);
		put("join", JoinHandler.class);
		put("leave", LeaveHandler.class);
		put("signup", SignupHandler.class);
		put("close", CloseHandler.class);
		put("play", PlayHandler.class);
	}};

	public static void main(String[] args) throws IOException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
		HashMap<String, String> accounts = new HashMap<>();
		HashMap<String, String> tokens = new HashMap<>();
		HashMap<String, String> sessions = new HashMap<>();
		int port = 8666;
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-p")) {
				port = Integer.parseInt(args[i + 1]);
				i++;
			}
		}
		HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
		for (String route : routes.keySet()) {
			server.createContext("/" + route, (HttpHandler) routes.get(route).getConstructor(HashMap.class, HashMap.class, HashMap.class).newInstance(accounts, tokens, sessions));
		}
		server.setExecutor(null);
		server.start();
	}
}