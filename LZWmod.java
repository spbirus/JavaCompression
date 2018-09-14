
public class LZWmod {
    private static final int R = 256;        // number of input chars
    private static int W;         // codeword width
    private static int L;       // number of codewords = 2^W
    private static final int H = 16;

    public static void compress() {
        StringBuilder strIn = new StringBuilder();
        strIn.append(BinaryStdIn.readChar());
        W = 9;
        L = (int)Math.pow(2, W);

        //load the single chars into the st
        TST<Integer> st = new TST<Integer>();
        for (int i = 0; i < R; i++)
            st.put("" + (char) i, i);
        int code = R+1;  // R is codeword for EOF
        
        while(true){
            //search for the largest string in the st
            while(st.contains(strIn.toString()) && !BinaryStdIn.isEmpty()){
                strIn.append(BinaryStdIn.readChar());
            } 
            //write to the output file
            BinaryStdOut.write(st.get(strIn.substring(0,strIn.length()-1).toString()), W); //write code to output file
            //System.err.println("Output: codeword: " +st.get(strIn.substring(0,strIn.length()-1).toString())+ "\tValue: " + strIn.substring(0,strIn.length()-1).toString());
            
        
            if(code < L){
                //add the codeword to the st if there is space
                st.put(strIn.toString(), code++); //put code in st
            } else {
                //else increase the codeword width and then add the value
                if(W != H){
                    W++;
                    L = (int)Math.pow(2, W);
                    st.put(strIn.toString(), code++);
                }
            }
            //System.err.println("\tSt: codeword: " + (code-1) + "\tValue: " + strIn.toString());
            strIn.delete(0, strIn.length()-1); //start over with new word

            //check for the end of the file
            if(BinaryStdIn.isEmpty()){
                BinaryStdOut.write(st.get(strIn.toString()), W); //write code to output file
                //System.err.println("Output: codeword: " +st.get(strIn.toString())+ "\tValue: " + strIn.toString());
                break;
            }
            
        }
        
        BinaryStdOut.write(R, W); //write the end of file byte
        BinaryStdOut.close();
        //System.err.println("End of compress");
    } 


    public static void expand() {
        W = 9;
        L = (int)Math.pow(2, W);
        String[] st = new String[L];
        int i; // next available codeword value
        

        // initialize symbol table with all 1-character strings
        for (i = 0; i < R; i++)
            st[i] = "" + (char) i;
        st[i++] = "";                        // (unused) lookahead for EOF

        int codeword = BinaryStdIn.readInt(W);
        String val = st[codeword];

        while (true) {
            BinaryStdOut.write(val);
            //System.err.println("codeword: " + codeword + "\t value: " + val);
            codeword = BinaryStdIn.readInt(W); //read next codeword
            if (codeword == R){
                break; //end of file breaker
            }
            //look up codeword in symbol table
            String s = st[codeword];

            //if codeword isnt in the st yet, its the value plus the first char of value
            if (i == codeword){
                s = val + val.charAt(0);   // special case hack
            }
            //add the next thing to symbol table
            if (i < L) {
                st[i++] = val + s.charAt(0);
            }
            if(i >= L && W != H){
                //account for codeword increase
                W++;
                L = (int)Math.pow(2, W);
                //increase the array size 
                String[] temp = new String[L];
                System.arraycopy(st, 0, temp, 0, (int)Math.pow(2, W - 1));
                st = temp;
            }

            val = s;
        }
        BinaryStdOut.close();
        //System.err.println("End of uncompressing");
    }



    public static void main(String[] args) {
        if      (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new RuntimeException("Illegal command line argument");
    }

}
