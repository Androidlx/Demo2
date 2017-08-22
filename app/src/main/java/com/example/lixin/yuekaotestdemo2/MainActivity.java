package com.example.lixin.yuekaotestdemo2;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.lixin.yuekaotestdemo2.bean.Yuekao2Info;
import com.google.gson.Gson;
import com.limxing.xlistview.view.XListView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.nio.channels.NonReadableChannelException;
import java.util.List;

import static android.R.attr.versionCode;

public class MainActivity extends AppCompatActivity implements XListView.IXListViewListener {

    private XListView xListView;
    private  int index = 1;
    private MyBaseAdapter adapter;
    private boolean flag;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        xListView = (XListView) findViewById(R.id.xlistview);
        xListView.setPullLoadEnable(true);
        xListView.setXListViewListener(this);
        xListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(R.mipmap.ic_launcher)
                        .setTitle("网络选择")
                        .setSingleChoiceItems(new String[] {"wife","手机流量"}, 0,
                                new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which){
                                            case 0:
                                                Toast.makeText(MainActivity.this, "wife", Toast.LENGTH_SHORT).show();
                                                new AlertDialog.Builder(MainActivity.this)
                                                        .setTitle("版本更新")
                                                        .setMessage("现在检测到新版本,是否更新？")
                                                        .setPositiveButton("下载", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                downloadApk();
                                                            }
                                                        })
                                                        .setNegativeButton("取消", null)
                                                        .create()
                                                        .show();
                                                break;
                                            case 1:
                                                Toast.makeText(MainActivity.this, "跳到wife界面", Toast.LENGTH_SHORT).show();
                                                //跳转网络设置界面  隐士意图
                                                Intent intent = new Intent();
                                                intent.setAction("android.settings.WIRELESS_SETTINGS");
                                                startActivity(intent);
                                                break;
                                        }
                                    }
                                }
                        )
                        .setNegativeButton("取消", null)
                        .show();
            }
        });
        getData();
    }

    private void getData() {
        String path = "http://v.juhe.cn/toutiao/index";
        RequestParams params = new RequestParams(path);
        params.addQueryStringParameter("key","5b6258c74f4346147b12fe38490a12b2");
        params.addQueryStringParameter("type","top");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("-----------------"+result);
                Gson gson = new Gson();
                Yuekao2Info yuekao2Info = gson.fromJson(result, Yuekao2Info.class);
                List<Yuekao2Info.ResultBean.DataBean> data = yuekao2Info.getResult().getData();
               if (adapter == null) {
                   adapter = new MyBaseAdapter(data,MainActivity.this);
                   xListView.setAdapter(adapter);
               }else {
                    adapter.loadMore(data,flag);
                   adapter.notifyDataSetChanged();
               }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    @Override
    public void onRefresh() {
        index++;
        getData();
        flag = true;
        xListView.stopRefresh(true);
    }

    @Override
    public void onLoadMore() {

        index++;
        getData();
        flag = false;
        xListView.stopLoadMore();

    }
    private void downloadApk() {
        String url = "http://down11.zol.com.cn/suyan/lulutong3.6.5g.apk";
        String path = Environment.getExternalStorageDirectory().getPath() + "/teme1/myapk.apk";
        File file = new File(path);
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdir();
        }

        RequestParams params = new RequestParams(url);
        params.setAutoRename(false);
        params.setAutoResume(true);

        //设置保存路径
        params.setSaveFilePath(path);
        x.http().get(params, new Callback.ProgressCallback<File>() {
            @Override
            public void onSuccess(File result) {
                Toast.makeText(MainActivity.this, "下载成功", Toast.LENGTH_SHORT).show();
                installDownloadApk(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(MainActivity.this, "下载失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                cancleProgressDialog();
            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {
                showProgressDialog();
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                int progress = (int) (current / total * 100);
                if (progress >= 0 && progress <= 100) {
                    updataProgressDialog(progress);
                }
            }
        });
    }

    //安卓开发 apk安装
    private void installDownloadApk(File result) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(result), "application/vnd.android.package-archive");
        startActivity(intent);
    }

    private void cancleProgressDialog() {
        if (progressDialog == null) {
            return;
        }
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void updataProgressDialog(int progress) {
        if (progressDialog == null) {
            return;
        }
        progressDialog.setProgress(progress);
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(this);

        //设置progressDialog显示样式
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage("我正在下载东西");
        progressDialog.setTitle("请等待");
        progressDialog.setProgress(0);
        progressDialog.show();
    }
}
