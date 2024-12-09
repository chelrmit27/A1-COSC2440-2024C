package sortingFeature;

import domain.RentalAgreement;
import domain.RentalStatus;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <Tran Tu Tam - s3999159>
 */

public class SortingByStatus implements Comparator<RentalAgreement> {

    private static final Map<RentalStatus, Integer> statusOrderMap = new HashMap<>();

    static {
        statusOrderMap.put(RentalStatus.ACTIVE, 1);
        statusOrderMap.put(RentalStatus.NEW, 2);
        statusOrderMap.put(RentalStatus.COMPLETED, 3);
    }

    /**
     * Compares two {@link RentalAgreement} objects based on their rental statuses.
     * <p>
     * The comparison is performed using a predefined order stored in {@code statusOrderMap},
     * which maps {@link RentalStatus} values to integer priorities. Agreements with null statuses
     * are considered less than those with non-null statuses. If a status is not found in the map,
     * it is assigned the maximum possible priority.
     * </p>
     *
     * @param o1 the first {@link RentalAgreement} to compare
     * @param o2 the second {@link RentalAgreement} to compare
     * @return a negative integer, zero, or a positive integer if the priority of the status in the first agreement
     *         is less than, equal to, or greater than that of the second agreement, respectively.
     *         Returns -1 if o1's status is null, and 1 if o2's status is null.
     */
    @Override
    public int compare(RentalAgreement o1, RentalAgreement o2) {
        if (o1.getStatus() == null && o2.getStatus() == null) return 0;
        if (o1.getStatus() == null) return -1;
        if (o2.getStatus() == null) return 1;

        int order1 = statusOrderMap.getOrDefault(o1.getStatus(), Integer.MAX_VALUE);
        int order2 = statusOrderMap.getOrDefault(o2.getStatus(), Integer.MAX_VALUE);

        return Integer.compare(order1, order2);
    }
}
