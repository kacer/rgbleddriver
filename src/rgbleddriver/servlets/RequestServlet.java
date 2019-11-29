package rgbleddriver.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import rgbleddriver.beans.ColorBean;
import rgbleddriver.model.Color;

/**
 * Servlet implementation class RequestServlet
 */
@WebServlet("/RequestServlet")
public class RequestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private ColorBean colorBean;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RequestServlet() {
        super();
    }
    
     @Override
    public void init() throws ServletException {
    	 colorBean = (ColorBean) getServletContext().getAttribute("colorBean");
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
		JSONParser parser = new JSONParser();
		JSONObject obj;
		try {
			obj = (JSONObject) parser.parse(request.getReader());
			JSONArray leds = (JSONArray) obj.get("leds");
			if(leds != null) {
				for(int i = 0; i < colorBean.getLeds().getLedCount(); i++) {
					JSONObject led = (JSONObject) leds.get(i);
					Color color = new Color(Integer.parseInt((String) led.get("color")));
					colorBean.getLeds().setColorToLed(color, i);
				}
			}
			colorBean.getLeds().update();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
