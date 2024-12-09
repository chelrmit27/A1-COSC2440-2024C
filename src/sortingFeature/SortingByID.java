package sortingFeature;

import domain.RentalAgreement;

import java.util.Comparator;

/**
 * @author <Tran Tu Tam - s3999159>
 */

public class SortingByID implements Comparator<RentalAgreement> {

    /**
     * Compares two {@link RentalAgreement} objects based on their IDs.
     * <p>
     * The IDs are assumed to contain a numeric component (e.g., "RA001").
     * This method extracts the numeric part of the IDs, ignoring any non-digit characters,
     * and compares them as integers in ascending order.
     * If both IDs are null, they are considered equal. A null ID is considered less than a non-null ID.
     * </p>
     *
     * @param o1 the first {@link RentalAgreement} to compare
     * @param o2 the second {@link RentalAgreement} to compare
     * @return a negative integer, zero, or a positive integer if the numeric component of the first ID
     *         is less than, equal to, or greater than that of the second ID, respectively.
     *         Returns -1 if o1's ID is null, and 1 if o2's ID is null.
     * @throws NumberFormatException if the ID does not contain a valid numeric component.
     */
    @Override
    public int compare(RentalAgreement o1, RentalAgreement o2){
        if (o1.getId() == null && o2.getId() == null) {
            return 0;
        } else if (o1.getId() == null) {
            return -1;
        } else if (o2.getId() == null) {
            return 1;
        }

        int id1 = Integer.parseInt(o1.getId().replaceAll("\\D+", ""));
        int id2 = Integer.parseInt(o2.getId().replaceAll("\\D+", ""));

        return Integer.compare(id1, id2);
    }
}