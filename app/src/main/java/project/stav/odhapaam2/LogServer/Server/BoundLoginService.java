package project.stav.odhapaam2.LogServer.Server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by hackeru on 27/08/2017.
 */

public class BoundLoginService extends Service implements Runnable {

    private Thread t;
    private boolean isRunning;
    private String uName;
    private String uPass;
    private LoginActivity loginActivity;

    @Override
    public IBinder onBind(Intent intent) {
        if(t == null){
            t = new Thread(this);
            t.start();
            isRunning = true;
            return new LoginBinder(this);
        }
        return null;
    }

    public synchronized void tryLogin(String uName, String uPass, LoginActivity loginActivity){
        this.uName = uName;
        this.uPass = uPass;
        this.loginActivity = loginActivity;
    }

    @Override//Asynchronous task
    public void run() {
        while(isRunning){
            if(uName != null && uPass != null && loginActivity != null){
                String info = loginAndGetInfo();
                if(info != null){//if login succeed
                    loginActivity.validLogin(uName, info);
                }else{
                    loginActivity.invalidLogin();
                }
            }
        }
    }
    //Remote authentication over HTTP
    private String loginAndGetInfo(){
        String url = "http://nikita.hackeruweb.co.il/hackSwift/";
        try {
            String data = "uName="+uName+"&uPass="+uPass;
            String res = new HttpRequest(url).prepare(HttpRequest.Method.POST).withData(data).sendAndReadString();
            InputSource is = new InputSource(new StringReader(res));
            Document d = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
            //if succeed - return valid info of logged user
            return d.getElementsByTagName("info").item(0).getTextContent();
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
        return null;//otherwise -> return null
    }

    @Override
    public boolean onUnbind(Intent intent) {
        isRunning = false;
        return super.onUnbind(intent);
    }
}
