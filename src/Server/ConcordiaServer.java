package Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import ServerObjectInterfaceApp.ServerObjectInterface;
import ServerObjectInterfaceApp.ServerObjectInterfaceHelper;
import ImplementRemoteInterface.ConcordiaClass;



public class ConcordiaServer {

	public static void main(String args[])
	{
		try {
			// create and initialize the ORB //// get reference to rootpoa &amp; activate
			// the POAManager
			ORB orb = ORB.init(args, null);
			// -ORBInitialPort 1050 -ORBInitialHost localhost
			POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
			rootpoa.the_POAManager().activate();

			// create servant and register it with the ORB
			ConcordiaClass obj = new ConcordiaClass();
			obj.setORB(orb);

			// get object reference from the servant
			org.omg.CORBA.Object ref = rootpoa.servant_to_reference(obj);
			ServerObjectInterface href = ServerObjectInterfaceHelper.narrow(ref);

			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

			NameComponent path[] = ncRef.to_name("con");
			ncRef.rebind(path, href);

			System.out.println("Concordia Server ready and waiting ...");
			Runnable task = () -> {
				receive(obj);
			};
			Thread thread = new Thread(task);
			thread.start();

			// wait for invocations from clients
			for (;;) {
				orb.run();
			}
		}

		catch (Exception e) {
			System.err.println("ERROR: " + e);
			e.printStackTrace(System.out);
		}

		System.out.println("ConcordiaServer Exiting ...");

	}





	private static void receive(ConcordiaClass obj) {
		DatagramSocket aSocket = null;
		String sendingResult = "";
		try {
			aSocket = new DatagramSocket(8888);
			byte[] buffer = new byte[1000];
			System.out.println("Concordia UDP Server 8888 Started............");
			while (true) {
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				aSocket.receive(request);
				String sentence = new String( request.getData(), 0,
						request.getLength() );
				String[] parts = sentence.split(";");
				String function = parts[0]; 
				String userID = parts[1]; 
				String itemName = parts[2]; 
				String itemId = parts[3]; 
				int numberOfDays = Integer.parseInt(parts[4]); 
				if(function.equals("borrow")) {
					boolean result = obj.borrowItem(userID, itemId, numberOfDays);
					sendingResult = Boolean.toString(result);
					sendingResult= sendingResult+";";
				}else if(function.equals("find")) {
					String result = obj.findItem(userID, itemName);
					sendingResult = result;
					sendingResult= sendingResult+";";
				}else if(function.equals("return")) {
					boolean result = obj.returnItem(userID, itemId);
					sendingResult = Boolean.toString(result);
					sendingResult= sendingResult+";";
				}else if(function.equals("wait")) {
					boolean result = obj.waitingList(userID, itemId);
					sendingResult = Boolean.toString(result);
					sendingResult= sendingResult+";";
				}else if(function.equals("isAvailableInLibrary")) {
					boolean result = obj.isAvailableInLibrary(itemId);
					sendingResult = Boolean.toString(result);
					sendingResult= sendingResult+";";
				}else if(function.equals("isBorrowed")) {
					boolean result = obj.isBorrowed(userID,itemId);
					sendingResult = Boolean.toString(result);
					sendingResult= sendingResult+";";
				}else if(function.equals("isAlreadyBorrowed")) {
					boolean result = obj.isAlreadyBorrowed(userID);
					sendingResult = Boolean.toString(result);
					sendingResult= sendingResult+";";
				}
				byte[] sendData = sendingResult.getBytes();
				DatagramPacket reply = new DatagramPacket(sendData, sendingResult.length(), request.getAddress(),
						request.getPort());
				aSocket.send(reply);
			}
		} catch (SocketException e) {
			System.out.println("Socket: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		} finally {
			if (aSocket != null)
				aSocket.close();
		}
	}
}
