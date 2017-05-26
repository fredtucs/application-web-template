package org.wifry.fooddelivery.util.view;
import java.io.Serializable;
/**
 * Created by wtuco on 05/06/2015.
 */
public class SortOption implements Serializable {

    private SortDirection sortDirection;

    private String sortField;

    private Integer rank;

    public SortOption() {
    }

    public SortDirection getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(SortDirection sortDirection) {
        this.sortDirection = sortDirection;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }
}
