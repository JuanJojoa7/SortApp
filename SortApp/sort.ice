module Sorting {
    sequence<string> StringSeq;

    interface SortFile {
        StringSeq sortFileList(StringSeq strings);
    }
}