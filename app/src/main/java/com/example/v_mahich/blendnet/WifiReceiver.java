package com.example.v_mahich.blendnet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteOrder;

public class WifiReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        if(info != null && info.isConnected()) {
            // Do your work.

            // e.g. To check the Network Name or other info:
            WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            String ssid = wifiInfo.getSSID();


            Log.i("BLENDNET",ssid + ":" + wifiIpAddress(wifiInfo.getIpAddress()) + ": " + wifiInfo.getNetworkId());
            //Logic to restrict to certain wifi only
            //if(ssid.equalsIgnoreCase("II Floor D"))

            Intent downloadService = new Intent(context, DownloadService.class);
            downloadService.putExtra("HUB_ADDRESS",wifiIpAddress(wifiInfo.getIpAddress()));


            if(ssid.equals("\"raspi-webgu\"i"))
                context.startService(downloadService);


        }



        //throw new UnsupportedOperationException("Not yet implemented");
    }

    String wifiIpAddress(int ipAddress) {


        // Convert little-endian to big-endianif needed
        if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
            ipAddress = Integer.reverseBytes(ipAddress);
        }

        byte[] ipByteArray = BigInteger.valueOf(ipAddress).toByteArray();

        String ipAddressString;
        try {
            ipAddressString = InetAddress.getByAddress(ipByteArray).getHostAddress();
        } catch (UnknownHostException ex) {
            Log.e("WIFIIP", "Unable to get host address.");
            ipAddressString = null;
        }
        int lastIndexOf = ipAddressString.lastIndexOf('.');
        ipAddressString = ipAddressString.substring(0,lastIndexOf+1).concat("1");

        return ipAddressString;
    }

}
