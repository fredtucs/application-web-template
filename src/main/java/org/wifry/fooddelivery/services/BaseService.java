package org.wifry.fooddelivery.services;

import org.wifry.fooddelivery.exceptions.ChangeStatusException;
import org.wifry.fooddelivery.exceptions.DeleteEntityException;
import org.wifry.fooddelivery.exceptions.NullPeriodoException;
import org.wifry.fooddelivery.util.view.LoadOptions;
import org.wifry.fooddelivery.util.view.SearchResult;
import org.wifry.fooddelivery.exceptions.SaveEntityException;

import java.util.List;

/**
 * Created by wtuco on 04/06/2015.
 * <p>
 * BaseService
 */
public interface BaseService<E> {

    default E getByID(Long id) {
        throw new UnsupportedOperationException("Find by Id is not implemented.");
    }

    List<E> listAll();

    List<E> list();

    default List<E> find(String valor) {
        throw new UnsupportedOperationException("Find by value is not implemented.");
    }

    default List<E> listByIdParent(Long idParent) {
        throw new UnsupportedOperationException("List by IdParent is not implemented.");
    }

    default SearchResult<E> listPaginate(LoadOptions loadOptions) {
        throw new UnsupportedOperationException("List Paginate is not implemented.");
    }

    void save(E entity) throws SaveEntityException, NullPeriodoException;

    void delete(E entity) throws DeleteEntityException;

    default void updateState(E entity) throws ChangeStatusException {
        throw new UnsupportedOperationException("updateState is not implemented.");
    }

}
