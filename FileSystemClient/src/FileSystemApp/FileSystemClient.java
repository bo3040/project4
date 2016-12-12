package FileSystemApp;

import org.omg.CosNaming.*;

import java.io.PrintWriter;
import java.util.Scanner;

import org.omg.CORBA.*;

/**
 * A simple client that just gets a
 * @author Merlin
 *
 */
public class FileSystemClient
{
	static FileSystem fileSystemImpl;
	
	/**
	 * Just do each operation once
	 * @param args ignored
	 */
	public static void main(String args[])
	{
		try
		{		
			// create and initialize the ORB
			ORB orb = ORB.init(args, null);

			// get the root naming context
			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			// Use NamingContextExt instead of NamingContext. This is
			// part of the Interoperable naming Service.
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

			// resolve the Object Reference in Naming
			String name = "FileSystem";
			fileSystemImpl = FileSystemHelper.narrow(ncRef.resolve_str(name));
			System.out.println("Opened Connection");
			
			Scanner input = new Scanner(System.in);
			boolean looping = true;
			while(looping)
			{
				System.out.println("What do you want to do?");
				System.out.println("1.Read file.");
				System.out.println("2.Modify a files record.");
				System.out.println("3.Exit");
				int num = input.nextInt();
				switch(num)
				{
				case 1:
					readFile();
				case 2:
					updateRecord();
				case 3:
					looping = false;
					break;
				default:
					System.out.println("Invalid Option");
				}
			}
			System.out.println(fileSystemImpl.getFileTimeStamp("test1.txt"));
			
			// This is how we would shut down the server
			//fileSystemImpl.shutdown();

		} catch (Exception e)
		{
			System.out.println("ERROR : " + e);
			e.printStackTrace(System.out);
		}
	}
	private static void readFile() 
	{
		System.out.println("Enter the name of the file to read");
		Scanner input = new Scanner(System.in);
		String fileName = input.nextLine();
		fileSystemImpl.openReadFile(fileName);
		
		fileSystemImpl.readFile(fileName);
		
		fileSystemImpl.closeFile(fileName);
	}
	private static void updateRecord() 
	{
		Scanner input = new Scanner(System.in);
		System.out.println("Enter the name of the file to update.");
		String fileName = input.nextLine();
		fileSystemImpl.openWriteFile(fileName);
		System.out.println("File Opened");
		System.out.println("Enter the number of the record update.");
		int record = input.nextInt();
		System.out.println("Enter what you want to change the record to.");
		String modifications = input.nextLine();
		fileSystemImpl.updateRecord(record, fileName, modifications);
		System.out.println("Record updated");
		fileSystemImpl.closeFile(fileName);
		System.out.println("File closed");
	}
}
