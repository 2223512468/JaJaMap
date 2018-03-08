package com.jajamap.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import butterknife.ButterKnife;


/**
 * AppCompatActivity基类
 * Created by lhz
 */
public abstract class BaseActivity extends AppCompatActivity {

    /**
     * 退出登录的动作
     */
    public static final int LOGIN_OUT_ACTION = 0X99;
    protected Context mContext;
    protected ProgressDialog mDialog;


    /**
     * 设置界面布局id
     *
     * @return :layout id
     */
    protected abstract int getViewId();

    /**
     * 业务处理
     */
    protected abstract void initEvent();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置软键盘状态
        if (getViewId() != 0) {
            View view = LayoutInflater.from(this).inflate(getViewId(), null);
            setContentView(view);
        }
        ButterKnife.bind(this);
        mContext = this;
        initEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dissmisProgressDialog();
    }

    /**
     * 显示动画弹窗
     *
     * @param text ：显示的问题
     */
    public void showProgressDialog(@NonNull String text) {
        if (null == mDialog) {
            mDialog = new ProgressDialog(this);
        }
        mDialog.setMessage(text);
        if (!mDialog.isShowing()) {
            mDialog.show();
        }
    }

    /**
     * 去动画弹窗
     */
    public void dissmisProgressDialog() {
        if (null != mDialog && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }


    /**
     * 去动画弹窗
     */
    protected void showToast(int stringId) {
        if (stringId > 0) {
            //去换行
            Toast.makeText(this, getResources().getString(stringId), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 打开新activity
     *
     * @param tClass
     */
    public void gotoNewAty(Class<?> tClass) {
        Intent intent = new Intent(this, tClass);
        startActivity(intent);
    }

    /**
     * 打开新activity,并且接受返回
     *
     * @param tClass
     */
    public void gotoNewAtyForResult(Class<?> tClass, int requestCode) {
        Intent intent = new Intent(this, tClass);
        startActivityForResult(intent, requestCode);
    }

}
