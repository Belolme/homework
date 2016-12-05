package operation.banker;

import java.util.Random;
import java.util.function.Function;
import java.util.stream.IntStream;

/**
 * banker algorithm
 * <p/>
 * Created by Billin on 2016/11/18.
 */
public class BankerAlgorithm {

    // control data
    private final int tableLength;

    private static final int resourceLength = 4;

    private static final int claimRange = 5;

    private static final int maxResource = 10;

    private Random random;

    private Resource resource;

    private AllocationTable[] tables;

    public BankerAlgorithm(int tableLength) {

        this.tableLength = tableLength;

        random = new Random();

        // init allocation table
        tables = new AllocationTable[tableLength];

        resource = new Resource(new int[]{random.nextInt(maxResource),
                random.nextInt(maxResource),
                random.nextInt(maxResource),
                random.nextInt(maxResource)});

        for (int i = 0; i < tables.length; i++) {
            AllocationTable allocation = new AllocationTable("P" + i, new Resource(new int[]{
                    random.nextInt(claimRange),
                    random.nextInt(claimRange),
                    random.nextInt(claimRange),
                    random.nextInt(claimRange)
            }));


            int[] allocationArray = new int[resourceLength];
            for (int j = 0; j < resourceLength; j++) {
                int bound = allocation.claim.r[j];
                if (bound > 0)
                    allocationArray[j] = random.nextInt(allocation.claim.r[j] + 1);
                else
                    allocationArray[j] = 0;
            }
            allocation.allocation = new Resource(allocationArray);

            tables[i] = allocation;
        }
    }

    public Resource getRequest(int processIndex) {

        AllocationTable allocationTable = tables[processIndex];

        int[] request = new int[resourceLength];
        for (int j = 0; j < resourceLength; j++) {
            int bound = allocationTable.claim.r[j];
            if (bound > 0)
                request[j] = random.nextInt(allocationTable.need(j) + 1);
            else
                request[j] = 0;
        }

        return new Resource(request);
    }

    /*
    public AllocationTable getRequestTable() {
        int processIndex = random.nextInt(tableLength);

        Resource request = new Resource(new int[]{
                random.nextInt(allocationRange),
                random.nextInt(allocationRange),
                random.nextInt(allocationRange),
                random.nextInt(allocationRange)});

        AllocationTable[] allocationTables = new AllocationTable[tableLength];

        AllocationTable allocationTable = allocationTables[processIndex];




    }*/

    public AllocationTable[] getSequence() {
        AllocationTable[] allocationTables = new AllocationTable[tableLength];

        boolean[] check = new boolean[tableLength];

        Resource free = new Resource(resource.r);

        for (int i = 0; i < tableLength; i++) {
            // find process can be allocated
            int canBeAllocate = 0;
            int cannotBeAllocate = 0;
            for (; canBeAllocate < tableLength; canBeAllocate++) {
                if (!check[canBeAllocate] && free.isContain(tables[canBeAllocate].need())) {
                    check[canBeAllocate] = true;
                    break;
                } else {
                    cannotBeAllocate = canBeAllocate;
                }
            }
            if (canBeAllocate == tableLength) {
                System.out.println("unsafe sequence");
                AllocationTable allocationTable = new AllocationTable(tables[cannotBeAllocate].processName, tables[cannotBeAllocate].claim);
                allocationTable.free = free;
                allocationTable.allocation = (Resource) tables[cannotBeAllocate].allocation.clone();
                allocationTable.finish = false;
                allocationTables[i] = allocationTable;
            } else {
                AllocationTable allocationTable = new AllocationTable(tables[canBeAllocate].processName, tables[canBeAllocate].claim);
                allocationTable.free = free;
                allocationTable.allocation = (Resource) tables[canBeAllocate].allocation.clone();
                allocationTable.finish = true;
                free = (Resource) free.clone();
                free.add(allocationTable.allocation);
                allocationTables[i] = allocationTable;
            }
        }

        return allocationTables;
    }

    public boolean allocate(int index, Resource request) {
        Function<Resource, Function<Integer, Boolean>> isLENeed = r -> tableIndex ->
                IntStream.range(0, resourceLength)
                        .allMatch(v -> request.r[v] <= tables[tableIndex].need(v));

        Function<Resource, Function<Integer, Boolean>> isLEAvailable = r -> tableIndex ->
                IntStream.range(0, resourceLength)
                        .allMatch(v -> request.r[v] <= resource.r[v]);

        if (!isLENeed.apply(request).apply(index) || !isLEAvailable.apply(request).apply(index)) {
            return false;
        }

        resource.minus(request);
        tables[index].allocation.add(request);
        return true;
    }

    public void unAllocate(int index, Resource r) {
        resource.add(r);
        tables[index].allocation.minus(r);
    }

    public Resource getAvailable() {
        return (Resource) resource.clone();
    }

    public AllocationTable[] getTables() {
        return tables.clone();
    }

    /*
    public static void main(String[] args) {
        BankerAlgorithm bankerAlgorithm = new BankerAlgorithm();
        System.out.println(Arrays.toString(bankerAlgorithm.tables));
        System.out.println(bankerAlgorithm.resource);
        System.out.println(Arrays.toString(bankerAlgorithm.getSequence()));
    }
    */
}
