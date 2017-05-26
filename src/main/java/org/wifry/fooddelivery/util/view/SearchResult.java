package org.wifry.fooddelivery.util.view;
import java.io.Serializable;
import java.util.List;
/**
 * Created by wtuco on 04/06/2015.
 */
public class SearchResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    protected List<T> result;
    protected int totalCount = -1;

    /**
     * The results of the search.
     */
    public List<T> getResult() {
        return this.result;
    }

    /**
     * The results of the search.
     */
    public void setResult(List<T> results) {
        this.result = results;
    }

    /**
     * The total number of results that would have been returned if no
     * maxResults had been specified. (-1 means unspecified.)
     */
    public int getTotalCount() {
        return this.totalCount;
    }

    /**
     * The total number of results that would have been returned if no
     * maxResults had been specified. (-1 means unspecified.)
     */
    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
