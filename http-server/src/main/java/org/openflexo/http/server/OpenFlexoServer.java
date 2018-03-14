package org.openflexo.http.server;

import java.util.ArrayList;
import java.util.List;

/**
 * Main class for OpenFlexo server
 */
public class OpenFlexoServer {

	public static class Options {

		public HttpService.Options serverOptions = new HttpService.Options();

		public final List<String> resourcesPaths = new ArrayList<>();

		public boolean verbose = false;
	}

	private static void usage() {
		StringBuilder usage = new StringBuilder();
		usage.append("Usage: server [options]\n");
		usage.append("\n");
		usage.append("- -h|--help: show this help.\n");
		usage.append("- -v|--verbose: verbose mode.\n");
		usage.append("- --port port: server port.\n");
		usage.append("- --host host: server host.\n");
		usage.append("- --resources path: resources folder to register (may have several).\n");
		usage.append("\n");
		usage.append("\n");

		System.out.println(usage);
		System.exit(0);
	}

	private static Options parseOptions(String[] args) {
		Options options = new Options();

		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			switch (arg) {
				case "--help":
					usage();
					break;
				case "--verbose":
					options.verbose = true;
					break;
				case "--port":
					if (i + 1 < args.length) {
						try {
							options.serverOptions.port = Integer.parseInt(args[++i]);
						} catch (NumberFormatException e) {
							System.err.println("Port must be an integer.");
							System.exit(1);
						}
					}
					else {
						System.err.println("Option " + arg + " needs an argument.");
						System.exit(1);
					}
					break;
				case "--host":
					if (i + 1 < args.length) {
						options.serverOptions.host = args[++i];
					}
					else {
						System.err.println("Option " + arg + " needs an argument.");
						System.exit(1);
					}
					break;
				case "--resources":
					if (i + 1 < args.length) {
						options.resourcesPaths.add(args[++i]);
					}
					else {
						System.err.println("Option " + arg + " needs an argument.");
						System.exit(1);
					}
					break;
				default: {
					if (arg.length() >= 2 && arg.charAt(0) == '-' && arg.charAt(1) != '-') {
						for (int j = 1; j < arg.length(); j++) {
							switch (arg.charAt(j)) {
								case 'h':
									usage();
									break;
								case 'v':
									options.verbose = true;
									break;
								default:
									System.err.println("Unknown short option '" + arg.charAt(j) + "'");
									System.exit(1);

							}
						}
					}
					else {
						System.err.println("Unknown long option '" + arg + "'");
						System.exit(1);
					}
				}

			}
		}

		return options;
	}

	public static void main(String[] args) {
		Options options = parseOptions(args);

		HttpService httpService = new HttpService(options.serverOptions);
		httpService.initialize();
	}
}
