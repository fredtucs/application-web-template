package org.wifry.fooddelivery.util.view;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.wifry.fooddelivery.services.BaseService;
import org.wifry.fooddelivery.util.ObjectUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by wtuco on 04/06/2015.
 */
public class DataTableLazyList<E> extends LazyDataModel<E> implements Serializable {

    private static final long serialVersionUID = 1L;

    private SearchResult<E> searchResult;

    // Data Source for binding data to the DataTable
    private List<E> datasource;
    // Selected Page size in the DataTable
    private int pageSize;
    // Current row index number
    private int rowIndex;
    // Total row number
    private int rowCount;
    // Data Access Service for create read update delete operations
    private BaseService<E> service;

    private Map<String, Object> filtersCustom;

    private String idEntity;

    /**
     * @param service
     */
    public DataTableLazyList(BaseService<E> service, String idEntity) {
        this.searchResult = new SearchResult<>();
        this.service = service;
        this.idEntity = idEntity;
    }

    public DataTableLazyList(BaseService<E> service, Map<String, Object> filtersCustom, String idEntity) {
        this.filtersCustom = filtersCustom;
        this.searchResult = new SearchResult<>();
        this.service = service;
        this.idEntity = idEntity;
    }

    /**
     * Lazy loading entity list with sorting ability
     *
     * @param first     Inicial row page
     * @param pageSize  size for page result
     * @param sortField sort field in page
     * @param sortOrder order sort of the field in page
     * @param filters   filter result for page
     * @return List<E> result select for page
     */
    @Override
    public List<E> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        if (filtersCustom != null && !filtersCustom.isEmpty())
            filters.putAll(filtersCustom);
        LoadOptions loadOptions = convert(first, pageSize, sortField, sortOrder, filters);
        this.searchResult = service.listPaginate(loadOptions);
        this.datasource = searchResult.getResult();
        setRowCount(searchResult.getTotalCount());
        return datasource;
    }

    private LoadOptions convert(int first, int pageSize, String sortField, SortOrder sortOrder,
                                Map<String, Object> filters) {
        List<SortMeta> sorts = new ArrayList<>();
        if (!StringUtils.isBlank(sortField)) {
            SortMeta sort = new SortMeta();
            sort.setSortField(sortField);
            sort.setSortOrder(sortOrder);
            sorts.add(sort);
        }
        return convert(first, pageSize, sorts, filters);
    }

    private LoadOptions convert(int first, int pageSize, List<SortMeta> multiSortMeta, Map<String, Object> filters) {
        LoadOptions loadOptions = new LoadOptions();
        loadOptions.setFrom(first);
        loadOptions.setTo(pageSize);

        List<SortOption> sorts = new ArrayList<>();
        for (int i = 0; i < multiSortMeta.size(); ++i) {
            SortMeta sortMeta = multiSortMeta.get(i);
            if (sortMeta != null) {
                String sortField = sortMeta.getSortField();
                if (!StringUtils.isBlank(sortField)) {
                    SortOption sort = new SortOption();
                    sort.setSortField(sortField);
                    sort.setSortDirection(convert(sortMeta.getSortOrder()));
                    sort.setRank(i);
                    sorts.add(sort);
                }
            }
        }
        loadOptions.setSorts(sorts);

        List<String> filterFields = new ArrayList<>(filters.keySet());
        List<FilterOption> filterOptions = new ArrayList<>();
        for (String filterField : filterFields) {
            if (!ObjectUtils.isEmpty(filterField)) {
                Object filterValue = filters.get(filterField);
                FilterOption filterOption = new FilterOption();
                filterOption.setField(filterField);
                filterOption.setValue(filterValue);
                filterOptions.add(filterOption);
            }
        }
        loadOptions.setFilters(filterOptions);

        return loadOptions;
    }

    private SortDirection convert(SortOrder sortOrder) {
        switch (sortOrder) {
            case ASCENDING:
                return SortDirection.ASCENDING;
            case DESCENDING:
                return SortDirection.DESCENDING;
            case UNSORTED:
                return SortDirection.UNSORTED;
            default:
                throw new IllegalArgumentException("Unknown sort order");
        }
    }

    /**
     * Checks if the row is available
     *
     * @return boolean
     */
    @Override
    public boolean isRowAvailable() {
        if (datasource == null)
            return false;
        int index = rowIndex % pageSize;
        return index >= 0 && index < datasource.size();
    }

    /**
     * Gets the entity object's primary key
     *
     * @param entity
     * @return Object
     */
    @Override
    public Object getRowKey(E entity) {
        Long id = null;
        try {
            id = (Long) FieldUtils.readField(entity, idEntity);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return id;
    }

    /**
     * Returns the entity object at the specified position in datasource.
     *
     * @return
     */
    @Override
    public E getRowData() {
        if (datasource == null)
            return null;
        int index = rowIndex % pageSize;
        if (index > datasource.size()) {
            return null;
        }
        return datasource.get(index);
    }

    /**
     * Returns the entity object that has the row key.
     *
     * @param rowKey
     * @return
     */
    @Override
    public E getRowData(String rowKey) {
        try {
            if (datasource == null)
                return null;
            for (E entity : datasource) {
                Long id = (Long) FieldUtils.readField(entity, idEntity);
                if (id.toString().equals(rowKey))
                    return entity;
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

	/*
     * ===== Getters and Setters of LazyEDataModel fields
	 */

    /**
     * @param pageSize
     */
    @Override
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * Returns page size
     *
     * @return int
     */
    @Override
    public int getPageSize() {
        return pageSize;
    }

    /**
     * Returns current row index
     *
     * @return int
     */
    @Override
    public int getRowIndex() {
        return this.rowIndex;
    }

    /**
     * Sets row index
     *
     * @param rowIndex
     */
    @Override
    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    /**
     * Sets row count
     *
     * @param rowCount
     */
    @Override
    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    /**
     * Returns row count
     *
     * @return int
     */
    @Override
    public int getRowCount() {
        if (this.rowCount == 0) {
            return this.searchResult.getTotalCount();
        }
        return this.rowCount;
    }

    /**
     * Sets wrapped data
     *
     * @param list
     */
    @SuppressWarnings("unchecked")
    @Override
    public void setWrappedData(Object list) {
        this.datasource = (List<E>) list;
    }

    /**
     * Returns wrapped data
     *
     * @return
     */
    @Override
    public Object getWrappedData() {
        return datasource;
    }

}
