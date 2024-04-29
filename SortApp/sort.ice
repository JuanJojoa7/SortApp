module Sorting {
    sequence<string> StringSeq;
    sequence<StringSeq> StringSeqSeq;

    interface SortFile {
        bool createFile();
        StringSeq sortFileList(StringSeq strings);
        StringSeqSeq divideFile(int parts);
        string register();
        void waitForAllClients();
    }
}