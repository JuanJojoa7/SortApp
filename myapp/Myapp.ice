module Demo
{
     sequence<int> IntList;
    interface Order
    {
        void sortList(IntList numbers, int numServers);
    }
}