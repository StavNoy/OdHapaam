package project.stav.odhapaam2.LogServer.Server;
//fhddrhd
import android.os.Binder;

/**
 * Created by hackeru on 27/08/2017.
 */

public class LoginBinder extends Binder {
    //Service to bind with
    private final BoundLoginService service;
    //Dependency injection for the service
    public LoginBinder(BoundLoginService service){
        this.service = service;
    }
    //Getter for the service
    public BoundLoginService getService() {
        return this.service;
    }
}
