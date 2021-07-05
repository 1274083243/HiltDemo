package com.ike.hiltdemo

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.arthenica.mobileffmpeg.Config
import com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS
import com.arthenica.mobileffmpeg.FFmpeg
import com.arthenica.mobileffmpeg.FFprobe
import com.permissionx.guolindev.PermissionX


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.ceshi).setOnClickListener {
//            Log.d("MainActivity","开始执行方法没碰过明皇陵")
//            FFprobe.execute(
//                "-i /sdcard/in.mp4"
//            ) { executionId, returnCode ->
//                Log.d("MainActivity","执行完毕")
//
//                Config.printLastCommandOutput(Log.INFO);
//            }
            PermissionX.init(this)
                .permissions(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                .request { allGranted, grantedList, deniedList ->
                    if (allGranted) {
                        Log.d("MainActivity", "开始执行方法")
                        val data = LinkedHashMap<String, Int>()
                        data.put("/sdcard/in.mp4", 0)
                        data.put("/sdcard/11.mp3", 2000)
                        data.put("/sdcard/22.mp3", 4000)

                        val getmixAudiocommandLine =
                            getmixAudiocommandLine(data, " /sdcard/out.mp4")
                        Log.d("MainActivity", "ffmpeg 命令:$getmixAudiocommandLine")
                        val rc = FFmpeg.executeAsync("""-y -i /sdcard/in.mp4  -i /sdcard/22.mp3 -i /sdcard/11.mp3 -filter_complex  "[1]adelay=2000|2000[1_];  [2]adelay=3000|3000[2_]; [0][1_][2_]amix=3" -c:v copy  /sdcard/out.mp4""") { executionId, returnId ->

                            if (returnId == RETURN_CODE_SUCCESS) {
                                Log.i("MainActivity", "Command execution completed successfully.");
                            } else {
                                Log.i(
                                    "MainActivity",
                                    String.format(
                                        "Command execution failed with rc=%d and the output below.",
                                        returnId
                                    )
                                );
                                Config.printLastCommandOutput(Log.INFO);
                            }
                        }


                    } else {
                        Toast.makeText(this, "您拒绝了拨打电话权限", Toast.LENGTH_SHORT).show()
                    }
                }

        }


    }

    /**
     * key  录音音频地址
     * value 配音的截止时长  多少毫秒
     */
    fun getmixAudiocommandLine(
        para: LinkedHashMap<String, Int>,
        outputAudioPath: String
    ): String? {
        var preCommand = "-y"
        var lastCommand = " -filter_complex  \""
        var index = 0
        val tip = "_"
        for ((audioAddress, audioLength) in para.entries) {
            index++
            preCommand += " -i $audioAddress"
            if (index == para.entries.size) {
                for (j in 0 until para.entries.size) {
                    lastCommand += if (j == 0) {
                        "[0]"
                    } else if (j == para.entries.size - 1) {
                        "[$j$tip]amix=" + para.entries.size + "\"  "
                    } else {
                        "[$j$tip]"
                    }
                }
            } else {
                lastCommand += "[$index]adelay=$audioLength|$audioLength[$index$tip];  "
            }
        }
        val commandLine = preCommand + lastCommand + outputAudioPath
        Log.e("commandLine---", commandLine)
        return commandLine
    }
}