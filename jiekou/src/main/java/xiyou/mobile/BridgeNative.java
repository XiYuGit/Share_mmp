package xiyou.mobile;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by user on 2017/6/11.
 */

public class BridgeNative {

    public static final byte METHOD_INVOKE=0;
    public static final byte METHOD_CONNECT=1;
    public static final String RESULT="result";
    public static final String OK="ok";
    public static final String METHOD="method";
    public static final String PARAM="param";
    public static final String CLASS="cls";
    public static final String REQUESTCODE="request";
    public static final String RESPONSECODE="response";
    public static final String CALLER="caller";
    public static final String IP="ip";
    public static final String X="x";
    public static final String Y="y";
    public static final String DATA="data";
    public static final String POSITION="position";

    public static final int ADDFRIEND=0;
    public static final int PERMITADD=1;
    public static final int GETIP=2;
    public static final int ACTION_DOWN=3;
    public static final int ACTION_MOVE=4;
    public static final int REQUESTSYNC=5;
    public static final int PERMITSYNC=6;
    public static final int PERMITCONTROL=7;
    public static final int REQUESTCONTROL=8;
    public static final int SENDSCREENSIZE=9;
    public static final int ENDSYNC=10;
    public static final int SENDDATA=11;
    public static final int SEEK=12;
    public static final int REFUSEDSYNC=13;
    public static final int REFUSEDCONTROL=14;
    public static final int REFUSEDADDFRIEND=15;
    public static final int CLEARSCREEN=16;
    public static final int STARTPAUSE=17;

    public static int port=12543;
    private static int times=10;
    static Socket socket=null;
    static OutputStream os=null;
    static InputStream is=null;
    private static boolean alive=true;
    static LinkedBlockingQueue<JSONObject> responses_invoke=new LinkedBlockingQueue<>(),responses_connect=new LinkedBlockingQueue<>();
    private static Thread readThread=null;

    public static boolean debug=true,debug_f=false;

    static boolean connect()
    {
        p("connect");
        if (socket!=null)
        {
            try {
                socket.close();
                is.close();
                os.close();
            } catch (IOException e) {
                p(e.toString());
            }
        }

        try {
            socket=new Socket("123.207.152.184",port);
            os=socket.getOutputStream();
            is=socket.getInputStream();
        } catch (IOException e) {
            p(e.toString());
            return false;
        }

        start();

        return true;
    }

    static
    {
        connect();
    }

    public static void start()
    {

        alive=true;
        new Thread()
        {
            @Override
            public void run() {
                FileOutputStream fos=null;
                File a=new File("/sdcard/bridgenative.txt");
                if (debug_f) {
                    if (!a.exists())
                        try {
                            a.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    try {
                        fos = new FileOutputStream(a);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                readThread=this;
                while(alive)
                {
                    byte []cc=new byte[4];
                    int datalen=0;
                    if (!read(is,cc))
                    {
                        if (!connect())return;
                        continue;
                    }
                    for (int i=0;i<4;i++) {
                        datalen = datalen << 8 | (0xff &cc[i]);
                    }
                    cc=new byte[datalen];
                    if (debug)
                        p("recv:"+datalen);
                    if (!read(is,cc))
                    {
                        if (!connect())return;
                        continue;
                    }
                    String r= null;
                    try {
                        r = new String(cc,"utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    if (debug)
                    {
                        p("get:");
                        p(r);
                        if (debug_f)
                            try {
                                fos.write(r.getBytes());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                    }

                    JSONObject o=null;
                    try {
                        o= JSONObject.fromObject(r);
                    }catch (JSONException e)
                    {
                        break;
                    }


                    if (o.containsKey(RESULT))
                    {
                        responses_invoke.add(o);
                    }else
                    {
                        responses_connect.add(o);
                        new HandleThread(o).start();
                    }
                }
                if (debug_f)
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                super.run();

            }
        }.start();
    }

    private static void handleMsg(JSONObject o)
    {
        if (User.get()==null)
            return;

        switch (o.getInt(RESPONSECODE))
        {
            case ADDFRIEND:
                User.get().notifyAddFriend(o.getString(CALLER));
                break;
            case PERMITADD:
                User.get().notifyPermitAdd(o.getString(CALLER));
                break;
            case ACTION_DOWN:
                User.get().notifyAction(ACTION_DOWN,o.getInt(X),o.getInt(Y));
                break;
            case ACTION_MOVE:
                User.get().notifyAction(ACTION_MOVE,o.getInt(X),o.getInt(Y));
                break;
            case REQUESTSYNC:
                User.get().notifyRequestSync(o.getString(CALLER));
                break;
            case REQUESTCONTROL:
                User.get().notifyRequestControl(o.getString(CALLER));
                break;
            case PERMITCONTROL:
                User.get().notifyPermitControl(o.getString(CALLER));
                break;
            case PERMITSYNC:
                User.get().notifyPermitSync(o.getString(CALLER));
                break;
            case ENDSYNC:
                User.get().notifyEndSync(o.getString(CALLER));
                break;
            case SENDSCREENSIZE:
                User.get().notifyScreenSizeSet(o.getString(CALLER),o.getInt(X),o.getInt(Y));
                break;
            case SENDDATA:
                User.get().notifyDataSet(o.getString(DATA),o.getString(CALLER));
                break;
            case SEEK:
                User.get().notifyOnSeek(o.getInt(POSITION),o.getString(CALLER));
                break;
            case REFUSEDADDFRIEND:
                User.get().notifyRefusedAddFriend(o.getString(CALLER));
                break;
            case REFUSEDCONTROL:
                User.get().notifyRefusedControl(o.getString(CALLER));
                break;
            case REFUSEDSYNC:
                User.get().notifyRefusedSync(o.getString(CALLER));
                break;
            case CLEARSCREEN:
                User.get().notifyClearScreen();
                break;
            case STARTPAUSE:
                User.get().notifyStartPasue();
                break;
        }
    }

    public static void close()
    {
        alive=false;
        if (readThread!=null)
        {
            readThread.interrupt();
            try{
                readThread.stop();
            }catch (UnsupportedOperationException e)
            {
            }
        }
        try {
            socket.close();
        } catch (IOException e) {
            p(e.toString());
        }
    }

    public static JSONObject invoke(String cls, String method, Object ...params)
    {
        JSONObject o=new JSONObject();
        o.put(CLASS,cls);
        o.put(METHOD,method);
        JSONArray array=new JSONArray();
        for (int i=0;i<params.length;i++)
        {
            array.add(params[i]);
        }
        o.put(PARAM,array);
        for (int time=0;time<times;time++)
        {
            try {
                if (!send(METHOD_INVOKE,o.toString().getBytes("utf-8")))
                {
                    connect();
                    continue;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (debug)
            {
                p("send:");
                p(o.toString());
            }


            try {
                return responses_invoke.take();
            } catch (InterruptedException e) {
                p(e.toString());
            }
        }

        return null;
    }

    public static JSONObject connect(JSONObject request)
    {
        for (int time=0;time<times;time++)
        {
            try {
                if (!send(METHOD_CONNECT,request.toString().getBytes("utf-8")))
                {
                    connect();
                    continue;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (debug)
            {
                p("send:");
                p(request.toString());
            }

            try {
                return responses_invoke.take();
            } catch (InterruptedException e) {
                p(e.toString());
            }
        }

        return null;
    }

    private static synchronized boolean send(byte method,byte []cc)
    {
        int datalen=cc.length;
        byte []len_byte=new byte[4];
        for (int i=0;i<4;i++)
        {
            len_byte[3-i]=(byte)(datalen&(int)0xff);
            datalen=datalen>>8;
        }
        if (!write(os,len_byte))return false;
        if (!write(os,new byte[]{method}))return false;
        if (!write(os,cc)) return false;

        return true;
    }

    public static boolean write(OutputStream os,byte[] cc)
    {
        try {
            os.write(cc,0,cc.length);
        } catch (SocketException e)
        {
            return false;
        } catch (IOException e) {
            e.printStackTrace();

            return false;
        }

        return true;
    }

    public static boolean read(InputStream is,byte[] cc)
    {
        int readcount=0;
        try {
            int x=0;
            while (true)
            {
                x=is.read(cc,readcount,cc.length-readcount);
                if (x==-1)
                {
                    p("read failed");
                    return false;
                }

                readcount+=x;
                if (readcount==cc.length)
                    break;
            }
        } catch (IOException e) {
            p(e.toString());
            return false;
        }
        return true;
    }

    static class HandleThread extends Thread
    {
        JSONObject o;
        public HandleThread(JSONObject o)
        {
            this.o=o;
        }

        @Override
        public void run() {
            super.run();
            handleMsg(o);
        }
    }

    public static void p(String s)
    {
        System.out.println(s);
    }
}
