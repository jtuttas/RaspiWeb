/**
 * Parameter:
 * out (plain,xml,json)
 * set (high,low)
 * dim (any Value)
 */

package de.tuttas.raspi;

import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import de.raspi.LEDControl;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Jörg
 */
public class LedServlet extends HttpServlet {

    private String outFormat="plain";
    LEDControl ledControl = LEDControl.getInstance(RaspiPin.GPIO_01, PinState.LOW);
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try (PrintWriter out = response.getWriter()) {
            if (outFormat.compareTo("plain")==0) {
                response.setContentType("text/plain;charset=UTF-8");
                out.print(ledControl.getDimValue());
            }
            else if (outFormat.compareTo("html")==0) {
                                out.println("<html><head><title>Sensordata</title>");
                    out.println("<link rel=\"stylesheet\" href=\"css/sensordata.css\" />");
                    out.println("</head><body>");
                    out.println ("<div class=\"measurement\">"+ledControl.getDimValue()+"</div>");
                    out.println("</body></html>");
            }

            else if (outFormat.compareTo("xml")==0) {
                response.setContentType("text/xml;charset=UTF-8");
                out.println ("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                out.println("<led>");
                out.println("<dim>"+ledControl.getDimValue()+"</dim>");
                out.println("</led>");
            }
            else if (outFormat.compareTo("json")==0) {
                response.setContentType("application/json;charset=UTF-8");
                out.println ("{ \"dim\": "+ledControl.getDimValue()+"}");
            }
            else {
                response.setContentType("text/plain;charset=UTF-8");
                out.print(ledControl.getDimValue());                
            }
        }
        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        
        if (request.getParameter("out")!=null) outFormat=request.getParameterValues("out")[0];
        else outFormat="plain";
        if (request.getParameter("set")!=null) {
            String set=request.getParameterValues("set")[0];
            if (set.compareTo("low")==0) {
             ledControl.turnOn(false);
            }
            if (set.compareTo("hight")==0) {
             ledControl.turnOn(true);
                
            }
            
        }
        if (request.getParameter("dim")!=null) {
            int dim= Integer.parseInt(request.getParameterValues("dim")[0]);
            ledControl.dim(dim);
        }
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
