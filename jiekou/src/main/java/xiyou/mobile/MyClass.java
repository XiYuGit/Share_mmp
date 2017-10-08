package xiyou.mobile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MyClass {

    public static void main(String []ssssss) throws IOException, InterruptedException {
       send();
    }

    public static void test()
    {
        int datalen=232;
        byte []len_byte=new byte[4];
        for (int i=0;i<4;i++)
        {
            len_byte[3-i]= (byte) ((byte)datalen&(byte)0xff);
            datalen=datalen>>8;
            p(""+len_byte[3-i]);
        }
    }

    public static void p(String s)
    {
        System.out.println(s);
    }

    public static void send()
    {
        User.login("test2","test2");
        User.get().addOnPermitSyncListener(new User.OnPermitSyncListener() {
            @Override
            public void onPermitSync(String name) {
                User.get().sendPlayData("667737400",name);
                User.get().sendScreenSize(800,600,name);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                User.get().sendAction(BridgeNative.ACTION_DOWN,0,0,name);
                for (int i=0;i<100;i++)
                {
                    User.get().sendAction(BridgeNative.ACTION_MOVE,8*i,6*i,name);
                }
            }
        });
        User.get().requestSync("test1");
        //User.get().sendAction(BridgeNative.ACTION_DOWN,100,100,"test2");
        //BridgeNative.close();
    }

}
