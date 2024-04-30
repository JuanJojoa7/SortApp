module Sorting {
    sequence<string> StringSeq;
    sequence<StringSeq> StringSeqSeq;

    interface SortFile {
        bool createFile();
        StringSeq sortFileList(StringSeq strings);
        string register();
        void waitForAllClients();
    }
}