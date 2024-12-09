package sortingFeature;

import domain.RentalAgreement;

import java.util.Comparator;

/**
 * @author <Tran Tu Tam - s3999159>
 */

public class SortingByRentingFee implements Comparator<RentalAgreement> {

    /**
     * Compares two {@link RentalAgreement} objects based on their renting fees.
     * <p>
     * The comparison is performed using {@link Double#compare(double, double)}. If the renting fee of one or both
     * agreements is null, agreements with null renting fees are considered less than those with non-null renting fees.
     * </p>
     *
     * @param o1 the first {@link RentalAgreement} to compare
     * @param o2 the second {@link RentalAgreement} to compare
     * @return a negative integer, zero, or a positive integer if the renting fee in the first agreement is
     *         less than, equal to, or greater than that of the second agreement, respectively.
     *         Returns -1 if o1's renting fee is null, and 1 if o2's renting fee is null.
     */
    @Override
    public int compare(RentalAgreement o1, RentalAgreement o2) {
        if (o1.getRentingFee() == null && o2.getRentingFee() == null) {
            return 0;
        } else if (o1.getRentingFee() == null) {
            return -1;
        } else if (o2.getRentingFee() == null) {
            return 1;
        }

        return Double.compare(o1.getRentingFee(), o2.getRentingFee());
    }
}
