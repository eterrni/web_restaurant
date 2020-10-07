package by.epamtc.restaurant.controller.command.impl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import by.epamtc.restaurant.controller.command.Command;

public class GoToMenuPageCommand implements Command {

	private static final String MENU_PAGE = "WEB-INF/jsp/menu_page.jsp";

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");

		if (request.getSession().getAttribute("drinkList") == null
				|| request.getSession().getAttribute("dishList") == null
				|| request.getSession().getAttribute("desertList") == null) {
			DownloadMenuDataCommand downloadMenuDataCommand = new DownloadMenuDataCommand();
			downloadMenuDataCommand.execute(request, response);
		}
		request.getRequestDispatcher(MENU_PAGE).forward(request, response);

	}

}