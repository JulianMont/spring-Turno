package com.oo2.grupo3.helpers;

public class ViewRouteHelper {

    private ViewRouteHelper(){}

    //Auth
    public static final String USER_LOGIN = "authentication/login";

    //Home
    public static final String HOME_INDEX = "home/index";

    //Example
    public static final String EXAMPLE_LIST = "example/list";
    public static final String EXAMPLE_ADMIN_LIST = "example/admin-list";
    public static final String EXAMPLE_FORM = "example/form";
    
    //Empleado
    public static final String EMPLEADO_LIST = "empleados/listar";
    public static final String EMPLEADO_DETALLE = "empleados/detalle";
    public static final String EMPLEADO_NUEVO = "empleados/nuevo";
    public static final String EMPLEADO_EDITAR = "empleados/editar";
    public static final String REDIRECT_EMPLEADOS = "redirect:/empleados";
}