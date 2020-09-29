package by.epamtc.restaurant.controller.command.impl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import by.epamtc.restaurant.controller.command.Command;

public class GoToStockPageCommand implements Command {

	private static final String STOCK_PAGE = "WEB-INF/jsp/stock_page.jsp";

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");

		request.getRequestDispatcher(STOCK_PAGE).forward(request, response);

	}

}
