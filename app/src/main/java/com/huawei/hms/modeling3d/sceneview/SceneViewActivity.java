/*
 * Copyright 2021 Fang Binbin.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.huawei.hms.modeling3d.sceneview;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.huawei.hms.magicresource.R;
import com.huawei.hms.scene.sdk.SceneView;

import java.io.File;
import java.util.Vector;

public class SceneViewActivity extends Activity{
    private AlertDialog alertDialog2;
    int counts = 0;
    int backgroundcount = 0;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        Toast.makeText(SceneViewActivity.this, "当前为预置模型，点击切换模型预览已下载模型", Toast.LENGTH_LONG).show();
        //判断文件读写的权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Log.d("testwirteroot:", "ok");
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);
        }


            final SceneView sceneView = (SceneView) findViewById(R.id.sceneview);
            //实现点击切换预览已经下载的模型
            //change downloaded model view and show
            Button change_model = findViewById(R.id.change_model);

            change_model.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //获取已下载的模型路径
                    String rootpath = Environment.getStorageDirectory().getPath() + "/emulated/0/Android/data/com.huawei.hms.fbb3d/files/3dModeling/model/download";
                    File file = new File(rootpath);
                    File[] tempList = file.listFiles();
                    if (tempList!=null) {
                        Vector<String> fileList = new Vector<String>(); //存储gltf模型文件路径
                        for (File files : tempList) {
                            fileList.add(rootpath + "/" + files.getName() + "/out.gltf");
                        }

                        if (fileList.size() != 0) {
                            if (counts < fileList.size()) {
                                sceneView.loadScene(fileList.get(counts));
                            } else {
                                if (counts == fileList.size()) {
                                    Toast.makeText(SceneViewActivity.this, "您已浏览到下载列表最后模型，将返回下载列表中第一个模型", Toast.LENGTH_SHORT).show();
                                }
                                counts = 0;
                                sceneView.loadScene(fileList.get(counts));
                            }
                        }
                        counts++;
                        Toast.makeText(SceneViewActivity.this, "模型正在赶来的路上了,请稍候···", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(SceneViewActivity.this, "您还没有可用模型,只能预览默认模型", Toast.LENGTH_SHORT).show();
                        sceneView.loadScene("SceneView/out_konglong.gltf");
                    }


                }
            });

            Button button = findViewById(R.id.btn_bg);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    backgroundcount++;
                    //加载贴图
                    final String[] items = {"SceneView/xingkong.dds", "SceneView/skyboxTexture.dds", "SceneView/sky1.dds"};
                    if (backgroundcount < items.length) {
                        sceneView.loadSkyBox(items[backgroundcount]);
                    } else {
                        backgroundcount = 0;
                        sceneView.loadSkyBox(items[backgroundcount]);
                    }
                }
            });

    }
}
