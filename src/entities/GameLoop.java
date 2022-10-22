package entities;

import java.io.InputStream;
import java.util.Random;
import java.util.Scanner;

/**
 * Class handling game logic
 * */
public class GameLoop {

    Table table;
    Player[] players;

    public GameLoop(){
        this.table = new Table();
    }

    /**
     * Call this function to run the game
     * */
    public void run(){
        //init all required fields for the first time
        init();
        //start the game loop
        loop();
    }

    /**
     * Function to initialize everything needed
     * */
    private void init(){
        System.out.println("Welcome to JINX! How many players do you wish to play with?");
        Scanner s = new Scanner(System.in);
        int playerCount = s.nextInt();
        // set size of players to user specified value
        this.players = new Player[playerCount];

        // init all players
        initPlayers();
    }

    /**
     * Main loop function handling the logic
     * */
    private void loop(){
        Random rand = new Random();
        //Display table
        log("\n" + table.toString());

        //run 3 rounds
        for(int j = 0; j < 3; j++){
            //each run iterate over each player
            for (Player player : players) {
                log("Your turn " + player.getName() + "!");
                // "throw" the dice
                int diceCount = rand.nextInt(6) + 1;
                log("You throw the dice and roll a: " + diceCount);
            }
        }
    }

    /**
     * Function to reset the game
     * */
    private void resetGame(){
        //create new table
        this.table = new Table();
        //delete all players
        this.players = null;

        //restart game;
        init();
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
            //ask player for name, until confirmed
            while(true) {
                log("Welcome Player" + (i + 1) + " whats your name?");
                String name = s.nextLine();
                log("Are you sure your Name is: " + name + " [y/n]");
                String con = s.nextLine();
                //check confirmation
                if (con.equals("y")) {
                    //created player with entered name
                    players[i] = new Player(name);
                    break;
                }
            }
        }
    }
}
