package org.neo4j.conn;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class neo4jInterface
 */
 
 //author Mohamed Mohamed
@WebServlet("/neo4jInterface")

public class neo4jInterface extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    	public neo4jJDBC obj1;
    	public String recommendations;
        //public neo4jODBC2() {
            //super();
            public void init() throws ServletException {

                obj1 = new neo4jJDBC();
                
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String tweet = request.getParameter("q");
		String uid=request.getParameter("id");
		String useFollow=request.getParameter("useFollow");
		//if useFollow=0 we don't use neo4j and direct;y return lucene's results
		try {
            recommendations = obj1.getRecommendations(tweet,uid,useFollow);
          } catch (Exception e) {
            getServletContext().log(e.getLocalizedMessage(), e);
            throw new ServletException("An exception occurred in "
                + e.getMessage());
          }
          
	    PrintWriter out = response.getWriter();
	    out.println(recommendations);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
	
	public void destroy() {
	    super.destroy();
	  }


}
