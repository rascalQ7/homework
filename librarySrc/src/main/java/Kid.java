import java.util.ArrayList;
import java.util.HashMap;

public class Kid {
    protected String name;
    protected int age, ticketNo;
    protected boolean vip;
    protected long startTime;
    protected PlaySite currentSite;
    protected String state;
    protected HashMap<PlaySite, Long> history;


    protected Kid(String name, int age, boolean vip, int ticketNo) {
        this.name = name;
        this.age = age;
        this.ticketNo = ticketNo;
        this.vip = vip;
        this.history = new HashMap();
        this.startTime = 0;
        this.currentSite = null;
        this.state = "available";
    }
    protected boolean getTicketStatus() {
        return this.vip;
    }

    protected void startTimer() {this.startTime = System.currentTimeMillis(); }

    protected void addHistory(PlaySite currentSite) {

        if (this.history.containsKey(currentSite)) {
            this.history.put(currentSite, this.history.get(currentSite) +  System.currentTimeMillis() - this.startTime);
        }
        else {
            this.history.put(currentSite, System.currentTimeMillis() - this.startTime);
        }
    }
    protected void changeState(PlaySite currentSite, String state) {
        this.currentSite = currentSite;
        this.state = state;
    }
}