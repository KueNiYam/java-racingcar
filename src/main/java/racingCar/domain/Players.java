package racingCar.domain;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Players {
    private static final int ZERO_INDEX = 0;

    private List<Player> players;

    public Players(String input) {
        players = new ArrayList<>();
        try {
            List<PlayerName> nameList = NameParser.parse(input);
            players = nameList.stream()
                    .map(Player::new)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            players.clear();
        }
    }

    private void checkDuplication() throws IllegalArgumentException {
        Set<String> set = new HashSet<>();
        players.forEach((t) -> set.add(t.toString()));
        if (players.size() != set.size()) {
            throw new IllegalArgumentException();
        }
    }

    public boolean isEmpty() {
        return players.isEmpty();
    }

    public List<Player> play(Deciders deciders) {
        checkSameNum(deciders);
        IntStream.range(ZERO_INDEX, players.size())
                .forEach((t) -> players.get(t).goOrWait(deciders.get(t)));
        return Collections.unmodifiableList(players);
    }

    private void checkSameNum(Deciders deciders) {
        if (deciders.isNotEqualSize(this)) {
            throw new RuntimeException();
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        players.forEach(
                (t) -> stringBuilder.append(t)
                        .append("\n")
        );

        return stringBuilder.toString();
    }

    public String getWinners() {
        int max = getMax();

        List<Player> winnerList = players.stream()
                .filter((t) -> t.isWinner(max))
                .collect(Collectors.toList());

        StringBuilder stringBuilder = new StringBuilder();

        winnerList.stream()
                .peek((t) -> stringBuilder.append(t.getName()))
                .forEach((t) -> stringBuilder.append(", "));

        stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());

        return stringBuilder.toString();
    }

    private int getMax() {
        return players.stream()
                .max(Player::compare)
                .orElseThrow(RuntimeException::new)
                .getPosition();
    }

    public int size() {
        return players.size();
    }
}
