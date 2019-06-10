package com.trading.brokergateway.DEMO;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.rmi.ServerException;

class JPADemo {
    public static void main(String[] args) throws Exception{
        OrderDEMO.doTrade("SB");
    }

}