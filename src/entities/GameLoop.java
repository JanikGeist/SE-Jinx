package entities;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import actions.ReUnDo.Runde;
import actions.ReUnDo.Verlauf;
import cards.Card;
import cards.LuckCard;


/**
 * Class handling game logic
 * TODO Change the way y and x are used to determine a place on the field
 */
public class GameLoop {

    static int currentRound = 1;
    Player currentPlayer;

    Player[] players;
    Table table;
    Verlauf verlauf;

    ArrayList<String> highscores;
    boolean rff;
    int cP;
    int anzahlKI;

    public GameLoop(boolean rff) {
        this.rff=rff;
        this.table = new Table(rff);
        this.highscores=new ArrayList<>();
        this.getHighscore();
        this.verlauf = new Verlauf();
        this.cP=0;
        this.anzahlKI=0;
        this.currentRound=1;
    }

    /**
     * Call this function to run the game
     */
    public void run() {
        //init all required fields for the first time
        init();
        //start the game loop
        this.showHighscore();
        this.saveHighscores();
        impLoop();
        //loop();
    }

    /**
     * Function to initialize everything needed
     */
    private void init() {
        System.out.println("Welcome to JINX! How many players do you wish to play with? (2-4 Players)");
        int playerCount;
        while (true) {
            try {
                Scanner s = new Scanner(System.in);
                playerCount = s.nextInt();

                if (playerCount < 2 || playerCount > 4) {
                    System.out.println("This game is designed for 2-4 Players! Choose again!");
                } else {
                    // set size of players to user specified value
                    this.players = new Player[playerCount];

                    System.out.println("Please tell us if you like do modifier any player into KI: y/n");
                    String kiInvolvieren=s.next();
                    if (kiInvolvieren.equals("y")){
                        initKI();
                    }
                    else{
                        System.out.println("No KI's involved in this game.");
                    }
                    break;
                }
            }catch (Exception e){
                log("Enter a valid number!");
            }
        }
        if (playerCount!=anzahlKI){
            // init all players
            initPlayers();
        }
    }


    /**
     * New and improved main loop
     * */
    private void impLoop(){

        //current player counter
         cP = 0;
        //determines if a round is over
        boolean roundOver;

        // Run the loop for 3 Rounds maximum
        while(currentRound <= 3){
            roundOver = false;
            //Round is only over if a player cant choose a card anymore
            while(!roundOver){
                // set the next player
                currentPlayer = players[cP];
                currentPlayer.setActive();
                currentPlayer.clearUsedCards();
                currentPlayer.resetRolls();

                // Let the player perform certain actions until he is done
                while(currentPlayer.isActive()){

                    //display the current round
                    log("Round: " + currentRound);
                    //display the field
                    log("\n" + this.table.toString());

                    // Let the player choose an action
                    String action = currentPlayer.chooseAction(this.table);

                    switch (action) {
                        //let the player roll the dice
                        case "R" -> currentPlayer.roll();
                        case "L" -> {
                            //let the player choose a luckCard to play
                            LuckCard chosenOne = currentPlayer.selectLuckCard(this.table);

                            //check if player selected a card
                            if (chosenOne == null) {
                                break;
                            }

                            //switch over all possible card types
                            switch (chosenOne.getCardType()) {
                                // let the player change his diceCount to a set value
                                case ONETOTHREE -> currentPlayer.mintomax(chosenOne, 1, 3);
                                // let the player change his diceCount to a set value
                                case FOURTOSIX -> currentPlayer.mintomax(chosenOne, 4, 6);
                                // give the player an extra throw
                                case EXTRATHROW -> currentPlayer.extraThrow(chosenOne);
                                // reduce the diceCount of the player by one
                                case MINUSONE -> currentPlayer.minusOne(chosenOne);
                                // increase the diceCount of the player by one
                                case PLUSONE -> currentPlayer.plusOne(chosenOne);
                                // let the player choose a collection of cards based on his dice count
                                case CARDSUM -> currentPlayer.cardSum(chosenOne, this.table);
                            }
                        }
                        case "C" -> {
                            //let the player choose a card from the field
                            boolean chosen = currentPlayer.chooseCard(this.table);

                            //was the player not able to choose a card? --> end the round!
                            if (!chosen && !currentPlayer.isActive()) {
                                roundOver = true;
                            }
                        }
                        case "N" -> {
                            verlauf.verlaufAnzeigen();
                        }
                        case "M" ->{
                            Runde veraendert=verlauf.jump();
                            if (!veraendert.equals(verlauf.getTail())){
                                manipulieren(veraendert);
                            }
                        }
                    }
                }
                //make sure current player always loops, only when round is active
                if(!roundOver) {
                    verlaufAktualisieren(currentPlayer);
                    cP = (cP + 1) % (players.length);
                }
            }

            //clean up after round!

            log("Round Over! All your cards will be checked and possibly removed now");
            //check which cards the players have to drop
            for (Player p : players) {
                checkPlayerHand(p);
            }


            //let the player who ended the round drop his highest card!
            currentPlayer.selectHighCard();

            //let each player exchange a card for a luck card
            for (Player p: players){
                p.drawLuckCard(this.table,players);
            }

            //deal new cards
            this.table.resetField();
            verlaufAktualisieren(currentPlayer);

            //increase the round count
            currentRound++;
        }

        //all 3 rounds ended, calculate score here
        this.addCurrentHighscores();
        this.saveHighscores();
        log("Game Over!");
    }


    /**
     * fuegt die letzte Spielrunde in den Spielverlauf ein
     * @param aktiv Spieler der den letzten Zug gemacht hat
     */
    public void verlaufAktualisieren(Player aktiv){
        Table aktuellerTisch = new Table(rff);
        aktuellerTisch.setField(table.getField());
        aktuellerTisch.setCardStack(table.getCardStack());
        aktuellerTisch.setLuckStack(table.getLuckStack());

        ArrayList<Player> aktuelleSpielerStaende= new ArrayList<>();
        Player ak= new Player(aktiv.getName());
        ak.setCards(aktiv.getCards());
        ak.setLuckCards(aktiv.getLuckCards());
        for (int i=cP; i<players.length; i++){
            Player dummy= new Player(players[i].getName());
            dummy.setCards(players[i].getCards());
            dummy.setLuckCards(players[i].getLuckCards());
            aktuelleSpielerStaende.add(dummy);
        }
        for (int i=0; i<cP; i++){
            Player dummy= new Player(players[i].getName());
            dummy.setCards(players[i].getCards());
            dummy.setLuckCards(players[i].getLuckCards());
            aktuelleSpielerStaende.add(dummy);
        }

        Runde neu = new Runde(aktuelleSpielerStaende, aktuellerTisch);
        verlauf.rundeHinzufuegen(neu);
    }

    /**
     * aktualisiert die aktive Runde auf eine ausgewaehlte andere Runde
     * @param neuerStand Runde, von der weiter gespielt werden soll
     */
    public void manipulieren(Runde neuerStand){
        this.table.setField(neuerStand.getTischStand().getField());
        this.table.setCardStack(neuerStand.getTischStand().getCardStack());
        this.table.setLuckStack(neuerStand.getTischStand().getLuckStack());

        int laenge= players.length;
        this.players=new Player[laenge];
        int z=0;
        for (Player p: neuerStand.getSpieler()){
            this.players[z]=p;
            z++;
        }
        Runde vorher=verlauf.getTail().getDavor();
        vorher.setDahinter(neuerStand);
        verlauf.getTail().setDavor(neuerStand);
        neuerStand.setDavor(vorher);
        neuerStand.setDahinter(verlauf.getTail());
        log("status updated");

    }





    /**
     * Function to remove cards from player hand if round ends
     */
    private void checkPlayerHand(Player p) {
        Card[] hand = p.getCards().toArray(new Card[0]);
        Card[][] field = this.table.getField();

        for (Card cP : hand) {
            for (Card[] row : field) {
                for (Card cF : row) {
                    if (cF != null && cP.getColor() == cF.getColor()) {
                        //Player has a card with the same color on his hand --> remove it!
                        p.removeCard(cP);
                    }
                }
            }
        }
    }


    /**
     * Function to reset the game
     */
    private void resetGame() {
        //create new table
        this.table = new Table(false);
        //delete all players
        this.players = null;

        //restart game;
        init();
    }

    /**
     * Function to easily log a msg on the console
     */
    private void log(String msg) {
        System.out.println("[JINX] " + msg);
    }


    //TODO: Fail save, only let player enter valid names
    /**
     * Function to initialize all players by name
     */
    private void initPlayers() {

        Scanner s = new Scanner(System.in);

        //create as many players as needed
        for (int i = anzahlKI; i < players.length; i++) {
            //ask player for name, until confirmed
            while (true) {
                log("Welcome Player" + (i + 1) + " whats your name?");
                String name = s.nextLine().replaceAll(" ", "");
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
        //set the first player
        currentPlayer = players[0];

    }

    /**
     * loads data from Highscore file
     */
    private void getHighscore() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("entities/highscore.txt"));

            String line = br.readLine();

            while (line != null) {
                this.highscores.add(line);
                line = br.readLine();
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * shows all high scores
     */
    private void showHighscore() {
        log("High scores:");
        int ranking = 0;
        for (String score : this.highscores) {
            ranking++;
            log(ranking + ". " + score);
        }
    }

    /**
     * saves high scores in textfile
     */
    private void saveHighscores() {
        try {
            PrintWriter pw = new PrintWriter("entities/highscore.txt");

            for (String entry : this.highscores) {
                pw.println(entry);
                pw.flush();
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * adds Highscore of the current game, sorted by score
     */
    private void addCurrentHighscores() {
        for (Player player : this.players) {
            ArrayList<String> newHighscore = new ArrayList<>();
            boolean added = false;
            int lines = 0;
            for (String line : this.highscores) {
                lines++;
                String[] nameAndScore = line.split(" ");
                //wenn der alte wert kleiner ist als der score, score muss also darüber, bei gleichen werten kommt der neue nach unten
                if (Integer.parseInt(nameAndScore[1]) < player.getScore() && !added) {
                    newHighscore.add(player.getName() + " " + player.getScore());
                    added=true;
                    newHighscore.add(nameAndScore[0] + " " + nameAndScore[1]);
                }else if(!added && this.highscores.size()==lines){
                    newHighscore.add(nameAndScore[0] + " " + nameAndScore[1]);
                    newHighscore.add(player.getName() + " " + player.getScore());
                } else {
                    newHighscore.add(nameAndScore[0] + " " + nameAndScore[1]);
                }
            }
            this.highscores = newHighscore;
        }
    }

    /**
     * Ki menue, Auswahl der Anzahl der KIs im Spiel und deren Initialisierung
     */
    public void initKI(){
        int players=this.players.length;
        ArrayList<Player> ki = new ArrayList<>();
        while(true){
            System.out.println("This game will have "+players+ " players. Choose between 0-"+players+".\n" +
                    "How many do you want to substitute with KI's?");
            Scanner s = new Scanner(System.in);
            String kiAnzahl = s.next();
            try{
                int anzahl = Integer.parseInt(kiAnzahl);
                if (anzahl>0&&anzahl<=players){
                    for (int a=0; a<anzahl; a++){
                        ki.add(buildingKI());
                    }
                    int b=0;
                    for (Player p: ki){
                        this.players[b]=p;
                        b++;
                    }
                    break;
                }
                else{
                    System.out.println("Incorrect input.");
                }
            }
            catch (NumberFormatException n){
                System.out.println("Not an acceptable answer. You will play without any KI.");
            }
        }
    }

    /**
     * erstellt individuelle KI
     * @return einzelne KI
     */
    public Player buildingKI(){
        Player k;
        String name="";
        String level="";
        Scanner s = new Scanner(System.in);
        while (true){
            System.out.println("Please enter a Name for your KI:");
            name=s.next();
            if (!name.equals("")){
                System.out.println("Please choose a level for your KI:  "+
                        "easy / medium / hard");
                level=s.next();
                if (level.equals("easy")){
                    k = new EasyKI(name);
                    break;
                }
                else if (level.equals("medium")){
                    k = new MediumAI(name);
                    break;
                }
                else if (level.equals("hard")){
                    k=new AIPLayer3(name);
                    break;
                }
                else{
                    System.out.println("Not an option. Try again.");
                }
            }
            else{
                System.out.println("Wrong input.");
            }
        }
        System.out.println("You created: KI: "+ k.getName()+ "  Level: "+ k.getClass().getSimpleName());
        this.anzahlKI++;
        return k;
    }
}
