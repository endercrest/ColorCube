package com.endercrest.colorcube.utils;

import com.endercrest.colorcube.MessageManager;

import java.util.Arrays;
import java.util.HashMap;

public class MessageUtil {

    private static HashMap<String, String>varcache = new HashMap<String, String>();

    public static String replaceVars(String msg, HashMap<String, String> vars){
        boolean error = false;
        for(String s:vars.keySet()){
            try{
                msg.replace("{$"+s+"}", vars.get(s));
            }catch(Exception e){
                MessageManager.getInstance().debugConsole("Failed to replace string vars. Error on " + s);
                error = true;
            }
        }
        if(error){
            MessageManager.getInstance().debugConsole("Error replacing vars in message: " + msg);
            MessageManager.getInstance().debugConsole("Vars: " + vars.toString());
            MessageManager.getInstance().debugConsole("Vars Cache: " + varcache.toString());
        }
        return msg;
    }

    public static String replaceVars(String msg, String[] vars){
        for(String str: vars){
            String[] s = str.split("-");
            varcache.put(s[0], s[1]);
        }
        boolean error = false;
        for(String str: varcache.keySet()){
            try{
                msg = msg.replace("{$"+str+"}", varcache.get(str));
            }catch(Exception e){
                MessageManager.getInstance().debugConsole("Failed to replace string vars. Error on "+str);
                error = true;
            }
        }
        if(error){
            MessageManager.getInstance().debugConsole("Error replacing vars in message: " + msg);
            MessageManager.getInstance().debugConsole("Vars: " + Arrays.toString(vars));
            MessageManager.getInstance().debugConsole("Vars Cache: "+varcache.toString());
        }

        return msg;
    }
}
