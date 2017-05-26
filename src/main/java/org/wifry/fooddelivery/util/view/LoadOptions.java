package org.wifry.fooddelivery.util.view;
import java.io.Serializable;
import java.util.List;
/**
 * Created by wtuco on 05/06/2015.
 */
public class LoadOptions implements Serializable {

    private List<FilterOption> filters;

    private List<SortOption> sorts;

    private Integer from;

    private Integer to;

    public LoadOptions() {
    }

    public List<FilterOption> getFilters() {
        return filters;
    }

    public void setFilters(List<FilterOption> filters) {
        this.filters = filters;
    }

    public List<SortOption> getSorts() {
        return sorts;
    }

    public void setSorts(List<SortOption> sorts) {
        this.sorts = sorts;
    }

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public Integer getTo() {
        return to;
    }

    public void setTo(Integer to) {
        this.to = to;
    }
}
