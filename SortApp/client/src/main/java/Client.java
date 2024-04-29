public class Client {
    public static void main(String[] args) {
        try(com.zeroc.Ice.Communicator communicator = com.zeroc.Ice.Util.initialize(args)) {
            com.zeroc.Ice.ObjectPrx base = communicator.stringToProxy("SortingFileAdapter:tcp -h localhost -p 10000");
            Sorting.SortFilePrx sortProxy = Sorting.SortFilePrx.checkedCast(base);
            if(sortProxy == null) {
                throw new Error("Invalid proxy");
            }
        }
    }
}
