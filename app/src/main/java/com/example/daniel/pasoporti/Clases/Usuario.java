package com.example.daniel.pasoporti.Clases;

import com.google.firebase.database.Exclude;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Daniel on 9/24/2017.
 */

public class Usuario {
    private String Nombre, Email,Rol,Direccion, TipoId,Ciudad, UID,Id,Telefono;

    public Usuario() {
    }

    public Usuario(String nombre, String email, String rol, String direccion, String tipoId, String ciudad, String UID, String id, String telefono) {
        Nombre = nombre;
        Email = email;
        Rol = rol;
        Direccion = direccion;
        TipoId = tipoId;
        Ciudad = ciudad;
        this.UID = UID;
        Id = id;
        Telefono = telefono;
    }

    public Usuario(String nombre, String email, String direccion, String tipoId, String ciudad, String UID, String id, String telefono) {
        Nombre = nombre;
        Email = email;
        Direccion = direccion;
        TipoId = tipoId;
        Ciudad = ciudad;
        this.UID = UID;
        Id = id;
        Telefono = telefono;
        Rol="Cliente";
    }


    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("nombre", Nombre);
        result.put("email", Email);
        result.put("direccion", Direccion);
        result.put("tipoId", TipoId);
        result.put("ciudad", Ciudad);
        result.put("uid", UID);
        result.put("id", Id);
        result.put("telefono", Telefono);
        result.put("rol", Rol);


        return result;
    }



    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getRol() {
        return Rol;
    }

    public void setRol(String rol) {
        Rol = rol;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String direccion) {
        Direccion = direccion;
    }

    public String getTipoId() {
        return TipoId;
    }

    public void setTipoId(String tipoId) {
        TipoId = tipoId;
    }

    public String getCiudad() {
        return Ciudad;
    }

    public void setCiudad(String ciudad) {
        Ciudad = ciudad;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        Telefono = telefono;
    }
}
