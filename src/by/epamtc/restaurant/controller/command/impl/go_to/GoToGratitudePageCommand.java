package by.epamtc.restaurant.controller.command.impl.go_to;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import by.epamtc.restaurant.controller.command.Command;

public class GoToGratitudePageCommand implements Command {

	private static final String GRATITUDE_PAGE = "WEB-INF/jsp/gratitude_page.jsp";
	private static final String WELCOME_PAGE = "WEB-INF/jsp/welcome_page.jsp";

	private static final String CONTENT_TYPE = "text/html";

	private static final String ATTRIBUTE_ORDER = "order";
	private static final String ATTRIBUTE_USER = "user";

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getSession().getAttribute(ATTRIBUTE_USER) == null) {
			request.getRequestDispatcher(WELCOME_PAGE).forward(request, response);
		} else {
			response.setContentType(CONTENT_TYPE);

			request.getRequestDispatcher(GRATITUDE_PAGE).forward(request, response);

			request.getSession().removeAttribute(ATTRIBUTE_ORDER);
		}
	}

}
