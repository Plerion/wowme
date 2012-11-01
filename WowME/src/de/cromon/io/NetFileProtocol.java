package de.cromon.io;

public final class NetFileProtocol {
	public static final int CMSG_AUTH_CHALLENGE		= 0x01;
	public static final int CMSG_FILE_REQUEST		= 0x10;
	public static final int SMSG_FILE_RESPONSE		= 0x11;
	public static final int SMSG_FILE_CHUNK			= 0x12;
}
