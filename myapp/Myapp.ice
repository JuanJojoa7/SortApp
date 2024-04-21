module Demo
{
     sequence<int> IntList;
     sequence<IntList> SequenceOfIntLists;
    interface Order
    {
        void sortList(IntList numbers, int numServers);
    }
}