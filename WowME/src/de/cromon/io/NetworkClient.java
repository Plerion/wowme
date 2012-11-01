package de.cromon.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;

import android.util.Log;

public class NetworkClient {
	public NetworkClient(String hostName, int port) {

		mHostName = hostName;
		mPort = port;
	}
	
	public void initConnection() throws java.io.IOException {
		mSocket = new Socket(mHostName, mPort);
		mSocketOutput = mSocket.getOutputStream();
		mOutStream = new OutByteStream(mSocketOutput);
		mSocketInput = mSocket.getInputStream();
		mInStream = new InByteStream(mSocketInput);
		mSocket.setTcpNoDelay(true);
		
		String model = android.os.Build.MODEL;
		byte[] stringData = mOutStream.getStringBytes(model);
		int totalLength = stringData.length + 1 + 1;
		mOutStream.writeInt32(totalLength);
		mOutStream.writeByte(NetFileProtocol.CMSG_AUTH_CHALLENGE);
		mOutStream.writeString(model);
		mSocketOutput.flush();
	}
	
	public void processReadRequest(NetworkFileRequest request) throws IOException {
		byte[] stringData = mOutStream.getStringBytes(request.FileName);
		if(stringData == null || stringData.length == 0)
			throw new IOException("Invalid filename provided!");
		
		int totalLength = stringData.length + 1 + 1;
		mOutStream.writeInt32(totalLength);
		mOutStream.writeByte(NetFileProtocol.CMSG_FILE_REQUEST);
		mOutStream.writeString(request.FileName);
		mSocketOutput.flush();
		
		int packetLength = mInStream.readInt32();
		if(packetLength < 2) {
			throw new IOException("Unexpected end of stream!");
		}
		
		int opcode = mInStream.readByte();
		if(opcode != 0x11) {
			throw new IOException("Unexpected opcode received!");
		}
		
		int status = mInStream.readByte();
		boolean isFileAvailable = status != 0;
		request.FileAvailable = isFileAvailable;
		
		Log.d("NetworkStreaming", "Requested file '" + request.FileName + "'. This files is " + (isFileAvailable ? "available" : "not available") + ".");
		if(isFileAvailable) {
			int fileSize = mInStream.readInt32();
			request.FileSize = fileSize;
			Log.d("NetworkStreaming", "Awaiting " + fileSize + " file bytes....");
			
			request.DataStream = new NetworkStream(request);
		}
	}
	
	/**
	 * @param request The active request to fetch the next chunk of data
	 * @return true if the request is fully read, false if there is still data missing.
	 * @throws IOException On network failure
	 */
	public boolean processRequestChunk(NetworkFileRequest request) throws IOException {
		// no data stream associated or no file size -> we are done
		if(request.DataStream == null || request.FileSize == 0) {
			return true;
		}
		
		Log.d("NetworkStreaming", "Processing a chunk...");
		int packetLength = mInStream.readInt32();
		// we got invalid data for that request -> dump it...
		if(packetLength == 0)
			return true;
		
		int opcode = mInStream.readByte();
		
		// invalid opcode -> remove it, something seems wrong
		if(opcode != NetFileProtocol.SMSG_FILE_CHUNK)
			return true;
		
		int chunkLen = mInStream.readInt32();
		byte[] chunk = new byte[chunkLen];
		Log.d("NetworkStreaming", "before read");
		mInStream.readBytes(chunk);
		Log.d("NetworkStreaming", "after read");
		
		request.DataStream.pushNetworkChunk(chunk);
		return request.DataStream.isComplete();
	}
	
	private OutputStream mSocketOutput;
	private InputStream mSocketInput;
	private OutByteStream mOutStream;
	private InByteStream mInStream;
	private Socket mSocket = null;
	private String mHostName;
	private int mPort;
}
