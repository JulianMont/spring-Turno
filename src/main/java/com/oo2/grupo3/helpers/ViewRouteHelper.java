package com.oo2.grupo3.helpers;

public class ViewRouteHelper {

    private ViewRouteHelper(){}

    //Auth
    public static final String USER_LOGIN = "authentication/login";

    //Home
    public static final String HOME_INDEX = "home/index";

    //Empleado
    public static final String EMPLEADOS_LIST = "empleados/list";
    public static final String EMPLEADOS_FORM = "empleados/form";
    public static final String EMPLEADOS_DETALLE = "empleados/detalle";
    public static final String REDIRECT_EMPLEADOS_LIST = "redirect:/empleados/list";
    public static final String EMPLEADOS_DETALLE_REDIRECT = "redirect:/empleados/";
    
    
    // HorarioLaboral
    public static final String HORARIO_FORM = "horarios/form";
    
    //AusenciaEmpleado
    public static final String AUSENCIA_FORM = "ausencias/form";

    //Especialidad
    public static final String ESPECIALIDAD_LISTA = "especialidades/list";
    public static final String ESPECIALIDAD_FORM = "especialidades/form";
    public static final String REDIRECT_ESPECIALIDAD_LISTA = "redirect:/especialidades/list";
    
    //Cliente
    public static final String CLIENTE_TURNOS_LIST = "cliente/turnos";
    public static final String CLIENTE_LIST = "cliente/list";
    public static final String CLIENTE_FORM = "cliente/form";


    //Turno
    public static final String TURNO_GENERAR = "turnos/GenerarTurno";
    public static final String TURNO_GENERAR_REDIRECT = "redirect:/turnos/GenerarTurno";
    public static final String TURNO_LIST = "turnos/list";
    public static final String TURNO_LIST_REDIRECT = "redirect:/turnos/list";
    

    // Servicio
    public static final String SERVICIO_LIST = "servicios/list";
    public static final String SERVICIO_FORM = "servicios/form";
    public static final String REDIRECT_SERVICIO_LIST = "redirect:/servicios/list";

    // Ubicación
    public static final String UBICACION_LIST = "ubicaciones/list";
    public static final String UBICACION_FORM = "ubicaciones/form";
    public static final String REDIRECT_UBICACION_LIST = "redirect:/ubicaciones/list";
   
   
    
}