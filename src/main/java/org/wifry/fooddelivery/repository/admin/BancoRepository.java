package org.wifry.fooddelivery.repository.admin;


import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.wifry.fooddelivery.base.BaseRepository;
import org.wifry.fooddelivery.model.Banco;

import java.util.List;

/**
 * Created by wtuco on 10/08/2015.
 * <p>
 * BancoDao
 */
@Repository
public interface BancoRepository extends BaseRepository<Banco, Long> {

    @Query("SELECT b FROM Banco b WHERE b.idBanco = ?1")
    Banco getByID(Long id);

    @Query("SELECT b FROM Banco b WHERE b.estado = 1 order by b.nombre")
    List<Banco> listBanco();

    @Query("SELECT b FROM Banco b order by b.nombre")
    List<Banco> listBancoAll();

    @Query("SELECT b FROM Banco b WHERE UPPER(b.nombre) LIKE UPPER(CONCAT('%', ?1, '%') ) ORDER BY b.nombre")
    List<Banco> findBanco(String valor);
}
