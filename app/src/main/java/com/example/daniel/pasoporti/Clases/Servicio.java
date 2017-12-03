package com.example.daniel.pasoporti.Clases;

import android.support.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Exclude;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Daniel on 9/29/2017.
 */

public class Servicio {
    String UID,Estado,TipoServicio,Ciudad,DirRecogida,DirLlevar,DirRegreso,Observaciones,Informe;
    String CAcompanante,CConductor,CVehiculo;
    Long Fecha,Id;


    public Servicio() {
    }
    //Fecha Long
    public Servicio(String UID, String estado, String tipoServicio, String ciudad, String dirRecogida, String dirLlevar, String dirRegreso, String observaciones, Long fecha,Long id,String acompanante,String conductor,String vehiculo) {
        this.UID = UID;
        Estado = estado;
        TipoServicio = tipoServicio;
        Ciudad = ciudad;
        DirRecogida = dirRecogida;
        DirLlevar = dirLlevar;
        DirRegreso = dirRegreso;
        Observaciones = observaciones;
        Fecha = fecha;
        Id=id;
        CAcompanante=acompanante;
        CConductor=conductor;
        CVehiculo=vehiculo;
    }

    public Servicio(String UID, String estado, String tipoServicio, String ciudad, String dirRecogida, String dirLlevar, String dirRegreso, String observaciones, Long fecha,Long id) {
        this.UID = UID;
        Estado = estado;
        TipoServicio = tipoServicio;
        Ciudad = ciudad;
        DirRecogida = dirRecogida;
        DirLlevar = dirLlevar;
        DirRegreso = dirRegreso;
        Observaciones = observaciones;
        Fecha = fecha;
        Id=id;
        CAcompanante="0";
        CConductor="0";
        CVehiculo="0";
    }
    //Fecha String

    public Servicio(String UID, String estado, String tipoServicio, String ciudad, String dirRecogida, String dirLlevar, String dirRegreso, String observaciones,String fecha,String hora,Long id,String acompanante,String conductor,String vehiculo) {
        this.UID = UID;
        Estado = estado;
        TipoServicio = tipoServicio;
        Ciudad = ciudad;
        DirRecogida = dirRecogida;
        DirLlevar = dirLlevar;
        DirRegreso = dirRegreso;
        Observaciones = observaciones;
        Fecha=parseDate(fecha,hora);
        Id=id;
        CAcompanante=acompanante;
        CConductor=conductor;
        CVehiculo=vehiculo;

    }

    public Servicio(String UID, String estado, String tipoServicio, String ciudad, String dirRecogida, String dirLlevar, String dirRegreso, String observaciones,String fecha,String hora,Long id) {
        this.UID = UID;
        Estado = estado;
        TipoServicio = tipoServicio;
        Ciudad = ciudad;
        DirRecogida = dirRecogida;
        DirLlevar = dirLlevar;
        DirRegreso = dirRegreso;
        Observaciones = observaciones;
        Fecha=parseDate(fecha,hora);
        Id=id;
        CAcompanante="0";
        CConductor="0";
        CVehiculo="0";

    }

    @Exclude
    public Map<String,Object> toMap(){
        HashMap<String ,Object> result= new HashMap<>();
        result.put("uid",UID);
        result.put("estado",Estado);
        result.put("tipoServicio",TipoServicio);
        result.put("ciudad",Ciudad);
        result.put("dirRecogida",DirRecogida);
        result.put("dirLlevar",DirLlevar);
        result.put("dirRegreso",DirRegreso);
        result.put("observaciones",Observaciones);
        result.put("fecha",Fecha);
        result.put("id",Id);
        result.put("cacompanante",CAcompanante);
        result.put("cconductor",CConductor);
        result.put("cvehiculo",CVehiculo);
        return result;
    }

    @Nullable
    private static Long parseDate(String Fecha, String Hora){
        DateFormat dateFormat= new SimpleDateFormat("dd/MM/yyyy HH:mm");
        try {
            Date d=dateFormat.parse(Fecha+" "+Hora);
            return new Long(d.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String estado) {
        Estado = estado;
    }

    public String getTipoServicio() {
        return TipoServicio;
    }

    public void setTipoServicio(String tipoServicio) {
        TipoServicio = tipoServicio;
    }

    public String getCiudad() {
        return Ciudad;
    }

    public void setCiudad(String ciudad) {
        Ciudad = ciudad;
    }

    public String getDirRecogida() {
        return DirRecogida;
    }

    public void setDirRecogida(String dirRecogida) {
        DirRecogida = dirRecogida;
    }

    public String getDirLlevar() {
        return DirLlevar;
    }

    public void setDirLlevar(String dirLlevar) {
        DirLlevar = dirLlevar;
    }

    public String getDirRegreso() {
        return DirRegreso;
    }

    public void setDirRegreso(String dirRegreso) {
        DirRegreso = dirRegreso;
    }

    public String getObservaciones() {
        return Observaciones;
    }

    public void setObservaciones(String observaciones) {
        Observaciones = observaciones;
    }

    public String getFecha() {
        Date date=new Date(Fecha);
        DateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(date);
    }

    public String getHora(){
        Date time=new Date(Fecha);
        DateFormat dateFormat=new SimpleDateFormat("HH:mm");
        return dateFormat.format(time);
    }

    public void setFecha(Long fecha) {
        Fecha = fecha;
    }

    public void setFecha(String fecha,String hora){
        Fecha=parseDate(fecha,hora);
    }

    public String getInforme() {
        return Informe;
    }

    public void setInforme(String informe) {
        Informe = informe;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getCAcompanante() {
        return CAcompanante;
    }

    public void setCAcompanante(String CAcompanante) {
        this.CAcompanante = CAcompanante;
    }

    public String getCConductor() {
        return CConductor;
    }

    public void setCConductor(String CConductor) {
        this.CConductor = CConductor;
    }

    public String getCVehiculo() {
        return CVehiculo;
    }

    public void setCVehiculo(String CVehiculo) {
        this.CVehiculo = CVehiculo;
    }

    public Servicio getConvertedObject(DataSnapshot snapshot) {
        String UID= (String)snapshot.child("uid").getValue();
        String Estado= (String)snapshot.child("estado").getValue();
        String TipoServicio= (String)snapshot.child("tipoServicio").getValue();
        String Ciudad= (String)snapshot.child("ciudad").getValue();
        String dirRecogida= (String)snapshot.child("dirRecogida").getValue();
        String dirLlevar= (String)snapshot.child("dirLlevar").getValue();
        String dirRegreso= (String)snapshot.child("dirRegreso").getValue();
        String Observaciones= (String)snapshot.child("observaciones").getValue();
        Long Id=(Long) snapshot.child("id").getValue() ;
        Long Fecha =(Long) snapshot.child("fecha").getValue();
        String acompanante=(String)snapshot.child("Cacompanante").getValue();
        String conductor=(String)snapshot.child("Cconductor").getValue();
        String vehiculo=(String)snapshot.child("Cvehiculo").getValue();

        return new Servicio(UID,Estado,TipoServicio,Ciudad,dirRecogida,dirLlevar,dirRegreso,Observaciones,Fecha,Id,acompanante,conductor,vehiculo);
    }
}
