package de.cromon.io;

import android.content.Context;
import android.net.wifi.*;

import java.lang.Math;
import java.util.ArrayList;

import de.cromon.wowme.Game;
import de.cromon.wowme.Settings;

public class NetworkDataLink {
	public NetworkDataLink() {
		mWifi = (WifiManager)Game.Instance.requestService(Context.WIFI_SERVICE);
		initNetworkClients();
	}
	
	private void initNetworkClients() {
		WifiInfo connectionInfo = mWifi.getConnectionInfo();
		int speed = connectionInfo.getLinkSpeed();
		int numClients = Math.min(speed, 4);
		
		for(int i = 0; i < numClients; ++i)
		{
			NetworkClientThread clientThread = new NetworkClientThread(
					Settings.Instance.DataHost,
					Settings.Instance.DataPort
					);

			clientThread.setClientID(i);
			
			clientThread.start();
			
			synchronized(clientThread) {
				try {
					clientThread.wait();
				} catch (InterruptedException e) {
				}
			}

			mClients.add(clientThread);
		}
	}
	
	public DataLinkStream requestFile(String fileName) {
		int minQueue = 0;
		NetworkClientThread curMin = null;
		NetworkFileRequest request = new NetworkFileRequest(fileName);
		
		for(NetworkClientThread netClient : mClients) {
			if(netClient.getQueueSize() == 0) {
				netClient.processFileRequest(request);
				return buildResponseForRequest(request);
			}
			
			if(curMin == null) {
				curMin = netClient;
				minQueue = netClient.getQueueSize();
				continue;
			}
			
			if(netClient.getQueueSize() < minQueue) {
				minQueue = netClient.getQueueSize();
				curMin = netClient;
			}
		}
		
		curMin.processFileRequest(request);
		return buildResponseForRequest(request);
	}
	
	private DataLinkStream buildResponseForRequest(NetworkFileRequest request) {
		if(request.FileAvailable == false)
			return null;
		
		return request.DataStream;
	}

	private ArrayList<NetworkClientThread> mClients = new ArrayList<NetworkClientThread>();
	private WifiManager mWifi;
}
