package com.byteshaft.kidmonitor.utils;

import android.content.Intent;
import android.util.Log;

import com.byteshaft.kidmonitor.AppGlobals;
import com.byteshaft.kidmonitor.constants.AppConstants;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import java.io.File;
import java.util.Properties;

public class SftpHelpers {

    private static final String SFTPUSER = "deploy";
    private static final String SFTPPASS = "admin";
    private static final String SFTPHOST = "128.199.125.71";
    private static final int SFTPPORT = 22;
    public static ChannelSftp mChannelSftp;

    public static boolean upload(String contentType, String path, int id) {
        JSch jSch = new JSch();
        Session session = null;
        try {
            session = jSch.getSession(SFTPUSER, SFTPHOST, SFTPPORT);
            session.setPassword(SFTPPASS);
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.setTimeout(10000);
            session.connect();
            Log.i("APP", "Host connected.");
            Channel channel = session.openChannel("sftp");
            channel.connect();
            mChannelSftp = (ChannelSftp) channel;
            System.out.println(getRemoteWorkingDirectoryForType(contentType));
            mChannelSftp.cd(getRemoteWorkingDirectoryForType(contentType));
            File toUpload = new File(path);
            Log.i(AppGlobals.getLogTag(AppGlobals.getContext().getClass()), "started uploading....");
            mChannelSftp.put(toUpload.getAbsolutePath(), toUpload.getName());
            Log.i(AppGlobals.getLogTag(AppGlobals.getContext().getClass()), "current file uploaded");
            Intent intent = new Intent("com.byteshaft.deleteData");
            intent.putExtra("url", path);
            intent.putExtra("id", id);
            AppGlobals.getContext().sendBroadcast(intent);
            Log.i(AppGlobals.getLogTag(AppGlobals.getContext().getClass()), "Delete BroadCast sent!!");
        } catch (JSchException e) {
            Log.i(AppGlobals.getLogTag(AppGlobals.getContext().getClass()), "file upload failed!!");
        } catch (SftpException e) {
            Log.i(AppGlobals.getLogTag(AppGlobals.getContext().getClass()), "Not connected to server!!");
        }
        if (mChannelSftp == null) {
            return false;
        }
        mChannelSftp.exit();
        session.disconnect();
        return true;
    }

    private static String getRemoteWorkingDirectoryForType(String type) {
        String root = "backup/";

        switch (type) {
            case AppConstants.TYPE_CALL_RECORDINGS:
                return root + AppConstants.TYPE_CALL_RECORDINGS;
            case AppConstants.TYPE_SOUND_RECORDINGS:
                return root + AppConstants.TYPE_SOUND_RECORDINGS;
            case AppConstants.TYPE_VIDEO_RECORDINGS:
                return root + AppConstants.TYPE_VIDEO_RECORDINGS;
            default:
                return root + AppConstants.TYPE_OTHERS;
        }
    }
}
