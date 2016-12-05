package operation.banker;

import java.util.stream.IntStream;

/**
 * resource allocation table
 * <p/>
 * Created by Billin on 2016/11/18.
 */
public class AllocationTable {

    public static final String PROCESS = "Process";

    public static final String ALLOCATION = "Allocation";

    public static final String NEED = "Need";

    public static final String AVAILABLE = "Available";

    public static final String FREE = "Free";

    public static final String FINISH = "Finish";

    String processName;

    Resource claim;

    Resource allocation;

    Resource free;

    boolean finish;

    public AllocationTable(String processName, Resource claim) {
        this.processName = processName;
        this.claim = (Resource) claim.clone();
    }

    int need(int index) {
        return claim.r[index] - allocation.r[index];
    }

    Resource need() {
        return new Resource(IntStream.range(0, claim.r.length)
                .map(i -> claim.r[i] - allocation.r[i])
                .toArray());
    }

    @Override
    public String toString() {
        return "AllocationTable{" +
                "processName='" + processName + '\'' +
                ", claim=" + claim +
                ", allocation=" + allocation +
                ", free=" + free +
                ", finish=" + finish +
                '}';
    }
}
