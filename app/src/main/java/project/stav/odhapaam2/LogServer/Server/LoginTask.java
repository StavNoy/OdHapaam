package project.stav.odhapaam2.LogServer.Server;

import android.os.AsyncTask;

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

public class LoginTask extends AsyncTask<String, Void, String> {
    private static final String URL = "http://nikita.hackeruweb.co.il/hackSwift/";
    private String name;
    private String pass;
    @Override//What to do in background thread
    protected String doInBackground(String... params) {
        name = params[0];//first param is username
        pass = params[1];//second param is password
        return loginAndGetInfo();//return Result
    }
    //Try to login - and return logged user info or null if failed
    private String loginAndGetInfo(){
        try {
            String data = "uName="+name+"&uPass="+pass;
            String res = new HttpRequest(URL).prepare(HttpRequest.Method.POST).withData(data).sendAndReadString();
            InputSource is = new InputSource(new StringReader(res));
            Document d = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
            return d.getElementsByTagName("info").item(0).getTextContent();
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
        return null;
    }
}
