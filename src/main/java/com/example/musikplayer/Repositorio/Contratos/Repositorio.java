package com.example.musikplayer.Repositorio.Contratos;

import java.util.List;

public interface Repositorio<T> {
    List<T> GetAll();
    void GetId(int id);
    void insert();
    boolean update(int id);
    boolean delete(int id);
}
