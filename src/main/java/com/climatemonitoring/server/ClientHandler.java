package com.climatemonitoring.server;

import com.climatemonitoring.util.DatabaseManager;

import java.io.*;
import java.net.*;
import java.sql.*;

public class ClientHandler implements Runnable {

    private final Socket cliSocket;

    public ClientHandler(Socket socket) {
        this.cliSocket=socket;
    }

    @Override
    public void run() {
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(cliSocket.getInputStream()));
            PrintWriter out = new PrintWriter(cliSocket.getOutputStream());

            String inputLine;
            while((inputLine = in.readLine())!= null){
                String response = processRequest(inputLine);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try{
                cliSocket.close();
            }catch (IOException e1){
                System.err.println("errore nella chiusura del socket" +e1.getMessage());
            }
        }

    }


    // Test: eseguire una query per ottenere il nome di un'area di monitoraggio                    ---DA MODIFICARE--
    private String processRequest(String request){
        String query = "Select nome FROM centromonitoraggio LIMIT !";
        try{
             ResultSet rs = DatabaseManager.getInstance().executeQuery(query);

            if(rs.next()){
                return "nome del centro di monitoraggio "+rs.getString("nome");
            }else{
                return "nessun centro di monitoraggio salvato";
            }
        } catch (SQLException e) {
            return "Errore nella query "+e.getMessage();
        }
    }
}
