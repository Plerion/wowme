package de.cromon.io;

import java.util.LinkedList;
import java.util.Queue;

import android.util.Log;

import de.cromon.wowme.Game;

public class NetworkClientThread extends Thread {
	public NetworkClientThread(String host, int port) {
		mHost = host;
		mPort = port;
		mClientID = 0;
	}
	
	public void setClientID(int id) {
		mClientID = id;
	}
	
	public int getClientID() {
		return mClientID;
	}
	
	 public void run() {
		 try
		 {
			 NetworkClient client = new NetworkClient(mHost, mPort);
			 client.initConnection();
			 mClient = client;
			 
			 synchronized(this) {
				 notifyAll();
			 }
			 
			 doReceiveCallback();
		 }
		 catch(Exception e) {
			 Game.Instance.displayError("Client " + mClientID + " - Error:\n" + e.toString(), true);
		 }
	 }
	 

	private void doReceiveCallback() {
		 while(Thread.interrupted() == false) {
			 if(mCurrentRequest == null) {
				 boolean isEmpty = false;
				 synchronized(mRequestQueue) {
					 if(mRequestQueue.isEmpty()) {
						 isEmpty = true;
					 } else {
						 isEmpty = false;
						 mCurrentRequest = mRequestQueue.poll();
					 }
				 }
				 
				 if(isEmpty) {
					 try {
						 Thread.sleep(100);
					 } catch (InterruptedException e) {
						 Thread.currentThread().interrupt();
					 }
					 
					 continue;
				 }
					 
				 try {
					 mClient.processReadRequest(mCurrentRequest);
				 } catch (Exception e) {
					 mCurrentRequest.ThrownException = e;
				 }
	
				 mCurrentRequest.signalCompletion();
			 } else {
				 try {
					 boolean finished = mClient.processRequestChunk(mCurrentRequest);
					 if(finished) {
						 mCurrentRequest.DataStream.signalReadFinished();
						 mCurrentRequest = null;
						 Log.d("NetworkStreaming", "Finished request!");
						 
					 }
					 
				 } catch (Exception e) {
					 mCurrentRequest.ThrownException = e;
					 mCurrentRequest = null;
					 continue;
				 }
			 }
		 }
	 }
	 
	 public void processFileRequest(NetworkFileRequest request) {
		 synchronized(mRequestQueue) {
			 mRequestQueue.add(request);
		 }
		 
		 request.awaitCompletion();
	 }
	 
	 public int getQueueSize() {
		 synchronized(mRequestQueue) {
			 return mRequestQueue.size();
		 }
	 }
	 
	 private String mHost;
	 private int mPort;
	 private NetworkClient mClient;
	 private int mClientID;
	 private Queue<NetworkFileRequest> mRequestQueue = new LinkedList<NetworkFileRequest>();
	 private NetworkFileRequest mCurrentRequest = null;
}
