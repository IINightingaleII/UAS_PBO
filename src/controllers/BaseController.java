/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controllers;


/**
 *
 * @author hp
 */


import java.util.List;

// Interface dasar generic untuk semua controller
public interface BaseController<T> {
    List<T> getAll();
    void insert(T obj);
    void update(T obj);
    void delete(int id);
}


