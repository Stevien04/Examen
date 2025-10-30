package InterfacesSMA;

import ModeloSMA.ClsUsuarioSMA;

public interface CRUDLoginSMA {

    ClsUsuarioSMA validarCredenciales(String correo, String password);
}