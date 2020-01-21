import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PlayGround {
    private ArrayList<PlaySite> sites;
    private ArrayList<Kid> kids;
    private int siteLicenseId;
    private HashMap<String, Integer> siteTypes;
    private ArrayList<Long> snapshotsList;
    private JSONArray snapshots;
    private long lastSnapshot;

    public PlayGround() {
        this.sites = new ArrayList();
        this.kids = new ArrayList();
        this.snapshots = new JSONArray();
        this.snapshotsList = new ArrayList<Long>();
        this.lastSnapshot = 0;
        this.siteLicenseId = 0;
        this.siteTypes = new HashMap();
        this.siteTypes.put("slide", 1);
        this.siteTypes.put("double swings", 2);
        this.siteTypes.put("carousel", 0);
        this.siteTypes.put("ball pit", 0);
    }

    public void removeSite(int id) {
        if (this.kids.size() == 0) {
            PlaySite site = this.getPlaySite(id);
            sites.remove(site);
        }
    }

    public JSONArray getBluePrint() {
        JSONArray arr = new JSONArray();

        for (PlaySite site : this.sites) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("id", site.id);
                obj.put("type", site.type);
                obj.put("capacity", site.capacity);
                obj.put("utilization", site.getStatus());
            } catch (JSONException e) {}
            arr.put(obj);
        }
        return arr;
    }

    public JSONArray getKids() {
        JSONArray arr = new JSONArray();

        for (Kid kid : this.kids) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("ticket number", kid.ticketNo);
                obj.put("name", kid.name);
                obj.put("age", kid.age);
                obj.put("vip", kid.getTicketStatus());
            } catch (JSONException e) {}
            arr.put(obj);
        }
        return arr;
    }

    public void addKidToTheSite(int ticketNo, int siteId) {
        this.checkIfTimeForSnapshot();
        Kid kid = getKid(ticketNo);
        PlaySite currentSite = kid.currentSite;
        PlaySite newSite = getPlaySite(siteId);
        if (currentSite != null) { currentSite.removeKid(kid); }
        newSite.addKidToPlaySite(kid);
    }

    public String[] getSiteTypes() {
        String[] types = new String[this.siteTypes.size()];
        int i = 0;
        for (Map.Entry<String, Integer> type : this.siteTypes.entrySet()) {
            types[i] = type.getKey();
            i++;
        }
        return types;
    }

    public void createSnapshot(Timestamp snapshot) {
        snapshotsList.add(snapshot.getTime());
    }

    public void createSite(int preferedCapacity, String type) {
        int capacity;

        if (this.siteTypes.get(type) > 0) { capacity = this.siteTypes.get(type); }
        else { capacity = preferedCapacity; }

        this.sites.add(new PlaySite(issueLicense(), capacity, type));
    }

    public void newKid(String name, int age, boolean vip, int ticketNo) {
        this.kids.add(new Kid(name, age, vip, ticketNo));
    }

    public JSONObject getSiteStatus(int siteId) {
        JSONObject obj = new JSONObject();

        for (PlaySite site : this.sites) {
            if (site.id == siteId) {
                try {
                    obj.put("id", site.id);
                    obj.put("type", site.type);
                    obj.put("capacity", site.capacity);
                    obj.put("on site", this.getKidsFromTheList(site.kidsOnSite));
                    obj.put("in queue", this.getKidsFromTheList(site.kidsQueue));
                    obj.put("in acceptance queue", this.getKidsFromTheList(site.acceptanceQueue));
                    if (site.kidsOnSite.size() > 0) {
                        JSONArray onSiteArr = new JSONArray();
                    }

                } catch (JSONException e) {}
            }
        }
        return obj;
    }

    public JSONArray getSnapshotHistory() { return this.snapshots; }

    public JSONArray getKidHistory(int ticketNo) {
        JSONArray arr = new JSONArray();
        Kid kid = this.getKid(ticketNo);

        for (PlaySite site: kid.history.keySet()){

            long millis = kid.history.get(site);
            String time = String.format("%02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(millis),
                    TimeUnit.MILLISECONDS.toMinutes(millis),
                    TimeUnit.MILLISECONDS.toSeconds(millis) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
            );

            JSONObject obj = new JSONObject();
            try {
                obj.put("site id", site.id);
                obj.put("site type", site.type);
                obj.put("time spent", time);
            }
            catch (JSONException e) {}
            arr.put(obj);
        }
        return arr;
    }

    public void acceptQueue(int ticketNo) {
        this.checkIfTimeForSnapshot();
        Kid kid = getKid(ticketNo);
        PlaySite site = kid.currentSite;
        site.acceptQueue(kid);

    }

    public void declineQueue(int ticketNo) { this.removeKid(ticketNo); }

    public void removeFromSite(int ticketNo) { this.removeKid(ticketNo); }

    public void close() {
        this.checkIfTimeForSnapshot();
        for (PlaySite site : this.sites) {
            site.kidsOnSite.clear();
            site.kidsQueue.clear();
            site.acceptanceQueue.clear();
        }
        this.snapshotsList.clear();
        this.kids.clear();
    }

    private int issueLicense() { return this.siteLicenseId++; }

    private Kid getKid(int ticketNo) {
        for (Kid kid :  kids) {
            if (kid.ticketNo == ticketNo ) {
                return kid;
            }
        }
        return null;
    }

    private PlaySite getPlaySite(int siteId) {
        for (PlaySite site :  sites) {
            if (site.id == siteId ) {
                return site;
            }
        }
        return null;
    }

    private JSONArray getKidsFromTheList(ArrayList<Kid> kids) {
        JSONArray arr = new JSONArray();
        for (Kid kid : kids) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("name", kid.name);
                obj.put("age", kid.age);
                obj.put("ticket number", kid.ticketNo);
                obj.put("vip", kid.vip);
            }
            catch (JSONException e) {}
            arr.put(obj);
        }
        return arr;
    }

    private void removeKid(int ticketNo) {
        this.checkIfTimeForSnapshot();
        Kid kid = getKid(ticketNo);
        PlaySite site = kid.currentSite;
        site.removeKid(kid);
    }

    private void checkIfTimeForSnapshot() {
        Collections.sort(this.snapshotsList);
        for (long snapshot : this.snapshotsList) {
            if (snapshot <= System.currentTimeMillis() &
                snapshot > this.lastSnapshot) {
                this.lastSnapshot = snapshot;
                JSONObject obj = new JSONObject();
                try {
                    obj.put(new Timestamp(snapshot).toString(), this.getBluePrint());
                } catch (JSONException e) { }
                this.snapshots.put(obj);
            }
        }

    }

}
