package sortingFeature;

import domain.RentalAgreement;
import domain.LeasePeriod;

import java.util.Comparator;
import java.util.EnumMap;
import java.util.Map;

/**
 * @author <Tran Tu Tam - s3999159>
 */

public class SortingByLeasePeriod implements Comparator<RentalAgreement> {

    private static final Map<LeasePeriod, Integer> leasePeriodPriority;

    static {
        leasePeriodPriority = new EnumMap<>(LeasePeriod.class);
        leasePeriodPriority.put(LeasePeriod.DAILY, 1);
        leasePeriodPriority.put(LeasePeriod.WEEKLY, 2);
        leasePeriodPriority.put(LeasePeriod.FORTNIGHTLY, 3);
        leasePeriodPriority.put(LeasePeriod.MONTHLY, 4);
        leasePeriodPriority.put(LeasePeriod.QUARTERLY, 5);
        leasePeriodPriority.put(LeasePeriod.BIANNUALLY, 6);
        leasePeriodPriority.put(LeasePeriod.ANNUALLY, 7);
    }

    /**
     * Compares two {@link RentalAgreement} objects based on their lease periods.
     * <p>
     * The comparison uses a predefined priority mapping (`leasePeriodPriority`) that assigns an integer priority
     * to each {@link LeasePeriod} value. Lower priority values indicate higher precedence. If a lease period is
     * null, it is considered less than a non-null lease period.
     * </p>
     *
     * @param o1 the first {@link RentalAgreement} to compare
     * @param o2 the second {@link RentalAgreement} to compare
     * @return a negative integer, zero, or a positive integer if the priority of the lease period in the first
     *         agreement is less than, equal to, or greater than that of the second agreement, respectively.
     *         Returns -1 if o1's lease period is null, and 1 if o2's lease period is null.
     *         Defaults to {@link Integer#MAX_VALUE} priority if the lease period is not found in the priority map.
     */
    @Override
    public int compare(RentalAgreement o1, RentalAgreement o2) {
        LeasePeriod period1 = o1.getLeasePeriod();
        LeasePeriod period2 = o2.getLeasePeriod();

        if (period1 == null && period2 == null) {
            return 0;
        } else if (period1 == null) {
            return -1;
        } else if (period2 == null) {
            return 1;
        }

        int priority1 = leasePeriodPriority.getOrDefault(period1, Integer.MAX_VALUE);
        int priority2 = leasePeriodPriority.getOrDefault(period2, Integer.MAX_VALUE);

        return Integer.compare(priority1, priority2);
    }
}
