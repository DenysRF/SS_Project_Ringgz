package Game.Players;

import Game.Strategies.Strategy;

public class ComputerPlayer extends Player {

    Strategy strategy;

    public ComputerPlayer(String name, int noOfPlayers, Strategy strategy) {
        super(name, noOfPlayers);
        this.strategy = strategy;
    }



}
