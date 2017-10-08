package xiyou.mobile;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;

import static xiyou.mobile.BridgeNative.DATA;
import static xiyou.mobile.BridgeNative.OK;
import static xiyou.mobile.BridgeNative.POSITION;
import static xiyou.mobile.BridgeNative.RESULT;
import static xiyou.mobile.BridgeNative.X;
import static xiyou.mobile.BridgeNative.Y;

/**
 * Created by user on 2017/6/11.
 */

public class User {

    public static final String FRIENDS="friends_";
    public static final String FROM="user";
    public static final String USR="usrname";
    public static final String NAME="name";
    public static final String PASSWD="passwd";
    public static final String LV="lv";
    public static final String SEX="sex";
    public static final String AGE="age";
    public static final String ID="id";
    public static final String LOG_FAIL="fail";
    public static final String LOG_SUCCES="success";
    public static final String EXIST="exist";
    public static final String NOEXIST="no_exist";
    public static final String EXIST_NAME="exist_name";
    public static final String EXIST_USR="exist_usr";
    public static final String ONLINE="online";

    public static final String METHOD_REGISTER="user_register";
    public static final String METHOD_LOGIN="user_login";

    private static User current=null;

    public String usrname,passwd,name;

    public boolean isOnline() {
        return online;
    }

    public String getName() {
        return name;
    }
    public String getUsrname()
    {
        return usrname;
    }


    private boolean online=false;
    public ArrayList<User> friends=new ArrayList<>();
    private ArrayList<OnAddFriendListener> onAddFriendListeners=new ArrayList<>();
    private ArrayList<OnPermittAddListener> onPermittAddListeners=new ArrayList<>();
    private ArrayList<OnPermitControlListener> onPermitControlListeners=new ArrayList<>();
    private ArrayList<OnRequestControlListener> onRequestControlListeners=new ArrayList<>();
    private ArrayList<OnActionListener> onActionListeners=new ArrayList<>();
    private ArrayList<OnRequestSyncListener> onRequestSyncListeners=new ArrayList<>();
    private ArrayList<OnPermitSyncListener> onPermitSyncListeners=new ArrayList<>();
    private ArrayList<OnScreenSizeSetListener> onScreenSizeSetListeners=new ArrayList<>();
    private ArrayList<OnEndSyncListener> onEndSyncListeners=new ArrayList<>();
    private ArrayList<OnDataSetListener> onDataSetListeners=new ArrayList<>();
    private ArrayList<OnSeekListener> onSeekListeners=new ArrayList<>();
    private ArrayList<OnRefusedAddFriendListener> onRefusedAddFriendListeners=new ArrayList<>();
    private ArrayList<OnRefusedControlListener> onRefusedControlListeners=new ArrayList<>();
    private ArrayList<OnRefusedSyncListener> onRefusedSyncListeners=new ArrayList<>();
    private ArrayList<OnClearScreenListener> onClearScreenListeners=new ArrayList<>();
    private ArrayList<OnStartPauseListener> onStartPauseListeners=new ArrayList<>();

    private User(String usrname,String name,String passwd)
    {
        this.usrname=usrname;
        this.passwd=passwd;
        this.name=name;
    }

    private User(String usrname,String name,String passwd,boolean online)
    {
        this(usrname, name, passwd);
        this.online=online;
    }

    public static String login(String usrname,String passwd)
    {
        JSONObject r=BridgeNative.invoke(User.class.getName(),"login",usrname,passwd);
        if (r.getString(BridgeNative.RESULT).equals(LOG_SUCCES))
        {
            current=new User(usrname,r.getString(NAME),passwd);
            current.freshFriends();
        }

        return r.getString(BridgeNative.RESULT);
    }

    static void login()
    {
        if (current!=null)
        {
            login(current.usrname,current.passwd);
        }
    }

    public static void logout()
    {
        if (current==null)
            return ;

        current=null;
        BridgeNative.close();
        BridgeNative.connect();
        // BridgeNative.start();
    }

    public static boolean register(String usrname,String passwd,String name)
    {
        JSONObject r=BridgeNative.invoke(User.class.getName(),"register",usrname,passwd,name);
        if (r.getString(BridgeNative.RESULT).equals(BridgeNative.OK))
            return true;

        return false;
    }

    public void addOnAddFriendListener(OnAddFriendListener l)
    {
        onAddFriendListeners.add(l);
    }

    public void addOnPermittAddListener(OnPermittAddListener l)
    {
        onPermittAddListeners.add(l);
    }

    public void addOnRequestControlListener(OnRequestControlListener l)
    {
        onRequestControlListeners.add(l);
    }

    public void addOnPermitControlListener(OnPermitControlListener l)
    {
        onPermitControlListeners.add(l);
    }

    public void addOnDataSetListener(OnDataSetListener l)
    {
        onDataSetListeners.add(l);
    }

    public void addOnSeekListener(OnSeekListener l)
    {
        onSeekListeners.add(l);
    }

    public void addOnRefusedAddFriendListener(OnRefusedAddFriendListener l)
    {
        onRefusedAddFriendListeners.add(l);
    }

    public void addOnRefusedControlListener(OnRefusedControlListener l)
    {
        onRefusedControlListeners.add(l);
    }

    public void addOnRefusedSyncListener(OnRefusedSyncListener l)
    {
        onRefusedSyncListeners.add(l);
    }

    public void addOnScreenSizeSetListener(OnScreenSizeSetListener l)
    {
        onScreenSizeSetListeners.add(l);
    }

    public void addOnClearScreenListener(OnClearScreenListener l)
    {
        onClearScreenListeners.add(l);
    }

    public void addOnStartPauseListener(OnStartPauseListener l)
    {
        onStartPauseListeners.add(l);
    }

    public void addOnEndSyncListener(OnEndSyncListener l)
    {
        onEndSyncListeners.add(l);
    }

    public void removeOnEndSyncListener(OnEndSyncListener l)
    {
        onEndSyncListeners.remove(l);
    }

    public void removeOnStartPauseListener(OnStartPauseListener l)
    {
        onStartPauseListeners.remove(l);
    }

    public void removeOnClearScreenListener(OnClearScreenListener l)
    {
        onClearScreenListeners.remove(l);
    }

    public void removeOnScreenSizeSetListener(OnScreenSizeSetListener l)
    {
        onScreenSizeSetListeners.remove(l);
    }

    public void removeOnRefusedAddFriendListener(OnRefusedAddFriendListener l)
    {
        onRefusedAddFriendListeners.remove(l);
    }

    public void removeOnRefusedControlListener(OnRefusedControlListener l)
    {
        onRefusedControlListeners.remove(l);
    }

    public void removeOnRefusedSyncListener(OnRefusedSyncListener l)
    {
        onRefusedSyncListeners.remove(l);
    }

    public void removeOnSeekListener(OnSeekListener l)
    {
        onSeekListeners.remove(l);
    }

    public void removeOnDataSetListener(OnDataSetListener l)
    {
        onDataSetListeners.remove(l);
    }

    public void removeOnActionListener(OnActionListener l)
    {
        onActionListeners.remove(l);
    }

    public void removeOnRequestSyncListener(OnRequestSyncListener l)
    {
        onRequestSyncListeners.remove(l);
    }

    public void removeOnPermitSyncListener(OnPermitSyncListener l)
    {
        onPermitSyncListeners.remove(l);
    }

    public void removeOnAddFriendListener(OnAddFriendListener l)
    {
        onAddFriendListeners.remove(l);
    }

    public void removeOnPermittAddListener(OnPermittAddListener l)
    {
        onPermittAddListeners.remove(l);
    }

    public void removeOnRequestControlListener(OnRequestControlListener l)
    {
        onRequestControlListeners.remove(l);
    }

    public void removeOnPermitControlListener(OnPermitControlListener l)
    {
        onPermitControlListeners.remove(l);
    }

    public void addOnActionListener(OnActionListener l)
    {
        onActionListeners.add(l);
    }

    public void addOnRequestSyncListener(OnRequestSyncListener l)
    {
        onRequestSyncListeners.add(l);
    }

    public void addOnPermitSyncListener(OnPermitSyncListener l)
    {
        onPermitSyncListeners.add(l);
    }

    void notifyStartPasue()
    {
        for (int i=0;i<onStartPauseListeners.size();i++)
        {
            onStartPauseListeners.get(i).onStartPause();
        }
    }

    void notifyRefusedAddFriend(String name)
    {
        for (int i=0;i<onRefusedAddFriendListeners.size();i++)
        {
            onRefusedAddFriendListeners.get(i).onRefused(name);
        }
    }

    void notifyRefusedControl(String name)
    {
        for (int i=0;i<onRefusedControlListeners.size();i++)
        {
            onRefusedControlListeners.get(i).onRefused(name);
        }
    }

    void notifyRefusedSync(String name)
    {
        for (int i=0;i<onRefusedSyncListeners.size();i++)
        {
            onRefusedSyncListeners.get(i).onRefused(name);
        }
    }

    void notifyDataSet(String data,String name)
    {
        for (int i=0;i<onDataSetListeners.size();i++)
        {
            onDataSetListeners.get(i).onDataSet(data,name);
        }
    }

    void notifyAddFriend(String name)
    {
        for (int i=0;i<onAddFriendListeners.size();i++)
        {
            onAddFriendListeners.get(i).onAddFriend(name);
        }
    }

    void notifyOnSeek(int position,String name)
    {
        for (int i=0;i<onSeekListeners.size();i++)
        {
            onSeekListeners.get(i).onSeek(position,name);
        }
    }

    void notifyPermitAdd(String name)
    {
        for (int i=0;i<onPermittAddListeners.size();i++)
        {
            onPermittAddListeners.get(i).onPermittAdd(name);
        }
    }

    void notifyRequestControl(String name)
    {
        for (int i=0;i<onRequestControlListeners.size();i++)
        {
            onRequestControlListeners.get(i).onRequest(name);
        }
    }

    void notifyPermitControl(String name)
    {
        for (int i=0;i<onPermitControlListeners.size();i++)
        {
            onPermitControlListeners.get(i).onPermit(name);
        }
    }

    void notifyAction(int action,int x,int y)
    {
        for (int i=0;i<onActionListeners.size();i++)
        {
            onActionListeners.get(i).onAction(action, x, y);
        }
    }

    void notifyRequestSync(String name)
    {
        for (int i=0;i<onRequestSyncListeners.size();i++)
        {
            onRequestSyncListeners.get(i).onRequestSync(name);
        }
    }

    void notifyPermitSync(String name)
    {
        for (int i=0;i<onPermitSyncListeners.size();i++)
        {
            onPermitSyncListeners.get(i).onPermitSync(name);
        }
    }

    void notifyEndSync(String name)
    {
        for (int i=0;i<onEndSyncListeners.size();i++)
        {
            onEndSyncListeners.get(i).onEndSync(name);
        }
    }

    void notifyClearScreen()
    {
        for (int i=0;i<onClearScreenListeners.size();i++)
        {
            onClearScreenListeners.get(i).onClear();
        }
    }

    void notifyScreenSizeSet(String name,int w,int h)
    {
        for (int i=0;i<onScreenSizeSetListeners.size();i++)
        {
            onScreenSizeSetListeners.get(i).onScreenSizeSet(name, w, h);
        }
    }

    public void freshFriends()
    {
        JSONObject r=BridgeNative.invoke(User.class.getName(),"friendList",name);
        JSONArray ar=r.getJSONArray(BridgeNative.RESULT);
        while (friends.size()!=0)
            friends.remove(0);
        for (int i=0;i<ar.size();i++)
        {
            JSONObject o=ar.getJSONObject(i);
            current.friends.add(new User(null,o.getString(NAME),null,o.getBoolean(ONLINE)));
        }
    }

    public void setIp(String ip)
    {
        BridgeNative.invoke(User.class.getName(),"setIp",name,ip);
    }

    public String getFriendIp(String fname)
    {
        return BridgeNative.invoke(User.class.getName(),"getIp",fname).getString(BridgeNative.IP);
    }

    public boolean clearScreen(String name)
    {
        JSONObject o=new JSONObject();
        o.put(BridgeNative.REQUESTCODE,BridgeNative.CLEARSCREEN);
        o.put(NAME,name);
        if (BridgeNative.connect(o).getString(RESULT).equals(OK))
            return true;

        return false;
    }


    public boolean addFriend(String name)
    {
        JSONObject o=new JSONObject();
        o.put(BridgeNative.REQUESTCODE,BridgeNative.ADDFRIEND);
        o.put(NAME,name);
        if (BridgeNative.connect(o).getString(RESULT).equals(OK))
            return true;

        return false;
    }

    public boolean permittAdd(String name) {
        JSONObject o = new JSONObject();
        o.put(BridgeNative.REQUESTCODE, BridgeNative.PERMITADD);
        o.put(NAME, name);
        if (BridgeNative.connect(o).getString(RESULT).equals(OK))
            return true;

        return false;
    }

    public boolean sendScreenSize(int w,int h,String name)
    {
        JSONObject o = new JSONObject();
        o.put(BridgeNative.REQUESTCODE, BridgeNative.SENDSCREENSIZE);
        o.put(NAME, name);
        o.put(X,w);
        o.put(Y,h);
        if (BridgeNative.connect(o).getString(RESULT).equals(OK))
            return true;

        return false;
    }

    public boolean sendPlayData(String data,String name)
    {
        JSONObject o = new JSONObject();
        o.put(BridgeNative.REQUESTCODE, BridgeNative.SENDDATA);
        o.put(NAME, name);
        o.put(DATA,data);
        if (BridgeNative.connect(o).getString(RESULT).equals(OK))
            return true;

        return false;
    }

    public boolean syncSeek(int position,String name)
    {
        JSONObject o = new JSONObject();
        o.put(BridgeNative.REQUESTCODE, BridgeNative.SEEK);
        o.put(NAME, name);
        o.put(POSITION,position);
        if (BridgeNative.connect(o).getString(RESULT).equals(OK))
            return true;

        return false;
    }

    public boolean endSync(String name)
    {
        JSONObject o = new JSONObject();
        o.put(BridgeNative.REQUESTCODE, BridgeNative.ENDSYNC);
        o.put(NAME, name);
        if (BridgeNative.connect(o).getString(RESULT).equals(OK))
            return true;

        return false;
    }

    public boolean startpause(String name)
    {
        JSONObject o = new JSONObject();
        o.put(BridgeNative.REQUESTCODE, BridgeNative.STARTPAUSE);
        o.put(NAME, name);
        if (BridgeNative.connect(o).getString(RESULT).equals(OK))
            return true;

        return false;
    }

    public boolean sendAction(int action,int x,int y,String name)
    {
        JSONObject o=new JSONObject();
        o.put(BridgeNative.REQUESTCODE,action);
        o.put(X,x);
        o.put(Y,y);
        o.put(NAME,name);
        if (BridgeNative.connect(o).getString(RESULT).equals(OK))
            return true;

        return false;
    }

    public boolean requestSync(String name)
    {
        JSONObject o=new JSONObject();
        o.put(BridgeNative.REQUESTCODE,BridgeNative.REQUESTSYNC);
        o.put(NAME,name);
        if (BridgeNative.connect(o).getString(RESULT).equals(OK))
            return true;

        return false;
    }

    public boolean permitSync(String name)
    {
        JSONObject o=new JSONObject();
        o.put(BridgeNative.REQUESTCODE,BridgeNative.PERMITSYNC);
        o.put(NAME,name);
        if (BridgeNative.connect(o).getString(RESULT).equals(OK))
            return true;

        return false;
    }

    public boolean requestControl(String name)
    {
        JSONObject o=new JSONObject();
        o.put(BridgeNative.REQUESTCODE,BridgeNative.REQUESTCONTROL);
        o.put(NAME,name);
        if (BridgeNative.connect(o).getString(RESULT).equals(OK))
            return true;

        return false;
    }

    public boolean permitControl(String name)
    {
        JSONObject o=new JSONObject();
        o.put(BridgeNative.REQUESTCODE,BridgeNative.PERMITCONTROL);
        o.put(NAME,name);
        if (BridgeNative.connect(o).getString(RESULT).equals(OK))
            return true;

        return false;
    }

    public boolean refusedControl(String name)
    {
        JSONObject o=new JSONObject();
        o.put(BridgeNative.REQUESTCODE,BridgeNative.REFUSEDCONTROL);
        o.put(NAME,name);
        if (BridgeNative.connect(o).getString(RESULT).equals(OK))
            return true;

        return false;
    }

    public boolean refusedSync(String name)
    {
        JSONObject o=new JSONObject();
        o.put(BridgeNative.REQUESTCODE,BridgeNative.REFUSEDSYNC);
        o.put(NAME,name);
        if (BridgeNative.connect(o).getString(RESULT).equals(OK))
            return true;

        return false;
    }

    public boolean refusedAddFriend(String name)
    {
        JSONObject o=new JSONObject();
        o.put(BridgeNative.REQUESTCODE,BridgeNative.REFUSEDADDFRIEND);
        o.put(NAME,name);
        if (BridgeNative.connect(o).getString(RESULT).equals(OK))
            return true;

        return false;
    }

    public static User get()
    {
        return current;
    }

    public interface OnAddFriendListener
    {
        public void onAddFriend(String name);
    }

    public interface OnPermittAddListener
    {
        public void onPermittAdd(String name);
    }

    public interface OnRequestControlListener
    {
        public void onRequest(String name);
    }

    public interface OnPermitControlListener
    {
        public void onPermit(String name);
    }

    public interface OnActionListener
    {
        public void onAction(int action,int x,int y);
    }

    public interface OnSizeChangeListener
    {
        public void onSizeChanged(int w,int h);
    }

    public interface OnRequestSyncListener
    {
        public void onRequestSync(String name);
    }

    public interface OnPermitSyncListener
    {
        public void onPermitSync(String name);
    }

    public interface OnEndSyncListener
    {
        public void onEndSync(String name);
    }

    public interface OnScreenSizeSetListener
    {
        public void onScreenSizeSet(String name,int w,int h);
    }

    public interface OnDataSetListener
    {
        public void onDataSet(String data,String name);
    }

    public interface OnSeekListener
    {
        public void onSeek(int position,String name);
    }

    public interface OnRefusedControlListener
    {
        public void onRefused(String name);
    }

    public interface OnRefusedSyncListener
    {
        public void onRefused(String name);
    }

    public interface OnRefusedAddFriendListener
    {
        public void onRefused(String name);
    }

    public interface OnClearScreenListener
    {
        public void onClear();
    }

    public interface OnStartPauseListener
    {
        public void onStartPause();
    }

}
