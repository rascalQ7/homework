import java.util.ArrayList;

public class PlaySite {
    protected int capacity, id;
    protected String type;
    protected ArrayList<Kid> kidsOnSite, kidsQueue, acceptanceQueue;

    protected PlaySite(int id, int capacity, String type) {
        this.id = id;
        this.capacity = capacity;
        this.type = type;
        this.kidsOnSite = new ArrayList();
        this.kidsQueue = new ArrayList();
        this.acceptanceQueue = new ArrayList();
    }

    protected void acceptQueue(Kid kid) {
        this.removeKid(kid);
        if (kidsOnSite.size() == this.capacity){
            addToQueue(kid);
        }
        else {
            kid.startTimer();
            this.kidsOnSite.add(kid);
            kid.changeState(this, "site");
        }
    }

    protected void addKidToPlaySite(Kid kid) {
        if (this.kidsOnSite.size() == this.capacity) {
            this.acceptanceQueue.add(kid);
            kid.changeState(this, "accept");
        }
        else {
            this.removeKid(kid);
            kid.startTimer();
            this.kidsOnSite.add(kid);
            kid.changeState(this, "site");
        }
    }

    protected void removeKid(Kid kid) {
        switch(kid.state) {
            case "site":
                this.kidsOnSite.remove(kid);
                kid.addHistory(this);
                kid.changeState(null, "available");
                int queueSize = this.kidsQueue.size();
                if (queueSize > 0) {
                    kid = this.kidsQueue.get(queueSize-1);
                    this.kidsQueue.remove(kid);
                    kid.startTimer();
                    this.kidsOnSite.add(kid);
                    kid.changeState(this, "site");
                }
                break;
            case "accept":
                this.acceptanceQueue.remove(kid);
                kid.changeState(null, "available");
                break;
            case "queue":
                this.kidsQueue.remove(kid);
                kid.changeState(null, "available");
                break;
            default:
        }
    }

    protected int getStatus() {
        int result = 0;
        if (this.kidsOnSite.size() != 0) {
            result = (int)((double)this.kidsOnSite.size() / capacity   * 100);
        }
        return result;
    }

    private void addToQueue(Kid kid) {
        int placeInTheQueue = 0;
        if (kid.vip) {
            for (int i=0; i < this.kidsQueue.size(); i++) {
                placeInTheQueue = this.kidsQueue.size();
                if (this.kidsQueue.get(i).getTicketStatus()) {
                    if (i > 3) { placeInTheQueue = i - 3; }
                    else { placeInTheQueue = 0; }
                }
            }
        }
        this.kidsQueue.add(placeInTheQueue, kid);
        kid.changeState(this, "queue");
    }
}
