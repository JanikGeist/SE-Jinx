package entities;

import java.io.InputStream;
import java.util.Scanner;

/**
 * Class handling game logic
 * */
public class GameLoop {

    Table table;
    Player[] players;

    public GameLoop(int playerCount){
        this.table = new Table();
        this.players = new Player[playerCount];

        init();
    }

    /**
     * Function to initialize everything needed
     * */
    private void init(){
        initPlayers();
    }

    /**
     * Function to easily log a msg on the console
     * */
    private void log(String msg){
        System.out.println("[JINX] " + msg);
    }

    //TODO: Fail save, only let player enter valid names
    /**
     * Function to initialize all players by name
     * */
    private void initPlayers(){

        Scanner s = new Scanner(System.in);

        //create as many players as needed
        for(int i = 0; i < players.length; i++){
            while(true) {
                log("Welcome Player" + (i + 1) + " whats your name?");
                String name = s.nextLine();
                log("Are you sure your Name is: " + name + " [y/n]");
                String con = s.nextLine();
                if (con.equals("y")) {
                    players[i] = new Player(name);
                    break;
                }
            }
        }
    }
}
