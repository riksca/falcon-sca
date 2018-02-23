/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.calontir.marshallate.falcon;

import java.io.IOException;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.calontir.marshallate.falcon.db.AuthTypeDAO;
import org.calontir.marshallate.falcon.db.LocalCache;

/**
 *
 * @author rik
 */
public class AdminServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String message = "";
        String clearcache = request.getParameter("clearcache");

        if (clearcache != null && clearcache.equals("AuthType")) {
            LocalCache atCache = AuthTypeDAO.LocalCacheImpl.getInstance();

            System.out.print("Size before clearing:");
            System.out.println(atCache.getCount());
            atCache.clear();
            System.out.print("Size after clearing:");
            System.out.println(atCache.getCount());
            message += "Cache cleared";
        }

        String calonbar = request.getParameter("calonbar");
        if (calonbar != null) {
            Date now = new Date();
//            String timestamp = now.toString();

            Cookie cookie = new Cookie("calonbar", calonbar.equals("0") ? "0" : "1");

            cookie.setMaxAge(365 * 24 * 60 * 60);
            response.addCookie(cookie);
        }

        response.sendRedirect("/index.jsp");
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
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
