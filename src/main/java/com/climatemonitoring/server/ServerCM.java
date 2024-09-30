package com.climatemonitoring.server;


import com.climatemonitoring.util.DatabaseManager;

import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerCM {
    private static final int PORT = 8888;
    private static final int MAXTHREADS=50;

    public static void main (String [] args){
        //uso executor semplifica la gestione e le risorse di sistema (pool di thread), consente di gestire + richieste risp ai thread classici
        ExecutorService pool = Executors.newFixedThreadPool(MAXTHREADS);

        try{
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server in ascolto sulla porta "+PORT);
            while(true){
                Socket cliSocket = serverSocket.accept();
                System.out.println("Nuovo client connesso : "+cliSocket.getInetAddress());

                pool.submit(new ClientHandler(cliSocket));
            }
        }catch(IOException e){
            System.err.println("Errore del server <"+e);
        }finally{
            pool.shutdown();
        }
    }

    //Connettore db per stabilire la connesione al db
    public static Connection getDbConnection() throws SQLException{
        return DatabaseManager.getInstance().getConnection();
    }

}
