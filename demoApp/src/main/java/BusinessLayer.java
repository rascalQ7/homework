import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

public class BusinessLayer {

    public static void main(String[] args) throws InterruptedException {

        // Initiate your brand new Play ground
        PlayGround hadesPark = new PlayGround();
        System.out.println("Play ground initiated.");
        System.out.println("");

        // Get the predefined site types
        String[] playSiteTypes = hadesPark.getSiteTypes();

        // What types we got?
        System.out.println("Available site types:");
        for (String type : playSiteTypes) {
            System.out.println(type);
        }
        System.out.println("");

        // Create some sites for the kids.
        // Arguments:
        //      capacity (some are already predefined due to science); int
        //      one of the types returned from getSiteTypes() method; String
        hadesPark.createSite(4, playSiteTypes[3]);
        hadesPark.createSite(3, playSiteTypes[2]);
        hadesPark.createSite(9, playSiteTypes[1]); // capacity = 1
        hadesPark.createSite(9, playSiteTypes[2]);

        // Need a blueprint of the play ground?
        // It also provides current utilization status in percentages
        System.out.println("First blueprint after all sites creation:");
        System.out.println(hadesPark.getBluePrint());
        System.out.println("");

        // It is possible to remove site when play ground is empty
        hadesPark.removeSite(3);
        System.out.println("Blueprint after site's removal:");
        System.out.println(hadesPark.getBluePrint());
        System.out.println("");

        // Let's invite some kids to the Play ground and sell some tickets
        // GDPR? meh, provide arguments and let's start tracking those guys
        // Arguments:
        //      name; String
        //      age; int
        //      ticket type - vip?; boolean
        //      ticket number; int
        int ticketSeq = 0;
        hadesPark.newKid("Karolis", 15, false, ticketSeq++);
        hadesPark.newKid("Mantas", 12, false, ticketSeq++);
        hadesPark.newKid("Igoris", 8, false, ticketSeq++);
        hadesPark.newKid("Valdas", 7, false, ticketSeq++);
        hadesPark.newKid("Lina", 10, false, ticketSeq++);
        hadesPark.newKid("Beata", 10, false, ticketSeq++);
        hadesPark.newKid("Vilius", 15, false, ticketSeq++);
        hadesPark.newKid("Zita", 12, false, ticketSeq++);
        hadesPark.newKid("Kilimandzaras", 8, false, ticketSeq++);
        hadesPark.newKid("Nijole", 7, false, ticketSeq++);
        hadesPark.newKid("Joe", 10, true, ticketSeq++);
        hadesPark.newKid("Darius", 10, true, ticketSeq++);

        // Check all visitors
        System.out.println("Invited visitors:");
        System.out.println(hadesPark.getKids());
        System.out.println("");

        // Play ground is settled, let the fun begin
        // Kid can mark his ticket and go to the selected site
        // Arguments:
        // ticket number; int
        // play site id number; int
        hadesPark.addKidToTheSite(0, 0);
        hadesPark.addKidToTheSite(1, 0);
        hadesPark.addKidToTheSite(2, 2);
        hadesPark.addKidToTheSite(3, 2);
        hadesPark.addKidToTheSite(4, 2);

        // Check utilization status
        System.out.println("Utilization check:");
        System.out.println(hadesPark.getBluePrint());
        System.out.println("");

        // Slide is fully booked (1/1) and all other kids are in acceptance queue
        // lets inspect what the situation there.
        // Arguments:
        //      site id; int
        System.out.println("Inspect on slide site and queues:");
        System.out.println(hadesPark.getSiteStatus(2));
        System.out.println("");

        // Now kids should respond if they want to get into queue or decline
        // Arguments:
        //      ticket number; int
        hadesPark.acceptQueue(3);
        hadesPark.declineQueue(4);
        System.out.println("Inspect on slide site after visitors decisions on queue acceptance:");
        System.out.println(hadesPark.getSiteStatus(2));
        System.out.println("");

        // If kid who is on site will be removed another kid from the queue will be put on site
        // Arguments:
        //      ticket number; int
        hadesPark.removeFromSite(2);
        System.out.println("Inspect on slide site after kid left the site:");
        System.out.println(hadesPark.getSiteStatus(2));
        System.out.println("");

        // If kid who is on site will try his luck on another site he will be automatically
        // removed from current queues or site
        hadesPark.addKidToTheSite(3, 1);
        System.out.println("Inspect on sites after kid decision to go to another site:");
        System.out.println(hadesPark.getSiteStatus(2));
        System.out.println(hadesPark.getSiteStatus(1));
        System.out.println("");

        // Okay, let's try vip function
        // First step: utilize slide (example)
        hadesPark.addKidToTheSite(0, 2);
        System.out.println("Slide utilized, queues are empty:");
        System.out.println(hadesPark.getSiteStatus(2));
        System.out.println("");
        // Second step: add all non vip kids to the queue
        hadesPark.addKidToTheSite(1, 2);
        hadesPark.addKidToTheSite(2, 2);
        hadesPark.addKidToTheSite(3, 2);
        hadesPark.addKidToTheSite(4, 2);
        hadesPark.addKidToTheSite(5, 2);
        hadesPark.addKidToTheSite(6, 2);
        hadesPark.addKidToTheSite(7, 2);
        hadesPark.addKidToTheSite(8, 2);
        hadesPark.addKidToTheSite(9, 2);
        hadesPark.acceptQueue(1);
        hadesPark.acceptQueue(2);
        hadesPark.acceptQueue(3);
        hadesPark.acceptQueue(4);
        hadesPark.acceptQueue(5);
        hadesPark.acceptQueue(6);
        hadesPark.acceptQueue(7);
        hadesPark.acceptQueue(8);
        hadesPark.acceptQueue(9);
        System.out.println("Slide utilized, queue are filled with none vip kids:");
        System.out.println(hadesPark.getSiteStatus(2));
        System.out.println("");
        // Third step let's invite vip kids Joe and Darius.
        // By the rule first vip will be put to the first place in the line
        // Second vip will be put in the fifth position
        hadesPark.addKidToTheSite(10, 2);
        hadesPark.addKidToTheSite(11, 2);
        hadesPark.acceptQueue(10);
        hadesPark.acceptQueue(11);
        System.out.println("Slide utilized, vips got into the queue:");
        System.out.println(hadesPark.getSiteStatus(2));
        System.out.println("");
        // Last step: remove kid from the slide and check if vip Joe got into his place
        hadesPark.removeFromSite(0);
        System.out.println("Vip got into the slide:");
        System.out.println(hadesPark.getSiteStatus(2));
        System.out.println("");

        // Enough for today, let's close the gates
        hadesPark.close();

        System.out.println("Park is closed all queues are now empty:");
        System.out.println(hadesPark.getBluePrint());
        System.out.println("");

        // If mr. Stakeholder would like to look for some ideas on earning money
        // Here are some reporting:
        // Total visitors: just count the tickets (this is not a play ground's domain):
        System.out.println("Total visitors:");
        System.out.println(ticketSeq);
        System.out.println("");
        ticketSeq = 0;

        // For the sake of the reporting let's get back kids to the field
        hadesPark.newKid("Karolis", 15, false, ticketSeq++);
        hadesPark.newKid("Mantas", 12, false, ticketSeq++);
        hadesPark.newKid("Igoris", 8, false, ticketSeq++);
        hadesPark.newKid("Valdas", 7, false, ticketSeq++);
        hadesPark.newKid("Lina", 10, false, ticketSeq++);

        // Let's add some timestamp for the snapshots
        hadesPark.createSnapshot(new Timestamp(System.currentTimeMillis() + 1000));
        hadesPark.createSnapshot(new Timestamp(System.currentTimeMillis() + 2000));
        hadesPark.createSnapshot(new Timestamp(System.currentTimeMillis() + 3000));
        hadesPark.createSnapshot(new Timestamp(System.currentTimeMillis() + 4000));

        hadesPark.addKidToTheSite(0, 0);
        hadesPark.addKidToTheSite(1, 0);
        hadesPark.addKidToTheSite(2, 0);
        hadesPark.addKidToTheSite(3, 2);
        hadesPark.addKidToTheSite(4, 2);

        TimeUnit.SECONDS.sleep(1);

        hadesPark.addKidToTheSite(0, 0);
        hadesPark.addKidToTheSite(1, 0);
        hadesPark.addKidToTheSite(2, 1);
        hadesPark.addKidToTheSite(3, 1);
        hadesPark.addKidToTheSite(4, 2);
        // Let's check the reporting
        System.out.println("Snapshots history after one second:");
        System.out.println(hadesPark.getSnapshotHistory());
        System.out.println("");
        // Timestamp with a utilization snapshot saved

        TimeUnit.SECONDS.sleep(2);

        hadesPark.addKidToTheSite(0, 0);
        hadesPark.addKidToTheSite(1, 0);
        hadesPark.addKidToTheSite(2, 0);
        hadesPark.addKidToTheSite(3, 0);
        hadesPark.addKidToTheSite(4, 0);
        System.out.println("Snapshots history after three seconds:");
        System.out.println(hadesPark.getSnapshotHistory());
        System.out.println("");
        // Two timestamps with a identical utilization snapshots added

        TimeUnit.SECONDS.sleep(1);

        System.out.println("Snapshots history after four seconds:");
        System.out.println(hadesPark.getSnapshotHistory());
        System.out.println("");

        // Also we can check how kid's statistics
        // ticket number 2 was on site zero for a one second,
        // then went to the site one and stayed for 2 two seconds,
        // finally he came back to the site zero,
        // thus total should be 2 seconds in site zero
        // two seconds in site one.
        System.out.println("Kid with ticket number two was on these sites:");
        hadesPark.removeFromSite(2);
        System.out.println(hadesPark.getKidHistory(2));
        System.out.println("");

    }
}
