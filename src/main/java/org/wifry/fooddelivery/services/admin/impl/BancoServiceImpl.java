package org.wifry.fooddelivery.services.admin.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wifry.fooddelivery.model.Banco;
import org.wifry.fooddelivery.repository.admin.BancoRepository;
import org.wifry.fooddelivery.services.admin.BancoService;

import java.util.List;

@Service("bancoService")
public class BancoServiceImpl implements BancoService {

    @Autowired
    private BancoRepository bancoRepository;

    @Override
    public Banco getByID(Long id) {
        return bancoRepository.getByID(id);
    }

    @Override
    public List<Banco> listAll() {
        return bancoRepository.listBancoAll();
    }

    @Override
    public List<Banco> list() {
        return bancoRepository.listBanco();
    }

    @Override
    public List<Banco> find(String valor) {
        return bancoRepository.findBanco(valor);
    }

    @Override
    public void save(Banco entity) {
        bancoRepository.save(entity);
    }

    @Override
    public void delete(Banco entity) {
        bancoRepository.delete(entity);
    }

    @Override
    public void updateState(Banco entity) {
        bancoRepository.updateState(entity);
    }


}
