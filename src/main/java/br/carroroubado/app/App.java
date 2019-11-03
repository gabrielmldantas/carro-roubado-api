package br.carroroubado.app;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

import br.carroroubado.api.PesquisaCarroRoubadoServlet;

public class App {
	public static void main(String[] args) throws Exception {
		Server server = new Server(8080);
		ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.SESSIONS);
		handler.setContextPath("/carros");
		handler.setMaxFormContentSize(2 * 1024 * 1024);
		server.setHandler(handler);
		handler.addServlet(PesquisaCarroRoubadoServlet.class, "/pesquisa/*");
		server.start();
		server.join();
	}
}
