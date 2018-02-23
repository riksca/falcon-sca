package org.calontir.marshallate.falcon.client.user;

import org.calontir.marshallate.falcon.client.LoginInfo;

/**
 *
 * @author rikscarborough
 */
public class SecurityFactory {
    private static Security _instance = new Security();
    
    private SecurityFactory() {
        
    }
    
    public static void setLoginInfo(LoginInfo loginInfo) {
        _instance.setLoginInfo(loginInfo);
    }
    
    public static Security getSecurity() { 
        return _instance;
    }
    
}
