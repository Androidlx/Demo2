package com.example.lixin.yuekaotestdemo2;

import android.app.Application;
import android.os.Environment;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.xutils.x;

import java.io.File;


/**
 * Created by hua on 2017/8/22.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
         String path = Environment.getExternalStorageDirectory().getPath()+"/"+"Pictrues";
                 File file = new File(path);

                 ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                         .memoryCacheExtraOptions(480,800)//配置内存缓存图片的尺寸
                         //.diskCacheExtraOptions() bug 不推介你手动去配置
                         .memoryCacheSize(2 * 1024 * 1024)//配置内存缓存的大小  例如 : 2* 1024 * 1024 = 2MB
                         .threadPoolSize(3)//配置加载图片的线程数
                         .threadPriority(100)//配置线程的优先级
                         .diskCache(new UnlimitedDiskCache(file))//UnlimitedDiskCache 限制这个图片的缓存路径
                         .diskCacheSize(50 * 1024 * 1024)//在sdcard缓存50MB
                         .diskCacheFileNameGenerator(new Md5FileNameGenerator())//MD5这种方式生成缓存文件的名字
                         .diskCacheFileCount(20)//配置sdcard缓存文件的数量
                         .build();//配置构建完成

                 ImageLoader.getInstance().init(config);
    }
}
