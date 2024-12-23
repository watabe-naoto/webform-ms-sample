package webform.api.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;

/**
 * 初期起動Servletクラス
 *
 */
public class InitializeServlet extends HttpServlet {
	/**　Serial Version UID */
	private static final long serialVersionUID = -8906831387485317277L;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init() {
		ServletConfig config = super.getServletConfig();
		System.out.println("- " + config.getInitParameter("application-name") + "を起動しました。 -");
	}
}
