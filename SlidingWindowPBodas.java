import java.io.*;
import java.util.*;
/**
 * Prachi Bodas Period III
 * This lab took me 18 hours.
 * This lab took me unholy amounts of time. I wrote it,
 * found out it didn't work, redid part, employed a hex
 * editor, and finally gave up. I came back later, relearned 
 * what I wrote, and began to fix it. I asked my dad for help.
 * This lab was too hard. No me gusta.
 */
public class SlidingWindowPBodas {
    boolean debug = true;
    // Declare a stream of input
    DataInputStream inStream;

    // Store the bytes of input file in a String
    ArrayList<Character> fileArray = new ArrayList<Character>();

    // Store file sizes to see how much compression we get
    long inFileSize;
    long outFileSize;

    // Track how many bytes we've read. Useful for large files.
    int byteCount;
    
    //stores occurences

    public void readFile(String fileName) {

        try {
            // Create a new File object, get size
            File inputFile = new File(fileName);
            inFileSize = inputFile.length();

            // The constructor of DataInputStream requires an InputStream
            inStream = new DataInputStream(new FileInputStream(inputFile));
        }

        // Oops.  Errors.
        catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(0);
        }

        // Read the input file
        try {
            System.out.println("Enter!!!\n");

            // While there are more bytes available to read...
            while (inStream.available() > 0) {

                // Read in a single byte and store it in a character
                char c = (char)inStream.readByte();

                if ((++byteCount)% 1024 == 0)
                    System.out.println("Read " + byteCount/1024 + " of " + inFileSize/1024 + " KB...");

                // Print the characters to see them for debugging purposes
                //System.out.print(c);

                // Add the character to an ArrayList
                fileArray.add(c);
            }
            
            System.out.println(fileArray);

            // clean up
            inStream.close();
            System.out.println("Done!!!\n");
        }

        // Oops.  Errors.
        catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }

        // Print the ArrayList contents for debugging purposes
    }

    public void compressAndSaveAs(String fileName) {
        // At this point, the ArrayList fileArray contains the file as a raw collection of Characters
        // Compress the input file by processing the ArrayList and writing the result to a new output file
        File output = new File (fileName);
        DataOutputStream outStream;
        try
        {    
             outStream = new DataOutputStream(new FileOutputStream(output));
        }catch (FileNotFoundException e)
        {
            outStream = null;
            System.exit (0);
        }
        int beginWindow = 0;
        int endWindow = 29; // beginning and ending of windows
        int beginFile = 30; // beginning of file reading
        int endFile = 30; // end of File reading
        String str = generateString(beginWindow, endWindow);
        try
        {    
            outStream.writeBytes(str); 
        } catch (IOException e)
        {
            System.exit(0);
        }
        while (endFile<fileArray.size())
        {
            int index = 0; // index 
            int longest = 0; // length of longest
            int begin = 0; // beginning of longest
            int end = 0; // end of longest
            int current = 0; // length of current
            int startIndex = 0;
            int matchIndex = 0;
            str = generateString(beginWindow, endWindow);
          
            while (index <= 29 && str.indexOf(fileArray.get(beginFile)+"", index) != -1)
            {
                index = str.indexOf(fileArray.get(beginFile)+"", index);
                endFile = beginFile ;
                current = 0;
                startIndex = index;
                
                while (index <= 29 && (fileArray.get(endFile) == str.charAt(index)))
                {
                    current++;
                    endFile++;
                    index++;
                }
                if (current > longest)
                {
                   
                    longest = current;
                    begin = beginFile;
                    end = endFile;
                    matchIndex = startIndex;
                }
          
            } // now one has the longest occurence
            if (longest>3)
            {
                try{
                    outStream.writeByte(7);
                    outStream.writeByte(matchIndex);
                    outStream.writeByte(longest);
                } catch (IOException e)
                {
                    System.exit(0);
                }
                beginWindow +=longest;
                endWindow+=longest;
            }else
            {
                if (longest == 0) 
                    longest = 1;
                    
                end = begin + longest;
                int j = 0;
                for (int i = begin; i<end; i++)
                {
                    try{
                        outStream.writeByte(fileArray.get(beginFile + j));
                    }catch (IOException e)
                    {
                        System.exit(0);
                    }
                    j++;
                }
                beginWindow += (end - begin); 
                endWindow += (end - begin); 
            }
            beginFile = endWindow+1;
            endFile = beginFile;
        }
        outFileSize = output.length();
        try{    
            outStream.close();
        }catch (IOException e)
        {
            System.exit(0);
        }
    }
    
    public String generateString(int a, int b) // inclusive
    {
        String k = "";
        for (int i = a; i<=b; i++)
        {
            k += fileArray.get(i) + "";
        }
        return k;
    }

    public void printStats() {

        System.out.printf("Original size = %,d bytes\n", inFileSize);
        System.out.printf("Compressed size = %,d bytes\n", outFileSize);
        System.out.printf("Compression ratio = %.1f%s\n\n", 100 * (1.0 - outFileSize/(double)inFileSize), "%");

    }
    
    public void decompress (String filename)
    {
        File de = new File (filename);
        DataOutputStream outStream;
        try
        {    
             outStream = new DataOutputStream(new FileOutputStream(de));
        }catch (FileNotFoundException e)
        {
            outStream = null;
            System.exit (0);
        }
        int a = fileArray.indexOf((char)7); // file Array is the compressed file
        if(debug)
        {
            System.out.println("Initial a is " + a);
        }
        int endOfPrevious = 0;
        if(debug)
        {
            //System.out.println("Generated string file = " + file);
        }
        while (a != -1)
        {
            //for (int i = endOfPrevious; i<a; i++)
            //{
              //  if (debug)
                //{
                  //  System.out.println ("i is " + i + " val is " + file.charAt(i));
                //}
                //try{
                  //  outStream.write(file.charAt(i)); // before compression stuff
                //}catch (IOException e)
                //{
                  //  System.exit(0);
                //}
            //}
            int beginWindow = a - 30;
            int endWindow = a - 1;
            if(debug)
            {
                System.out.println ("a is " + (int)fileArray.get(a));
                System.out.println ("a + 1 is " + (int)fileArray.get(a +1));
                System.out.println ("a +2 is " + (int)fileArray.get(a+2));
            }
            int begComp = (int)fileArray.get(a+1) + beginWindow; // position of beginning of compressed thing
            int length = (int)fileArray.get(a+2);
            fileArray.remove(a+2);
            fileArray.remove(a+1);
            fileArray.remove(a);
            for (int i = length-1; i >= 0 ;i--)
            {
                if(debug)
                {
                    System.out.println("i is " + i);
                    //System.out.println ("The char is " + (file.charAt((int)file.charAt(a+1) + beginWindow + i)));
                }
                char b = fileArray.get(begComp+i);
                fileArray.add(a,b);
            }
            if(debug)
            {
                System.out.println("FileArray: " + fileArray);
            }
            a = fileArray.indexOf((char)7);
            if(debug)
            {
                System.out.println("final a is " + a);
            }
        }
        for(char c: fileArray)
        {
            try
            {
                outStream.write(c);
            }catch (IOException e)
            {
                System.exit(0);
            }
            
        }
    }
    
    public void resetArray()
    {
        while (!fileArray.isEmpty())
        {
            fileArray.remove(0);
        }
    }

    public static void main(String[] args) {

        SlidingWindowPBodas demo = new SlidingWindowPBodas();
        System.out.println((char)7);
        demo.readFile("input.txt");
        demo.compressAndSaveAs("output.txt");
        demo.printStats();
        demo.resetArray();
        demo.readFile("output.txt");
        demo.decompress("decompressed.txt");
    }

}


