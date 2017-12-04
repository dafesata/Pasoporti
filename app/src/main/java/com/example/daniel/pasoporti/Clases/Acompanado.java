package com.example.daniel.pasoporti.Clases;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Daniel on 9/27/2017.
 */

public class Acompanado {
    private String Nombre, Email,Direccion, TipoId, UID,Id,Telefono,EPS,Parentesco;

    public Acompanado() {
    }

    public Acompanado(String nombre,String UID){
        Nombre=nombre;
        this.UID=UID;
    }

    public Acompanado(String nombre, String email, String direccion, String tipoId, String UID, String id, String telefono, String EPS,String parentesco) {
        Nombre = nombre;
        Email = email;
        Direccion = direccion;
        TipoId = tipoId;
        this.UID = UID;
        Id = id;
        Telefono = telefono;
        this.EPS = EPS;
        Parentesco=parentesco;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("nombre", Nombre);
        result.put("email", Email);
        result.put("direccion", Direccion);
        result.put("tipoId", TipoId);
        result.put("uid", UID);
        result.put("id", Id);
        result.put("telefono", Telefono);
        result.put("eps", EPS);
        result.put("parentesco",Parentesco);
        return result;
    }

    public String toString(){
        return Nombre;
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

    public String getEPS() {
        return EPS;
    }

    public void setEPS(String EPS) {
        this.EPS = EPS;
    }

    public String getParentesco() {
        return Parentesco;
    }

    public void setParentesco(String parentesco) {
        Parentesco = parentesco;
    }

    public Acompanado getConvertedObject(DataSnapshot snapshot) {
        String Nombre= (String)snapshot.child("nombre").getValue();
        String TipoId= (String)snapshot.child("tipoId").getValue();
        String Identificacion= (String)snapshot.child("id").getValue();
        String Direccion= (String)snapshot.child("direccion").getValue();
        String Telefono= (String)snapshot.child("telefono").getValue();
        String Email= (String)snapshot.child("email").getValue();
        String EPS= (String)snapshot.child("eps").getValue();
        String UID= (String)snapshot.child("uid").getValue();
        String Parentesco=(String)snapshot.child("parentesco").getValue();
        return new Acompanado(Nombre,Email,Direccion,TipoId,UID,Identificacion,Telefono,EPS,Parentesco);
    }
}
