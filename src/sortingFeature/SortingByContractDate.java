package sortingFeature;

import domain.RentalAgreement;

import java.util.Comparator;

/**
 * @author <Tran Tu Tam - s3999159>
 */

public class SortingByContractDate implements Comparator<RentalAgreement> {

    /**
     * Compares two {@link RentalAgreement} objects based on their contract dates.
     * <p>
     * This method handles null contract dates by treating them as less than non-null dates.
     * If both contract dates are null, the comparison returns 0 (equal).
     * Otherwise, it compares the non-null dates in ascending order.
     * </p>
     *
     * @param o1 the first {@link RentalAgreement} to compare
     * @param o2 the second {@link RentalAgreement} to compare
     * @return a negative integer, zero, or a positive integer if the first contract date
     *         is earlier than, equal to, or later than the second contract date, respectively.
     *         Returns -1 if o1's contract date is null, and 1 if o2's contract date is null.
     */
    @Override
    public int compare(RentalAgreement o1, RentalAgreement o2) {
        if (o1.getContractDate() == null && o2.getContractDate() == null) {
            return 0;
        } else if (o1.getContractDate() == null) {
            return -1;
        } else if (o2.getContractDate() == null) {
            return 1;
        }

        return o1.getContractDate().compareTo(o2.getContractDate());
    }
}

