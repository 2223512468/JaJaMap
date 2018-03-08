package com.jajamap.act;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.VersionInfo;
import com.jajamap.R;
import com.jajamap.base.BaseActivity;

import java.util.ArrayList;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    private static final String LTAG = MainActivity.class.getSimpleName();
    private final int SDK_PERMISSION_REQUEST = 127;
    @BindView(R.id.text_Info)
    TextView text;
    @BindView(R.id.listView)
    ListView mListView;
    private SDKReceiver mReceiver;
    private String permissionInfo;
    private static final DemoInfo[] DEMOS = {
            new DemoInfo(R.string.demo_title_basemap,
                    R.string.demo_desc_basemap, LocationActivity.class),
            new DemoInfo(R.string.demo_title_map_fragment,
                    R.string.demo_desc_map_fragment, MapFragAct.class),
            new DemoInfo(R.string.demo_title_control,
                    R.string.demo_desc_control, MapControlAct.class),
            new DemoInfo(R.string.demo_title_ui, R.string.demo_desc_ui,
                    UISettingAct.class),
            new DemoInfo(R.string.demo_title_overlay,
                    R.string.demo_desc_overlay, OverlayAct.class),
            new DemoInfo(R.string.demo_title_geocode,
                    R.string.demo_desc_geocode, GeoCoderAct.class),
            new DemoInfo(R.string.demo_title_poi, R.string.demo_desc_poi,
                    PoiSearchAct.class),
            new DemoInfo(R.string.demo_title_route, R.string.demo_desc_route,
                    RoutePlanAct.class),
            new DemoInfo(R.string.demo_title_bus, R.string.demo_desc_bus,
                    BusLineSearchAct.class),
            new DemoInfo(R.string.demo_title_share, R.string.demo_desc_share,
                    ShareAct.class),
            new DemoInfo(R.string.demo_title_indoor, R.string.demo_desc_indoor, IndoorMapAct.class),
            new DemoInfo(R.string.demo_title_indoorsearch, R.string.demo_desc_indoorsearch, IndoorSearchAct.class),
            new DemoInfo(R.string.demo_track_show, R.string.demo_desc_track_show, TrackShowAct.class)


    };

    /**
     * 构造广播监听类，监听 SDK key 验证以及网络异常广播
     */
    public class SDKReceiver extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent) {
            String s = intent.getAction();
            TextView text = (TextView) findViewById(R.id.text_Info);
            text.setTextColor(Color.RED);
            if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
                text.setText("key 验证出错! 错误码 :" + intent.getIntExtra
                        (SDKInitializer.SDK_BROADTCAST_INTENT_EXTRA_INFO_KEY_ERROR_CODE, 0)
                        + " ; 请在 AndroidManifest.xml 文件中检查 key 设置");
            } else if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK)) {
                text.setText("key 验证成功! 功能可以正常使用");
                text.setTextColor(Color.YELLOW);
            } else if (s.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
                text.setText("网络出错");
            }
        }
    }


    @Override
    protected int getViewId() {
        return R.layout.main;
    }

    @Override
    protected void initEvent() {
        text.setTextColor(Color.YELLOW);
        text.setText("欢迎使用百度地图Android SDK v" + VersionInfo.getApiVersion());
        // 添加ListItem，设置事件响应
        mListView.setAdapter(new DemoListAdapter());
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View v, int index,
                                    long arg3) {
                onListItemClick(index);
            }
        });

        // 注册 SDK 广播监听者
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK);
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
        iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
        mReceiver = new SDKReceiver();
        registerReceiver(mReceiver, iFilter);

        getPersimmions();
    }

    @TargetApi(23)
    private void getPersimmions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();
            /***
             * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
             */
            // 定位精确位置
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            /*
             * 读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
			 */
            // 读写权限
            if (addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionInfo += "Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n";
            }
            // 读取电话状态权限
            if (addPermission(permissions, Manifest.permission.READ_PHONE_STATE)) {
                permissionInfo += "Manifest.permission.READ_PHONE_STATE Deny \n";
            }

            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
            }
        }
    }

    @TargetApi(23)
    private boolean addPermission(ArrayList<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) { // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
            if (shouldShowRequestPermissionRationale(permission)) {
                return true;
            } else {
                permissionsList.add(permission);
                return false;
            }

        } else {
            return true;
        }
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // TODO Auto-generated method stub
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }


    void onListItemClick(int index) {
        Intent intent;
        intent = new Intent(MainActivity.this, DEMOS[index].demoClass);
        this.startActivity(intent);
    }


    private class DemoListAdapter extends BaseAdapter {
        public DemoListAdapter() {
            super();
        }

        @Override
        public View getView(int index, View convertView, ViewGroup parent) {
            convertView = View.inflate(MainActivity.this,
                    R.layout.demo_info_item, null);
            TextView title = (TextView) convertView.findViewById(R.id.title);
            TextView desc = (TextView) convertView.findViewById(R.id.desc);
            title.setText(DEMOS[index].title);
            desc.setText(DEMOS[index].desc);
            if (index >= 25) {
                title.setTextColor(Color.YELLOW);
            }
            return convertView;
        }

        @Override
        public int getCount() {
            return DEMOS.length;
        }

        @Override
        public Object getItem(int index) {
            return DEMOS[index];
        }

        @Override
        public long getItemId(int id) {
            return id;
        }
    }

    private static class DemoInfo {
        private final int title;
        private final int desc;
        private final Class<? extends Activity> demoClass;

        public DemoInfo(int title, int desc,
                        Class<? extends Activity> demoClass) {
            this.title = title;
            this.desc = desc;
            this.demoClass = demoClass;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
}
