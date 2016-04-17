package datasets;

public class Travel {
    int userId;
    int numberOfPassengers;
    String startingStop;
    String endingStop;

    public Travel() {
    }

    public Travel(int userId, int numberOfPassengers) {
        this.userId = userId;
        this.numberOfPassengers = numberOfPassengers;
    }

    public int getUserId() {
        return userId;
    }

    public int getNumberOfPassengers() {
        return numberOfPassengers;
    }

    public String getStartingStop() {
        return startingStop;
    }

    public void setStartingStop(String startingStop) {
        this.startingStop = startingStop;
    }

    public String getEndingStop() {
        return endingStop;
    }

    public void setEndingStop(String endingStop) {
        this.endingStop = endingStop;
    }

    @Override
    public String toString() {
        return "Travel{" +
                "userId=" + userId +
                ", numberOfPassengers=" + numberOfPassengers +
                ", startingStop='" + startingStop + '\'' +
                ", endingStop='" + endingStop + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Travel)) return false;

        Travel travel = (Travel) o;

        return userId == travel.userId;

    }

    @Override
    public int hashCode() {
        return userId;
    }
}
